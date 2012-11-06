package walnoot.dodgegame;

import java.util.ArrayList;

import walnoot.dodgegame.components.PlayerComponent;
import walnoot.dodgegame.components.SpriteComponent;
import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map{
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private PlayerComponent playerComponent;
	
	public Map(){
		Entity backgroundEntity = new Entity(this, 0, 0, 0);
		backgroundEntity.addComponent(new SpriteComponent(backgroundEntity, Util.FIELD, 2f * GameState.MAP_SIZE));
		
		addEntity(backgroundEntity);
	}
	
	public void update(){
		ArrayList<Entity> removedEntities = null;
		
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			
			if(!entity.isRemoved()) entity.update();
			else{
				if(removedEntities == null) removedEntities = new ArrayList<Entity>();
				removedEntities.add(entity);
				
				//if(gameState.getUnusedEntities().size() < GameState.MAX_UNUSED_ENTITIES) gameState.getUnusedEntities().add(entity);
			}
		}
		
		if(removedEntities != null) entities.removeAll(removedEntities);
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < entities.size(); i++){
			if(!entities.get(i).isRemoved()) entities.get(i).render(batch);
		}
	}
	
	public void setPlayerComponent(PlayerComponent playerComponent){
		this.playerComponent = playerComponent;
	}
	
	public ArrayList<Entity> getEntities(){
		return entities;
	}
	
	public void addEntity(Entity e){
		entities.add(e);
	}
	
	public void removeEntity(Entity e){
		entities.remove(e);
	}
	
	public PlayerComponent getPlayerComponent(){
		return playerComponent;
	}
}
