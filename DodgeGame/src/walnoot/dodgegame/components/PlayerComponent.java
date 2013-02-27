package walnoot.dodgegame.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Stat;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PlayerComponent extends Component{
	public static final float MOVE_RADIUS = 2f;//radius of circle player moves in
	
	public static final int NUM_START_LIVES = 3;
	private static final int INVINCIBILITY_TIME = (int) (2 * DodgeGame.UPDATES_PER_SECOND);//ticks
	private static final int COMBO_BREAK_TIME = (int) (1.1f * DodgeGame.UPDATES_PER_SECOND);
	private static final int HAND_CLOSE_TIME = (int) (0.25f * DodgeGame.UPDATES_PER_SECOND);
	
	public static final String[] GROW_STATUS_TEXTS = {"AWESOME!", "NOT BAD!", "SPLENDID!", "GOOD!", "COOL!"};
	public static final String[] BAD_STUFF_STATUS_TEXTS = {"TOO BAD!", "AWW!", "NOT GOOD!", "QUITE BAD!"};
	
	private float radius = 0.5f;
	
	private int lives = NUM_START_LIVES;
	
	private int invincibilityTimer = 0;
	private int handCloseTimer;
	
	private float targetRotation, actualRotation;//actualRotation follow targetRotation
	
	private boolean newHighscore;
	private int score;
	private int combo;
	
	private int lastGrowTime;
	
	private final GameState gameState;
	
	private Sprite sprite;
	
	public PlayerComponent(Entity owner, GameState gameState){
		super(owner);
		this.gameState = gameState;
		
		sprite = new Sprite(Util.HAND);
		sprite.setSize(1f, 2f);
		sprite.setOrigin(0.5f, 0f);
	}
	
	public void update(){
		if(DodgeGame.INPUT.left.isPressed()) targetRotation -= 10f;
		if(DodgeGame.INPUT.right.isPressed()) targetRotation += 10f;
		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			if(DodgeGame.INPUT.getInputX() > 0) targetRotation += 10f;
			else targetRotation -= 10f;
		}
		
		actualRotation += (targetRotation - actualRotation) * 0.5f;

		sprite.setRotation(-actualRotation);
		
		Vector2.tmp.set(MathUtils.sinDeg(actualRotation), MathUtils.cosDeg(actualRotation));
		
		float handDist = (0.7f - handCloseTimer / 50f);
		sprite.setPosition(Vector2.tmp.x * handDist - 0.5f, Vector2.tmp.y * handDist);
		
		owner.setPosition(Vector2.tmp.mul(MOVE_RADIUS));
		
		if(invincibilityTimer > 0) invincibilityTimer--;

		SpriteComponent spriteComponent = owner.getComponent(SpriteComponent.class);
		if(spriteComponent != null){
			spriteComponent.getSprite().setScale(radius);
			
			if(isInvincible()) spriteComponent.getSprite().setColor(Color.GRAY);
			else spriteComponent.getSprite().setColor(Color.BLACK);
		}
		
		if(DodgeGame.gameTime - lastGrowTime == COMBO_BREAK_TIME){
			combo = 0;
			
			gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
		}
		
		if(handCloseTimer > 0){
			handCloseTimer--;
			
			if(handCloseTimer == 0){
				sprite.setRegion(Util.HAND);
			}
		}
	}
	
	public void render(SpriteBatch batch){
		if(!isInvincible() || (DodgeGame.gameTime / 5) % 2 == 0) sprite.draw(batch);
	}

	public void score(){
		gameState.setAnnouncement(getRandomText(GROW_STATUS_TEXTS), Color.GREEN);
		
		score += getScoreMultiplier();
		
		if(getScore() > Stat.HIGH_SCORE.getInt()){
			if(!newHighscore){
				gameState.setAnnouncement("NEW HIGHSCORE!", Color.GREEN);
				
				newHighscore = true;
			}
		}
		
		combo++;
		gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
		gameState.getScoreElement().setText(Integer.toString(score));
		
		lastGrowTime = DodgeGame.gameTime;
		
		Stat.NUM_FOOD_EATEN.addInt(1);
		
		closeHand();
	}

	public void die(){
		if(isInvincible()) return;
		
		lives--;
		invincibilityTimer = INVINCIBILITY_TIME;
		
		if(lives == 0){
			owner.remove();
			
			((GameState) DodgeGame.state).gameOver();
		}
		
		gameState.setAnnouncement(getRandomText(BAD_STUFF_STATUS_TEXTS), Color.BLACK);
		
		combo = 0;
		gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
		
		Stat.NUM_DEATHS.addInt(1);
		
		closeHand();
	}
	
	private void closeHand(){
		handCloseTimer = HAND_CLOSE_TIME;
		sprite.setRegion(Util.HAND_CLOSED);
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
