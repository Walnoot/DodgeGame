package walnoot.dodgegame;

import static com.badlogic.gdx.Input.Keys.*;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor{
	public Key up = new Key(W, UP);
	public Key down = new Key(S, DOWN);
	public Key left = new Key(A, LEFT);
	public Key right = new Key(D, RIGHT);
	public Key toggleSound = new Key(P);
	public Key escape = new Key(ESCAPE);
	public Key tab = new Key(TAB);
	
	private ArrayList<Key> keys;
	
	/**
	 * Make sure to call after game logic update() is called
	 */
	public void update(){
		for(int i = 0; i < keys.size(); i++){
			keys.get(i).update();
		}
	}
	
	public boolean keyDown(int keyCode){
		for(int i = 0; i < keys.size(); i++){
			if(keys.get(i).has(keyCode)) keys.get(i).press();
		}
		
		return true;
	}
	
	public boolean keyUp(int keyCode){
		for(int i = 0; i < keys.size(); i++){
			if(keys.get(i).has(keyCode)) keys.get(i).release();
		}
		
		return true;
	}
	
	public boolean keyTyped(char character){
		return true;
	}
	
	public boolean touchDown(int x, int y, int pointer, int button){
		return true;
	}
	
	public boolean touchUp(int x, int y, int pointer, int button){
		return true;
	}
	
	public boolean touchDragged(int x, int y, int pointer){
		return true;
	}
	
	public boolean touchMoved(int x, int y){
		return true;
	}
	
	public boolean scrolled(int amount){
		return true;
	}
	
	public class Key{
		private final int[] keyCodes;
		private boolean pressed, justPressed;
		
		public Key(int...keyCodes){
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
