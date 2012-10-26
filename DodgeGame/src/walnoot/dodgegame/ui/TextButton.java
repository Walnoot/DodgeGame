package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

public abstract class TextButton extends TextElement{
	private int shortcut = Keys.UNKNOWN;
	
	/**
	 * @param text
	 * @param x
	 *            - coordinate of middle of button
	 * @param y
	 *            - coordinate of middle of button
	 */
	public TextButton(String text, float x, float y){
		super(text, x, y);
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
		this(text, x, y);
		this.shortcut = shortcut;
	}
	
	public void update(){
		if(Gdx.input.isButtonPressed(Buttons.LEFT)){
			if(rectangle.contains(DodgeGame.INPUT.getInputX(), DodgeGame.INPUT.getInputY())){
				doAction();
			}
		}
		
		if(shortcut != Keys.UNKNOWN){
			if(Gdx.input.isKeyPressed(shortcut)){
				doAction();
			}
		}
	}
	
	public abstract void doAction();
}
