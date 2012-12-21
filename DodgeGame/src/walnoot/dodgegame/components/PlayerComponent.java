package walnoot.dodgegame.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.SpriteAccessor;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.states.GameOverState;
import walnoot.dodgegame.states.GameState;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PlayerComponent extends Component{
	public static final int NUM_START_LIVES = 3;
	private static final float WALK_SPEED = 6f;//per second
	public static final int SCORE_MULTIPLIER_DEFAULT = 32;
	private static final float RADIUS_GROW_RATE = 1 / 32f, RADIUS_SHRINK_FACTOR = 4f / 5f;
	private static final float MINIMAL_RADIUS = 0.5f;
	private static final int INVINCIBILITY_TIME = 240;//ticks
	private static final int COMBO_BREAK_TIME = (int) (4 * DodgeGame.UPDATES_PER_SECOND);
	
	public static final String[] GROW_STATUS_TEXTS = {"AWESOME!", "NOT BAD!", "SPLENDID!", "GOOD!", "COOL!"};
	public static final String[] BAD_STUFF_STATUS_TEXTS = {"TOO BAD!", "AWW!", "NOT GOOD!", "QUITE BAD!"};//for both shrink and death types because I'm lazy as fuck
	
	private float radius = 1f;
	
	private int lives = NUM_START_LIVES;
	
	private int invincibilityTimer = INVINCIBILITY_TIME;
	private int movementLockTimer = 0;
	
	private final int highscore;
	private boolean newHighscore;
	private int score;
	private int combo;
	
	private int lastGrowTime;
	
	private final GameState gameState;
	private ParticleEffect effect;
	
	public PlayerComponent(Entity owner, GameState gameState){
		super(owner);
		this.gameState = gameState;
		
		highscore = DodgeGame.PREFERENCES.getInteger(GameOverState.HIGH_SCORE_KEY, 0);
		
		effect = new ParticleEffect();
		effect.loadEmitters(Gdx.files.internal("effects/shine.dat"));
		effect.getEmitters().get(0).setSprite(new Sprite(Util.SHINE));
	}
	
	public void update(){
		effect.update(DodgeGame.SECONDS_PER_UPDATE);
		
		if(movementLockTimer <= 0){
			Vector2 translation = Vector2.tmp;
			translation.set(0, 0);
			
			if(Gdx.input.isButtonPressed(Buttons.LEFT)){
				float dx = DodgeGame.INPUT.getInputX() - owner.getxPos();
				float dy = DodgeGame.INPUT.getInputY() - owner.getyPos();
				
				translation.set(dx, dy);
			}
			
			if(DodgeGame.INPUT.up.isPressed()) translation.add(0, 1);
			if(DodgeGame.INPUT.down.isPressed()) translation.add(0, -1);
			if(DodgeGame.INPUT.left.isPressed()) translation.add(-1, 0);
			if(DodgeGame.INPUT.right.isPressed()) translation.add(1, 0);
			
			float length = translation.len();
			
			if(length > DodgeGame.SECONDS_PER_UPDATE * WALK_SPEED){
				//normalize the vector
				translation.x /= length;
				translation.y /= length;
				
				translation.mul(DodgeGame.SECONDS_PER_UPDATE * WALK_SPEED);
			}
			
			owner.translate(translation);
		}else{
			movementLockTimer--;
		}
		
		SpriteComponent spriteComponent = owner.getComponent(SpriteComponent.class);
		spriteComponent.getSprite().setScale(radius);
		
		if(invincibilityTimer > 0){
			invincibilityTimer--;
			spriteComponent.getSprite().setColor(Color.GRAY);
		}else spriteComponent.getSprite().setColor(Color.BLACK);
		
		if(DodgeGame.gameTime - lastGrowTime == COMBO_BREAK_TIME){
			combo = 0;
			
			gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
		}
	}
	
	public void render(SpriteBatch batch){
		effect.draw(batch);
	}
	
	public void grow(){
		effect.setPosition(owner.getxPos(), owner.getyPos());
		effect.start();
		
		radius += RADIUS_GROW_RATE;
		
		gameState.setStatusText(getRandomText(GROW_STATUS_TEXTS), Color.GREEN);
		
		score += (int) (RADIUS_GROW_RATE * SCORE_MULTIPLIER_DEFAULT * getScoreMultiplier());
		
		if(getScore() > highscore){
			if(!newHighscore){
				gameState.setStatusText("NEW HIGHSCORE!", Color.GREEN);
				
				newHighscore = true;
			}
		}
		
		combo++;
		gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
		gameState.getScoreElement().setText(Integer.toString(score));
		
		lastGrowTime = DodgeGame.gameTime;
	}
	
	public void shrink(){
		if(!isInvincible()){
			radius *= RADIUS_SHRINK_FACTOR;
			if(radius < MINIMAL_RADIUS) radius = MINIMAL_RADIUS;
		}
		
		gameState.setStatusText(getRandomText(BAD_STUFF_STATUS_TEXTS), Color.RED);
		
		combo = 0;
		gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
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
		
		Tween.from(owner.getComponent(SpriteComponent.class).getSprite(),
				SpriteAccessor.TRANSPARANCY, 0.5f).target(0).start(DodgeGame.TWEEN_MANAGER);
		
		gameState.setStatusText(getRandomText(BAD_STUFF_STATUS_TEXTS), Color.BLACK);

		combo = 0;
		gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
	}
	
	public int getScoreMultiplier(){
		return combo / 5 + 1;
	}
	
	private String getRandomText(String[] texts){
		return texts[MathUtils.random(0, texts.length - 1)];
	}
	
	public int getScore(){
		return score;
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
	
	public int getCombo(){
		return combo;
	}
}
