package walnoot.dodgegame;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.StringBuilder;

public abstract class Stat{
	public static final IntStat HIGH_SCORE = new IntStat("HIGHSCORE", "highScore");
	public static final IntStat NUM_DEATHS = new IntStat("NUMBER OF DEATHS", "numDeaths");
	public static final IntStat NUM_COINS_TAKEN = new IntStat("COINS GRABBED", "coinsTaken");
	public static final IntStat NUM_TIMES_PLAYED = new IntStat("TIMES PLAYED", "timesPlayed");
	public static final TimeStat TICKS_PLAYED = new TimeStat("TIME PLAYED", "ticksPlayed");
	
	public static Stat[] STATS = {HIGH_SCORE, NUM_DEATHS, NUM_COINS_TAKEN, NUM_TIMES_PLAYED, TICKS_PLAYED};
	
	protected final String name, key;
	
	public Stat(String name, String key){
		this.name = name;
		this.key = key;
	}
	
	public CharSequence getName(){
		return name;
	}
	
	public abstract CharSequence getValue();
	
	public abstract void putValue(Preferences prefs);
	
	public static void saveStats(){
		for(Stat stat : STATS){
			stat.putValue(DodgeGame.PREFERENCES);
		}
	}
	
	public static class IntStat extends Stat{
		private int value;
		
		public IntStat(String name, String key){
			super(name, key);
			
			value = DodgeGame.PREFERENCES.getInteger(key, 0);
		}
		
		public CharSequence getValue(){
			return Integer.toString(value);
		}
		
		public void putValue(Preferences prefs){
			prefs.putInteger(key, value);
		}
		
		public int getInt(){
			return value;
		}
		
		public void setInt(int value){
			this.value = value;
		}
		
		public void addInt(int amount){
			value += amount;
		}
	}
	
	public static class TimeStat extends IntStat{
		public TimeStat(String name, String key){
			super(name, key);
		}
		
		public CharSequence getValue(){
			StringBuilder builder = new StringBuilder();
			
			int seconds = (int) (getInt() / DodgeGame.UPDATES_PER_SECOND);
			
			int hours = seconds / 3600;
			if(hours >= 1){
				builder.append(hours);
				builder.append(":");
			}
			
			builder.append(String.format("%02d", (seconds / 60) % 60));
			builder.append(":");
			
			builder.append(String.format("%02d", seconds % 60));
			
			return builder;
		}
	}
}
