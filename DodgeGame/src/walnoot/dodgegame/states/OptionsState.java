package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.SoundManager;
import walnoot.dodgegame.ui.BooleanPreferenceButton;
import walnoot.dodgegame.ui.ReturnButton;
import walnoot.dodgegame.ui.TextButton;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OptionsState extends State{
	private final TextButton[] buttons;
	
	public OptionsState(OrthographicCamera camera){
		super(camera);
		
		ReturnButton returnButton = new ReturnButton(camera, new MainMenuState(camera));
		
		TextButton soundButton = new BooleanPreferenceButton("SOUND:", 0, 4f, 3f, SoundManager.PREF_SOUND_KEY){
			public void setChanges(boolean value){
				DodgeGame.SOUND_MANAGER.toggleSound();
			}
		};
		
		TextButton tutorialButton = new BooleanPreferenceButton("TUTORIAL:", 0, 1f, 3f, TutorialState.PREF_TUTORIAL_KEY){
			public void setChanges(boolean value){
			}
		};
		
		buttons = new TextButton[] {returnButton, soundButton, tutorialButton};
	}
	
	public void update(){
		for(int i = 0; i < buttons.length; i++){
			buttons[i].update();
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < buttons.length; i++){
			buttons[i].render(batch);
		}
	}
	
	public void dispose(){
	}
}
