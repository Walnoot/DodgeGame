package walnoot.dodgegame;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class LoadingThread implements Runnable{
	private String loadText = "";
	private boolean done = false;
	
	public void run(){
		loadText = "LOADING PREFERENCES";
		DodgeGame.PREFERENCES = Gdx.app.getPreferences("DodgeGamePrefs");
		
		loadText = "INITIALIZING TWEEN MANAGER";
		DodgeGame.TWEEN_MANAGER = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		loadText = "LOADING FONT";
		
		DodgeGame.FONT = new BitmapFont(Gdx.files.internal("komika_axis.fnt"), Util.FONT, false);
		
		DodgeGame.FONT.setUseIntegerPositions(false);
		DodgeGame.FONT.setScale(DodgeGame.FONT_SCALE);
		
		loadText = "LOADING SOUNDS";
		DodgeGame.SOUND_MANAGER.init();
		
		done = true;
	}
	
	public String getLoadText(){
		return loadText;
	}
	
	public boolean isDone(){
		return done;
	}
}
