package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class CreditsState extends State{
	private static final float KEYS_SCROLL_SPEED = 0.08f;
	private static final float MOUSE_SCROLL_SPEED = 0.03f;
	
	private String text;
	private float yPos;
	
	private float maxY = 0f;
	
	public CreditsState(OrthographicCamera camera){
		super(camera);
		
		text = Gdx.files.internal("credits.txt").readString();
	}
	
	public void update(){
		if(DodgeGame.INPUT.back.isPressed()) DodgeGame.setState(new MainMenuState(camera));
		
		if(DodgeGame.INPUT.down.isPressed())
			yPos += KEYS_SCROLL_SPEED;
		else if(DodgeGame.INPUT.up.isPressed())
			yPos -= KEYS_SCROLL_SPEED;
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT))
			yPos -= DodgeGame.INPUT.getInputY() * MOUSE_SCROLL_SPEED;
		
		yPos = MathUtils.clamp(yPos, 0, Math.max(maxY - (camera.viewportHeight * camera.zoom) + 2f, 0));
	}
	
	public void render(SpriteBatch batch){
		DodgeGame.FONT.setColor(Color.BLACK);
		
		float textX = -3.5f * camera.viewportWidth;
		float textY = 3.5f * camera.viewportHeight + yPos;
		
		TextBounds bounds = DodgeGame.FONT.drawWrapped(batch, text, textX, textY, camera.viewportWidth * camera.zoom - 2f);
		maxY = bounds.height;
	}
	
	public void dispose(){
	}
}
