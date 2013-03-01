package walnoot.dodgegame.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.SpriteAccessor;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.math.Vector2;

public class MoveComponent extends Component{
	private static final float SPEED = 2.5f;
	private static final float FADE_OUT_TIME = 0.5f;
	private static final float FADE_OUT_THRESHOLD = PlayerComponent.MOVE_RADIUS * PlayerComponent.MOVE_RADIUS;
	private int fadeTimer = 0;
//	private float dx, dy;
	private Vector2 translation = new Vector2();
	
	public MoveComponent(Entity owner){
		super(owner);
	}
	
	public void init(Vector2 translation){
		init(translation.x, translation.y);
	}
	
	public void init(float dx, float dy){
		fadeTimer = 0;
		
		setTranslation(dx, dy);
	}
	
	public void update(){
		owner.translate(translation);
		
		float absX = owner.getxPos();
		if(absX < 0) absX = -absX;
		
		float absY = owner.getyPos();
		if(absY < 0) absY = -absY;
		
//		if(absX > GameState.MAP_SIZE || absY > GameState.MAP_SIZE){
		if(owner.getPos().len2() < FADE_OUT_THRESHOLD){
			if(fadeTimer == 0)
				Tween.to(owner.getComponent(SpriteComponent.class).getSprite(), SpriteAccessor.TRANSPARANCY,
						FADE_OUT_TIME).target(0f).start(DodgeGame.TWEEN_MANAGER);
			
			fadeTimer++;
			
			if(fadeTimer > DodgeGame.UPDATES_PER_SECOND * FADE_OUT_TIME) owner.remove();
		}
	}
	
	/**
	 * Sets the translation, give an normalized vector dictating the direction
	 * 
	 * @param dx
	 * @param dy
	 */
	public void setTranslation(float dx, float dy){
		translation.set(dx * SPEED * DodgeGame.SECONDS_PER_UPDATE, dy * SPEED * DodgeGame.SECONDS_PER_UPDATE);
	}
}
