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

		root = new Node();

		BSPRec(root, segments, heuristic);
	}

	private void BSPRec(Node root, ArrayList<Segment> segments, int heuristic){

		if(segments.isEmpty())
			return;

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
		root.addSegment(segments.get(split));
		segments.remove(split);
		root.setM(slope(segment));
		root.setP(intercept(segment));

		ArrayList<Segment> left = new ArrayList<Segment>();
		ArrayList<Segment> right = new ArrayList<Segment>();

		split(root.getM(), root.getP(), segments, left, right);

		root.addSegment(segments);

		if(left.size()==0){
			root.setLeft(new Node());
		}
		else if(left.size()==1){
			root.setLeft(new Node(left.get(0)));
		}
		else{
			Node leftSon = new Node();
			BSPRec(leftSon, left, heuristic);
			root.setLeft(leftSon);
		}
		if(right.size()==0){
			root.setRight(new Node());
		}
		else if(right.size()==1){
			root.setRight(new Node(right.get(0)));
		}
		else{
			Node rightSon = new Node();
			BSPRec(rightSon, right, heuristic);
			root.setRight(rightSon);
		}
	}

	private int freeSplit(ArrayList<Segment> segments){
		return 0;
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

	/**
	* Reads a Scene file and initialize x_bound, y_bound and n_segments before returning the segments.
	*
	* @param path 	String, path to the Scene2D file.
	* @return 		ArrayList<String[]> containing all the segment as they are described in the Scene2D file, as Strings.
	*/
	private ArrayList<Segment> openBSPFile(String path){
		ArrayList<Segment> segments = new ArrayList<Segment>();

		try{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			Boolean first_line=true;
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

		} catch(IOException ioe){
			throw new RuntimeException(ioe);
		}

		return segments;
		
	}

	private float slope(Segment segment){
		if(segment.getP1().getX()-segment.getP2().getX() == 0.f)
			return INF;
		else
			return (float)((segment.getP1().getY()-segment.getP2().getY())/(segment.getP1().getX()-segment.getP2().getX()));
	}

	private float intercept(Segment segment){
		float slope = slope(segment);
		if(slope == INF)
			return (float)segment.getP1().getX();
		else
			return (float)(segment.getP1().getY()-slope*segment.getP1().getX());
	}

	/**
	* Splits an ArrayList of Segment into two ArrayLists of Segment given a split line y = mx+p.
	* Segments above that line are added to right ArrayList.
	* Segments under that line are added to left ArrayList.
	* Segments included in that line are left in the ArrayList
	* Segments that are intersecting the line are split and each half is processed individually.
	*/
	private void split(float m, float p, ArrayList<Segment> segments, ArrayList<Segment> left, ArrayList<Segment> right){
		
		for(int i=0; i<segments.size();){

			Segment segment = segments.get(i);
			float line, a, b;

			if(m==INF){
				line = p;
				a = (float)segment.getP1().getX();
				b = (float)segment.getP2().getX();
			}
			else{
				line = 0.f;
				a = (float)(m*segment.getP1().getX()-p-segment.getP1().getY());
				b = (float)(m*segment.getP2().getX()-p-segment.getP2().getY());
			}

			if(a==line && b==line){
				i++;
				continue;
			}
			else if(a>=line && b>=line)
				left.add(segment);
			else if(a<=line && b<=line)
				right.add(segment);
			else{
				Point2D.Float intersect = intersection(segment, m, p);

				if(a>=line){
					left.add(new Segment(segment.getP1(), intersect, segment.getColor()));
					right.add(new Segment(segment.getP2(), intersect, segment.getColor()));
				}
				else{
					right.add(new Segment(segment.getP1(), intersect, segment.getColor()));
					left.add(new Segment(segment.getP2(), intersect, segment.getColor()));
				}
			}

			segments.remove(i);
		}
	}

	private Point2D.Float intersection(Segment segment, Float m, Float p){
		float m2 = slope(segment);
		float p2 = intercept(segment);

		if(m2==INF)
			return new Point2D.Float((float)segment.getP1().getX(), m*p2+p);
		else{
			float intersectionX = (p2-p)/(m-m2);
			float intersectionY = m*intersectionX+p;

			return new Point2D.Float(intersectionX, intersectionY);
		}
	}
}