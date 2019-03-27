import java.util.*;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
* Represents a segment contained in a Node.
*
* @author HUYLENBROECK Florent
*/
public class Segment{
	private Point2D.Float p1, p2;
	private Color color;

	/**
	* @param x1 	float, x coordinate for the first extremity of the Segment.
	* @param y1 	float, y coordinate for the first extremity of the Segment.
	* @param x2 	float, x coordinate for the second extremity of the Segment.
	* @param y2 	float, y coordinate for the second extremity of the Segment.
	* @param color 	Color of the Segment.
	*/
	public Segment(float x1, float y1, float x2, float y2, Color color){
		p1=new Point2D.Float(x1, y1);
		p2=new Point2D.Float(x2, y2);
		this.color=color;
	}

	public Segment(Point2D.Float p1, Point2D.Float p2, Color color){
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
	}

	public Segment(String[] fromFile){
		p1=new Point2D.Float(Float.parseFloat(fromFile[0]),Float.parseFloat(fromFile[1]));
		p2=new Point2D.Float(Float.parseFloat(fromFile[2]),Float.parseFloat(fromFile[3]));
		switch(fromFile[4]){
			case "Bleu": color=Color.BLUE;
			break;
			case "Rouge": color=Color.RED;
			break;
			case "Orange": color=Color.ORANGE;
			break;
			case "Jaune": color=Color.YELLOW;
			break;
			case "Noir": color=Color.BLACK;
			break;
			case "Violet": color=Color.MAGENTA;
			break;
			case "Marron": color=Color.DARK_GRAY;
			break;
			case "Vert": color=Color.GREEN;
			break;
			case "Gris": color=Color.LIGHT_GRAY;
			break;
			case "Rose": color=Color.PINK;
			break;
			default: color=Color.WHITE;
		}
	}

	/**
	* Returns the coordinates of the first extremity of the segment.
	*
	* @return 	Point2D.Float first extremity of the segment.
	*/
	public Point2D.Float getP1(){
		return p1;
	}

	/**
	* Sets the coordinates for the first extremity of the segment.
	*
	* @param x1 	float, x coordinate for the first extremity of the Segment.
	* @param y1 	float, y coordinate for the first extremity of the Segment.
	*/
	public void setP1(float x1, float y1){
		p1=new Point2D.Float(x1, y1);
	}

	/**
	* Returns the coordinates of the second extremity of the segment.
	*
	* @return 	Point2D.Float second extremity of the segment.
	*/
	public Point2D.Float getP2(){
		return p2;
	}

	/**
	* Sets the coordinates for the second extremity of the segment.
	*
	* @param x2 	float, x coordinate for the second extremity of the Segment.
	* @param y2 	float, y coordinate for the second extremity of the Segment.
	*/
	public void setP2(float x2, float y2){
		p2=new Point2D.Float(x2, y2);
	}

	/**
	* Returns the color fo the segment.
	*
	* @return 	Color of the Segment.
	*/
	public Color getColor(){
		return color;
	}

	/**
	* Sets the color of the segment.
	*
	* @param color 	Color to set.
	*/
	public void setColor(Color color){
		this.color=color;
	}

	public String toString(){
		return "Segment extremities : "+p1.toString()+" & "+p2.toString()+" // color : "+color.toString();
	}
}