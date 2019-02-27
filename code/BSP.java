import java.util.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class BSP{
	Node root;

	private int x_bound;
	private int y_bound;
	private int n_segments;

	public BSP(String path){
		ArrayList<String[]> segments = openBSPFile(path);
	}

	public int getXBound(){
		return x_bound;
	}

	public int getYBound(){
		return y_bound;
	}

	public int getNSegments(){
		return n_segments;
	}

	/**
	* Reads a Scene file and initialize x_bound, y_bound and n_segments before returning the segments
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

		public Node getLeft(){
			return left;
		}

		public void setLeft(Node left){
			this.left=left;
		}

		public Node getRight(){
			return right;
		}

		public void setRight(Node right){
			this.right=right;
		}

		public Iterator getSegments(){
			return segments.iterator();
		}

		public void addSegment(Segment segment){
			segments.add(segment);
		}

		public void clearSegments(){
			segments=new LinkedList<Segment>();
		}

		public int getSize(){
			return segments.size();
		}
	}

	class Segment{
		private float x1, y1, x2, y2;
		private Color color;

		public Segment(float x1, float y1, float x2, float y2, Color color){
			this.x1=x1;
			this.y1=y1;
			this.x2=x2;
			this.y2=y2;
			this.color=color;
		}

		public float getX1(){
			return x1;
		}

		public void setX1(float x1){
			this.x1=x1;
		}

		public float getY1(){
			return y1;
		}

		public void setY1(float y1){
			this.y1=y1;
		}

		public float getX2(){
			return x2;
		}

		public void setX2(float x2){
			this.x2=x2;
		}

		public float getY2(){
			return y2;
		}

		public void setY2(float y2){
			this.y2=y2;
		}

		public Color getColor(){
			return color;
		}

		public void setColor(Color color){
			this.color=color;
		}
	}
}