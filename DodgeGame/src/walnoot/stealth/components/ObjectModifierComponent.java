package walnoot.stealth.components;

import walnoot.dodgegame.Entity;
import walnoot.dodgegame.Map;

import com.badlogic.gdx.graphics.Color;

public class ObjectModifierComponent extends Component{
	private final ModifierType type;
	
	public ObjectModifierComponent(Entity owner, ModifierType type){
		super(owner);
		this.type = type;
		
		SpriteComponent spriteComponent = (SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT);
		if(type == ModifierType.GROW) spriteComponent.getSprite().setColor(Color.GREEN);
		else if(type == ModifierType.SHRINK) spriteComponent.getSprite().setColor(Color.RED);
		else if(type == ModifierType.DEATH) spriteComponent.getSprite().setColor(Color.MAGENTA);
	}
	
	public Component getCopy(Entity owner){
		return new ObjectModifierComponent(owner, type);
	}
	
	public void update(Map map){
		if(map.getPlayerComponent().isGameOver()) return;
		
		float dx = map.getPlayerComponent().owner.getxPos() - owner.getxPos();
		float dy = map.getPlayerComponent().owner.getyPos() - owner.getyPos();
		
		float minDistance = 0.5f + map.getPlayerComponent().getRadius() / 2f;
		if(dx * dx + dy * dy < minDistance  * minDistance){
			switch (type){
				case GROW:
					owner.remove();
					map.getPlayerComponent().grow();
					break;
				case SHRINK:
					owner.remove();
					map.getPlayerComponent().shrink();
					break;
				case DEATH:
					map.getPlayerComponent().die();
					break;
				default:
					break;
			}
		}
	}
	
	public ComponentIdentifier getIdentifier(){
		return ComponentIdentifier.OBJECT_MODIFIER_COMPONENT;
	}
	
	public enum ModifierType{
		GROW, SHRINK, DEATH;
	}
}
