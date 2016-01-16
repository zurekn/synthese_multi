package com.mygdx.game.game;

import imageprocessing.APIX;
import imageprocessing.APIXAdapter;
import imageprocessing.MovementEvent;
import imageprocessing.QRCodeEvent;

import java.lang.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import ai.AStar;
import ai.CharacterData;
import ai.CommandHandler;
import ai.CommandListener;
import ai.ActionEvent;
import ai.WindowGameData;
import data.*;
import exception.IllegalActionException;
import exception.IllegalCaracterClassException;
import exception.IllegalMovementException;

/**
 * Main class which handle the game Contain the init method for creating all
 * game objects
 * 
 * @author bob
 *
 */
public class WindowGame extends BasicGame {

	private APIX apix;
	private Thread thread;
	private CommandHandler commands;
	private GameHandler handler;

	private GameContainer container;
	private MobHandler mobHandler;
	private ArrayList<Mob> mobs;
	private PlayerHandler playerHandler;
	private ArrayList<Player> players;
	private MovementHandler movementHandler;
	private MessageHandler messageHandler;
	private ArrayList<Event> events = new ArrayList<Event>();
	private ArrayList<Trap> traps = new ArrayList<Trap>();
	private java.lang.Character previousCharacter = null;
	private java.lang.Character currentCharacter;
	private ArrayList<int[]> reachableBlock = new ArrayList<int[]>();

	private int playerNumber;
	private int turn;
	private int actionLeft = Data.ACTION_PER_TURN;

	public boolean gameOn = false;
	private boolean gameEnded = false;
	private boolean gameWin = false;
	private boolean gameLose = false;
	private int timerInitPlayer;

	private int turnTimer;
	private long timeStamp = -1;
	private static WindowGame windowGame = null;

	public static WindowGame getInstance() {
		if (windowGame == null)
			try {
				windowGame = new WindowGame();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		return windowGame;
	}

	private WindowGame() throws SlickException {
		super(Data.NAME);
	}

	private WindowGame(String title, GameContainer container, MobHandler mobHandler, ArrayList<Mob> mobs, game.PlayerHandler playerHandler,
			ArrayList<Player> players, MovementHandler movementHandler, ArrayList<Event> events, java.lang.Character currentCharacter, int playerNumber,
			int turn, int turnTimer, long timeStamp) {
		super(title);
		this.container = container;
		this.mobHandler = mobHandler;
		this.mobs = mobs;
		this.playerHandler = playerHandler;
		this.players = players;
		this.movementHandler = movementHandler;
		this.events = events;
		this.currentCharacter = currentCharacter;
		this.playerNumber = playerNumber;
		this.turn = turn;
		this.turnTimer = turnTimer;
		this.timeStamp = timeStamp;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		thread = Thread.currentThread();
		handler = new GameHandler(thread);

		container.setMouseGrabbed(true);;
		Data.loadMap();
		Data.loadGame();
		SpellData.loadSpell();
		MonsterData.loadMonster();
		HeroData.loadHeros();
		// TrapData.loadTrap();

		initAPIX();
		initCommandHandler();

		// Create the monster list
		mobs = MonsterData.initMobs();
		mobHandler = new MobHandler(mobs);

		messageHandler = new MessageHandler();

		// Create the player list
		initPlayers();

		playerHandler = new PlayerHandler(players);

		//new Thread(movementHandler).start();
		// Set the timer
		timerInitPlayer = Data.INIT_MAX_TIME;

		if (!Data.BACKGROUND_MUSIC.playing())
			Data.BACKGROUND_MUSIC.loop(Data.MUSIC_PITCH, Data.MUSIC_VOLUM);
		// start();
	}

	private void start() {
		playerNumber = players.size() + mobs.size();

		turnTimer = Data.TURN_MAX_TIME;
		turn = 0;
		players.get(turn).setMyTurn(true);
		currentCharacter = players.get(turn);

		reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));
		gameOn = true;
	}

	public void initPlayers() {

		players = new ArrayList<Player>();

		Collection<String> pos = Data.departureBlocks.keySet();
		pos.iterator();
		if (Data.debug) {
			try {
				if (Data.DEBUG_PLAYER > 0)
					addChalenger(10, 8, -1);
				// players.add(new Player(10, 12, "P0", "mage"));
				if (Data.DEBUG_PLAYER > 1)
					addChalenger(15, 15, -1);
				if (Data.DEBUG_PLAYER > 2)
					addChalenger(19, 15, -1);
				if (Data.DEBUG_PLAYER > 3)
					addChalenger(7, 12, -1);
			} catch (IllegalCaracterClassException e) {
				e.printStackTrace();
			} catch (IllegalMovementException e) {
				e.printStackTrace();
			} catch (IllegalActionException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Add a new player
	 * 
	 * @param x
	 * @param y
	 * @throws IllegalCaracterClassException
	 * @throws IllegalMovementException
	 * @throws IllegalActionException
	 */
	@SuppressWarnings("unused")
	public void addChalenger(int x, int y, int size) throws IllegalCaracterClassException, IllegalMovementException, IllegalActionException {
		if (gameOn)
			throw new IllegalActionException("Can not add player when game is on!");

		String position = x + ":" + y;

		if (WindowGame.getInstance().getAllPositions().contains(position)) {
			// messageHandler.addMessage(new
			// Message("Position ["+position+"] non disponible", 1));

			throw new IllegalMovementException("Caracter already at the position [" + position + "]");
		}

		if (Data.untraversableBlocks.containsKey(position)) {
			messageHandler.addGlobalMessage(new Message("Position [" + position + "] non disponible", 1));
			throw new IllegalMovementException("Untraversable block at [" + position + "]");
		}

		if (!Data.departureBlocks.containsKey(position) && !Data.DEBUG_DEPARTURE) {
			messageHandler.addGlobalMessage(new Message(Data.DEPARTURE_BLOCK_ERROR, Data.MESSAGE_TYPE_ERROR));
			throw new IllegalMovementException("Caracter must be at a departure position");
		} else {
			Data.departureBlocks.put(position, true);
		}

		if (Data.MAX_PLAYER <= players.size())
			return;
		String id = "P" + players.size();
		String type = HeroData.getRandomHero();

		Player p = new Player(x, y, id, type);
		p.setNumber(players.size());
		p.setSizeCharacter(size);
		players.add(p);

		timerInitPlayer = Data.INIT_MAX_TIME;
		if (players.size() >= Data.MAX_PLAYER) {
			System.out.println(" ----Max player reached ----");
			start();
		}
	}

	public void addPlayer(String position) {
		if (!Data.departureBlocks.get(position)) {
			String[] s = position.split(":");
			try {
				players.add(new Player(Integer.parseInt(s[0]), Integer.parseInt(s[1]), "P" + players.size(), "mage"));
				Data.departureBlocks.remove(position);
				Data.departureBlocks.put(position, true);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalCaracterClassException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public void initCommandHandler() {
		commands = CommandHandler.getInstance();
		commands.addCommandListener(new CommandListener() {

			public void newAction(ActionEvent e) {
				System.out.println("Nouvelle action recup de CommandHandler  : " + e.toString());
				try {
					decodeAction(e.getEvent());
				} catch (IllegalActionException e1) {
					System.err.println(e1.getLocalizedMessage());
				}
			}
		});
		commands.begin();
	}

	public void initAPIX() {
		apix = APIX.getInstance();
		if (!Data.RUN_APIX)
			return;

		movementHandler = new MovementHandler(this);

		apix.addAPIXListener(new APIXAdapter() {
			@Override
			public void newQRCode(QRCodeEvent e) {
				System.out.println("Un nouveau QRCode vien d'�tre recup�rer par WindowGame [" + e.toString() + "]");
				try {
					decodeAction(e.getId() + ":" + e.getDirection());
				} catch (IllegalActionException e1) {
					System.err.println(e1.getLocalizedMessage());
				}
			}

			public void newMouvement(MovementEvent e) {
				System.out.println("Une nouvelle position  vient d'�tre r�cup�r�e par WindowGame [" + e.toString() + "], position sur le plateau ["
						+ e.getX() / apix.getBlockSizeX() + ":" + e.getY() / apix.getBlockSizeY() + "]");
				try {
					if (gameOn)
						if (!currentCharacter.isMonster())
							decodeAction("m:" + (int)(e.getX() / apix.getBlockSizeX()) + ":" + (int)(e.getY() / apix.getBlockSizeY()));
						else
							System.err.println("R�cup�ration d'une valeur de l'apix durant le tour de l'ia");
					else
						addChalenger((int)(e.getX() / apix.getBlockSizeX()), (int)(e.getY() / apix.getBlockSizeY()), e.getSize());
				} catch (IllegalActionException e1) {
					System.err.println(e1.getLocalizedMessage());
				} catch (IllegalCaracterClassException e1) {
					System.err.println(e1.getLocalizedMessage());
				} catch (IllegalMovementException e1) {
					System.err.println(e1.getLocalizedMessage());
				}
			}
		});

		apix.begin();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}

	int i = 0;

	/**
	 * The render function Call all game's render
	 */
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.scale(Data.SCALE, Data.SCALE);
		if(gameEnded){
			Data.map.render(Data.MAP_X,  Data.MAP_Y);
			mobHandler.render(container, g);
			renderDeckArea(container, g);
			playerHandler.render(container, g);
			if(gameWin){
				Data.WIN_IMAGE.draw(Data.ENDING_ANIMATION_X, Data.ENDING_ANIMATION_Y, (float) Data.WIN_IMAGE.getWidth() * Data.ENDING_ANIMATION_SCALE, (float) Data.WIN_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE);
			}
			if(gameLose){
				Data.LOSE_IMAGE.draw(Data.ENDING_ANIMATION_X, Data.ENDING_ANIMATION_Y, (float) Data.LOSE_IMAGE.getWidth() * Data.ENDING_ANIMATION_SCALE, (float) Data.LOSE_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE);
			}
			
			if(Data.ENDING_ANIMATION_Y < (Data.MAP_HEIGHT - Data.LOSE_IMAGE.getHeight() * Data.ENDING_ANIMATION_SCALE) / 2)
				Data.ENDING_ANIMATION_Y ++;
			return;
		}
		if (!apix.isInit()) {
			g.setColor(Color.black);
			g.setBackground(Color.white);
			// TOP LEFT
			g.fillRect(Data.MAP_X - 20, Data.MAP_Y - 20, 40, 40);
			// TOP RIGHT
			g.fillRect(Data.MAP_X + Data.MAP_WIDTH - 20, Data.MAP_Y - 20, 40, 40);
			// //BOTTOM left
			g.fillRect(Data.MAP_X - 20, Data.MAP_Y + Data.MAP_HEIGHT - 20, 40, 40);
			i++;
			if (i > 60)
				apix.initTI();
			// Reset the timer unteal the paix init is over
			timerInitPlayer = Data.INIT_MAX_TIME;

		} else {
			Data.map.render(Data.MAP_X, Data.MAP_Y);

			if (gameOn) {
				mobHandler.render(container, g);
				renderDeckArea(container, g);
				playerHandler.render(container, g);
				renderReachableBlocks(container, g);
				renderEvents(container, g);
			} else {
				playerHandler.renderInitBlock(container, g);
			}
			playerHandler.renderPlayerStat(container, g);
			renderText(container, g);
		}
	}

	/**
	 * Display the reachables blocks of the current caracter
	 * 
	 * @param container
	 * @param g
	 */
	private void renderReachableBlocks(GameContainer container, Graphics g) {
		g.setColor(Data.BLOCK_REACHABLE_COLOR);
		for (int[] var : reachableBlock) {
			g.fillRect(Data.MAP_X + var[0] * Data.BLOCK_SIZE_X, Data.MAP_Y + var[1] * Data.BLOCK_SIZE_Y, Data.BLOCK_SIZE_X, Data.BLOCK_SIZE_Y);
		}
		g.setColor(Color.black);
	}

	/**
	 * Render the Deck Area
	 * 
	 * @param container
	 * @param g
	 */
	private void renderDeckArea(GameContainer container, Graphics g) {
		g.setColor(Color.magenta); // La coucleur a modifier
		// TOP
		g.fillRect(Data.MAP_X, Data.RELATIVE_Y_POS, Data.DECK_AREA_SIZE_X, Data.DECK_AREA_SIZE_Y);
		// BOTTOM
		g.fillRect(Data.MAP_X + Data.DECK_AREA_SIZE_X, Data.MAP_HEIGHT + Data.MAP_Y, Data.DECK_AREA_SIZE_X, Data.DECK_AREA_SIZE_Y);
		// LEFT
		g.fillRect(Data.MAP_X - Data.DECK_AREA_SIZE_Y, Data.MAP_Y + Data.MAP_WIDTH / 2, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_X);
		// RIGHT
		g.fillRect(Data.MAP_X + Data.MAP_WIDTH, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_Y, Data.DECK_AREA_SIZE_X);
		g.setColor(Color.white);
	}

	/**
	 * Render the Game Text
	 * 
	 * @param container
	 * @param g
	 */
	private void renderText(GameContainer container, Graphics g) {
		// render text
		g.setColor(Data.TEXT_COLOR);
		g.drawString(Data.MAIN_TEXT, 10, 20);
		messageHandler.render(container, g);

	}

	/**
	 * Render all events in the game
	 * 
	 * @param container
	 * @param g
	 */
	private void renderEvents(GameContainer container, Graphics g) {
		int x, y, xMin, yMin, xMax, yMax;
		xMin = Data.MAP_X;
		xMax = Data.MAP_X + Data.MAP_WIDTH;
		yMin = Data.MAP_Y;
		yMax = Data.MAP_Y + Data.BLOCK_NUMBER_Y * Data.BLOCK_SIZE_Y;
		
		for (int i = 0; i < events.size(); i++) {
			Event e = events.get(i);
			if(!e.isFinalFrame()){
				e.render(container, g);
				x = e.getX();
				y = e.getY();
				
				if(e.isMobile())
					e.move();
				
				if( e.getRange() <= 1){
				//	System.out.println("range <= 1");
					e.setMobile(false);
				}
			
				if (x < xMin || x > xMax || y < yMin || y > yMax)			
					e.setFinalFrame(true);
			}else{
				e.renderPostRemove(container, g);
				if(e.isNeeDelete())
					events.remove(i);
			}
		}
		long eventTime = 0;
	}

	

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if(gameEnded)
			return;
		long time = System.currentTimeMillis();
		if (time - timeStamp > 1000) {
			if (gameOn) {
				turnTimer--;
				timeStamp = time;
				Data.MAIN_TEXT = Data.TURN_TEXT + turnTimer;
			} else {
				timerInitPlayer--;
				timeStamp = time;
				Data.MAIN_TEXT = Data.INIT_PLAYER_TEXT + timerInitPlayer;
			}
		}

		// Turn timer
		if (gameOn) {
			if (turnTimer < 0) {
				switchTurn();
			}
		} else {
			if (timerInitPlayer < 0)
				start();
		}

		messageHandler.update();
	}

	/**
	 * Function call for switch the current character turn
	 */
	public void switchTurn() {
		// Reset the timer
		System.out.println("turn = " + turn + ", playerNumber = " + playerNumber + ", turnTimer = " + turnTimer);
		messageHandler.addGlobalMessage(new Message("Next turn"));
		turnTimer = Data.TURN_MAX_TIME;
		turn = (turn + 1) % playerNumber;

		previousCharacter = currentCharacter;
		previousCharacter.regenMana();
		// Switch the turn
		// Set the new character turn
		if (turn < players.size()) {
			players.get(turn).setMyTurn(true);
			currentCharacter = players.get(turn);
		} else {
			mobs.get(turn - players.size()).setMyTurn(true);
			currentCharacter = mobs.get(turn - players.size());
		}

		// set to false the previous character turn
		previousCharacter.setMyTurn(false);
		if (currentCharacter.isMonster() && !Data.SHOW_MOB_REACHABLE_BLOCKS)
			reachableBlock = new ArrayList<int[]>();
		else
			reachableBlock = AStar.getInstance().getReachableNodes(new WindowGameData(players, mobs, turn), new CharacterData(currentCharacter));

		messageHandler.addGlobalMessage(new Message("Turn of " + currentCharacter.getName()));
		actionLeft = Data.ACTION_PER_TURN;

		if (currentCharacter.isNpc() && !previousCharacter.isNpc())
			commands.startCommandsCalculation(currentCharacter, players, mobs, turn);

		// print the current turn in the console
		if (Data.debug) {
			System.out.println("========================");
			if (turn < players.size()) {
				System.out.println("Tour du Joueur " + turn);
				System.out.println("Player : " + players.get(turn).toString());
			} else {
				System.out.println("Tour du Monster" + (turn - players.size()));
				System.out.println("Monster " + mobs.get(turn - players.size()).toString());
			}
			System.out.println("========================");
		}
	}

	/**
	 * decode a action and create associated event
	 * 
	 * @param action
	 *            , a string which defines the action
	 * @throws IllegalMovementException
	 */
	public void decodeAction(String action) throws IllegalActionException {
		if(gameEnded || !gameOn)
			return;
		if (action.startsWith("s")) { // Spell action
			if(actionLeft <= 0 ){
				if(!Data.debug){
					messageHandler.addPlayerMessage(new Message(Data.ERROR_TOO_MUCH_ACTION, 1), turn);
					return;
				}else{
					messageHandler.addPlayerMessage(new Message("Action interdite, mais on est en mode debug... ", 1), turn);

				}
			}
			
			String[] tokens = action.split(":");
			if (tokens.length != 2)
				throw new IllegalActionException("Wrong number of arguments in action string");

			String spellID = tokens[0].split("\n")[0];
			int direction = Integer.parseInt(tokens[1]);
			
			if (currentCharacter.getSpell(spellID) == null){
				messageHandler.addPlayerMessage(new Message("Vous n'avez pas le sort : "+SpellData.getSpellById(spellID).getName(), Data.MESSAGE_TYPE_ERROR), turn);
				throw new IllegalActionException("Spell [" + spellID + "] not found");
			}
			float speed = currentCharacter.getSpell(spellID).getSpeed();
			Event e = currentCharacter.getSpell(spellID).getEvent().getCopiedEvent();

			e.setDirection(direction, speed);
			e.setX(Data.MAP_X + currentCharacter.getX() * Data.BLOCK_SIZE_X);
			e.setY(Data.MAP_Y + currentCharacter.getY() * Data.BLOCK_SIZE_Y);
			// Get the range to the next character to hit
			Focus focus = getFirstCharacterRange(getCharacterPositionOnLine(currentCharacter.getX(), currentCharacter.getY(), e.getDirection()), e);
			//System.out.println("get focus : " + focus.toString());
			if (focus.range > e.getRange()) {
				focus.range = e.getRange();
				focus.character = null;
			}
			e.setRange(focus.range);

			try {
				String res = currentCharacter.useSpell(spellID, direction);
				String []split = res.split(":");
				int damage = Integer.parseInt(split[0]);
				int heal = Integer.parseInt(split[1]);
				int state = Integer.parseInt(split[2]);
				
				if(state == -1){
					messageHandler.addPlayerMessage(new Message("Echec critique du sort "+SpellData.getSpellById(spellID).getName(), Data.MESSAGE_TYPE_ERROR), turn);
					if(heal > 0){
						currentCharacter.heal(heal);
						messageHandler.addPlayerMessage(new Message("Heal critic "+heal+" to the "+focus.character.getName()+"", Data.MESSAGE_TYPE_ERROR), turn);

					}else{
						currentCharacter.takeDamage(damage, e.getType());
						messageHandler.addPlayerMessage(new Message("Use "+SpellData.getSpellById(spellID).getName()+" on "+currentCharacter.getName()+" and deal critic "+damage, Data.MESSAGE_TYPE_ERROR), turn);	

					}
					if (currentCharacter.checkDeath()) {
						// TODO ADD a textual event
						System.out.println("-----------------------------------------");
						System.out.println("DEATH FOR" + currentCharacter.toString());
						System.out.println("-----------------------------------------");
						messageHandler.addPlayerMessage(new Message(currentCharacter.getName()+"Died "), turn);	
						turn--;
						players.remove(currentCharacter);
						mobs.remove(currentCharacter);
						playerNumber--;
						
						checkEndGame();
						switchTurn();
					}
					
				}else{
					if (focus.character != null) {
						if (currentCharacter.isMonster() == focus.character.isMonster())
							if (e.getHeal() > 0){
								focus.character.heal(heal);
								if(state > 0 )
									messageHandler.addPlayerMessage(new Message("Heal critic "+heal+" to the "+focus.character.getName()+"", Data.MESSAGE_TYPE_ERROR), turn);
								else
									messageHandler.addPlayerMessage(new Message("Heal "+heal+" to the "+focus.character.getName()+""), turn);

							}else{
								damage = focus.character.takeDamage(damage, e.getType());
								messageHandler.addPlayerMessage(new Message("Use "+SpellData.getSpellById(spellID).getName()+" on "+focus.character.getName()+" and deal "+damage), turn);	
							}
						else{
							damage = focus.character.takeDamage(damage, e.getType());
							if(state > 0)
								messageHandler.addPlayerMessage(new Message("Use "+SpellData.getSpellById(spellID).getName()+" on "+focus.character.getName()+" and deal critic "+damage, Data.MESSAGE_TYPE_ERROR), turn);	
							else
								messageHandler.addPlayerMessage(new Message("Use "+SpellData.getSpellById(spellID).getName()+" on "+focus.character.getName()+" and deal "+damage), turn);	

						}
						if (focus.character.checkDeath()) {
							System.out.println("-----------------------------------------");
							System.out.println("DEATH FOR" + focus.character.toString());
							System.out.println("-----------------------------------------");
							messageHandler.addPlayerMessage(new Message(focus.character.getName()+"Died "), turn);	
							int index = Math.max(players.indexOf(focus.character), mobs.indexOf(focus.character));
							int indexCurrent = Math.max(players.indexOf(currentCharacter), mobs.indexOf(currentCharacter));
							players.remove(focus.character);
							mobs.remove(focus.character);
							playerNumber--;
							if(index <= indexCurrent)
								turn = (turn - 1) % playerNumber;
							checkEndGame();
						}
					}else{
						messageHandler.addPlayerMessage(new Message("Vous avez lanc� "+SpellData.getSpellById(spellID).getName()+" mais personne n'a �t� touch�"), turn);
					}
				}
				events.add(e);
				System.out.println("Created " + e.toString());
				actionLeft --;
			} catch (IllegalActionException iae) {
				//iae.printStackTrace();
				System.out.println(iae.getLocalizedMessage() +"----------------------------"+iae.getMessage());
				messageHandler.addPlayerMessage(new Message(iae.getLocalizedMessage(),Data.MESSAGE_TYPE_ERROR), turn);
			}

		}

		else if (action.startsWith("t")) { // Trap action
			System.out.println("Find a trap action");
		}

		else if (action.startsWith("m")) {// Movement action
			try {
				String[] tokens = action.split(":");
				if (tokens.length != 3)
					throw new IllegalActionException("Wrong number of arguments in action string");

				String position = tokens[1] + ":" + tokens[2];
				// TODO call aStar and check if character don't fall into trap
				currentCharacter.moveTo(position);
				switchTurn();

			} catch (IllegalMovementException ime) {
				throw new IllegalActionException("Mob can't reach this block");
			}
		} else {
			throw new IllegalActionException("Action not found : " + action);
		}
	}

	/**
	 * Return the distance between the currentCharacter and the closer mob
	 * 
	 * @param chars
	 * @param e
	 * @return
	 */
	private Focus getFirstCharacterRange(ArrayList<java.lang.Character> chars, Event e) {
		float range = Data.MAX_RANGE;
		System.out.println("Search the first character range : " + e.toString() + ", " + chars.toString());
		java.lang.Character focus = null;
		for (java.lang.Character c : chars) {
			if (e.getDirection() == Data.NORTH || e.getDirection() == Data.SOUTH) {
				int i = (Math.abs(c.getY() - (e.getYOnBoard())));
				System.out.println("c.getY() = [" + c.getY() + "], e.getYOnBoard = [" + (e.getYOnBoard()) + "], i = [" + i + "]");

				if (i < range) {
					range = i;
					focus = c;
				}
			}
			if (e.getDirection() == Data.EAST || e.getDirection() == Data.WEST) {
				System.out.println("c.getX() = [" + c.getX() + "], e.getXOnBoard = [" + (e.getXOnBoard()) + "], i = [" + (c.getX() - e.getXOnBoard())
						+ "]");
				int i = (Math.abs(c.getX() - (e.getXOnBoard())));

				if (i < range) {
					range = i;
					focus = c;
				}
			}
		}
		
		if(e.getDirection() == Data.SELF)
		{
			return new Focus(0, currentCharacter);
		}
		
		if (Data.debug && focus != null)
			System.out.println("The Range is : " + range + ", focus is " + focus.toString());
		return new Focus(range, focus);
	}

	@Override
	public void keyReleased(int key, char c) {
		if (Data.debug) {
			if (gameOn)
				if (!currentCharacter.isNpc()) {
					System.out.println("WindowGame, keyReleased : " + key + ", char : " + c);
					try {
						if (Input.KEY_LEFT == key)
							decodeAction("m:" + (currentCharacter.getX() - 1) + ":" + currentCharacter.getY());
						if (Input.KEY_RIGHT == key)
							decodeAction("m:" + (currentCharacter.getX() + 1) + ":" + currentCharacter.getY());
						if (Input.KEY_UP == key)
							decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() - 1));
						if (Input.KEY_DOWN == key)
							decodeAction("m:" + currentCharacter.getX() + ":" + (currentCharacter.getY() + 1));
						if (Input.KEY_NUMPAD8 == key)
							decodeAction("s3:" + Data.NORTH);
						if (Input.KEY_NUMPAD6 == key)
							decodeAction("s9:" + Data.EAST);
						if (Input.KEY_NUMPAD2 == key)
							decodeAction("s10:" + Data.SOUTH);
						if (Input.KEY_NUMPAD4 == key)
							decodeAction("s4:" + Data.WEST);
						if (Input.KEY_NUMPAD5 == key)
							decodeAction("s1:" + Data.SELF);
					} catch (IllegalActionException e) {
						// TODO Auto-generated catch block
						System.err.println(e.getMessage());
					}
				}
			
			if (Input.KEY_DIVIDE == key) {
				currentCharacter.takeDamage(20, "magic");
			}
			if (Input.KEY_SUBTRACT == key) {
				start();
			}
			if (Input.KEY_ADD == key) {
				try {
					Random rand = new Random();
					int x = rand.nextInt(Data.BLOCK_NUMBER_X - 0) + 0;
					int y = rand.nextInt(Data.BLOCK_NUMBER_Y - 0) + 0;
					addChalenger(x, y, -1);
				} catch (IllegalCaracterClassException e) {
					e.printStackTrace();
				} catch (IllegalMovementException e) {
					e.printStackTrace();
				} catch (IllegalActionException e) {
					e.printStackTrace();
				}
			}
			
			if(Input.KEY_L == key){
				gameEnded = true;
				gameLose = true;
				stopAllThread();
			}
			
			if(Input.KEY_W == key){
				gameEnded = true;
				gameWin = true;
				stopAllThread();
			}
			
			
		}

		if (Input.KEY_ESCAPE == key) {
			container.exit();
		}
	}

	/**
	 * Move the current player to the destination x:y if possible
	 * 
	 * @param str
	 *            , x:y
	 */
	@Deprecated
	public void move(String str) {
		System.out.println("WindowGame get new movement : " + str);
		if (turn < players.size()) {
			try {
				players.get(turn).moveTo(str);
			} catch (IllegalMovementException e) {
				e.printStackTrace();
			}
		} else {
			try {
				mobs.get(turn - players.size()).moveTo(str);
			} catch (IllegalMovementException e) {
				e.printStackTrace();
			}
		}

	}

	public Mob getMobById(String id) {
		for (int i = 0; i < mobs.size(); i++) {
			if (mobs.get(i).getId().equals(id)) {
				return mobs.get(i);
			}
		}
		return null;
	}

	/**
	 * Get all the Character positions
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getAllPositions() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < players.size(); i++)
			list.add(players.get(i).getX() + ":" + players.get(i).getY());
		for (int i = 0; i < mobs.size(); i++)
			list.add(mobs.get(i).getX() + ":" + mobs.get(i).getY());
		return list;
	}

	public ArrayList<String> getAllTraps() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < traps.size(); i++)
			list.add(traps.get(i).getX() + ":" + traps.get(i).getY());
		return list;
	}

	public ArrayList<Mob> getMobs() {
		return mobs;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public GameHandler getHandler() {
		return handler;
	}
	

	public java.lang.Character getCurrentPlayer() {
		return currentCharacter;
	}
	
	public void checkEndGame(){
		if(mobs.size() <= 0 || players.size() <= 0){
			//GAME WIN
			stopAllThread();
			gameEnded = true;
			if(mobs.size() <= 0)
				gameWin = true;
			if(players.size() <= 0)
				gameLose = true;
		}
	}
	
	public void stopAllThread(){
		apix.stop();
		commands.getInstance().getThread().stop();
		turnTimer = Integer.MAX_VALUE;
	}
	
	/**
	 * Get all character on a line line = Horizontal or Vertical
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @return ArrayList<Character>
	 */
	private ArrayList<java.lang.Character> getCharacterPositionOnLine(int x, int y, int direction) {

		ArrayList<java.lang.Character> c = new ArrayList<java.lang.Character>();
		
		if(direction == Data.SELF)
		{
			c.add(currentCharacter);
			return c;
		}
		
		for (int i = 0; i < players.size(); i++) {
			// above
			if (direction == Data.NORTH && players.get(i).getY() < y && players.get(i).getX() == x)
				c.add(players.get(i));
			// bottom
			if (direction == Data.SOUTH && players.get(i).getY() > y && players.get(i).getX() == x)
				c.add(players.get(i));
			// on left
			if (direction == Data.EAST && players.get(i).getY() == y && players.get(i).getX() > x)
				c.add(players.get(i));
			// on right
			if (direction == Data.WEST && players.get(i).getY() == y && players.get(i).getX() < x)
				c.add(players.get(i));
		}

		for (int i = 0; i < mobs.size(); i++) {
			// above
			if (direction == Data.NORTH && mobs.get(i).getY() < y && mobs.get(i).getX() == x)
				c.add(mobs.get(i));
			// bottom
			if (direction == Data.SOUTH && mobs.get(i).getY() > y && mobs.get(i).getX() == x)
				c.add(mobs.get(i));
			// on left
			if (direction == Data.EAST && mobs.get(i).getY() == y && mobs.get(i).getX() > x)
				c.add(mobs.get(i));
			// on right
			if (direction == Data.WEST && mobs.get(i).getY() == y && mobs.get(i).getX() < x)
				c.add(mobs.get(i));
		}
		System.out.println("getCharacterePositionOnLine From [" + x + ", " + y + "], Found" + c.toString());
		return c;
	}

	/**
	 * Return the Character with have the x,y position
	 * 
	 * @param x
	 * @param y
	 * @return Character
	 */
	private java.lang.Character getCharacterByPosition(int x, int y) {
		for (int i = 0; i < players.size(); i++)
			if (players.get(i).getX() == x && players.get(i).getY() == y)
				return players.get(i);

		for (int i = 0; i < mobs.size(); i++)
			if (mobs.get(i).getX() == x && mobs.get(i).getY() == y)
				return mobs.get(i);

		return null;
	}

	private class Focus {
		protected float range;
		protected java.lang.Character character;

		public Focus(float range, java.lang.Character character) {
			this.range = range;
			this.character = character;
		}

		public String toString() {
			return "Focus [ range, " + range + ", " + character.toString() + "]";
		}
	}
}
