package walnoot.dodgegame.components;

import java.util.ArrayList;

import walnoot.dodgegame.gameplay.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Entity{
	private Vector2 pos = new Vector2();
	private ArrayList<Component> components = new ArrayList<Component>();//fuck yeah entity-component design
	private boolean removed = false;
	private Map map;
	
	public Entity(Map map, float xPos, float yPos){
		this.map = map;
		pos.set(xPos, yPos);
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < components.size(); i++){
			components.get(i).render(batch);
		}
	}
	
	public void update(){
		for(int i = 0; i < components.size(); i++){
			components.get(i).update();
		}
	}
	
	public void addComponent(Component component){
		components.add(component);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> componentClass){
		for(int i = 0; i < components.size(); i++){
			Component component = components.get(i);
			
			if(component.getClass() == componentClass) return (T) component;
		}
		
		return null;
	}
	
	/*public void moveForward(float amount){
		xPos -= MathUtils.sinDeg(rotation) * amount;
		yPos += MathUtils.cosDeg(rotation) * amount;
	}*/
	
	public void translate(float x, float y){
		pos.add(x, y);
	}
	
	public void translate(Vector2 translation){
		translate(translation.x, translation.y);
	}
	
	/*public void rotate(float rotation){
		this.rotation += rotation;
		
		if(rotation < MathUtils.PI) rotation += 2 * MathUtils.PI;
		if(rotation > MathUtils.PI) rotation -= 2 * MathUtils.PI;
	}*/
	
	public Map getMap(){
		return map;
	}
	
	public float getxPos(){
		return pos.x;
	}
	
	public float getyPos(){
		return pos.y;
	}
	
	public Vector2 getPos(){
		return pos;
	}
	
	/*public float getRotation(){
		return rotation;
	}*/
	
	public boolean isRemoved(){
		return removed;
	}
	
	public void setxPos(float xPos){
		pos.x = xPos;
	}
	
	public void setyPos(float yPos){
		pos.y = yPos;
	}
	
	public void setPosition(Vector2 position){
		pos.set(position);
	}

	/*public void setRotation(float rotation){
		this.rotation = rotation;
		
		if(rotation < MathUtils.PI) rotation += 2 * MathUtils.PI;
		if(rotation > MathUtils.PI) rotation -= 2 * MathUtils.PI;
	}*/
	
	public void setRemoved(boolean removed){
		this.removed = removed;
	}
	
	public void remove(){
		for(int i = 0; i < components.size(); i++){
			components.get(i).onEntityRemove();
		}
		
		removed = true;
	}
}
