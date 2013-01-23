package walnoot.dodgegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParticleHandler{
	public ParticleEffect shine;
	private boolean loaded;
	
	public void load(){
		shine = new ParticleEffect();
		shine.loadEmitters(Gdx.files.internal("effects/shine.dat"));
		shine.getEmitters().get(0).setSprite(new Sprite(Util.SHINE));
		
		loaded = true;
	}
	
	public void render(SpriteBatch batch){
		shine.draw(batch);
	}
	
	public void update(){
		shine.update(DodgeGame.SECONDS_PER_UPDATE);
	}
	
	public boolean isLoaded(){
		return loaded;
	}
}
