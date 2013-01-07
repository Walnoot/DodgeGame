package walnoot.dodgegame;

public class Profiler{
	public static Profiler instance = new Profiler();
	
	private long startTime;
	private long duration;
	
	public void start(){
		startTime = System.nanoTime();
	}
	
	public void stop(){
		duration = System.nanoTime() - startTime;
	}
	
	/**
	 * @return - how long the profiler ran, in nanoseconds
	 */
	public long getDuration(){
		return duration;
	}
	
	/**
	 * @return - how long the profiler ran, in microseconds
	 */
	public long getDurationMicros(){
		return duration / 1000;
	}
	
	/**
	 * @return - how long the profiler ran, in milliseconds
	 */
	public long getDurationMillis(){
		return duration / 1000;
	}
}
