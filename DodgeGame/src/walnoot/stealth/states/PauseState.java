package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseState extends State{
	private final State oldState;
	private TextElement text;

	public PauseState(OrthographicCamera camera, State oldState){
		super(camera);
		this.oldState = oldState;
		
		text = new TextElement("PAUSED", 0, 0);
	}
	
	public void update(){
		if(DodgeGame.INPUT.pause.isJustPressed() || Gdx.input.isButtonPressed(Buttons.LEFT)){
			DodgeGame.setState(oldState);
		}
	}
	
	public void render(SpriteBatch batch){
		oldState.render(batch);
		
		DodgeGame.FONT.setColor(Color.BLACK);
		text.render(batch);
	}
	
	public void dispose(){
		
	}
}
