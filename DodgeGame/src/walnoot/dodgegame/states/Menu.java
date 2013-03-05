package walnoot.dodgegame.states;

import walnoot.dodgegame.ButtonClickListener;
import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Stat;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public enum Menu{
	MAIN(){
		void setupStage(Stage stage, final MenuState state, final OrthographicCamera camera){
			Table table = new Table();
			table.setFillParent(true);
			stage.addActor(table);
			
			table.defaults().height(96f);
			
			TextButton playButton = new TextButton("PLAY", Util.SKIN);
			playButton.addListener(new ButtonClickListener(){
				public void click(Actor actor){
					if(DodgeGame.PREFERENCES.getBoolean(TutorialState.PREF_TUTORIAL_KEY, true)) DodgeGame
							.setState(new TutorialState(camera));
					else DodgeGame.setState(new GameState(camera));
				}
			});
			table.add(playButton).height(128f).padBottom(32f).width(480f).colspan(3);
			table.row();
			
			TextButton optionsButton = new TextButton("OPTIONS", Util.SKIN);
			optionsButton.addListener(new ButtonClickListener(){
				public void click(Actor actor){
					state.setMenu(OPTIONS);
				}
			});
			table.add(optionsButton).fillX();
			
			TextButton creditsButton = new TextButton("CREDITS", Util.SKIN);
			creditsButton.addListener(new ButtonClickListener(){
				public void click(Actor actor){
					state.setMenu(CREDITS);
				}
			});
			table.add(creditsButton).fillX();
			
			TextButton statsButton = new TextButton("STATS", Util.SKIN);
			statsButton.addListener(new ButtonClickListener(){
				public void click(Actor actor){
					state.setMenu(STAT);
				}
			});
			table.add(statsButton).fillX();
		}
	},
	STAT(){
		void setupStage(Stage stage, final MenuState state, OrthographicCamera camera){
			Table table = new Table();
			table.setFillParent(true);
			stage.addActor(table);
			
			addRow(Stat.HIGH_SCORE, table);
			addRow(Stat.NUM_DEATHS, table);
			addRow(Stat.NUM_FOOD_EATEN, table);
			addRow(Stat.TICKS_PLAYED, table);
			addRow(Stat.NUM_TIMES_PLAYED, table);
			
			addQuitButton(stage, state);
		}
		
		private void addRow(Stat stat, Table table){
			table.add(new Label(stat.getName(), Util.SKIN)).left();
			table.add(new Label(stat.getValue(), Util.SKIN)).right();
			table.row();
		}
	},
	OPTIONS(){
		void setupStage(Stage stage, MenuState state, OrthographicCamera camera){
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
			
			addQuitButton(stage, state);
		}
	},
	CREDITS(){
		void setupStage(Stage stage, MenuState state, OrthographicCamera camera){
			String text = Gdx.files.internal("credits.txt").readString();
			
			Label label = new Label(text, Util.SKIN);
			label.setWrap(true);
			
			ScrollPane pane = new ScrollPane(label, Util.SKIN);
			pane.setFillParent(true);
			
			Table container = new Table();
			container.setFillParent(true);
			container.add(pane).expand().fill();
			
			stage.addActor(container);
			
			addQuitButton(stage, state);
		}
	};
	
	void addQuitButton(Stage stage, final MenuState state){
		TextButton button = new TextButton("BACK", Util.SKIN);
		button.addListener(new ButtonClickListener(){
			public void click(Actor actor){
				state.setMenu(MAIN);
			}
		});
		
		stage.addActor(button);
	}
	
	abstract void setupStage(Stage stage, final MenuState state, final OrthographicCamera camera);
}