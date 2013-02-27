package walnoot.dodgegame.states;

import walnoot.dodgegame.ButtonClickListener;
import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class OptionsState extends State{
	public OptionsState(OrthographicCamera camera){
		super(camera);
		
		Table table = new Table();
		table.setFillParent(true);
		table.pad(64);
		stage.addActor(table);
		
		table.add(new Label("SOUND", Util.SKIN));
		
		Slider soundSlider = new Slider(0f, 1f, 1f / 1000f, false, Util.SKIN);
		soundSlider.setValue(DodgeGame.SOUND_MANAGER.getSoundVolume());
		
		soundSlider.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor){
				DodgeGame.SOUND_MANAGER.setSoundVolume(((Slider) actor).getValue());
			}
		});
		
		table.add(soundSlider).expandX().fill();
		table.row();
		
		table.add(new Label("MUSIC", Util.SKIN));
		
		Slider musicSlider = new Slider(0f, 1f, 1f / 1000f, false, Util.SKIN);
		musicSlider.setValue(DodgeGame.SOUND_MANAGER.getMusicVolume());
		
		musicSlider.addListener(new ChangeListener(){
			public void changed(ChangeEvent event, Actor actor){
				DodgeGame.SOUND_MANAGER.setMusicVolume(((Slider) actor).getValue());
			}
		});
		
		table.add(musicSlider).expandX().fill();
		table.row();
		
		CheckBox tutorialBox = new CheckBox("TUTORIAL", Util.SKIN);
		tutorialBox.setChecked(DodgeGame.PREFERENCES.getBoolean(TutorialState.PREF_TUTORIAL_KEY, true));
		tutorialBox.addListener(new ButtonClickListener(){
			public void click(Actor actor){
				DodgeGame.PREFERENCES.putBoolean(TutorialState.PREF_TUTORIAL_KEY, ((CheckBox) actor).isChecked());
			}
		});
		table.add(tutorialBox).colspan(2);
		
		Util.addQuitButton(stage, camera);
	}

	public void update(){
	}
	
	public void render(SpriteBatch batch){
	}
	
	public void dispose(){
		DodgeGame.PREFERENCES.flush();
	}
}
