package walnoot.dodgegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ParticleHandler{
	private boolean loaded;
	private ParticleEffectPool pool;
	
	private Array<PooledEffect> effects = new Array<PooledEffect>(8);

	public void load(){
		ParticleEffect shine = new ParticleEffect();
		shine.loadEmitters(Gdx.files.internal("effects/shine.dat"));
		shine.getEmitters().get(0).setSprite(new Sprite(Util.SHINE));
		
		pool = new ParticleEffectPool(shine, 2, 8);

		loaded = true;
	}
	
	public void addShineEffect(float x, float y){
		PooledEffect effect = pool.obtain();
		
		effect.setPosition(x, y);
		
		effects.add(effect);
	}

	public void render(SpriteBatch batch){
		for(int i = effects.size - 1; i >= 0; i--){
			PooledEffect effect = effects.get(i);
			effect.draw(batch);
			
			if(effect.isComplete()){
				effect.free();
				effects.removeIndex(i);
			}
		}
	}
	
	public void update(){
		for(int i = effects.size - 1; i >= 0; i--){
			effects.get(i).update(DodgeGame.SECONDS_PER_UPDATE);
		}
	}
	
	public boolean isLoaded(){
		return loaded;
	}
}
