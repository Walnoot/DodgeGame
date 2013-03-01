package walnoot.dodgegame.states;

import walnoot.dodgegame.ButtonClickListener;
import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainMenuState extends State{
	private Table table;
	private Sprite[] sprites = new Sprite[32];
	private float time;
	
	public MainMenuState(final OrthographicCamera camera){
		super(camera);
		
		for(int i = 0; i < sprites.length; i++){
			Sprite sprite = new Sprite(Util.HAND);
			
			float width = camera.viewportWidth * camera.zoom;
			
			sprite.setPosition(width * (i / (float) sprites.length) - width * 0.5f, -camera.viewportHeight
					* camera.zoom * 0.5f - 0.25f);
			
			sprite.setSize(1f, 2f);
			sprite.setOrigin(0.5f, 0f);
			sprite.flip(MathUtils.randomBoolean(), false);
			
			sprites[i] = sprite;
		}
		
		table = new Table();
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
				DodgeGame.setState(new OptionsState(camera));
			}
		});
		table.add(optionsButton).fillX();
		
		TextButton creditsButton = new TextButton("CREDITS", Util.SKIN);
		creditsButton.addListener(new ButtonClickListener(){
			public void click(Actor actor){
				DodgeGame.setState(new CreditsState(camera));
			}
		});
		table.add(creditsButton).fillX();
		
		TextButton statsButton = new TextButton("STATS", Util.SKIN);
		statsButton.addListener(new ButtonClickListener(){
			public void click(Actor actor){
				DodgeGame.setState(new StatState(camera));
			}
		});
		table.add(statsButton).fillX();
	}
	
	public void update(){
		time += DodgeGame.SECONDS_PER_UPDATE;
		
		for(int i = 0; i < sprites.length; i++){
			Sprite sprite = sprites[i];
			
			//random rotation based on index and time
			sprite.setRotation(MathUtils.cos(time * 4f + (i ^ (i * 47))) * 25f);
			
			sprite.setPosition(sprite.getX() + MathUtils.cos(time + (i ^ (i * 23))) * DodgeGame.SECONDS_PER_UPDATE
					* 0.5f, sprite.getY() + MathUtils.cos(time + (i ^ (i * 53))) * DodgeGame.SECONDS_PER_UPDATE * 0.1f);
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < sprites.length; i++){
			sprites[i].draw(batch);
		}
	}
	
	public void dispose(){
	}
}
