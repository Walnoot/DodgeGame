package walnoot.dodgegame.states;

import java.util.ArrayList;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class TutorialState extends State{
	public static final float FOOD_RADIUS = 5f;
	public static final float FOOD_SIZE = 4f;
	
	private static final int PLAYER_EXPLANATION = 0, GOOD_FOOD = 1, BAD_FOOD = 2, POISON_FOOD = 3;
	public static final String PREF_TUTORIAL_KEY = "showTuturial";
	private static final int MINIMAL_SKIP_TIME = 80;//ticks
	
	private TextElement descriptionElement, skipElement;
	private int state = PLAYER_EXPLANATION;
	private int skipTimer;
	
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>(3);
	private float angle;
	
	public TutorialState(OrthographicCamera camera){
		super(camera);
		
		DodgeGame.PREFERENCES.putBoolean(PREF_TUTORIAL_KEY, false);
		
		skipElement = new TextElement(String.format("%s TO CONTINUE",
				Gdx.app.getType() == ApplicationType.Android ? "TAP" : "SPACE"), 0, -7f, 2f);
		
		initState();
	}
	
	public void update(){
		skipTimer++;
		
		if(skipTimer > MINIMAL_SKIP_TIME){
			if(Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isButtonPressed(Buttons.LEFT)){
				skipTimer = 0;
				
				state++;
				initState();
			}
		}
		
		angle++;
		
		for(int i = 0; i < sprites.size(); i++){
			sprites.get(i).setPosition((MathUtils.cosDeg(i * (360f / sprites.size()) + angle) * FOOD_RADIUS) - 0.5f * FOOD_SIZE,
					(MathUtils.sinDeg(i * (360f / sprites.size()) + angle) * FOOD_RADIUS) - 0.5f * FOOD_SIZE);
		}
	}
	
	private void initState(){
		sprites.clear();
		
		String text = null;
		
		switch (state){
			case PLAYER_EXPLANATION:
				Sprite sprite = new Sprite(Util.DOT);
				sprite.setColor(Color.BLACK);
				sprites.add(sprite);
				
				text = "THIS IS YOU! (FOR NOW)";
				
				break;
			case GOOD_FOOD:
				sprites.add(new Sprite(Util.FOOD_ONE));
				sprites.add(new Sprite(Util.FOOD_TWO));
				sprites.add(new Sprite(Util.DOT));
				
				text = "EAT THESE TO GROW!";
				
				break;
			case BAD_FOOD:
				sprites.add(new Sprite(Util.BAD_FOOD_ONE));
				sprites.add(new Sprite(Util.DOT));
				sprites.add(new Sprite(Util.DOT));
				
				text = "THIS ROTTEN FOOD WILL MAKE YOU SHRINK!";
				
				break;
			case POISON_FOOD:
				text = "THE PURPLE DOTS KILL YOU, NO SPRITES YET!";
				
				break;
			default:
				DodgeGame.setState(new GameState(camera));
				break;
		}
		
		if(text != null) descriptionElement = new TextElement(text, 0, 0);
		
		for(int i = 0; i < sprites.size(); i++){
			sprites.get(i).setSize(FOOD_SIZE, FOOD_SIZE);
			sprites.get(i).setPosition((MathUtils.cosDeg(i * (360f / sprites.size()) + angle) * FOOD_RADIUS) - 0.5f * FOOD_SIZE,
					(MathUtils.sinDeg(i * (360f / sprites.size()) + angle) * FOOD_RADIUS) - 0.5f * FOOD_SIZE);
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < sprites.size(); i++){
			sprites.get(i).draw(batch);
		}
		
		descriptionElement.render(batch);
		if(skipTimer > MINIMAL_SKIP_TIME) skipElement.render(batch);
	}
	
	public void dispose(){
	}
}
