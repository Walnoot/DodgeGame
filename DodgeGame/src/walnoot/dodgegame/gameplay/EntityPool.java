package walnoot.dodgegame.gameplay;

import walnoot.dodgegame.components.Entity;
import walnoot.dodgegame.components.UnitComponent;
import walnoot.dodgegame.components.MoveComponent;
import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.utils.Pool;

public class EntityPool extends Pool<Entity>{
	private Map map;
	private GameState gameState;
	
	public EntityPool(Map map, GameState gameState){
		this.map = map;
		this.gameState = gameState;
	}

	protected Entity newObject(){
		Entity e = new Entity(map, 0, 0);
		
		e.addComponent(new MoveComponent(e));
		e.addComponent(new UnitComponent(e, gameState));
		
		return e;
	}
}
