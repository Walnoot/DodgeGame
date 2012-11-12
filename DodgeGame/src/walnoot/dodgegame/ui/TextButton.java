package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class TextButton extends TextElement{
	private int shortcut = Keys.UNKNOWN;
	private boolean hovering;
	private boolean hasPressed;
	
	/**
	 * @param text
	 * @param x
	 *            - coordinate of middle of button
	 * @param y
	 *            - coordinate of middle of button
	 */
	public TextButton(String text, float x, float y, float scale){
		super(text, x, y, scale);
	}
	
	/**
	 * @param text
	 * @param x
	 *            - coordinate of middle of button
	 * @param y
	 *            - coordinate of middle of button
	 * @param shortcut
	 *            - keycode for the shortcut key, like Keys.A
	 */
	public TextButton(String text, float x, float y, int shortcut){
		this(text, x, y, 1f);
		this.shortcut = shortcut;
	}
	
	public TextButton(String text, float x, float y, float scale, int shortcut){
		this(text, x, y, scale);
		this.shortcut = shortcut;
	}
	
	public void update(){
		float x = DodgeGame.INPUT.getInputX();
		float y = DodgeGame.INPUT.getInputY();
		
		float halfWidth = getWidth() * 0.5f;
		float halfHeight = getHeight() * 0.5f;
		
		if(x >= getxPos() - halfWidth && y >= getyPos() - halfHeight && x <= getxPos() + halfWidth && y <= getyPos() + halfHeight){
			if(Gdx.input.isButtonPressed(Buttons.LEFT)){
				if(!hasPressed){
					doAction();
					DodgeGame.SOUND_MANAGER.playClickSound();
				}
				hasPressed = true;
			}else{
				hovering = true;
			}
		}else{
			hovering = false;
		}
		
		if(!Gdx.input.isButtonPressed(Buttons.LEFT)) hasPressed = false;
		
		if(shortcut != Keys.UNKNOWN){
			if(Gdx.input.isKeyPressed(shortcut)){
				doAction();
			}
		}
	}
	
	public void render(SpriteBatch batch){
		if(hovering) getColor().a = 0.5f;
		else getColor().a = 1f;
		super.render(batch);
	}
	
	public abstract void doAction();
}
