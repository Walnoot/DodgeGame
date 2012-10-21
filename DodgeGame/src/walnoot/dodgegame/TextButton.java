package walnoot.dodgegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class TextButton{
	private Rectangle rectangle;
	private String text;

	/**
	 * @param text
	 * @param x
	 *            - coordinate of middle of button
	 * @param y
	 *            - coordinate of middle of button
	 */
	public TextButton(String text, float x, float y){
		this.text = text;
		
		TextBounds bounds = DodgeGame.FONT.getBounds(text);
		
		rectangle = new Rectangle(x - bounds.width / 2, y - bounds.height / 2, bounds.width, bounds.height);
	}
	
	public void render(SpriteBatch batch){
		DodgeGame.FONT.draw(batch, text, rectangle.x, rectangle.y + rectangle.height);
	}
	
	public void update(){
		if(Gdx.input.justTouched()){
			if(rectangle.contains(DodgeGame.INPUT.getInputX(), DodgeGame.INPUT.getInputY())){
				doAction();
			}
		}
	}
	
	public abstract void doAction();
}
