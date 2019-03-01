import java.util.*;

import java.awt.Color;
import java.awt.geom.Point2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
* Binary Search Partition object class. For more details see '../references/reference.pdf'
*
* @author HUYLENBROECK Florent, DACHY Corentin
*/
class BSP{
	private Node root;

	/**
	* Random heuristic constant.
	*/
	public static final int RANDOM = 0;
	/**
	* Ordered heuristic constant.
	*/
	public static final int ORDERED = 1;
	/**
	* Free splits heuristic constant.
	*/
	public static final int FREE_SPLITS = 2; 

	private int x_bound;
	private int y_bound;
	private int n_segments;

	public BSP(String path, int heuristic)
	throws IllegalHeuristicException{
		ArrayList<String[]> segments = openBSPFile(path);

		switch(heuristic){
			case 0: randomHeuristicBSP(segments);
			break;
			case 1: orderedHeuristicBSP(segments);
			break;
			case 2: freeSplitsHeuristicBSP(segments);
			break;
			default: throw new IllegalHeuristicException("That heuristic doesn't exist");
		}
	}

	private void randomHeuristicBSP(ArrayList<String[]> segments){

	}

	private void orderedHeuristicBSP(ArrayList<String[]> segments){

	}

	private void freeSplitsHeuristicBSP(ArrayList<String[]> segments){

	}

	/**
	* Returns the root of the BSP.
	*/
	public Node getRoot(){
		return root;
	}

	/**
	* Returns the bound for the x axis. Every value is contained in [-x_bound;x_bound].
	*/
	public int getXBound(){
		return x_bound;
	}

	/**
	* Returns the bound for the y axis. Every value is contained in [-y_bound;y_bound].
	*/
	public int getYBound(){
		return y_bound;
	}

	/**
	* Returns the number of segment contained in the BSP.
	*/
	public int getNSegments(){
		return n_segments;
	}

	/**
	* Reads a Scene file and initialize x_bound, y_bound and n_segments before returning the segments.
	*/
	private ArrayList<String[]> openBSPFile(String path){
		ArrayList<String[]> segments = new ArrayList<String[]>();

		try{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			boolean first_line=true;
			while((line=reader.readLine())!=null){
				String[] words = line.split(" ");
				if(first_line){
					x_bound=Integer.parseInt(words[1]); // skip useless first ">" char
					y_bound=Integer.parseInt(words[2]);
					n_segments=Integer.parseInt(words[3]);
					first_line=false;
				}
				else{
					segments.add(line.split(" "));
				}
				
			}

		} catch(IOException ioe){
			throw new RuntimeException(ioe);
		}

		return segments;
		
	}

	/**
	* Represents a node of the BSP tree
	*
	* @author HUYLENBROECK Florent
	*/
	class Node{
		private LinkedList<Segment> segments;
		private Node left;
		private Node right;

		public Node(){
			segments=new LinkedList<Segment>();
		}

		public Node(Segment segment){
			this();
			segments.add(segment);
		}

		public Node(Segment segment, Node left, Node right){
			this(segment);
			this.left=left;
			this.right=right;
		}

		/**
		* Returns the left son of the Node.
		*/
		public Node getLeft(){
			return left;
		}

		/**
		* Sets the left son of the Node.
		*/
		public void setLeft(Node left){
			this.left=left;
		}

		/**
		* Returns the right son of the Node.
		*/
		public Node getRight(){
			return right;
		}

		/**
		* Sets the right son of the Node.
		*/ 
		public void setRight(Node right){
			this.right=right;
		}

		/**
		* Returns an iterator for the LinkedList of the segments contained in the Node.
		*/
		public Iterator getSegments(){
			return segments.iterator();
		}

		/**
		* Adds a segment to a Node.
		*/
		public void addSegment(Segment segment){
			segments.add(segment);
		}

		/**
		* Clears all the segments contained in a Node.
		*/
		public void clearSegments(){
			segments=new LinkedList<Segment>();
		}

		/**
		* Returns the number of segment contianed in a Node.
		*/
		public int getSize(){
			return segments.size();
		}

		/**
		* Tells if the Node is a leaf or an inner Node.
		*/
		public boolean isLeaf(){
			return(left.equals(null) && right.equals(null));
		}
	}

	/**
	* Represents a segment contained in a Node.
	*
	* @author HUYLENBROECK Florent
	*/
	class Segment{
		private Point2D.Float p1, p2;
		private Color color;

		public Segment(float x1, float y1, float x2, float y2, Color color){
			p1=new Point2D.Float(x1, y1);
			p2=new Point2D.Float(x2, y2);
			this.color=color;
		}

		/**
		* Returns the coordinates of the first extremity of the semgent.
		*/
		public Point2D.Float getP1(){
			return p1;
		}

		/**
		* Sets the coordinates for the first extremity of the segment.
		*/
		public void setP1(float x1, float y1)
		throws OutOfSceneException{
			if(x1<=x_bound && x1>=-x_bound && y1<=y_bound && y1>=-y_bound)
				p1=new Point2D.Float(x1, y1);
			else
				throw new OutOfSceneException("Out of scene's bounds");
		}

		/**
		* Returns the coordinates of the second extremity of the semgent.
		*/
		public Point2D.Float getP2(){
			return p2;
		}

		/**
		* Sets the coordinates for the second extremity of the segment.
		*/
		public void setP2(float x2, float y2)
		throws OutOfSceneException{
			if(x2<=x_bound && x2>=-x_bound && y2<=y_bound && y2>=-y_bound)
				p2=new Point2D.Float(x2, y2);
			else
				throw new OutOfSceneException("Out of scene's bounds");
		}

		/**
		* Returns the color fo the segment.
		*/
		public Color getColor(){
			return color;
		}

		/**
		* Sets the color of the segment.
		*/
		public void setColor(Color color){
			this.color=color;
		}
	}

	/**
	* Exception class for when an Illegal heuristic argument is passed to BSP constructor.
	*
	* @author HUYLENBROECK Florent
	*/
	public class IllegalHeuristicException extends Exception{
		public IllegalHeuristicException(String message){
			super(message);
		}
	}

	/**
	* Exception class for when a point is trying to be set ouf of the scene bound's
	*
	* @author HUYLENBROECK Florent
	*/
	public class OutOfSceneException extends Exception{
		public OutOfSceneException(String message){
			super(message);
		}
	}
}