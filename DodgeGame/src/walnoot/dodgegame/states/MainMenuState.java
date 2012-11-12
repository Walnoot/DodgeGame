package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.TextButton;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class MainMenuState extends State{
	private static final float COLOR_VALUE_MAX = 0.5f;
	private static final float COLOR_VALUE_MIN = 0f;
	
	private TextElement[] textElements;
	private int colorChangeTimer;
	private Color oldColor, newColor;
	
	public MainMenuState(final OrthographicCamera camera){
		super(camera);
		
		oldColor = new Color(MathUtils.random(COLOR_VALUE_MIN, COLOR_VALUE_MAX), MathUtils.random(COLOR_VALUE_MIN,
				COLOR_VALUE_MAX), MathUtils.random(COLOR_VALUE_MIN, COLOR_VALUE_MAX), 1f);
		newColor = new Color(MathUtils.random(COLOR_VALUE_MIN, COLOR_VALUE_MAX), MathUtils.random(COLOR_VALUE_MIN,
				COLOR_VALUE_MAX), MathUtils.random(COLOR_VALUE_MIN, COLOR_VALUE_MAX), 1f);
		
		TextElement titleElement = new TextElement("DODGE GAME", 0, 5f, 4f);
		titleElement.setColor(oldColor);
		
		TextButton newGameButton = new TextButton("NEW GAME", 0, 2f, 2f){
			public void doAction(){
				if(DodgeGame.PREFERENCES.getBoolean(TutorialState.PREF_TUTORIAL_KEY, true))
					DodgeGame.setState(new TutorialState(camera));
				else
					DodgeGame.setState(new GameState(camera));
			}
		};
		
		TextButton creditsButton = new TextButton("CREDITS", 0, 0, 2f){
			public void doAction(){
				DodgeGame.setState(new CreditsState(camera));
			}
		};
		
		TextButton optionsButton = new TextButton("OPTIONS", 0, -2f, 2f){
			public void doAction(){
				DodgeGame.setState(new OptionsState(camera));
			}
		};
		
		TextButton quitGameButton = new TextButton("QUIT GAME", 0, -4f, 2f){
			public void doAction(){
				Gdx.app.exit();
			}
		};
		
		textElements = new TextElement[]{titleElement, newGameButton, creditsButton, optionsButton, quitGameButton};
	}
	
	public void update(){
		for(int i = 0; i < textElements.length; i++){
			if(textElements[i] instanceof TextButton) ((TextButton) textElements[i]).update();
		}
		
		Color currentColor = textElements[0].getColor();
		
		currentColor.r += (newColor.r - oldColor.r) * DodgeGame.SECONDS_PER_UPDATE;
		currentColor.g += (newColor.g - oldColor.g) * DodgeGame.SECONDS_PER_UPDATE;
		currentColor.b += (newColor.b - oldColor.b) * DodgeGame.SECONDS_PER_UPDATE;
		
		colorChangeTimer++;
		if(colorChangeTimer >= (int) DodgeGame.UPDATES_PER_SECOND){
			colorChangeTimer = 0;
			
			oldColor = newColor;
			currentColor.set(oldColor);
			
			newColor = new Color(getNewColorValue(oldColor.r), getNewColorValue(oldColor.g),
					getNewColorValue(oldColor.b), 1f);
		}
	}
	
	private float getNewColorValue(float oldValue){
		return MathUtils.clamp(oldValue + MathUtils.random(-0.3f, 0.3f), COLOR_VALUE_MIN, COLOR_VALUE_MAX);
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < textElements.length; i++){
			textElements[i].render(batch);
		}
	}
	
	public void dispose(){
	}
}
