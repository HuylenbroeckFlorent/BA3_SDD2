import java.util.*;

import javax.swing.*;

import java.awt.GraphicsConfiguration;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;

class BSPApp{
	private JFrame frame;

	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int screenHeight = screenSize.height;
	private static final int screenWidth = screenSize.width;

	public static void main(String[] args){

		BSP test = new BSP("./../../resources/Scenes/ellipses/ellipsesSmall.txt", BSP.ORDERED);

		JFrame frame = new JFrame();

		frame.setTitle("BSP Viewer - HUYLENBROECK Florent & DACHY Corentin");
		frame.setPreferredSize(new Dimension(2*screenWidth/3, 2*screenHeight/3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		BSPPanel panel = new BSPPanel(test);

		frame.setContentPane(panel);

		frame.pack();

		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
	}



	static class BSPPanel extends JPanel{

		BSP bsp;

		float zeroX = 0;
		float zeroY = 0;

		float dimX = 0;
		float dimY = 0;

		public BSPPanel(BSP bsp){
			this.bsp = bsp;
			this.dimX = bsp.getXBound();
			this.dimY = bsp.getYBound();
			this.setBackground(Color.WHITE);
		}
		public void paint(Graphics g){
			super.paint(g);

			this.zeroX = this.getWidth()/2;
			this.zeroY = this.getHeight()/2;

			System.out.println(""+zeroX +" "+ zeroY);

			Node root = bsp.getRoot();

			drawBSP(g, root);
		}

		private void drawBSP(Graphics g, Node root){

			for(Iterator i = root.getData(); i.hasNext();){
				Segment seg = (Segment) i.next();
				int x1 = (int) (zeroX*(seg.getP1().getX())/dimX +zeroX);
				int y1 = (int) (zeroY*(seg.getP1().getY())/dimY +zeroY);
				int x2 = (int) (zeroX*(seg.getP2().getX())/dimX +zeroX);
				int y2 = (int) (zeroY*(seg.getP2().getY())/dimY +zeroY);

				g.setColor(seg.getColor());
				g.drawLine(x1, y1, x2, y2);
			}

			if(root.hasLeft()){
				drawBSP(g, root.getLeft());
			}
			if(root.hasRight()){
				drawBSP(g, root.getRight());
			}
		}
	}

	public static void printBSP(BSP bsp){
		printrecBSP(bsp.getRoot());
	}

	private static void printrecBSP(Node root){
		System.out.println(root.toString());
		if(root.getLeft()!=null)
			printrecBSP(root.getLeft());
		if(root.getRight()!=null)
			printrecBSP(root.getRight());
	}
}
