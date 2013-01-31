package walnoot.dodgegame.states;

import walnoot.dodgegame.ButtonClickListener;
import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class OptionsState extends State{
	public OptionsState(OrthographicCamera camera){
		super(camera);
		
		Table table = new Table();
		table.setFillParent(true);
		table.pad(64);
		stage.addActor(table);
		
		LabelStyle labelStyle = new LabelStyle(DodgeGame.UI_FONT, Color.BLACK);
		Label label = new Label("VOLUME", labelStyle);
		table.add(label);
		
		Slider slider = new Slider(0f, 1f, 1f / 1000f, false, Util.SKIN);
		slider.setValue(DodgeGame.SOUND_MANAGER.getVolume());
		
		slider.addListener(new EventListener(){
			public boolean handle(Event event){
				DodgeGame.SOUND_MANAGER.setVolume(((Slider) event.getTarget()).getValue());
				
				return false;
			}
		});
		
		table.add(slider).expandX().fill();
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
