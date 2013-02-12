package walnoot.dodgegame.states;

import walnoot.dodgegame.ButtonClickListener;
import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenuState extends State{
	private static final float COLOR_VALUE_MAX = 0.5f;
	private static final float COLOR_VALUE_MIN = 0f;
	
	private int colorChangeTimer;//this color stuff is for the now non-existant logo, may be used later
	private Color oldColor, newColor;
	private Table table;
	
	public MainMenuState(final OrthographicCamera camera){
		super(camera);
		
		oldColor = new Color(MathUtils.random(COLOR_VALUE_MIN, COLOR_VALUE_MAX), MathUtils.random(COLOR_VALUE_MIN,
				COLOR_VALUE_MAX), MathUtils.random(COLOR_VALUE_MIN, COLOR_VALUE_MAX), 1f);
		newColor = new Color(MathUtils.random(COLOR_VALUE_MIN, COLOR_VALUE_MAX), MathUtils.random(COLOR_VALUE_MIN,
				COLOR_VALUE_MAX), MathUtils.random(COLOR_VALUE_MIN, COLOR_VALUE_MAX), 1f);
		
		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		table.defaults();
		table.defaults().height(96f);
		
		TextButton playButton = new TextButton("PLAY", Util.SKIN);
		playButton.addListener(new ButtonClickListener(){
			public void click(Actor actor){
				if(DodgeGame.PREFERENCES.getBoolean(TutorialState.PREF_TUTORIAL_KEY, true))
					DodgeGame.setState(new TutorialState(camera));
				else
					DodgeGame.setState(new GameState(camera));
			}
		});
		table.add(playButton).height(128f).padBottom(32f).width(480f).colspan(2);
		table.row();
		
		TextButton optionsButton = new TextButton("OPTIONS", Util.SKIN);
		optionsButton.addListener(new ButtonClickListener(){
			public void click(Actor actor){
				DodgeGame.setState(new OptionsState(camera));
			}
		});
		table.add(optionsButton).fillX();
		
		TextButton creditsButton = new TextButton("CREDITS", Util.SKIN);
		creditsButton.addListener(new ButtonClickListener(){
			public void click(Actor actor){
				DodgeGame.setState(new CreditsState(camera));
			}
		});
		table.add(creditsButton).fillX();
		table.row();
	}
	
	public void update(){
		/*for(int i = 0; i < textElements.length; i++){
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
		}*/
	}
	
	private float getNewColorValue(float oldValue){
		return MathUtils.clamp(oldValue + MathUtils.random(-0.3f, 0.3f), COLOR_VALUE_MIN, COLOR_VALUE_MAX);
	}
	
	public void render(SpriteBatch batch){
		/*for(int i = 0; i < textElements.length; i++){
			textElements[i].render(batch);
		}*/
	}
	
	public void dispose(){
	}
}
