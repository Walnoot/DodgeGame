package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class TextElement{
	private String text;
	protected final Rectangle rectangle;
	private final float scale;
	private Color color = new Color(Color.BLACK);
	
	public TextElement(String text, float x, float y, float scale){
		this.text = text;
		this.scale = scale;
		
		TextBounds bounds = DodgeGame.FONT.getBounds(text);
		
		rectangle = new Rectangle(x - (bounds.width / 2) * scale, y - (bounds.height / 2) * scale,
				bounds.width * scale, bounds.height * scale);
	}
	
	public TextElement(String text, float x, float y){
		this(text, x, y, 1f);
	}
	
	public void render(SpriteBatch batch){
		DodgeGame.FONT.setColor(color);
		DodgeGame.FONT.setScale(DodgeGame.FONT_SCALE * scale);
		DodgeGame.FONT.draw(batch, text, rectangle.x, rectangle.y + rectangle.height);
		DodgeGame.FONT.setScale(DodgeGame.FONT_SCALE);
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setPos(float x, float y){
		rectangle.x = x - rectangle.width / 2;
		rectangle.y = y - rectangle.height / 2;
	}
}
