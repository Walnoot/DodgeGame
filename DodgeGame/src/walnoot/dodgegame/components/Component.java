package walnoot.dodgegame.components;

import walnoot.dodgegame.gameplay.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Component{
	protected final Entity owner;
	protected final Map map;

	public Component(Entity owner){
		this.owner = owner;
		
		map = owner.getMap();
	}
	
	public void render(SpriteBatch batch){
		
	}
	
	public void update(){
		
	}
	
	public void onEntityRemove(){
		
	}
}
