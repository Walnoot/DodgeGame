package walnoot.stealth.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.Map;
import walnoot.dodgegame.SpriteAccessor;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.graphics.Color;

public class ObjectModifierComponent extends Component{
	public static final float FADE_OUT_DURATION = .125f, FADE_IN_DURATION = .25f;//seconds
	
	private final ModifierType type;
	private boolean consumed;
	private int removeTimer;
	
	public ObjectModifierComponent(Entity owner, ModifierType type){
		super(owner);
		this.type = type;
		
		SpriteComponent spriteComponent = (SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT);
		if(type == ModifierType.GROW) spriteComponent.getSprite().setColor(Color.GREEN);
		else if(type == ModifierType.SHRINK) spriteComponent.getSprite().setColor(Color.RED);
		else if(type == ModifierType.DEATH) spriteComponent.getSprite().setColor(Color.MAGENTA);
		
		Tween tween = Tween.from(((SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT)).getSprite(), SpriteAccessor.TRANSPARANCY, FADE_IN_DURATION);
		tween.target(0).start(DodgeGame.TWEEN_MANAGER);
	}
	
	public Component getCopy(Entity owner){
		return new ObjectModifierComponent(owner, type);
	}
	
	public void update(Map map){
		if(consumed){
			removeTimer++;
			if(removeTimer > FADE_OUT_DURATION * DodgeGame.UPDATES_PER_SECOND) owner.remove();
		}
		
		if(map.getPlayerComponent().isGameOver() || consumed) return;
		
		float dx = map.getPlayerComponent().owner.getxPos() - owner.getxPos();
		float dy = map.getPlayerComponent().owner.getyPos() - owner.getyPos();
		
		float minDistance = 0.5f + map.getPlayerComponent().getRadius() / 2f;
		if(dx * dx + dy * dy < minDistance  * minDistance){
			if(type.canDisappear){
				consumed = true;
				
				Tween tween = Tween.to(((SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT)).getSprite(), SpriteAccessor.TRANSPARANCY, FADE_OUT_DURATION);
				tween.target(0).start(DodgeGame.TWEEN_MANAGER);
			}
			
			switch (type){
				case GROW:
					map.getPlayerComponent().grow();
					break;
				case SHRINK:
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
		GROW(true), SHRINK(true), DEATH(false);
		
		private final boolean canDisappear;

		private ModifierType(boolean canDissappear){
			this.canDisappear = canDissappear;
		}
	}
}
