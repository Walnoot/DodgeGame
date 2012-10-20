package walnoot.dodgegame;

import walnoot.stealth.states.GameState;
import walnoot.stealth.states.State;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DodgeGame implements ApplicationListener{
	public static final float UPDATES_PER_SECOND = 60, SECONDS_PER_UPDATE = 1 / UPDATES_PER_SECOND;
	public static final float FONT_SCALE = 1f / 64f;
	public static BitmapFont FONT;
	public static TextureRegion[][] TEXTURES;
	public static Preferences PREFERENCES;
	public static final MusicManager MUSIC_MANAGER = new MusicManager();
	public static final InputHandler INPUT = new InputHandler();
	public static TweenManager TWEEN_MANAGER;
	
	public static State state;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private float updateTimer;
	
	public void create(){
		Gdx.input.setInputProcessor(INPUT);
		PREFERENCES = Gdx.app.getPreferences("DodgeGamePrefs");
		
		TWEEN_MANAGER = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		camera = new OrthographicCamera();
		
		batch = new SpriteBatch();
		
		FONT = new BitmapFont(Gdx.files.internal("font/font.fnt"), false);
		
		FONT.setUseIntegerPositions(false);
		FONT.setScale(FONT_SCALE);
		FONT.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture texture = new Texture("images.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TEXTURES = new TextureRegion(texture).split(256, 256);
		
		MUSIC_MANAGER.init();
		
		state = new GameState(camera);
	}
	
	public void dispose(){
		PREFERENCES.putBoolean(MusicManager.SOUND_PREF_NAME, MUSIC_MANAGER.isPlaying());
		PREFERENCES.flush();
		
		batch.dispose();
		FONT.dispose();
		TEXTURES[0][0].getTexture().dispose();
		MUSIC_MANAGER.dispose();
	}
	
	public void render(){
		updateTimer += Gdx.graphics.getDeltaTime();
		while(updateTimer > SECONDS_PER_UPDATE){
			updateTimer -= SECONDS_PER_UPDATE;
			
			update();
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.apply(Gdx.gl10);
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		state.render(batch);
		
		FONT.setColor(1, 0, 0, 1);
		FONT.draw(batch, "FPS: " + (int) (1f / Gdx.graphics.getDeltaTime()), -camera.viewportWidth * camera.zoom / 2f, camera.viewportHeight * camera.zoom / 2f);
		
		batch.end();
	}
	
	public void update(){
		if(INPUT.escape.isPressed()) Gdx.app.exit();
		
		state.update();
		MUSIC_MANAGER.update();
		INPUT.update();
		TWEEN_MANAGER.update(SECONDS_PER_UPDATE);
	}
	
	public static void setState(State state){
		DodgeGame.state = state;
	}
	
	public void resize(int width, int height){
		if(width > height){
			camera.viewportWidth = 2f * width / height;
			camera.viewportHeight = 2f;
		}else{
			camera.viewportWidth = 2f;
			camera.viewportHeight = 2f * height / width;
		}
	}
	
	public void pause(){
	}
	
	public void resume(){
	}
}
