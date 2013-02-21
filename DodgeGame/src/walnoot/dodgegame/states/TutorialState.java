package walnoot.dodgegame.states;

import java.util.ArrayList;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class TutorialState extends State{
	public static final float FOOD_RADIUS = 5f;
	public static final float FOOD_SIZE = 4f;
	
	private static final int PLAYER_EXPLANATION = 0, COINS = 1, BOMBS = 2;
	public static final String PREF_TUTORIAL_KEY = "showTuturial";
	private static final float MINIMAL_SKIP_TIME = 1.0f;//seconds
	
	private TextElement descriptionElement, skipElement;
	private int state = PLAYER_EXPLANATION;
	private int skipTimer;
	
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>(3);
	private float angle;
	
	public TutorialState(OrthographicCamera camera){
		super(camera);
		
		DodgeGame.PREFERENCES.putBoolean(PREF_TUTORIAL_KEY, false);
		
		skipElement = new TextElement("TAP TO CONTINUE", 0, GameState.MAP_SIZE - 1f, 2f);
		
		initState();
	}
	
	public void update(){
		skipTimer++;
		
		if(skipTimer > MINIMAL_SKIP_TIME * DodgeGame.UPDATES_PER_SECOND){
			if(Gdx.input.isKeyPressed(Keys.SPACE) || DodgeGame.INPUT.isJustTouched()){
				skipTimer = 0;
				
				state++;
				initState();
			}
		}
		
		angle++;
		
		for(int i = 0; i < sprites.size(); i++){
			sprites.get(i).setPosition(
					(MathUtils.cosDeg(i * (360f / sprites.size()) + angle) * FOOD_RADIUS) - 0.5f * FOOD_SIZE,
					(MathUtils.sinDeg(i * (360f / sprites.size()) + angle) * FOOD_RADIUS) - 0.5f * FOOD_SIZE);
		}
	}
	
	private void initState(){
		sprites.clear();
		
		String text = null;
		
		switch (state){
			case PLAYER_EXPLANATION:
				sprites.add(new Sprite(Util.HAND));
				
				text = "THIS IS YOU!";
				
				break;
			case COINS:
				sprites.add(new Sprite(Util.COIN));
				sprites.add(new Sprite(Util.COIN));
				sprites.add(new Sprite(Util.COIN));
				
				text = "GRAB THESE COINS!";
				
				break;
			case BOMBS:
				sprites.add(new Sprite(Util.BOMB));
				sprites.add(new Sprite(Util.BOMB));
				sprites.add(new Sprite(Util.BOMB));
				
				text = "WATCH OUT FOR BOMBS!";
				
				break;
			default:
				DodgeGame.setState(new GameState(camera));
				break;
		}
		
		if(text != null) descriptionElement = new TextElement(text, 0, 0);
		
		for(int i = 0; i < sprites.size(); i++){
			if(state == PLAYER_EXPLANATION) sprites.get(i).setSize(FOOD_SIZE * 0.5f, FOOD_SIZE);
			else sprites.get(i).setSize(FOOD_SIZE, FOOD_SIZE);
			sprites.get(i).setPosition(
					(MathUtils.cosDeg(i * (360f / sprites.size()) + angle) * FOOD_RADIUS) - 0.5f * FOOD_SIZE,
					(MathUtils.sinDeg(i * (360f / sprites.size()) + angle) * FOOD_RADIUS) - 0.5f * FOOD_SIZE);
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < sprites.size(); i++){
			sprites.get(i).draw(batch);
		}
		
		descriptionElement.render(batch);
		if(skipTimer > MINIMAL_SKIP_TIME * DodgeGame.UPDATES_PER_SECOND) skipElement.render(batch);
	}
	
	public void dispose(){
	}
}
