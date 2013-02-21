package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.SoundManager;
import walnoot.dodgegame.SpriteAccessor;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.ui.TextElement;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoadingState extends State{
	private TextElement loadText;
	private int loadIndex;
	
	public LoadingState(OrthographicCamera camera){
		super(camera);
	}
	
	public void update(){
	}
	
	public void render(SpriteBatch batch){
		switch (loadIndex){
			case 0:
				DodgeGame.SCALE_FONT = new BitmapFont(Gdx.files.internal("komika_axis.fnt"), Util.FONT, false);
				DodgeGame.SCALE_FONT.setUseIntegerPositions(false);
				DodgeGame.SCALE_FONT.setScale(DodgeGame.FONT_SCALE);
				
				DodgeGame.UI_FONT = new BitmapFont(Gdx.files.internal("komika_axis.fnt"), Util.FONT, false);
				DodgeGame.UI_FONT.setUseIntegerPositions(false);
				
				loadText = new TextElement("LOADED FONT", 0, 0);
				break;
			case 1:
				DodgeGame.PREFERENCES = Gdx.app.getPreferences("DodgeGamePrefs");
				
				if(!DodgeGame.PREFERENCES.contains(SoundManager.PREF_SOUND_KEY))
					DodgeGame.PREFERENCES.putFloat(SoundManager.PREF_SOUND_KEY, 1f);
				
				if(!DodgeGame.PREFERENCES.contains(TutorialState.PREF_TUTORIAL_KEY))
					DodgeGame.PREFERENCES.putBoolean(TutorialState.PREF_TUTORIAL_KEY, true);
				
				loadText.setText("LOADED PREFERENCES");
				break;
			case 2:
				DodgeGame.TWEEN_MANAGER = new TweenManager();
				Tween.registerAccessor(Sprite.class, new SpriteAccessor());
				
				loadText.setText("LOADED TWEENMANAGER");
				break;
			case 3:
				DodgeGame.SOUND_MANAGER.init();
				
				loadText.setText("LOADED SOUNDS");
				break;
			case 4:
				DodgeGame.PARTICLE_HANDLER.load();
				
				break;
			case 5:
				Util.setSkin();
				
				break;
			default:
				DodgeGame.setState(new MainMenuState(camera));
				//DodgeGame.setState(new StageState(camera));
				
				break;
		}
		loadIndex++;
		
		if(loadText != null) loadText.render(batch);
	}
	
	public void dispose(){
	}
}
