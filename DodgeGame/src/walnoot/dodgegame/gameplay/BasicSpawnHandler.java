package walnoot.dodgegame.gameplay;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.components.Entity;
import walnoot.dodgegame.components.UnitComponent.UnitType;
import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BasicSpawnHandler extends SpawnHandler{
	public static final SpawnHandler instance = new BasicSpawnHandler();
	private static final int GROW_OBJECT_CHANGE = 70;//out of 100
	
	public void spawn(Entity entity, int time){
		Vector2 translation = Vector2.tmp;
		
		translation.x = MathUtils.random(-GameState.MAP_SIZE, GameState.MAP_SIZE);
		translation.y = MathUtils.randomBoolean() ? -GameState.MAP_SIZE : GameState.MAP_SIZE;
		if(MathUtils.randomBoolean()) translation.set(translation.y, translation.x);
		
		spawnToCenter(entity, translation, MathUtils.random(99) < GROW_OBJECT_CHANGE ? UnitType.SCORE : UnitType.DIE);
	}
	
	public int getDuration(){
		return (int) (10 * DodgeGame.UPDATES_PER_SECOND);
//		return Integer.MAX_VALUE;
	}
	
	public SpawnHandler getNextHandler(){
		return TestSpawnHandler.instance;
	}
}
