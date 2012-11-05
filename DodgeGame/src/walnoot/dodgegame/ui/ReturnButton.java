package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;
import walnoot.stealth.states.State;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;

public class ReturnButton extends TextButton{
	private static final String TEXT = "BACK";
	private static final float SCALE = 3f;
	
	private TextBounds bounds;
	private final OrthographicCamera camera;
	private final State previousState;
	
	public ReturnButton(OrthographicCamera camera, State previousScreen){
		super(TEXT, 0, 0, SCALE, Keys.BACK);
		this.camera = camera;
		this.previousState = previousScreen;
		
		DodgeGame.FONT.setScale(DodgeGame.FONT_SCALE * SCALE);
		bounds = new TextBounds(DodgeGame.FONT.getBounds(TEXT));
	}
	
	public void update(){
		float x = ((camera.viewportWidth * camera.zoom) - bounds.width) * 0.5f;
		float y = (-(camera.viewportHeight * camera.zoom) + bounds.height) * 0.5f;
		
		setPos(x, y);
		
		super.update();
	}
	
	public void doAction(){
		DodgeGame.setState(previousState);
	}
}
