package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;

public class TextElement{
	private String text;
	protected Rectangle rectangle;
	
	public TextElement(String text, float x, float y){
		this.text = text;
		
		TextBounds bounds = DodgeGame.FONT.getBounds(text);
		
		rectangle = new Rectangle(x - bounds.width / 2, y - bounds.height / 2, bounds.width, bounds.height);
	}
	
	public void render(SpriteBatch batch){
		DodgeGame.FONT.draw(batch, text, rectangle.x, rectangle.y + rectangle.height);
	}
}
