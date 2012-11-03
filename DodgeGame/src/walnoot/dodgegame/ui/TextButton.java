package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class TextButton extends TextElement{
	private int shortcut = Keys.UNKNOWN;
	private boolean hovering;
	
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
		if(rectangle.contains(DodgeGame.INPUT.getInputX(), DodgeGame.INPUT.getInputY())){
			if(Gdx.input.isButtonPressed(Buttons.LEFT)){
				doAction();
				
				DodgeGame.SOUND_MANAGER.playClickSound();
			}else hovering = true;
		}else hovering = false;
		
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
