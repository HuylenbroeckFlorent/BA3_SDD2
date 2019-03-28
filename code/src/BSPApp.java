import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.GraphicsConfiguration;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.HeadlessException;

import java.io.File;

public class BSPApp{
	private static JFrame frame;
	private static BSPPanel panel;

	private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int screenHeight = screenSize.height;
	private static final int screenWidth = screenSize.width;

	private static BSP bsp;
	private static int heuristic=0;

	public static void main(String[] args){

		//whereAreWe();

		//BSP test = new BSP("./../../resources/Scenes/ellipses/ellipsesSmall.txt", BSP.ORDERED);

		JFrame frame = new JFrame();

		frame.setTitle("BSP Viewer - HUYLENBROECK Florent & DACHY Corentin");
		frame.setPreferredSize(new Dimension(2*screenWidth/3, 2*screenHeight/3));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
			}
		});
		random.setSelected(true);
		heuristicGroup.add(random);
		heuristicMenu.add(random);
		JRadioButtonMenuItem ordered = new JRadioButtonMenuItem("Ordered");
		ordered.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				heuristic=BSP.ORDERED;
			}
		});
		heuristicGroup.add(ordered);
		heuristicMenu.add(ordered);
		JRadioButtonMenuItem freeSplits = new JRadioButtonMenuItem("Free splits");
		freeSplits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				heuristic=BSP.FREE_SPLITS;
			}
		});
		heuristicGroup.add(freeSplits);
		heuristicMenu.add(freeSplits);

		menu.add(open);
		menu.add(quit);

		menuBar.add(menu);
		menuBar.add(heuristicMenu);

		panel = new BSPPanel();

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

		public BSPPanel(){
			this.setBackground(Color.WHITE);
		}

		public BSPPanel(BSP bsp){
			this();
			setBSP(bsp);
		}

		public void paint(Graphics g){
			super.paint(g);

			if(this.bsp!=null){

				this.zeroX = this.getWidth()/2;
				this.zeroY = this.getHeight()/2;

				Node root = bsp.getRoot();

				drawBSP(g, root);
			}
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

		public void setBSP(BSP bsp){
			this.bsp = bsp;
			this.dimX = bsp.getXBound();
			this.dimY = bsp.getYBound();
		}
	}

	static class PainterLinePanel{

	}

	private static void open(){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new FileNameExtensionFilter(".txt", "txt"));
		jfc.setDialogTitle("BSP Chooser");
		jfc.setCurrentDirectory(new File(".."));

		int action = 0;
		try{
			action = jfc.showOpenDialog(frame);
		} catch(HeadlessException e){
			throw new IllegalStateException(e);
		}

		String path = "";

		if(action == JFileChooser.APPROVE_OPTION){
			path = jfc.getSelectedFile().getAbsolutePath();
		}
		else{
			return;
		}

		bsp = new BSP(path, heuristic);

		panel.setBSP(bsp);
		//panel.updateUI();
		panel.revalidate();
		panel.repaint();
	}
}
