package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class SpriteButton{
	private Sprite sprite;
	private Rectangle bounds = new Rectangle();
	
	public SpriteButton(float x, float y, float scale, TextureRegion regionOn){
		sprite = new Sprite(regionOn);
		sprite.setSize(1f, 1f);
		sprite.setOrigin(0.5f, 0.5f);
		sprite.setScale(scale);
		
		setPosition(x, y);
	}
	
	public void render(SpriteBatch batch){
		sprite.draw(batch);
	}
	
	public void update(){
		if(DodgeGame.INPUT.isJustTouched()){
			float x = DodgeGame.INPUT.getInputX();
			float y = DodgeGame.INPUT.getInputY();
			
			if(x > sprite.getX() && y > sprite.getY()){
				if(x < sprite.getX() + sprite.getScaleX() && y < sprite.getY() + sprite.getScaleY())
					System.out.println("eag;huafouhspi;hurfg;ou;uhqaiagf");
			}
			
			//System.out.printf("%f, %f\n", DodgeGame.INPUT.getInputX(), DodgeGame.INPUT.getInputY());
			//System.out.printf("%f, %f, %f\n", sprite.getX(), sprite.getY(), sprite.getScaleX());
		}
	}
	
	public void setPosition(float x, float y){
		sprite.setPosition(x, y);
		sprite.setPosition(x - sprite.getScaleX() / 4f, y - sprite.getScaleY() / 4f);
		
		bounds.setX(x - sprite.getScaleX() / 2f);
		bounds.setY(x - sprite.getScaleY() / 2f);
	}
	
	public abstract void activate();
}
