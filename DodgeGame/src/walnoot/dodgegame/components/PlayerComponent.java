package walnoot.dodgegame.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Stat;
import walnoot.dodgegame.gameplay.Hand;
import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class PlayerComponent extends Component{
	public static final float MOVE_RADIUS = 2f;//radius of circle player moves in
	
	public static final int NUM_START_HANDS = 3;
	private static final int COMBO_BREAK_TIME = (int) (1.1f * DodgeGame.UPDATES_PER_SECOND);
	
	public static final String[] GROW_STATUS_TEXTS = {"AWESOME!", "NOT BAD!", "SPLENDID!", "GOOD!", "COOL!"};
	public static final String[] BAD_STUFF_STATUS_TEXTS = {"TOO BAD!", "AWW!", "NOT GOOD!", "QUITE BAD!"};
	
	private float radius = 0.25f;
	
	private int invincibilityTimer = 0;
	
	private float targetRotation, actualRotation;//actualRotation follow targetRotation
	private Array<Hand> hands = new Array<Hand>(NUM_START_HANDS);
	
	private boolean newHighscore;
	private int score;
	private int combo;
	
	private int lastGrowTime;
	
	private final GameState gameState;
	
	public PlayerComponent(Entity owner, GameState gameState){
		super(owner);
		this.gameState = gameState;
		
		for(int i = 0; i < NUM_START_HANDS; i++){
			hands.add(new Hand());
		}
	}
	
	public void update(){
		if(DodgeGame.INPUT.left.isPressed()) targetRotation -= 10f;
		if(DodgeGame.INPUT.right.isPressed()) targetRotation += 10f;
		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			if(DodgeGame.INPUT.getInputX() > 0) targetRotation += 10f;
			else targetRotation -= 10f;
		}
		
		actualRotation += (targetRotation - actualRotation) * 0.5f;
		
		for(int i = 0; i < hands.size; i++){
			Hand hand = hands.get(i);
			hand.update(actualRotation + (360f / hands.size) * i);
			
			if(hand.died){
				hands.removeValue(hand, true);
				if(hands.size == 0){
					gameState.gameOver();
					owner.remove();
				}
			}
		}
		
		if(invincibilityTimer > 0) invincibilityTimer--;
		
		if(DodgeGame.gameTime - lastGrowTime == COMBO_BREAK_TIME){
			combo = 0;
			
			gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < hands.size; i++){
			hands.get(i).sprite.draw(batch);
		}
	}
	
	public void score(){
		gameState.setAnnouncement(getRandomText(GROW_STATUS_TEXTS), Color.GREEN);
		
		score += getScoreMultiplier();
		
		if(getScore() > Stat.HIGH_SCORE.getInt()){
			if(!newHighscore){
				gameState.setAnnouncement("NEW HIGHSCORE!", Color.GREEN);
				
				newHighscore = true;
			}
			
			Stat.HIGH_SCORE.setInt(getScore());
		}
		
		combo++;
		gameState.getMultiplierElement().setText(Integer.toString(getScoreMultiplier()));
		gameState.getScoreElement().setText(Integer.toString(score));
		
		lastGrowTime = DodgeGame.gameTime;
		
		Stat.NUM_FOOD_EATEN.addInt(1);
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
	
	public boolean isGameOver(){
		return hands.size == 0;
	}
	
	public float getRadius(){
		return radius;
	}
	
	public Array<Hand> getHands(){
		return hands;
	}
	
	public int getCombo(){
		return combo;
	}
}
