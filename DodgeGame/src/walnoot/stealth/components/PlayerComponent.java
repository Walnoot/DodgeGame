package walnoot.stealth.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.SpriteAccessor;
import walnoot.stealth.states.GameState;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PlayerComponent extends Component{
	public static final int NUM_START_LIVES = 3;
	private static final float WALK_SPEED = 6f;
	private static final float RADIUS_GROW_RATE = 1 / 32f, RADIUS_SHRINK_FACTOR = 3f / 4f;
	private static final float MINIMAL_RADIUS = 0.5f;
	private static final int INVINCIBILITY_TIME = 240;//ticks
	
	public static final String[] GROW_STATUS_TEXTS = {"AWESOME", "NOT BAD", "SPLENDID", "GOOD", "COOL"};
	public static final String[] BAD_STUFF_STATUS_TEXTS = {"TOO BAD", "AWW", "NOT GOOD", "QUITE BAD"};//for both shrink and death types because I'm lazy as fuck
	
	private float radius = 1f;
	private int lives = NUM_START_LIVES;
	private int invincibilityTimer = INVINCIBILITY_TIME;
	private int movementLockTimer = 0;
	private final GameState gameState;
	
	public PlayerComponent(Entity owner, GameState gameState){
		super(owner);
		this.gameState = gameState;
	}
	
	public void update(){
		if(movementLockTimer <= 0){
			Vector2 translation = Vector2.tmp;
			translation.set(0, 0);
			
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT))
				translation.set(DodgeGame.INPUT.getInputX() - owner.getxPos(), DodgeGame.INPUT.getInputY() - owner.getyPos());
			
			if(DodgeGame.INPUT.up.isPressed()) translation.add(0, 1);
			if(DodgeGame.INPUT.down.isPressed()) translation.add(0, -1);
			if(DodgeGame.INPUT.left.isPressed()) translation.add(-1, 0);
			if(DodgeGame.INPUT.right.isPressed()) translation.add(1, 0);
			
			translation.nor();
			translation.mul(DodgeGame.SECONDS_PER_UPDATE * WALK_SPEED);
			owner.translate(translation);
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
		
		gameState.setStatusText(getRandomText(GROW_STATUS_TEXTS), Color.GREEN);
	}
	
	public void shrink(){
		if(!isInvincible()){
			radius *= RADIUS_SHRINK_FACTOR;
			if(radius < MINIMAL_RADIUS) radius = MINIMAL_RADIUS;
		}
		
		gameState.setStatusText(getRandomText(BAD_STUFF_STATUS_TEXTS), Color.RED);
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
		
		gameState.setStatusText(getRandomText(BAD_STUFF_STATUS_TEXTS), Color.BLACK);
	}
	
	private String getRandomText(String[] texts){
		return texts[MathUtils.random(0, texts.length - 1)] + "!";
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
		return new PlayerComponent(owner, gameState);
	}
	
	public ComponentIdentifier getIdentifier(){
		return ComponentIdentifier.CONTROLLER_COMPONENT;
	}
}
