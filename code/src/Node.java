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
	private float m, p;

	public Node(){
		data=new LinkedList<Segment>();
	}

	public Node(Segment segment){
		this();
		addSegment(segment);
		left=null;
		right=null;
		m=0.f;
		p=0.f;
	}

	/**
	* @param segment 	Segment to initialize the Node with.
	* @param left 		Node, left son.
	* @param right 		Node, right son.
	* @param m 			float, slope of the 2D line that the Node describes.
	* @param p 			float, intercept of the 2D line that the Node describes.
	*/
	public Node(Segment segment, Node left, Node right, float m, float p){
		this();
		addSegment(segment);
		this.left=left;
		this.right=right;
		this.m=m;
		this.p=p;
	}

	/**
	* @param segments 	ArrayList<Segment> to initialize the Node with.
	* @param left 		Node, left son.
	* @param right 		Node, right son.
	* @param m 			float, slope of the 2D line that the Node describes.
	* @param p 			float, intercept of the 2D line that the Node describes.
	*/
	public Node(ArrayList<Segment> segments, Node left, Node right, float m, float p){
		this();
		for(Segment segment : segments){
			addSegment(segment);
		}
		this.left=left;
		this.right=right;
		this.m=m;
		this.p=p;
	}

	public Boolean hasLeft(){
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

	public Boolean hasRight(){
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
	* @return 	Boolean, true if the Segment has been added successfuly (useful in Leaf sub-class)
	*/
	public Boolean addSegment(Segment segment){
		data.add(segment);
		return true;
	}

	public Boolean addSegment(ArrayList<Segment> segments){
		for(Segment segment : segments){
			data.add(segment);
		}
		return true;
	}

	/**
	* Clears all the segments contained in a Node.
	*/
	public void clearSegments(){
		data=new LinkedList<Segment>();
	}

	/**
	* Returns the number of segment contained in a Node.
	*
	* @return 	int, number of segments.
	*/
	public int getSize(){
		return data.size();
	}

	/**
	* Tells if the Node is a leaf or an inner Node.
	*
	* @return 	Boolean, true if the Node is a Leaf.
	*/
	public Boolean isLeaf(){
		return(left==null && right==null);
	}
	
	public float getM(){
		return m;
	}		

	public void setM(float m){
		this.m = m;
	}

	public float getP(){
		return p;
	}

	public void setP(float p){
		this.p = p;
	}

	public String toString(){
		String ret;

		if(isLeaf())
			ret = "Leaf";
		else
			ret = "Node";
		ret += " with line at y="+m+"x+"+p+ " contains : \n";
		for(Iterator i = getData(); i.hasNext();){
			ret+="\t";
			ret+=i.next().toString();
			ret+="\n";
		}
		return ret;
	}
}