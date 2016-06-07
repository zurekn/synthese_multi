package com.mygdx.game.game;

import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.data.Data;

public class MessageHandler {

	private int initialX = 10;
	private int inittialY = Data.SCREEN_HEIGHT - 20;
	public ArrayList<Message> globalMessages = new ArrayList<Message>();// Global
																		// Message
	public ArrayList<ArrayList<Message>> playerMessages = new ArrayList<ArrayList<Message>>();

	private ArrayList<Message> deletedMessage = new ArrayList<Message>();
	private ArrayList<Message> waitingMessage = new ArrayList<Message>();

	private ArrayList<ArrayList<Message>> deletedPlayerMessage = new ArrayList<ArrayList<Message>>();
	private ArrayList<ArrayList<Message>> waitingPlayerMessage = new ArrayList<ArrayList<Message>>();

	public void render(Batch batch) {
        /* Global messages */
		int i = Data.FONT_HEIGHT + 3;

		for(Message m : waitingMessage)
			globalMessages.add(m);
		waitingMessage.clear();
		
		for (Message m : globalMessages) {
			m.render(batch, initialX, inittialY - i);
			if (m.update())
				deletedMessage.add(m);
			i -= Data.FONT_HEIGHT +3;
		}


		// For the delete
		for (Message m : deletedMessage) {
			globalMessages.remove(m);
		}


        /* PLayer Messages */
		int n = 0;
		i = 0;
		for(ArrayList<Message> list : waitingPlayerMessage){
			for(Message m : list)
				playerMessages.get(i).add(m);
			waitingPlayerMessage.get(i).clear();
			i++;
		}

		n = 0;
		i = 0;
		Matrix4 oldMatrix = batch.getTransformMatrix().cpy();
        Matrix4 matrix = new Matrix4();
		for (ArrayList<Message> list : playerMessages) {
            matrix.rotate(new Vector3(Data.MAP_X + Data.MAP_WIDTH / 2, Data.MAP_Y + Data.MAP_HEIGHT / 2, 0), n * 90);
            batch.setTransformMatrix(matrix); // set the rotation

			for (Message m : list) {
				m.render(batch,Data.PLAYER_MESSAGE_X_POS + Data.MAP_X, Data.PLAYER_MESSAGE_Y_POS + Data.MAP_Y + Data.MAP_HEIGHT + i);
                if (m.update())
					deletedPlayerMessage.get(n).add(m);
				i += Data.FONT_HEIGHT;
			}
            batch.setTransformMatrix(oldMatrix);
			i = 0;
			n++;
		}


		n = 0;
		for (ArrayList<Message> list : deletedPlayerMessage) {
			for (Message m : list) {
				playerMessages.get(n).remove(m);
			}
			list.clear();
			n++;
		}

		deletedMessage.clear();

	}

	public void update() {

	}

	public void addGlobalMessage(Message message) {
        //Gdx.app.log("Message Handler", "Add message "+message);
		ArrayList<Message> splitMessage = split(message);
		for (Message m : splitMessage)
			waitingMessage.add(m);
	}

	public void addPlayerMessage(Message message, int player) {
        //Gdx.app.log("MessageHandler", "Create message "+message.toString()+" for player "+player);
        if (Data.singlePlayer) {
            addGlobalMessage(message);
            return;
        }
        // first call we create the playerList
		if (playerMessages.size() < 1 )
			for (int i = 0; i < GameStage.gameStage.getPlayers().size(); i++) {
				playerMessages.add(new ArrayList<Message>());
				deletedPlayerMessage.add(new ArrayList<Message>());
				waitingPlayerMessage.add(new ArrayList<Message>());
			}

		ArrayList<Message> splitMessage = split(message);
		if (player >= GameStage.gameStage.getPlayers().size())
			for (Message m : splitMessage)
				addGlobalMessage(m);
		else {
			switch (player) {
			case 0:
				for (Message m : splitMessage)
					m.setRotation(0);
				break;
			case 1:
				for (Message m : splitMessage)
					m.setRotation(90);
				break;
			case 2:
				for (Message m : splitMessage)
					m.setRotation(180);
				break;
			case 3:
				for (Message m : splitMessage)
					m.setRotation(-90);
				break;

			default:
				break;
			}
			for (Message m : splitMessage)
				waitingPlayerMessage.get(player).add(m);

		}
	}

	private ArrayList<Message> split(Message message) {
		ArrayList<Message> split = new ArrayList<Message>();
		int cut = 0;
		String var = "";
		if (message.getMessage().length() * Data.FONT_SIZE < Data.MESSAGE_MAX_LENGTH)
			split.add(message);
		else {

			while (message.getMessage().length() > Data.MESSAGE_MAX_LENGTH) {
				cut = Math.min(message.getMessage().length(),
						Data.MESSAGE_MAX_LENGTH);
				if (cut < message.getMessage().length()) {
					cut = message.getMessage().substring(0, cut)
							.lastIndexOf(" ");
					var = message.getMessage().substring(0, cut);
					split.add(new Message(var, message.getType()));
					message = new Message(message.getMessage().substring(cut),
							message.getType());
				}
			}
			split.add(message);

		}
		return split;
	}

}
