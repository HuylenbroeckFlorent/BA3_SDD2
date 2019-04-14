import java.util.*;

import java.awt.Color;
import java.awt.geom.Point2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
* Binary Search Partition object class. For more details see '../references/reference.pdf'
*
* @author HUYLENBROECK Florent, DACHY Corentin.
*/
public class BSP{
	private Node root;
	private String path;

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
	private int size=0;
	private int emptyLeaves=0;

	private Random rd = new Random();

	/**
	* @param path 		String, path to the Scene2D file.
	* @param heuristic 	int, type of heuristic to use, see class variables.
	*/
	public BSP(String path, int heuristic)
	throws IOException{
		this.path=path;
		ArrayList<Segment> segments = new ArrayList<Segment>();
		try{
			segments = openBSPFile(path);
		}catch(IOException ioe){
			throw new IOException();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		size=0;
		emptyLeaves=0;

		if(!segments.isEmpty())
			root = BSPRec(segments, heuristic);
		else
			root = new Node();	
	}

	/*
	* Recursively constructs a BSP tree given an heuristic.
	*
	* @param segments 	ArrayList<Segment> containing the segments of the Scene.
	* @param heuristic 	int, heuristic.
	* @return 			Node to be set as the root of the current BSP.
	*/
	private Node BSPRec(ArrayList<Segment> segments, int heuristic){

		size++;

		Node node = new Node();

		if(segments.size()==0){
			emptyLeaves++;
			return node;
		}
		else if(segments.size()==1){
			node.addSegment(segments);
			return node;
		}
		else{

			int split;

			switch(heuristic){
				case 1: split = 0;
				break;
				case 2: split = freeSplit(segments);
				break;
				case 0:
				default: split = rd.nextInt(segments.size());
			}

			Segment segment = segments.get(split);
			node.addSegment(segments.get(split));
			segments.remove(split);

			node.setA(a(segment));
			node.setB(b(segment));
			node.setC(c(segment));

			ArrayList<Segment> left = new ArrayList<Segment>();
			ArrayList<Segment> right = new ArrayList<Segment>();

			split(node.getA(), node.getB(), node.getC(), segments, left, right);

			node.addSegment(segments);

			node.setLeft(BSPRec(left, heuristic));
			node.setRight(BSPRec(right, heuristic));

			return node;
		}
	}

	/*
	* Finds if any segment within a list is a valid candidate for the FREE_SPLITS heuristic.
	*
	* @param segments 	ArrayList<Segment>, the list containing the Segments.
	* @return 			int, index of the first candidate found, and if no candidate are found, a random index.
	*/
	private int freeSplit(ArrayList<Segment> segments){
		for(int i=0; i<segments.size(); i++){
			Segment seg = segments.get(i);
			if(seg.isFreeSplit())
				return i;
		}
		return rd.nextInt(segments.size());
	}

	/**
	* Returns the root of the BSP.
	*
	* @return 	Node, root of the BSP.
	*/
	public Node getRoot(){
		return root;
	}

	/**
	* Returns the bound for the x axis. Every value is contained in [-x_bound;x_bound].
	*
	* @return int, bound for the x axis of the scene.
	*/
	public int getXBound(){
		return x_bound;
	}

	/**
	* Returns the bound for the y axis. Every value is contained in [-y_bound;y_bound].
	*
	* @return int, bound for the y axis of the scene.
	*/
	public int getYBound(){
		return y_bound;
	}

	/**
	* Returns the number of segment contained in the BSP.
	*
	* @return int, number of segments contained in the scene.
	*/
	public int getNSegments(){
		return n_segments;
	}

	/**
	* Returns the path to the Scene2D file.
	*
	* @return 	String, the path to SCene 2D file.
	*/
	public String getPath(){
		return path;
	}

	public static String getHeuristic(int i){
		switch(i){
			case 2: return "FREE_SPLITS";
			case 1: return "ORDERED";
			default: return "RANDOM";
		}
	}

	/*
	* Reads a Scene file and initialize x_bound, y_bound and n_segments before returning the segments.
	*
	* @param path 	String, path to the Scene2D file.
	* @throws 		IOException if the file doesn't exists or if it is not a Scene2D file.
	* @return 		ArrayList<String[]> containing all the segment as they are described in the Scene2D file, as Strings.
	*/
	private ArrayList<Segment> openBSPFile(String path)
	throws IOException{
		ArrayList<Segment> segments = new ArrayList<Segment>();

		try{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			boolean first_line=true;
			try{
				while((line=reader.readLine())!=null){
					String[] words = line.split(" ");
					if(first_line){
						x_bound=Integer.parseInt(words[1]); // skips useless first ">" char
						y_bound=Integer.parseInt(words[2]);
						n_segments=Integer.parseInt(words[3]);
						first_line=false;
					}
					else{
						segments.add(new Segment(line.split(" ")));
					}
					
				}
			} catch(NumberFormatException nfe){
				throw new IOException();
			} catch(ArrayIndexOutOfBoundsException aioobe){
				throw new IOException();
			}

		} catch(IOException ioe){
			throw new IOException();
		}

		return segments;
		
	}

	/*
	* Computes the a coeficient from a 2D line equation : ax+by+c=0.
	* Given P1(x1,y1) and P2(x2,y2) two points, the formula is :
	* a = (y1-y2).
	*
	*
	* @param segment 	Segment to compute the coeficient from.
	* @return 			float, the a coeficient.
	*/
	private float a(Segment segment){
		return (float)(segment.getP1().getY()-segment.getP2().getY());
	}

	/*
	* Computes the b coeficient from a 2D line equation : ax+by+c=0.
	* Given P1(x1,y1) and P2(x2,y2) two points, the formula is :
	* b = (x2-x1).
	*
	* @param segment 	Segment to compute the coeficient from.
	* @return 			float, the b coeficient.
	*/
	private float b(Segment segment){
		return (float)(segment.getP2().getX()-segment.getP1().getX());
	}

	/*
	* Computes the c coeficient from a 2D line equation : ax+by+c=0.
	* Given P1(x1,y1) and P2(x2,y2) two points, the formula is :
	* c = (x1y2-x2y1).
	*
	* @param segment 	Segment to compute the coeficient from.
	* @return 			float, the c coeficient.
	*/
	private float c(Segment segment){
		return (float)(segment.getP1().getX()*segment.getP2().getY()-segment.getP2().getX()*segment.getP1().getY());
	}

	/*
	* Splits an ArrayList of Segment into two ArrayLists of Segment given a split line ax+by+c=0.
	* Segments above that line are added to plus ArrayList.
	* Segments under that line are added to minus ArrayList.
	* Segments included in that line are left in the ArrayList segments.
	* Segments that are intersecting the line are split and each half is processed individually.
	*
	* @param a 			float, a coeficient of the 2D line equation ax+by+c=0.
	* @param b 			float, b coeficient of the 2D line equation ax+by+c=0.
	* @param c 			float, c coeficient of the 2D line equation ax+by+c=0.
	* @param segments 	ArrayList<Segment>, initial ArrayList of segments. Segments lying on the splitting line will remain in this list.
	* @param minus		ArrayList<Segment> to which the segments under the splitting line will be added.
	* @param plus 		ArrayList<Segment> to which the segments above the splitting line will be added.
	*/
	private void split(float a, float b, float c, ArrayList<Segment> segments, ArrayList<Segment> minus, ArrayList<Segment> plus){
		
		for(int i=0; i<segments.size();){

			Segment segment = segments.get(i);

			float p1 = (float) (a*segment.getP1().getX()+b*segment.getP1().getY()+c);
			float p2 = (float) (a*segment.getP2().getX()+b*segment.getP2().getY()+c);

			if(p1==0f && p2==0f){
				i++;
				continue;
			}
			else{
				if(p1==0f)
					segment.p1IntersectsBorder(true);
				if(p2==0f)
					segment.p2IntersectsBorder(true);
				if(p1>=0f && p2>=0f){
					plus.add(segment);
				}
				else if(p1<=0f && p2<=0f){
					minus.add(segment);
				}
				else{
					Point2D.Float intersect = intersection(segment, a, b, c);

					Segment s1 = new Segment(segment.getP1(), intersect, segment.getColor());
					s1.p2IntersectsBorder(true);
					Segment s2 = new Segment(intersect, segment.getP2(), segment.getColor());
					s2.p1IntersectsBorder(true);

					if(p1>=0f){
						plus.add(s1);
						minus.add(s2);
					}
					else{
						minus.add(s1);
						plus.add(s2);
					}
				}
			}
			segments.remove(i);
		}
	}

	/*
	* Computes the intersection between two 2D lines.
	* Given d1:a1x+b1y+c1=0 and d2:a2x+b2y+c2=0 two 2D lines, the formula is :
	* intersection = ( (c1b2-c2b1)/(b1a2-b2a1) ; (a1c2-a2c1)/(b1a2-b2a1) ).
	*
	* @param segment 	Segment, first 2D line.
	* @param a 			float, a coeficient of the second 2D line.
	* @param b 			float, b coeficient of the second 2D line.
	* @param c 			float, c coeficient of the second 2D line.
	* @return 			Point2D.Float, intersection point of the two 2D lines.
	*/
	private Point2D.Float intersection(Segment segment, float a, float b, float c){
		float a2 = a(segment);
		float b2 = b(segment);
		float c2 = c(segment);

		return new Point2D.Float((c*b2-c2*b)/(b*a2-b2*a),(a*c2-c*a2)/(b*a2-b2*a));
	}

	/**
	* Computes the height of the BSP.
	*
	* @return 	int, height of the BSP.
	*/
	public int height(){
		return root!=null ? heightRec(root, 1) : 0;
	}

	/*
	* Recursively computes the height of the tree.
	*
	* @param root 	Node, root of the current sub-BSP.
	* @param i 		int, current height.
	* @return 		int, computed height of the current sub-BSP.
	*/
	private int heightRec(Node root, int i){
		if(!root.hasRight() && !root.hasLeft()){
			return i;
		}
		else{
			i++;
			if(root.hasRight() && root.hasLeft()){
				return Math.max(heightRec(root.getRight(), i), heightRec(root.getLeft(), i));
			}
			else if(root.hasRight()){
				return heightRec(root.getRight(), i);
			}
			else{
				return heightRec(root.getLeft(), i);
			}
		}
	}

	/**
	* Gives the size of the BSP.
	*
	* @return 	int, size of the BSP.
	*/
	public int size(){
		return size;
	}

	/**
	* Gives the number of empty leaves in the BSP.
	*
	* @return 	int, number of empty leaves in the BSP.
	*/
	public int emptyLeaves(){
		return emptyLeaves;
	}

	/**
	* Applies the painter's algorithm to the BSP.
	*
	* @param eyeX 			float, position of the eye along the x axis.
	* @param eyeY 			float, position of the eye along the y axis.
	* @param toScanConvert 	ArrayList<Segment> containing the Segments to draw in the correct drawing order.
	*/
	public void painter(float eyeX, float eyeY, ArrayList<Segment> toScanConvert){
		painterRec(root, eyeX, eyeY, toScanConvert);
	}

	/*
	* Recursively applies the painter's algorithm to the BSP.
	*
	* @param root 			Node, root of the current sub-BSP.
	* @param eyeX 			float, position of the eye along the x axis.
	* @param eyeY 			float, position of the eye along the y axis.
	* @param toScanConvert 	ArrayList<Segment> containing the Segments to draw in the correct drawing order.
	*/
	private void painterRec(Node root, float eyeX, float eyeY, ArrayList<Segment> toScanConvert){
		if(root.isLeaf()){
				for(Iterator i=root.getData(); i.hasNext();){
					toScanConvert.add((Segment)i.next());
				}
			}
			else{
				float a = root.getA();
				float b = root.getB();
				float c = root.getC();
				float h = a*eyeX+b*eyeY+c;

				if(h>0.0f){
					painterRec(root.getLeft(), eyeX, eyeY, toScanConvert);
					for(Iterator i=root.getData(); i.hasNext();){
						toScanConvert.add((Segment)i.next());
					}
					painterRec(root.getRight(), eyeX, eyeY, toScanConvert);
				}
				else if(h<0.0f){
					painterRec(root.getRight(), eyeX, eyeY, toScanConvert);
					for(Iterator i=root.getData(); i.hasNext();){
						toScanConvert.add((Segment)i.next());
					}
					painterRec(root.getLeft(), eyeX, eyeY, toScanConvert);
				}
				else{
					painterRec(root.getRight(), eyeX, eyeY, toScanConvert);
					painterRec(root.getLeft(), eyeX, eyeY, toScanConvert);
				}
			}
	}
}