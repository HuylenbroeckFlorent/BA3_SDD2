import java.lang.management.*;

public class TestNoGUI{

	public static void main(String[] args){
		String path = "";
		int nRuns = 1000;

		if(args.length==1)
			path=args[0];
		else{
			System.out.println("Expected 1 argument of type \"String\", found "+args.length+".");
			System.exit(0);
		}

		for(int i=0; i<3; i++){
			System.out.println("Heuristic "+BSP.getHeuristic(i)+" :\n"+bspTest(path, i, 1000));
		}
	}

	/**
	* Thanks to : http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking
	* @return 	long, current CPU time of the current thread.
	*/
	public static long CPUTime(){
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean();
	    return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0L;
	}

	/**
	*
	*
	*
	*@return 	CPU time in ms that the task has taken to be ran nRuns times.
	*/
	public static BSPTestResult bspTest(String path, int heuristic, int nRuns){
		long time = 0;
		BSP bsp;
		int size = 0;
		int height = 0;
		int emptyLeaves = 0;
		long start;
		for(int i = 0; i<nRuns; i++){
			start = CPUTime();
			bsp = new BSP(path, heuristic);
			time += CPUTime() - start;
			size += bsp.size();
			height += bsp.height();
			emptyLeaves += bsp.emptyLeaves();
		}

		return new BSPTestResult(	(double)size/nRuns, 
											(double)emptyLeaves/nRuns,
											(double)height/nRuns, 
											(double)time/(nRuns*1000000));
	}

	public static class BSPTestResult{
		double avgSize;
		double avgEmptyLeaves;
		double avgHeight;
		double avgTime;
		

		public BSPTestResult(double avgSize, double avgEmptyLeaves, double avgHeight, double avgTime){
			this.avgSize=avgSize;
			this.avgEmptyLeaves=avgEmptyLeaves;
			this.avgHeight=avgHeight;
			this.avgTime=avgTime;
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

		public String toString(){
			return "\tAverage size = "+(int)avgSize+" nodes, including "+(int)avgEmptyLeaves+" empty leaves.\n"
					+"\tAverage height = "+(int)avgHeight+" nodes.\n"
					+"\tAverage construction time = "+String.format("%.3f",avgTime)+" ms.\n";
		}
	}
}