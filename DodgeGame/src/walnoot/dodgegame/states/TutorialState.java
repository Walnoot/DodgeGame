package walnoot.dodgegame.states;

import java.util.ArrayList;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class TutorialState extends State{
	public static final float FOOD_RADIUS = 6f;
	public static final float FOOD_SIZE = 3f;
	
	public static final int GOOD_FOOD = 0, BAD_FOOD = 1;
	public static final String PREF_TUTORIAL_KEY = "showTuturial";
	private static final int MINIMAL_SKIP_TIME = 120;//ticks
	
	private TextElement descriptionElement, skipElement;
	private int state = GOOD_FOOD;
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
		
		if(skipTimer > MINIMAL_SKIP_TIME && Gdx.input.isKeyPressed(Keys.SPACE)){
			skipTimer = 0;
			
			state++;
			initState();
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
			case GOOD_FOOD:
				sprites.add(new Sprite(Util.FOOD_ONE));
				sprites.add(new Sprite(Util.DOT));
				sprites.add(new Sprite(Util.DOT));
				
				text = "EAT THESE TO GROW!";
				
				break;
			case BAD_FOOD:
				sprites.add(new Sprite(Util.BAD_FOOD_ONE));
				sprites.add(new Sprite(Util.DOT));
				sprites.add(new Sprite(Util.DOT));
				
				text = "AVOID AT ALL COSTS!";
				
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
