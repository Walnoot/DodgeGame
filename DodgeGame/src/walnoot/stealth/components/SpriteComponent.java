package walnoot.stealth.components;

import walnoot.dodgegame.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteComponent extends Component{
	private Sprite sprite;
	private float spinSpeed = 0f;
	
	public SpriteComponent(Entity owner, TextureRegion region){
		super(owner);
		
		newSprite(region);
	}
	
	public SpriteComponent(Entity owner, Texture texture){
		this(owner, new TextureRegion(texture));
	}
	
	public SpriteComponent(Entity owner, TextureRegion region, Color color){
		this(owner, region);
		sprite.setColor(color);
	}

	public SpriteComponent(Entity owner, TextureRegion region, float scale){
		this(owner, region);
		sprite.setScale(scale);
	}
	
	public void update(){
		sprite.rotate(spinSpeed);
	}
	
	public void render(SpriteBatch batch){
		sprite.setPosition(owner.getxPos() - sprite.getWidth() / 2, owner.getyPos() - sprite.getHeight() / 2);
		sprite.draw(batch);
	}
	
	public void newSprite(TextureRegion region){
		sprite = new Sprite(region);
		sprite.setSize(1f, 1f);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}
	
	public void setSpinSpeed(float spinSpeed){
		this.spinSpeed = spinSpeed;
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public Component getCopy(Entity owner){
		return new SpriteComponent(owner, sprite);
	}
	
	public ComponentIdentifier getIdentifier(){
		return ComponentIdentifier.SPRITE_COMPONENT;
	}
}
