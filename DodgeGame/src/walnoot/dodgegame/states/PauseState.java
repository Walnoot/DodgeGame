package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.ReturnButton;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PauseState extends State{
	private static Vector2 textTranslation = new Vector2(0, 1);
	
	private TextElement pauseElement, continueElement;
	private ReturnButton returnButton;
	private int timer = 10;
	
	private final GameState oldState;
	
	public PauseState(OrthographicCamera camera, GameState oldState){
		super(camera);
		this.oldState = oldState;
		
		pauseElement = new TextElement("PAUSED", 0f, 0f, 2f);
		
		continueElement = new TextElement("TAP TO CONTINUE", 0, 0f);
		
		returnButton = new ReturnButton(camera, new MenuState(camera), "MAIN MENU");
		
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
		
		DodgeGame.SCALE_FONT.setColor(Color.BLACK);
		pauseElement.render(batch);
		continueElement.render(batch);
		returnButton.render(batch);
	}
	
	public void resize(){
		super.resize();
		
		oldState.resize();
	}
	
	public void dispose(){
	}
	
	public boolean playsGameMusic(){
		return true;
	}
}
