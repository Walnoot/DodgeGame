package walnoot.dodgegame.states;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State{
	protected final OrthographicCamera camera;
	
	public State(OrthographicCamera camera){
		this.camera = camera;
	}
	
	public abstract void update();
	
	public abstract void render(SpriteBatch batch);
	
	public void renderUI(){}
	
	public abstract void dispose();
	
	/**
	 * Called when the window is resized or the state is instantiated.
	 */
	public void resize(){
	}
	
	public Camera getCamera(){
		return camera;
	}
	
	public void pause(){
	}
}
