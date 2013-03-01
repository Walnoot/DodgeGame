package walnoot.dodgegame.gameplay;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.components.Entity;
import walnoot.dodgegame.components.MoveComponent;
import walnoot.dodgegame.components.UnitComponent;
import walnoot.dodgegame.components.UnitComponent.UnitType;

import com.badlogic.gdx.math.Vector2;

public abstract class SpawnHandler{
	public void init(){
	}
	
	/**
	 * @param entity
	 *            - The Entity whose position etc needs changing
	 * @param time
	 *            - How many ticks this SpawnHandler has been running
	 */
	public abstract void spawn(Entity entity, int time);
	
	public int getPauseTicks(int time){
		return (int) (15f / ((time / (10000f * (DodgeGame.UPDATES_PER_SECOND / 60f))) + 1f));
	}
	
	protected void spawnToCenter(Entity entity, Vector2 pos, UnitType type){
		spawnToCenter(entity, pos.x, pos.y, type);
	}
	
	protected void spawnToCenter(Entity entity, float x, float y, UnitType type){
		entity.setxPos(x);
		entity.setyPos(y);
		
		entity.getComponent(UnitComponent.class).init(type);
		
		entity.getComponent(MoveComponent.class).init(Vector2.tmp3.set(x, y).mul(-1f).nor());
	}
	
	public abstract int getDuration();
	
	public SpawnHandler getNextHandler(){
		return BasicSpawnHandler.instance;
	}
}
