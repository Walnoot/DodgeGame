package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextElement{
	private String text;
	private float xPos, yPos, scale;
	private Color color = new Color(Color.BLACK);
	private TextBounds bounds;
	
	public TextElement(String text, float x, float y, float scale){
		xPos = x;
		yPos = y;
		this.scale = scale;
		
		setText(text);
	}
	
	public TextElement(String text, float x, float y){
		this(text, x, y, 1f);
	}
	
	public void render(SpriteBatch batch){
		DodgeGame.SCALE_FONT.setColor(color);
		DodgeGame.SCALE_FONT.setScale(DodgeGame.FONT_SCALE * scale);
		DodgeGame.SCALE_FONT.draw(batch, text, xPos - getWidth() * 0.5f, yPos + getHeight() * 0.5f);
		DodgeGame.SCALE_FONT.setScale(DodgeGame.FONT_SCALE);
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
	
	public float getScale(){
		return scale;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
		
		DodgeGame.SCALE_FONT.setScale(DodgeGame.FONT_SCALE);
		bounds = new TextBounds(DodgeGame.SCALE_FONT.getBounds(text));
	}
	
	public void setPos(float x, float y){
		xPos = x;
		yPos = y;
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}
}
