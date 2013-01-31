package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.ReturnButton;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PauseState extends State{
	private static Vector2 textTranslation = new Vector2(0, 1);
	
	private TextElement pauseElement, continueElement;
	private ReturnButton returnButton;
	private int timer = 10;
	
	private final State oldState;
	
	public PauseState(OrthographicCamera camera, State oldState){
		super(camera);
		this.oldState = oldState;
		
		pauseElement = new TextElement("PAUSED", 0f, 0f, 2f);
		
		String continueText = String.format("%s TO CONTINUE",
				Gdx.app.getType() == ApplicationType.Android ? "TAP" : "SPACE");
		
		continueElement = new TextElement(continueText, 0, 0f);
		
		returnButton = new ReturnButton(camera, new MainMenuState(camera), "MAIN MENU");
		
		update();//so the position of the textelements is right at the beginning
	}
	
	public void update(){
		if(timer > 0) timer--;
		if(DodgeGame.INPUT.pause.isJustPressed() || (DodgeGame.INPUT.isJustTouched() && timer == 0)){
			DodgeGame.setState(oldState);
		}
		
		textTranslation.rotate(1f);
		
		pauseElement.setPos(textTranslation.x, textTranslation.y + 0.5f);
		continueElement.setPos(textTranslation.x, textTranslation.y - 0.5f);
		
		returnButton.update();
	}
	
	public void render(SpriteBatch batch){
		oldState.render(batch);
		
		DodgeGame.FONT.setColor(Color.BLACK);
		pauseElement.render(batch);
		continueElement.render(batch);
		returnButton.render(batch);
	}
	
	public void resize(){
	}
	
	public void dispose(){
		
	}
}
