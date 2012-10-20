package walnoot.stealth.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.Map;
import walnoot.dodgegame.SpriteAccessor;
import walnoot.stealth.states.GameState;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.math.MathUtils;

public class MoveComponent extends Component{
	private static final float SPEED = 3f;
	private int fadeTimer = 0;

	public MoveComponent(Entity owner){
		super(owner);
	}
	
	public void update(Map map){
		float dx = MathUtils.cosDeg(owner.getRotation()) * SPEED * DodgeGame.SECONDS_PER_UPDATE;
		float dy = MathUtils.sinDeg(owner.getRotation()) * SPEED * DodgeGame.SECONDS_PER_UPDATE;
		
		owner.translate(dx, dy);
		
		float absX = owner.getxPos();
		if(absX < 0) absX = -absX;
		
		float absY = owner.getyPos();
		if(absY < 0) absY = -absY;
		
		if(absX > GameState.MAP_SIZE || absY > GameState.MAP_SIZE){
			if(fadeTimer == 0) Tween.to(((SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT)).getSprite(), SpriteAccessor.TRANSPARANCY, 1f).target(0f).start(DodgeGame.TWEEN_MANAGER);
			
			fadeTimer ++;
			
			if(fadeTimer > DodgeGame.UPDATES_PER_SECOND) owner.remove();
		}
	}
	
	public Component getCopy(Entity owner){
		return new MoveComponent(owner);
	}
	
	public ComponentIdentifier getIdentifier(){
		return ComponentIdentifier.MOVE_COMPONENT;
	}
}
