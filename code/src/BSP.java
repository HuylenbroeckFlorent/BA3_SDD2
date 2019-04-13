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
	* Sentinel value for slope.
	*/
	public static final float INF = (float)Float.MAX_VALUE;

	/**
	* @param path 		String, path to the Scene2D file.
	* @param heuristic 	int, type of heuristic to use, see class variables.
	*/
	public BSP(String path, int heuristic){
		this.path=path;
		ArrayList<Segment> segments = openBSPFile(path);

		size=0;
		emptyLeaves=0;

		if(!segments.isEmpty())
			root = BSPRec(segments, heuristic);
		else
			root = new Node();	
	}

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

	/**
	* Reads a Scene file and initialize x_bound, y_bound and n_segments before returning the segments.
	*
	* @param path 	String, path to the Scene2D file.
	* @return 		ArrayList<String[]> containing all the segment as they are described in the Scene2D file, as Strings.
	*/
	private ArrayList<Segment> openBSPFile(String path)
	throws NumberFormatException{
		ArrayList<Segment> segments = new ArrayList<Segment>();

		try{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			Boolean first_line=true;
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
				throw nfe;
			}

		} catch(IOException ioe){
			throw new RuntimeException(ioe);
		}

		return segments;
		
	}

	private float a(Segment segment){
		return (float)(segment.getP1().getY()-segment.getP2().getY());
	}

	private float b(Segment segment){
		return (float)(segment.getP2().getX()-segment.getP1().getX());
	}

	private float c(Segment segment){
		return (float)(segment.getP1().getX()*segment.getP2().getY()-segment.getP2().getX()*segment.getP1().getY());
	}

	/**
	* Splits an ArrayList of Segment into two ArrayLists of Segment given a split line y = mx+p.
	* Segments above that line are added to plus ArrayList.
	* Segments under that line are added to minus ArrayList.
	* Segments included in that line are left in the ArrayList segments.
	* Segments that are intersecting the line are split and each half is processed individually.
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

	private Point2D.Float intersection(Segment segment, float a, float b, float c){
		float a2 = a(segment);
		float b2 = b(segment);
		float c2 = c(segment);

		return new Point2D.Float((c*b2-c2*b)/(b*a2-b2*a),(a*c2-c*a2)/(b*a2-b2*a));
	}

	public int height(){
		return root!=null ? heightRec(root, 1) : 0;
	}

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

	public int size(){
		return size;
	}

	public int emptyLeaves(){
		return emptyLeaves;
	}
}