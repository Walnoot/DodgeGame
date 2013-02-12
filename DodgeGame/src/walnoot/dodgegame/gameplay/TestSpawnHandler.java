package walnoot.dodgegame.gameplay;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.components.Entity;
import walnoot.dodgegame.components.FoodComponent.FoodType;
import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.math.MathUtils;

public class TestSpawnHandler extends SpawnHandler{
	public static final SpawnHandler instance = new TestSpawnHandler();
	
	private int startTime;
	private float startAngle;
	private FoodType type;
	private float speed;
	
	public void init(){
		startTime = DodgeGame.gameTime;
		
		startAngle = MathUtils.random(0f, 2f * MathUtils.PI);
		
		type = MathUtils.randomBoolean() ? FoodType.DIE : FoodType.EAT;
		
		speed = MathUtils.random(1f, 2f);
		if(MathUtils.randomBoolean()) speed = -speed;
	}
	
	public void spawn(Entity entity, int time){
		float rad =
				(((DodgeGame.gameTime - this.startTime) / (float) getDuration()) * 2 * MathUtils.PI + startAngle)
						* speed;
		spawnToCenter(entity, MathUtils.cos(rad) * GameState.MAP_SIZE, MathUtils.sin(rad) * GameState.MAP_SIZE, type);
	}
	
	public int getPauseTicks(int time){
		return 4;
	}

	public int getDuration(){
		return (int) (3 * DodgeGame.UPDATES_PER_SECOND);
	}
}
