package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PauseState extends State{
	private static Vector2 textTranslation = new Vector2(0, 1);
	
	private final State oldState;
	private TextElement pauseElement, continueElement;
	
	public PauseState(OrthographicCamera camera, State oldState){
		super(camera);
		this.oldState = oldState;
		
		pauseElement = new TextElement("PAUSED", 0, 0f);
		
		String continueText;
		
		if(Gdx.app.getType() == ApplicationType.Android) continueText = "TAP TO CONTINUE";
		else continueText = "SPACE TO CONTINUE";
		
		continueElement = new TextElement(continueText, 0, 0f);
		
		update();//so the position of the textelements is right at the beginning
	}
	
	public void update(){
		if(DodgeGame.INPUT.pause.isJustPressed() || Gdx.input.isButtonPressed(Buttons.LEFT)){
			DodgeGame.setState(oldState);
		}
		
		textTranslation.rotate(1f);
		
		pauseElement.setPos(textTranslation.x, textTranslation.y + 0.5f);
		continueElement.setPos(textTranslation.x, textTranslation.y - 0.5f);
	}
	
	public void render(SpriteBatch batch){
		oldState.render(batch);
		
		DodgeGame.FONT.setColor(Color.BLACK);
		pauseElement.render(batch);
		continueElement.render(batch);
	}
	
	public void dispose(){
		
	}
}
