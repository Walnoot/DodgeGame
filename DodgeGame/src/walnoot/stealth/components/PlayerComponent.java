package walnoot.stealth.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.Map;
import walnoot.stealth.states.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class PlayerComponent extends Component{
	public static final float TURN_SPEED = 120f, WALK_SPEED = 6f;
	private static final float RADIUS_GROW_RATE = 1 / 32f, RADIUS_SHRINK_RATE = 1 / 8f;
	private static final int NUM_START_LIVES = 3;
	private static final int INVINCIBILITY_TIME = 240;//ticks
	private final OrthographicCamera camera;
	private float radius = 1f;
	private int lives = NUM_START_LIVES;
	private int invincibilityTimer = INVINCIBILITY_TIME;
	private float movementLockTimer = 0f;
	
	public PlayerComponent(Entity owner, OrthographicCamera camera){
		super(owner);
		this.camera = camera;
	}
	
	public void update(Map map){
		if(movementLockTimer <= 0){
			Vector2.tmp.set(0, 0);
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT)){
				float inputX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2f);
				inputX /= (Gdx.graphics.getWidth() / 2f);
				inputX *= (camera.viewportWidth / 2f) * GameState.MAP_SIZE;
				
				float inputY = -(Gdx.input.getY() - Gdx.graphics.getHeight() / 2f);
				inputY /= (Gdx.graphics.getHeight() / 2f);
				inputY *= (camera.viewportHeight / 2f) * GameState.MAP_SIZE;
				
				Vector2.tmp.set(inputX - owner.getxPos(), inputY - owner.getyPos());
			}
			
			if(DodgeGame.INPUT.up.isPressed()) Vector2.tmp.add(0, 1);
			if(DodgeGame.INPUT.down.isPressed()) Vector2.tmp.add(0, -1);
			if(DodgeGame.INPUT.left.isPressed()) Vector2.tmp.add(-1, 0);
			if(DodgeGame.INPUT.right.isPressed()) Vector2.tmp.add(1, 0);
			
			Vector2.tmp.nor();
			Vector2.tmp.mul(DodgeGame.SECONDS_PER_UPDATE * WALK_SPEED);
			owner.translate(Vector2.tmp.x, Vector2.tmp.y);
		}else{
			movementLockTimer -= DodgeGame.SECONDS_PER_UPDATE;
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
		if(!isInvincible()) radius -= RADIUS_SHRINK_RATE;
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
			
			//move the player away so you cant see it for 1 tick after you died
			owner.setxPos(Float.MIN_VALUE);
			owner.setyPos(Float.MIN_VALUE);
		}
		
		movementLockTimer = 0.5f; //lock movement for half a second
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
	
	public Component getCopy(Entity owner){
		return new PlayerComponent(owner, camera);
	}
	
	public ComponentIdentifier getIdentifier(){
		return ComponentIdentifier.CONTROLLER_COMPONENT;
	}
}
