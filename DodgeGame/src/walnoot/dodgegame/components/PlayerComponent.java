package walnoot.dodgegame.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.states.GameOverState;
import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class PlayerComponent extends Component{
	public static final float MOVE_RADIUS = 2f;//radius of circle player moves in
	
	public static final int NUM_START_LIVES = 3;
	private static final int INVINCIBILITY_TIME = (int) (2 * DodgeGame.UPDATES_PER_SECOND);//ticks
	private static final int COMBO_BREAK_TIME = (int) (1 * DodgeGame.UPDATES_PER_SECOND);
	
	public static final String[] GROW_STATUS_TEXTS = {"AWESOME!", "NOT BAD!", "SPLENDID!", "GOOD!", "COOL!"};
	public static final String[] BAD_STUFF_STATUS_TEXTS = {"TOO BAD!", "AWW!", "NOT GOOD!", "QUITE BAD!"};//for both shrink and death types because I'm lazy as fuck
	
	private float radius = 1f;
	
	private int lives = NUM_START_LIVES;
	
	private int invincibilityTimer = INVINCIBILITY_TIME;
	
	private float rotation;
	
	private final int highscore;
	private boolean newHighscore;
	private int score;
	private int combo;
	
	private int lastGrowTime;
	
	private final GameState gameState;
	
	public PlayerComponent(Entity owner, GameState gameState){
		super(owner);
		this.gameState = gameState;
		
		highscore = DodgeGame.PREFERENCES.getInteger(GameOverState.HIGH_SCORE_KEY, 0);
	}
	
	public void update(){
		/*Vector2 translation = Vector2.tmp;
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
		
		owner.translate(translation);*/
		
		if(DodgeGame.INPUT.left.isPressed()) rotation -= 10f;
		if(DodgeGame.INPUT.right.isPressed()) rotation += 10f;
		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			if(DodgeGame.INPUT.getInputX() > 0) rotation += 10f;
			else rotation -= 10f;
		}
		
		owner.setxPos(MathUtils.sinDeg(rotation) * MOVE_RADIUS);
		owner.setyPos(MathUtils.cosDeg(rotation) * MOVE_RADIUS);
		
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
	
	public void eat(){
		gameState.setStatusText(getRandomText(GROW_STATUS_TEXTS), Color.GREEN);
		
		score += getScoreMultiplier();
		
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
	
	public void die(){
		if(isInvincible()) return;
		
		lives--;
		invincibilityTimer = INVINCIBILITY_TIME;
		
		if(lives == 0){
			owner.remove();
			
			((GameState) DodgeGame.state).gameOver();
		}
		
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
