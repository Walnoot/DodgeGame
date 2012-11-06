package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextElement{
	private String text;
	//protected final Rectangle rectangle;
	private float xPos, yPos, scale;
	private Color color = new Color(Color.BLACK);
	private TextBounds bounds;
	
	public TextElement(String text, float x, float y, float scale){
		this.text = text;
		xPos = x;
		yPos = y;
		this.scale = scale;
		
		bounds = new TextBounds(DodgeGame.FONT.getBounds(text));
	}
	
	public TextElement(String text, float x, float y){
		this(text, x, y, 1f);
	}
	
	public void render(SpriteBatch batch){
		DodgeGame.FONT.setColor(color);
		DodgeGame.FONT.setScale(DodgeGame.FONT_SCALE * scale);
		DodgeGame.FONT.draw(batch, text, xPos - getWidth() * 0.5f, yPos + getHeight() * 0.5f);
		DodgeGame.FONT.setScale(DodgeGame.FONT_SCALE);
	}
	
	protected float getWidth(){
		return bounds.width * scale;
	}
	
	protected float getHeight(){
		return bounds.height * scale;
	}
	
	public float getxPos(){
		return xPos;
	}
	
	public float getyPos(){
		return yPos;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setPos(float x, float y){
		xPos = x;
		yPos = y;
	}
}
