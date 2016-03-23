package com.mygdx.game.imageprocessing;

import com.mygdx.game.game.GameStage;
import com.mygdx.game.game.WindowGame;

import java.awt.im.InputMethodHighlight;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.event.EventListenerList;

import com.github.sarxos.webcam.Webcam;

import com.mygdx.game.data.Data;
import com.mygdx.game.data.Handler;

public class ImageProcessingHandler extends Handler {

	private Webcam webcam;
	private BufferedImage imageRef;
	private int seuil;
	private static ImageProcessingHandler handler;
	protected ImageProcessing ip = new ImageProcessing();
	private final EventListenerList listeners = new EventListenerList();

	private ImageProcessingHandler(Webcam webcam) {
		super();
		this.webcam = webcam;
		seuil = 25;
	}

	/**
	 * Is intended to be called just once
	 * 
	 * @param webcam
	 * @return
	 */
	public static ImageProcessingHandler getInstance(Webcam webcam) {
		if (handler == null)
			handler = new ImageProcessingHandler(webcam);
		return handler;
	}

	public static ImageProcessingHandler getInstance() {
		if (handler == null)
			System.err
					.println("You need to call getInstance with Webcam parameter once before using it.");
		return handler;
	}

	public void initImageRef() {
		System.out.println("Image ref taken at "+System.currentTimeMillis());
		imageRef = webcam.getImage();
		if (Data.tiDebug) {
			try {
				ImageIO.write(imageRef, "jpg", new File(Data.getImageDir()
						+ "imageRef" + Data.getDate() + ".jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addMovementListener(MovementListener listener) {
		listeners.add(MovementListener.class, listener);
	}

	public MovementListener[] getMouvementListener() {
		return listeners.getListeners(MovementListener.class);
	}

	public void addMovement(List<FormObject> lf) {
		if (lf == null)
			return;
		for (FormObject o : lf) 
		{
			MovementEvent e = new MovementEvent(o.getBaryCenter().getX(), o.getBaryCenter().getY(), o.getSurface());
			for (MovementListener listener : getMouvementListener())
				listener.newMovement(e);
		}

	}

	public void run() {

		BufferedImage image;
		System.out.println("Image Processing running");
		try {
			System.out.println("Waiting 4 sec");
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		initImageRef();

		APIX apix = APIX.getInstance();

		while (true) {
			// Wait APIX thread
			//System.out.println("Lock ...");
			
			//apix.waitLock();

			if (Data.tiDebug)
				System.out.println("lancement du traitement dans 2 sec-----------------------------------------------------------------------");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			image = null;
			System.out.println("Image prise � "+System.currentTimeMillis());
			image = webcam.getImage();
			try {
				ImageIO.write(image, "jpg", new File(Data.getImageDir()
						+ "traitementImage" + Data.getDate() + ".jpg"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Test de version opti
//			List<FormObject> lf = ip.etiquetageIntuitifImageGiveList2(imageRef, image, seuil);
			List<FormObject> lf = new ArrayList<FormObject>();
			if(GameStage.gameStage.getCurrentPlayer() != null)
			{
				com.mygdx.game.game.Character currentPlayer = GameStage.gameStage.getCurrentPlayer();
				int tempLimitMinX = ((currentPlayer.getX()*(int)APIX.blockSizeX + (int)APIX.relativeX) - ((int)APIX.blockSizeX*(currentPlayer.getStats().getMovementPoints()+2)));
				tempLimitMinX = tempLimitMinX < (int)APIX.relativeX ? 
												(int)APIX.relativeX : 
														tempLimitMinX;
				
				int tempLimitMinY = ((currentPlayer.getY()*(int)APIX.blockSizeY + (int)APIX.relativeY) - ((int)APIX.blockSizeY*(currentPlayer.getStats().getMovementPoints()+2)));
				tempLimitMinY = tempLimitMinY < (int)APIX.relativeY ? 
												(int)APIX.relativeY : 
														tempLimitMinY;
				
				int tempLimitMaxX = ((currentPlayer.getX()*(int)APIX.blockSizeX + (int)APIX.relativeX) + ((int)APIX.blockSizeX*(currentPlayer.getStats().getMovementPoints()+2)));
				tempLimitMaxX = tempLimitMaxX > Data.BLOCK_NUMBER_X*(int)APIX.blockSizeX+(int)APIX.relativeX ? 
								Data.BLOCK_NUMBER_X*(int)APIX.blockSizeX+(int)APIX.relativeX : 
								tempLimitMaxX;

				int tempLimitMaxY = ((currentPlayer.getY()*(int)APIX.blockSizeY + (int)APIX.relativeY) + ((int)APIX.blockSizeY*(currentPlayer.getStats().getMovementPoints()+2)));
				tempLimitMaxY = tempLimitMaxY > Data.BLOCK_NUMBER_Y*(int)APIX.blockSizeY+(int)APIX.relativeY ? 
								Data.BLOCK_NUMBER_Y*(int)APIX.blockSizeY+(int)APIX.relativeY : 
									tempLimitMaxY;


				lf = ip.etiquetageIntuitifImageGiveListOpti(imageRef, image, seuil, 
															tempLimitMinX,
															tempLimitMaxX,
															tempLimitMinY,
															tempLimitMaxY);
			}
			else
			{
				lf = ip.etiquetageIntuitifImageGiveListOpti(imageRef, image, seuil, 
						(int)APIX.relativeX, Data.BLOCK_NUMBER_X*(int)APIX.blockSizeX+(int)APIX.relativeX, 
						(int)APIX.relativeY, Data.BLOCK_NUMBER_Y*(int)APIX.blockSizeY+(int)APIX.relativeY);
			}

			/***********************************	debug	************************************************/
			/*List<FormObject> lf = null;
			try 
			{
				BufferedImage image1 = null;
				BufferedImage image2 = null;
				String urlImage = "C:/Users/fr�d�ric/Desktop/eclipse/workspace/TraitementImages/res/init/";
				ImageIO.write(imageRef, "jpg", new File(urlImage+ "imageRef_test_" + Data.getDate() + ".jpg"));
				System.out.println("-----");
				System.out.println("-----");
				System.out.println("-----");
				System.out.println("-----");
				System.out.println("lecture image 1");
				image1 = ImageIO.read(new File(urlImage + "TI1.jpg"));
				System.out.println("lecture image 2");
				image2 = ImageIO.read(new File(urlImage + "TI2.jpg"));
				
				ip.imgWidth = image1.getWidth();
				ip.imgHeight = image1.getHeight();

				lf = ip.etiquetageIntuitifImageGiveList2(image1, image2, 40);
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}*/
			
			/**********************************	 fin debug	*************************************************/
			
//			if(WindowGame.getInstance().gameOn)
//				addMovement(compareObjects(lf));
//			else
				addMovement(lf);
			System.out.println("fin du traitement -----------------------------------------------");
			try {
				System.out.println("Attente de "+Data.WAIT_TI/1000+" sec pour le prochain traitement");
				Thread.sleep(Data.WAIT_TI);
				//if(lf != null)
				//	imageRef = image; // permet de faire un traitement de l'image t-1 � t et non imageInit � image t
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void begin() {
		System.out.println("Launch the Image Processing Thread");
		getThread().start();

	}

	public int[][] cutBlackBorder(int[][] elementsRes, int width, int height) {
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				if (elementsRes[x][y] == 255)
					elementsRes[x][y] = 0;
				else
					break;
			}
		}
		for (int x = width - 1; x > 0; --x) {
			for (int y = 0; y < height; ++y) {
				if (elementsRes[x][y] == 255)
					elementsRes[x][y] = 0;
				else
					break;
			}
		}

		for (int x = 0; x < height; ++x) {
			for (int y = 0; y < width; ++y) {
				if (elementsRes[y][x] == 255)
					elementsRes[y][x] = 0;
				else
					break;
			}
		}
		for (int x = height - 1; x > 0; --x) {
			for (int y = 0; y < width; ++y) {
				if (elementsRes[y][x] == 255)
					elementsRes[y][x] = 0;
				else
					break;
			}
		}

		return elementsRes;
	}

	public List<FormObject> compareObjects(List<FormObject> fo)
	{
		int currentSize = GameStage.gameStage.getCurrentPlayer().getSizeCharacter();
		FormObject final_fo = null;
		List<FormObject> lf = new ArrayList<FormObject>();
		int zeroSize = Integer.MAX_VALUE;
		for (FormObject formObject : fo) {
			if(zeroSize < Math.abs(currentSize-formObject.getSurface()))
			{
				zeroSize = Math.abs(currentSize-formObject.getSurface());
				final_fo = formObject;
			}
				
		}
		lf.add(final_fo);
		return lf;
	}

	public void stop() {
		getThread().stop();
	}
}
