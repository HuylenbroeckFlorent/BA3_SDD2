import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GraphicsConfiguration;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.HeadlessException;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.geom.Point2D;

import java.io.File;

public class BSPApp{
	private static JFrame frame;
	private static JPanel mainPanel;
	private static BSPPanel bspPanel;
	private static PainterLinePanel painterPanel;

	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int screenHeight = screenSize.height;
	private static final int screenWidth = screenSize.width;

	private static GridBagLayout gbl = new GridBagLayout();
	private static GridBagConstraints gbc = new GridBagConstraints();

	private static String path;
	private static BSP bsp;
	private static int heuristic=0;

	/**
	* (bspPanelCenterX, bspPanelCenterY) is the center of the bspPanel.
	*/
	private static float bspPanelCenterX = 0;
	private static float bspPanelCenterY = 0;

	/**
	* BSP tree is bounded ont the x axis by [-bspBoundX, bspBoundX].
	*/
	private static float bspBoundX = 0;

	/**
	* BSP tree is bounded on the y axis by [-bspBoundY, bspBoundY].
	*/
	private static float bspBoundY = 0;

	/**
	* Coeficient applied to BSP bounds when drawing, for viewing purposes.
	*/ 
	private static float drawingCoef = 1.1f;

	private static float bspEyeX;
	private static float bspEyeY;

	private static int eyeX = (int) Integer.MAX_VALUE, eyeY = (int) Integer.MAX_VALUE;
	private static int eyeSize = 10;
	private static float eyeSpan = 60.0f;
	private static float eyeOrientation = 0.0f;
	private static float eyeTheta1 = (float)(eyeOrientation*(Math.PI/180.0));
	private static float eyeTheta2 = (float)((eyeOrientation+eyeSpan)*(Math.PI/180.0));

	public static void main(String[] args){

		// FRAME
		JFrame frame = new JFrame();

		frame.setTitle("BSP Viewer - HUYLENBROECK Florent & DACHY Corentin");
		frame.setPreferredSize(new Dimension(2*screenWidth/3, 2*screenHeight/3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// MENU BAR
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu menu = new JMenu("Actions");

		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				open();
			}
		});

		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});

		JMenu heuristicMenu = new JMenu("Heuristics");

		ButtonGroup heuristicGroup = new ButtonGroup();
		JRadioButtonMenuItem random = new JRadioButtonMenuItem("Random");
		random.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				heuristic=BSP.RANDOM;
				updateBSPPanel();
			}
		});
		random.setSelected(true);
		heuristicGroup.add(random);
		heuristicMenu.add(random);
		JRadioButtonMenuItem ordered = new JRadioButtonMenuItem("Ordered");
		ordered.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				heuristic=BSP.ORDERED;
				updateBSPPanel();
			}
		});
		heuristicGroup.add(ordered);
		heuristicMenu.add(ordered);
		JRadioButtonMenuItem freeSplits = new JRadioButtonMenuItem("Free splits");
		freeSplits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				heuristic=BSP.FREE_SPLITS;
				updateBSPPanel();
			}
		});
		heuristicGroup.add(freeSplits);
		heuristicMenu.add(freeSplits);

		menu.add(open);
		menu.add(quit);

		menuBar.add(menu);
		menuBar.add(heuristicMenu);

		// MAIN PANEL
		mainPanel = new JPanel();
		mainPanel.setLayout(gbl);

		// BSP PANEL
		bspPanel = new BSPPanel();

		bspPanel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				eyeX = e.getX();
				eyeY = e.getY();
				bspEyeX=((eyeX-bspPanelCenterX)*(bspBoundX*drawingCoef))/bspPanelCenterX;
				bspEyeY=((eyeY-bspPanelCenterY)*(bspBoundY*drawingCoef))/bspPanelCenterY;
				updateBSPPanel();
				painterPanel.removeAll();
				painterPanel.revalidate();
				painterPanel.repaint();

				/*System.out.println("Before : ( "+eyeX+" : "+eyeY+" )"+
									"\nAfter : ( "+bspEyeX+" : "+bspEyeY+" )"+
									"\nPanel : width="+bspPanel.getWidth()+" height="+bspPanel.getHeight()+
									"\nBounds : x="+bspBoundX+" y="+bspBoundY);*/
			}
		});

		gbc.weightx=0.5;
		gbc.weighty=0.9;
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START ;
		mainPanel.add(bspPanel, gbc);

		// PAINTER PANEL
		painterPanel = new PainterLinePanel();
		gbc.weightx=0.5;
		gbc.weighty=0.1;
		gbc.gridx=0;
		gbc.gridy=GridBagConstraints.RELATIVE; 
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		mainPanel.add(painterPanel, gbc);

		frame.setContentPane(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}



	static class BSPPanel extends JPanel{

		public BSPPanel(){
			this.setBackground(Color.WHITE);
		}

		public BSPPanel(BSP bsp){
			this();
			setBSP(bsp);
		}

		@Override
		public void paint(Graphics g){
			super.paint(g);

			if(bsp!=null){

				bspPanelCenterX = this.getWidth()/2;
				bspPanelCenterY = this.getHeight()/2;

				Node root = bsp.getRoot();

				drawBSP(g, root);
			}

			if(eyeX!=(int)Integer.MAX_VALUE && eyeY!=(int)Integer.MAX_VALUE){
				g.setColor(Color.BLACK);
				g.drawLine(eyeX, eyeY, ((int)(100*Math.cos(eyeTheta1)))+eyeX, ((int)(100*Math.sin(eyeTheta1)))+eyeY);
				g.drawLine(eyeX, eyeY, ((int)(100*Math.cos(eyeTheta2)))+eyeX, ((int)(100*Math.sin(eyeTheta2)))+eyeY);
				g.fillOval(eyeX-eyeSize/2, eyeY-eyeSize/2, eyeSize, eyeSize);
			}
		}

		private void drawBSP(Graphics g, Node root){

			for(Iterator i = root.getData(); i.hasNext();){
				Segment seg = (Segment) i.next();
				int x1 = (int) (bspPanelCenterX*(seg.getP1().getX())/(bspBoundX*drawingCoef)+bspPanelCenterX);
				int y1 = (int) (bspPanelCenterY*(seg.getP1().getY())/(bspBoundY*drawingCoef)+bspPanelCenterY);
				int x2 = (int) (bspPanelCenterX*(seg.getP2().getX())/(bspBoundX*drawingCoef)+bspPanelCenterX);
				int y2 = (int) (bspPanelCenterY*(seg.getP2().getY())/(bspBoundY*drawingCoef)+bspPanelCenterY);

				g.setColor(seg.getColor());
				g.drawLine(x1, this.getHeight()-y1, x2, this.getHeight()-y2);
			}

			if(root.hasLeft()){
				drawBSP(g, root.getLeft());
			}
			if(root.hasRight()){
				drawBSP(g, root.getRight());
			}
		}

		public void setBSP(BSP bsp){
			bsp = bsp;
			bspBoundX = bsp.getXBound();
			bspBoundY = bsp.getYBound();
		}
	}

	static class PainterLinePanel extends JPanel{

		int lineWidth;
		int lineOffset;
		int lineHeight;

		public PainterLinePanel(){
			this.setBackground(Color.WHITE);
			TitledBorder tb;

			tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),"Eye view");
			tb.setTitleJustification(TitledBorder.CENTER);
			this.setBorder(tb);
		}

		public void paint(Graphics g){
			super.paint(g);

			this.lineWidth=(int)(this.getWidth()*0.9);
			this.lineOffset=(int)(this.getWidth()*0.05);
			this.lineHeight=(int)(this.getHeight()/2);

			if(bsp !=null && bspEyeX!=(int)Integer.MAX_VALUE && bspEyeY!=(int)Integer.MAX_VALUE){
				paintersAlgorithm(g, bsp.getRoot());
			}
		}

		private void paintersAlgorithm(Graphics g, Node root){
			if(root.isLeaf()){
				scanConvert(g, root);
			}
			else{
				float a = root.getA();
				float b = root.getB();
				float c = root.getC();

				float h = a*bspEyeX+b*bspEyeY+c;

				if(h>0.0f){
					paintersAlgorithm(g, root.getLeft());
					scanConvert(g, root);
					paintersAlgorithm(g, root.getRight());
				}
				else if(h<0.0f){
					paintersAlgorithm(g, root.getRight());
					scanConvert(g, root);
					paintersAlgorithm(g, root.getLeft());
				}
				else{
					paintersAlgorithm(g, root.getRight());
					paintersAlgorithm(g, root.getLeft());
				}
			}
		}

		private void scanConvert(Graphics g, Node root){
			
			for(Iterator i = root.getData(); i.hasNext();){
				Segment segment = (Segment) i.next();

				float theta1 = polarAngle(segment.getP1());
				float theta2 = polarAngle(segment.getP2());

				//System.out.println("Theta1="+theta1+" Theta2="+theta2);

				int x1=0, x2=0;

				if(theta1>theta2){
					float tmp = theta1;
					theta1=theta2;
					theta2=tmp;
				}

				boolean draw=true;

				if(theta1<eyeTheta1){
					if(theta2<eyeTheta2 && theta2>eyeTheta1)
						x1=lineOffset;
					else
						draw=false;
				}
				else
					x1=lineOffset+(int)(((theta1-eyeTheta1)/(eyeTheta2-eyeTheta1))*lineWidth);

				if(theta2>eyeTheta2){
					if(theta1>eyeTheta1 && theta1<eyeTheta2)
						x2=lineOffset+lineWidth;
					else
						draw=false;
				}
				else
					x2=lineOffset+(int)(((theta2-eyeTheta1)/(eyeTheta2-eyeTheta1))*lineWidth);

				if(draw){
					g.setColor(segment.getColor());
					//System.out.println("x1="+x1+" x2="+x2);
					g.drawLine(x1, lineHeight, x2, lineHeight);
				}
			}
		}

		private float polarAngle(Point2D.Float p){
			float x = (float)(p.getX()-bspEyeX);
			float y = (float)(p.getY()-bspEyeY);

			float theta = 0f;

			if(x>0){
				if(y>=0)
					theta = (float)Math.atan(y/x);
				else
					theta = (float)(Math.atan(y/x)+2*Math.PI);
			}
			else if(x<0)
				theta = (float)(Math.atan(y/x)+Math.PI);
			else{
				if(y>0)
					theta = (float)(Math.PI/2);
				else if(y<0)
					theta = (float)(3*Math.PI/2);
			}


			return theta;
		}

		private float polarRadius(Point2D.Float p){
			float x = (float)(p.getX()-bspEyeX);
			float y = (float)(p.getY()-bspEyeY);

			return (float)(Math.sqrt(x*x+y*y));
		}
	}

	private static void open(){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
		jfc.setDialogTitle("BSP Chooser");
		jfc.setCurrentDirectory(new File("../resources/Scenes"));

		int action = 0;
		try{
			action = jfc.showOpenDialog(frame);
		} catch(HeadlessException e){
			throw new IllegalStateException(e);
		}

		if(action == JFileChooser.APPROVE_OPTION){
			path = jfc.getSelectedFile().getAbsolutePath();
			eyeX=(int)Integer.MAX_VALUE;
			eyeY=(int)Integer.MAX_VALUE;
			updateBSPPanel();
		}
	}

	private static void updateBSPPanel(){
		if(path!=null){
			bsp = new BSP(path, heuristic);
			bspPanel.removeAll();
			bspPanel.setBSP(bsp);
			bspPanel.revalidate();
			bspPanel.repaint();
		}
	}

	
}
