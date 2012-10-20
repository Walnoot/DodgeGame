package walnoot.dodgegame;

import java.util.ArrayList;

import walnoot.stealth.components.PlayerComponent;
import walnoot.stealth.components.SpriteComponent;
import walnoot.stealth.states.GameState;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map{
	public static final String MAP_FOLDER_NAME = "maps";
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private final PlayerComponent playerComponent;
	
	public Map(PlayerComponent playerComponent){
		Entity backgroundEntity = new Entity(0, 0, 0);
		backgroundEntity.addComponent(new SpriteComponent(backgroundEntity, DodgeGame.TEXTURES[0][1], 2f * GameState.MAP_SIZE));
		
		addEntity(backgroundEntity);
		
		this.playerComponent = playerComponent;
	}
	
	public void update(){
		ArrayList<Entity> removedEntities = new ArrayList<Entity>();
		
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			
			if(!entity.isRemoved()) entity.update(this);
			else removedEntities.add(entity);
		}
		
		entities.removeAll(removedEntities);
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).render(batch);
		}
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
