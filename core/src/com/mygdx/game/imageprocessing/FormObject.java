package com.mygdx.game.imageprocessing;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;


public class FormObject {
	int imgHeight;
	int imgWidth;
	List<Pixel> pixelList;
	int[][] matrix;
	int perimeter;
	int surface;
	int sigmaX;
	int sigmaY;
	Pixel gravityCenterCarre;
	Pixel baryCenter;
	typeForm myTypeForm;
	
	public enum typeForm{
		carre,
		rectangle,
		triangle,
		cercle,
		obiwanKenobi;
	}

	public FormObject(List<Pixel> pixelList, int imgHeight, int imgWidth) {
		super();
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.pixelList = pixelList;
		this.surface = getSurface();
		//this.gravityCenterCarre = getCenterCarreForm(pixelList);
		this.baryCenter = getBaryCenterForm(pixelList);
		getEcartTypeForm(pixelList);
		recreateMatrix();
//		findObjectType();
	}

	public void recreateMatrix(){
		matrix = new int[imgWidth][imgHeight];
		for(int i = 0; i < matrix.length; i++){
			for(int j = 1; j < matrix[i].length; j++){
				matrix[i][j] = 0;
			}
		}
		System.out.println("pixel list.size " + pixelList.size());
		for (Pixel pixel : pixelList) {
			matrix[pixel.getX()][pixel.getY()] = 255;	
		}	
	}
	
	/*
	 * Calcul de la compacit�
	 */
	public double calculCompacite(int perimeter, int surface) {
		double compacite;
		if(perimeter == 0)
			return 0;
		compacite = 4*Math.PI*(surface/(perimeter*perimeter));
		double t = perimeter*perimeter;
//		System.out.printf ("perimeter� = %f", t);
//		System.out.println();
		t = surface/t;
//		System.out.printf ("surface/perimeter� = %f", t);
//		System.out.println();
		double pi = 4*Math.PI;
//		System.out.printf ("4*Math.PI = %f", pi);
//		System.out.println();
		compacite = pi*t;
//		System.out.printf ("%f", compacite);
//		System.out.println();

//		System.out.println("compacit� de : "+compacite + " perimetre de : "+perimeter + " surface de : "+surface);
		return compacite;
	}
	
	/*
	 * Trouver type d'objet en fonction de la compacit�
	 */
	public void findObjectType() {
		double compacite = calculCompacite(perimeter, surface);
		if(compacite > 0.85)
		{	
			myTypeForm = typeForm.cercle;
		}
		else if (compacite <= 0.85 && compacite >0.65)
		{	
			myTypeForm = typeForm.rectangle;
		}
		else if (compacite <= 0.65 && compacite >= 0.45)
		{	
			myTypeForm = typeForm.triangle;
		}
		else
		{
			myTypeForm = typeForm.obiwanKenobi;
		}
//		System.out.println("objet de type "+myTypeForm.toString());

	}
	
	/*
	 * R�cup�re le centre de gravit� d'un ensemble de points d�sign�s par une �tiquette. 
	 * On repr�sente un carr� th�orique dont on calcule le barycentre.
	 * L'id�e n'est pas bonne si un point de balade.
	 */
/*	public Pixel getCenterCarreForm(List<Pixel>PointsInterests){
		int up, down, left, right;
		up = PointsInterests.get(0).getX();
		down = PointsInterests.get(0).getX();
		right= PointsInterests.get(0).getX();
		left = PointsInterests.get(0).getX();
		
		for (Pixel pixel : PointsInterests) {
			up = (up<pixel.getX())? up:pixel.getX();
			down = (down>pixel.getX())? down:pixel.getX();
			left = (left<pixel.getY())? left:pixel.getY();
			right = (right>pixel.getY())? right:pixel.getY();
		}		
		System.out.println("up : " + up + " down : " + down +" left : " + left +" right : " + right);
		Pixel gravityCenterCarre = new Pixel(((right+left)/2), ((down+up)/2));
		return gravityCenterCarre;
	}
 */
	
	/*
	 * R�cup�re le centre de gravit�
	 */
	public Pixel getBaryCenterForm(List<Pixel>PointsInterests){
		int gx, gy;
		gx = 0; gy = 0;
		for (Pixel pixel : PointsInterests) {
			gx += pixel.getX();
			gy += pixel.getY();
		}	
		Pixel gravityBaryCenter = new Pixel((gx/PointsInterests.size()),(gy/PointsInterests.size()));
		return gravityBaryCenter;
	}
	
	/*
	 * Calcul de l'�cart Type
	 */
	public void getEcartTypeForm(List<Pixel>PointsInterests) {
		int sX = 0;
		int sY = 0;
		for (Pixel pixel : PointsInterests) {
			sX +=Math.pow((pixel.getY()-baryCenter.getY()),2);
			sY += Math.pow((pixel.getX()-baryCenter.getX()),2);
		}
		this.sigmaX =  (int)Math.sqrt(sX/PointsInterests.size());
		this.sigmaY =  (int)Math.sqrt(sY/PointsInterests.size());
	}
	
	/*
	 * Affichage d'une matrice
	 */
	@SuppressWarnings("unused")
	private void display(int[][] myMatrix) {
		for(int i = 0; i < myMatrix.length; i++)
		{
			for(int j = 0; j < myMatrix[i].length; j++)
			{
				System.out.print(myMatrix[i][j]+" ");
			}
			System.out.println();
		}
	}
		
	/*
	 * Affiche tous les �l�ments d'une liste
	 */
	@SuppressWarnings("unused")
	private void displayList(List<Pixel> myList) {
		System.out.println("test");
		for (Pixel pixel : myList) {
			System.out.print(pixel.getX() +" "+ pixel.getY());
			System.out.println();
		}
	}
	
	//********************************** Getters & Setters ************************************//
	/*
	 * R�cup�ration de la surface de la forme
	 */
	public int getSurface() {
		return pixelList.size();
	}
	public int getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}
	public int getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}
	public int[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	public List<Pixel> getPixelList() {
		return pixelList;
	}
	public void setPixelList(List<Pixel> pixelList) {
		this.pixelList = pixelList;
	}
	public int getPerimeter() {
		return perimeter;
	}
	public void setPerimeter(int perimeter) {
		this.perimeter = perimeter;
	}
	public void setSurface(int surface) {
		this.surface = surface;
	}
	public Pixel getBaryCenter() {
		return baryCenter;
	}
	public Pixel getGravityCenter() {
		return gravityCenterCarre;
	}
	public void setGravityCenter(Pixel gravityCenter) {
		this.gravityCenterCarre = gravityCenter;
	}
	public typeForm getMyTypeForm() {
		return myTypeForm;
	}
	public void setMyTypeForm(typeForm myTypeForm) {
		this.myTypeForm = myTypeForm;
	}
	@Override
	public String toString() {
		return "FormObject [imgHeight=" + imgHeight + ", imgWidth=" + imgWidth
				+ ", pixelList=" + pixelList + ", perimeter=" + perimeter
				+ ", surface=" + surface + ", sigmaX=" + sigmaX + ", sigmaY="
				+ sigmaY + ", baryCenter.X =" + baryCenter.getX() + ", baryCenter.Y =" + baryCenter.getY() 
				+ ", myTypeForm=" + myTypeForm + "]";
	}	
}
