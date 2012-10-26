package walnoot.stealth.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.SpriteAccessor;
import walnoot.stealth.states.GameState;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class PlayerComponent extends Component{
	public static final int NUM_START_LIVES = 3;
	private static final float WALK_SPEED = 6f;
	private static final float RADIUS_GROW_RATE = 1 / 32f, RADIUS_SHRINK_RATE = 1 / 4f;
	private static final float MINIMAL_RADIUS = 0.5f;
	private static final int INVINCIBILITY_TIME = 240;//ticks
	
	private float radius = 1f;
	private int lives = NUM_START_LIVES;
	private int invincibilityTimer = INVINCIBILITY_TIME;
	private int movementLockTimer = 0;
	
	public PlayerComponent(Entity owner){
		super(owner);
	}
	
	public void update(){
		if(movementLockTimer <= 0){
			Vector2.tmp.set(0, 0);
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT))
				Vector2.tmp.set(DodgeGame.INPUT.getInputX() - owner.getxPos(), DodgeGame.INPUT.getInputY() - owner.getyPos());
			
			if(DodgeGame.INPUT.up.isPressed()) Vector2.tmp.add(0, 1);
			if(DodgeGame.INPUT.down.isPressed()) Vector2.tmp.add(0, -1);
			if(DodgeGame.INPUT.left.isPressed()) Vector2.tmp.add(-1, 0);
			if(DodgeGame.INPUT.right.isPressed()) Vector2.tmp.add(1, 0);
			
			Vector2.tmp.nor();
			Vector2.tmp.mul(DodgeGame.SECONDS_PER_UPDATE * WALK_SPEED);
			owner.translate(Vector2.tmp.x, Vector2.tmp.y);
		}else{
			movementLockTimer--;
		}
		
		SpriteComponent spriteComponent = (SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT);
		spriteComponent.getSprite().setScale(radius);
		
		if(invincibilityTimer > 0){
			invincibilityTimer--;
			spriteComponent.getSprite().setColor(Color.DARK_GRAY);
		}else spriteComponent.getSprite().setColor(Color.BLACK);
	}
	
	public void grow(){
		radius += RADIUS_GROW_RATE;
	}
	
	public void shrink(){
		if(!isInvincible()){
			radius -= RADIUS_SHRINK_RATE;
			if(radius < MINIMAL_RADIUS) radius = MINIMAL_RADIUS;
		}
	}
	
	public void die(){
		if(isInvincible()) return;
		
		lives--;
		invincibilityTimer = INVINCIBILITY_TIME;
		owner.setxPos(0);
		owner.setyPos(0);
		
		if(lives == 0){
			owner.remove();
			
			((GameState) DodgeGame.state).gameOver();
		}
		
		//lock movement for half a second, avoids confusion when respawning, may need to be changed later
		movementLockTimer = (int) (0.5f * DodgeGame.UPDATES_PER_SECOND);
		
		Tween.from(((SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT)).getSprite(),
				SpriteAccessor.TRANSPARANCY, 0.5f)
				.target(0).start(DodgeGame.TWEEN_MANAGER);
	}
	
	public int getScore(){
		return (int) (radius * 32f - 32f);
	}
	
	private boolean isInvincible(){
		return invincibilityTimer > 0;
	}
	
	public boolean isGameOver(){
		return lives == 0;
	}
	
	public float getRadius(){
		return radius;
	}
	
	public int getLives(){
		return lives;
	}
	
	public Component getCopy(Entity owner){
		return new PlayerComponent(owner);
	}
	
	public ComponentIdentifier getIdentifier(){
		return ComponentIdentifier.CONTROLLER_COMPONENT;
	}
}
