package walnoot.dodgegame;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.BACKSPACE;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.badlogic.gdx.Input.Keys.F12;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.P;
import static com.badlogic.gdx.Input.Keys.Q;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.TAB;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class InputHandler implements InputProcessor{
	public Key up = new Key(W, UP);
	public Key down = new Key(S, DOWN);
	public Key left = new Key(A, LEFT);
	public Key right = new Key(D, RIGHT);
	public Key toggleSound = new Key(Q);
	public Key escape = new Key(ESCAPE);
	public Key tab = new Key(TAB);
	public Key pause = new Key(P, SPACE);
	public Key fullscreen = new Key(F12);
	public Key back = new Key(BACKSPACE, SPACE);
	
	private ArrayList<Key> keys;
	private OrthographicCamera camera;
	private boolean keyDown;
	private int scrollAmount;
	private boolean justTouched;
	private Vector2 lastMousePos = new Vector2();
	
	/**
	 * Make sure to call after game logic update() is called
	 */
	public void update(){
		for(int i = 0; i < keys.size(); i++){
			keys.get(i).update();
		}
		
		keyDown = false;
		scrollAmount = 0;
		justTouched = false;
		
		lastMousePos.set(getInputX(), getInputY());
	}
	
	public float getInputX(){
		float inputX = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2f);
		inputX /= (Gdx.graphics.getWidth() / 2f);
		inputX *= (camera.viewportWidth / 2f) * camera.zoom;
		
		return inputX;
	}
	
	public float getInputY(){
		float inputY = -(Gdx.input.getY() - Gdx.graphics.getHeight() / 2f);
		inputY /= (Gdx.graphics.getHeight() / 2f);
		inputY *= (camera.viewportHeight / 2f) * camera.zoom;
		
		return inputY;
	}
	
	public float getMousedx(){
		return getInputX() - lastMousePos.x;
	}
	
	public float getMousedy(){
		return getInputY() - lastMousePos.y;
	}
	
	public boolean isAnyKeyDown(){
		return keyDown;
	}
	
	public int getScrollAmount(){
		return scrollAmount;
	}
	
	public boolean isJustTouched(){
		return justTouched;
	}
	
	public void setCamera(OrthographicCamera camera){
		this.camera = camera;
	}
	
	public boolean keyDown(int keyCode){
		for(int i = 0; i < keys.size(); i++){
			if(keys.get(i).has(keyCode)) keys.get(i).press();
		}
		
		keyDown = true;
		
		return false;
	}
	
	public boolean keyUp(int keyCode){
		for(int i = 0; i < keys.size(); i++){
			if(keys.get(i).has(keyCode)) keys.get(i).release();
		}
		
		return false;
	}
	
	public boolean keyTyped(char character){
		return false;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button){
		if(button == Buttons.LEFT) justTouched = true;
		return false;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button){
		return false;
	}
	
	public boolean touchDragged(int x, int y, int pointer){
		return false;
	}
	
	public boolean touchMoved(int x, int y){
		return false;
	}
	
	public boolean scrolled(int amount){
		scrollAmount += amount;
		
		return false;
	}
	
	public boolean mouseMoved(int screenX, int screenY){
		return false;
	}
	
	public class Key{
		private final int[] keyCodes;
		private boolean pressed, justPressed;
		
		public Key(int... keyCodes){
			this.keyCodes = keyCodes;
			
			if(keys == null) keys = new ArrayList<Key>();
			keys.add(this);
		}
		
		private void update(){
			justPressed = false;
		}
		
		public boolean has(int keyCode){
			for(int i = 0; i < keyCodes.length; i++){
				if(keyCodes[i] == keyCode) return true;
			}
			
			return false;
		}
		
		private void press(){
			pressed = true;
			justPressed = true;
		}
		
		private void release(){
			pressed = false;
		}
		
		public boolean isPressed(){
			return pressed;
		}
		
		public boolean isJustPressed(){
			return justPressed;
		}
	}
}
