package walnoot.dodgegame.states;

import walnoot.dodgegame.Util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class State{
	protected final OrthographicCamera camera;
	public Stage stage;
	
	public State(OrthographicCamera camera){
		this.camera = camera;
		
		Vector2 dimensions = Util.getStageDimensions();
		stage = new Stage(dimensions.x, dimensions.y, false);
	}
	
	public abstract void update();
	
	public abstract void render(SpriteBatch batch);
	
	public void renderUI(SpriteBatch stateBatch){
		stage.act();
		stage.draw();
		Table.drawDebug(stage);
	}
	
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
	
	public boolean playsGameMusic(){
		return false;
	}
}
