import java.util.*;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
* Represents a node of the BSP tree
*
* @author HUYLENBROECK Florent
*/
public class Node{

	private LinkedList<Segment> data;
	private Node left;
	private Node right;
	private float a, b, c;

	public Node(){
		data=new LinkedList<Segment>();
	}

	/**
	* Tells if the Node has a set left son.
	*
	* @return 	boolean, true if the left son is set and false otherwise.
	*/
	public boolean hasLeft(){
		return left!=null;
	}

	/**
	* Returns the left son of the Node.
	*
	* @return 	Node, left son.
	*/
	public Node getLeft(){
		return left;
	}

	/**
	* Sets the left son of the Node.
	*
	* @param left 	Node to set as the left son.
	*/
	public void setLeft(Node left){
		this.left=left;
	}

	/**
	* Tells if the Node has a set right son.
	*
	* @return 	boolean, true if the right son is set and false otherwise.
	*/
	public boolean hasRight(){
		return right!=null;
	}

	/**
	* Returns the right son of the Node.
	*
	* @return 	Node, right son.
	*/
	public Node getRight(){
		return right;
	}

	/**
	* Sets the right son of the Node.
	*
	* @param right 	Node to set as the right son.
	*/ 
	public void setRight(Node right){
		this.right=right;
	}

	/**
	* Returns an iterator for the LinkedList of the segments contained in the Node.
	*
	* @return 	Iterator for the segment list.
	*/
	public Iterator getData(){
		return data.iterator();
	}

	/**
	* Adds a segment to a Node.
	*
	* @param segment  	Segment to add.
	*/
	public void addSegment(Segment segment){
		data.add(segment);
	}

	/**
	* Adds mutiple segments from an ArrayList to a Node.
	*
	* @param segments 	ArrayList<Segment> containing the Segments to add.
	*/
	public void addSegment(ArrayList<Segment> segments){
		for(Segment segment : segments){
			data.add(segment);
		}
	}

	/**
	* Tells if the Node is a leaf or an inner Node.
	*
	* @return 	boolean, true if the Node is a Leaf.
	*/
	public boolean isLeaf(){
		return(left==null && right==null);
	}
	
	/**
	* Gives the a coeficient of the 2D splitting line equation ax+by+c=0 contained in the Node.
	*
	* @return 	float, a coeficient.
	*/
	public float getA(){
		return a;
	}		

	/**
	* Sets the a coeficient of the 2D splitting line equation ax+by+c=0 contained in the Node.
	*
	* @param a 	float, a coeficient.
	*/
	public void setA(float a){
		this.a = a;
	}

	/**
	* Gives the b coeficient of the 2D splitting line equation ax+by+c=0 contained in the Node.
	*
	* @return 	float, b coeficient.
	*/
	public float getB(){
		return b;
	}		

	/**
	* Sets the b coeficient of the 2D splitting line equation ax+by+c=0 contained in the Node.
	*
	* @param b 	float, b coeficient.
	*/
	public void setB(float b){
		this.b = b;
	}

	/**
	* Gives the c coeficient of the 2D splitting line equation ax+by+c=0 contained in the Node.
	*
	* @return 	float, c coeficient.
	*/
	public float getC(){
		return c;
	}		

	/**
	* Sets the c coeficient of the 2D splitting line equation ax+by+c=0 contained in the Node.
	*
	* @param c 	float, c coeficient.
	*/
	public void setC(float c){
		this.c = c;
	}

	public String toString(){
		String ret;

		if(isLeaf())
			ret = "Leaf";
		else
			ret = "Node";
		ret += " with line at y="+a+"x+"+b+ " contains : \n";
		for(Iterator i = getData(); i.hasNext();){
			ret+="\t";
			ret+=i.next().toString();
			ret+="\n";
		}
		return ret;
	}
}