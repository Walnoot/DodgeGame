package walnoot.dodgegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ParticleHandler{
	private boolean loaded;
	private ParticleEffectPool pool;
	
	private Array<PooledEffect> effects = new Array<PooledEffect>(8);

	public void load(){
		ParticleEffect shine = new ParticleEffect();
		shine.load(Gdx.files.internal("effects/shine.dat"), Util.ATLAS);
		
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
			effect.draw(batch, Gdx.graphics.getDeltaTime());
			
			if(effect.isComplete()){
				effect.free();
				effects.removeIndex(i);
			}
		}
	}
	
	public void update(){
	}
	
	public boolean isLoaded(){
		return loaded;
	}
}
