package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.ReturnButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class CreditsState extends State{
	private static final float KEYS_SCROLL_SPEED = 0.15f;
	private static final float WHEEL_SCROLL_SPEED = 1f;
	
	private String text;
	
	private float yPos;
	private float yVelocity;
	
	private int lastMouseY;
	
	private float maxY = 0f;
	private ReturnButton button;
	
	public CreditsState(OrthographicCamera camera){
		super(camera);
		
		text = Gdx.files.internal("credits.txt").readString();
		
		button = new ReturnButton(camera, new MainMenuState(camera));
		
		lastMouseY = Gdx.input.getY();
	}
	
	public void update(){
		button.update();
		
		if(DodgeGame.INPUT.down.isPressed()) yPos += KEYS_SCROLL_SPEED;
		else if(DodgeGame.INPUT.up.isPressed()) yPos -= KEYS_SCROLL_SPEED;
		
		yPos += DodgeGame.INPUT.getScrollAmount() * WHEEL_SCROLL_SPEED;
		
		int dy = Gdx.input.getY() - lastMouseY;
		
		yVelocity *= 0.85f;
		
		if(Gdx.input.isButtonPressed(Buttons.LEFT))
			yVelocity = -((float) dy / Gdx.graphics.getHeight()) * camera.viewportHeight * camera.zoom;
		
		yPos += yVelocity;
		
		lastMouseY = Gdx.input.getY();
		
		yPos = MathUtils.clamp(yPos, 0, Math.max(maxY - (camera.viewportHeight * camera.zoom) + 3f, 0));
	}
	
	public void render(SpriteBatch batch){
		DodgeGame.FONT.setColor(Color.BLACK);
		
		float textX = -2.5f * camera.viewportWidth;
		float textY = 2.5f * camera.viewportHeight + yPos;
		
		TextBounds bounds = DodgeGame.FONT.drawWrapped(batch, text, textX, textY,
				camera.viewportWidth * camera.zoom - 2f);
		maxY = bounds.height;
		
		button.render(batch);
	}
	
	public void dispose(){
	}
}
