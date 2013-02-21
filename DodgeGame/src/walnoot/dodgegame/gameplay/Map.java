package walnoot.dodgegame.gameplay;

import java.util.ArrayList;

import walnoot.dodgegame.Util;
import walnoot.dodgegame.components.Entity;
import walnoot.dodgegame.components.PlayerComponent;
import walnoot.dodgegame.components.SpriteComponent;
import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map{
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	ArrayList<Entity> removedEntities = new ArrayList<Entity>();
	
	private PlayerComponent playerComponent;
	
	public Map(){
		Entity backgroundEntity = new Entity(this, 0, 0);
		backgroundEntity.addComponent(new SpriteComponent(backgroundEntity, Util.FIELD, 2f * GameState.MAP_SIZE));
		
		addEntity(backgroundEntity);
	}
	
	public void update(){
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			
			if(!entity.isRemoved()) entity.update();
			else removedEntities.add(entity);
		}
		
		if(!removedEntities.isEmpty()){
			entities.removeAll(removedEntities);
			removedEntities.clear();
		}
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
