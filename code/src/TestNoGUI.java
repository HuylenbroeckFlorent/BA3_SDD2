import java.lang.management.*;
import java.io.IOException;
import java.util.*;

/**
* Test class for BSP's that does not show any GUI.
*
* @author HUYLENBROECK Florent, DACHY Corentin.
*/
public class TestNoGUI{

	public static void main(String[] args){
		String path = "";
		int nRuns = 1;
		float eyeX=0;
		float eyeY=0;

		long init = CPUTime();

		if(args.length>0){
			path=args[0];
			try{
				BSP test = new BSP(path, 0);
			}catch(IOException ioe){
				System.out.println("File "+path+" is not a valid Scene2D file.");
				System.exit(0);
			}
		}
		if(args.length>1){
			try{
				nRuns=Integer.parseInt(args[1]);
			}catch(NumberFormatException nfe){
				System.out.println("Invalid argument n, required integer.");
				System.exit(0);
			}
		}
		if(args.length==3){
			System.out.println("Invalid number of argument, required (path) (path, n) or (path, n, ex, ey).");
		}
		if(args.length>3){
			try{
				eyeX=Float.parseFloat(args[2]);
				eyeY=Float.parseFloat(args[3]);
			}catch(NumberFormatException nfe){
				System.out.println("Invalid argument ex or ey, required float.");
				System.exit(0);
			}
		}
		if(args.length>4){
			System.out.println("Ignored "+(args.length-4)+" last arguments.");
		}

		System.out.println("======= INIT ========\n");
		System.out.println("Settings :\n\tPath = \""+path+
									"\".\n\tNumber of run for each heuristic = "+nRuns+
									".\n\tEye position = ("+String.format("%.3f",eyeX)+":"+String.format("%.3f",eyeY)+").\n");

		System.out.println("=== RUNNING TESTS ===\n");
		for(int i=0; i<3; i++){
			System.out.println("Heuristic "+BSP.getHeuristic(i)+" :\n"+bspTest(path, i, nRuns, eyeX, eyeY));
		}

		System.out.println("=== DONE IN "+String.format("%.2f",(double)(CPUTime()-init)/1000000000)+"s ===");
	}

	/**
	* Returns the current CPU time inside current thread in nanoseconds.
	* Thanks to : http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking
	*
	* @return 	long, current CPU time inside the current thread, in ns.
	*/
	public static long CPUTime(){
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean();
	    return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
	}

	/**
	* Runs tests.
	* Construcs a BSP "nRuns" times, with the heuristic "heuristic".
	* Each time, it gathers all the data that is needed and runs 
	* the painter's algorithm while measuring the CPU time taken.
	*
	* @param path 		String, path to the Scene2D file.
	* @param heuristic 	int, heuristic to use in order to build the BSP.
	* @param eyeX 		float, position of the eye along the X axis. 
	* @param eyeY 		float, position of the eye along the Y axis. 
	* @return 			CPU time in ms that the task has taken to be ran nRuns times.
	*/
	public static BSPTestResult bspTest(String path, int heuristic, int nRuns, float eyeX, float eyeY){
		long time = 0;
		BSP bsp = null;
		int size = 0;
		int height = 0;
		int emptyLeaves = 0;
		long start;
		long timePainter = 0;
		for(int i = 0; i<nRuns; i++){
			try{
				start = CPUTime();
				bsp = new BSP(path, heuristic);
				time += CPUTime() - start;
			}catch(Exception e){
				System.out.println("Something went wrong while testing.");
				e.printStackTrace();
				System.exit(0);
			}
			size += bsp.size();
			height += bsp.height();
			emptyLeaves += bsp.emptyLeaves();
			start=CPUTime();
			bsp.painter(eyeX, eyeY, new ArrayList<Segment>());
			timePainter += CPUTime() - start;
		}

		return new BSPTestResult((double)size/nRuns, 
								(double)emptyLeaves/nRuns,
								(double)height/nRuns, 
								(double)time/(nRuns*1000000),
								(double)timePainter/(nRuns*1000000));
	}

	/**
	* Object that contains the result for a specific run of BSPTest.
	* 
	* @author HUYLENBROECK Florent, DACHY Corentin.
	*/
	public static class BSPTestResult{
		double avgSize;
		double avgEmptyLeaves;
		double avgHeight;
		double avgTime;
		double avgTimePainter;
		

		public BSPTestResult(	double avgSize, 
								double avgEmptyLeaves, 
								double avgHeight, 
								double avgTime, 
								double avgTimePainter){
			this.avgSize=avgSize;
			this.avgEmptyLeaves=avgEmptyLeaves;
			this.avgHeight=avgHeight;
			this.avgTime=avgTime;
			this.avgTimePainter=avgTimePainter;
		}

		public double getAvgSize(){
			return avgSize;
		}

		public double getAvgEmptyLeaves(){
			return avgEmptyLeaves;
		}

		public double getAvgHeight(){
			return avgHeight;
		}

		public double getAvgTime(){
			return avgTime;
		}

		public double getAvgTimePainter(){
			return avgTimePainter;
		}

		public String toString(){
			return "\tAverage size = "+(int)avgSize+" nodes, including "+(int)avgEmptyLeaves+" empty leaves.\n"
					+"\tAverage height = "+(int)avgHeight+" nodes.\n"
					+"\tAverage construction time = "+String.format("%.3f",avgTime)+"ms.\n"
					+"\tAverage time to run painter's algorithm = "+String.format("%.3f",avgTimePainter)+"ms.\n";
		}
	}
}