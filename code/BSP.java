import java.util.*;
import java.awt.Color;

class BSP{
	Node root;

	public BSP(String path){
		//TODO
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