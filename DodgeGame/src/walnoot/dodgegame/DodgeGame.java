package walnoot.dodgegame;

import walnoot.dodgegame.states.GameState;
import walnoot.dodgegame.states.LoadingState;
import walnoot.dodgegame.states.State;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DodgeGame implements ApplicationListener{
	public static final float UPDATES_PER_SECOND = 30, SECONDS_PER_UPDATE = 1 / UPDATES_PER_SECOND;
	public static final float FONT_SCALE = 1f / 64f;
	
	public static BitmapFont FONT;
	public static Preferences PREFERENCES;
	public static SoundManager SOUND_MANAGER = new SoundManager();
	public static final InputHandler INPUT = new InputHandler();
	public static final ParticleHandler PARTICLE_HANDLER = new ParticleHandler();
	public static TweenManager TWEEN_MANAGER;
	
	public static State state;
	public static int gameTime;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private float updateTimer;
	private Sprite backgroundSprite;
	private boolean paused;
	
	public void create(){
		Gdx.input.setInputProcessor(INPUT);
		
		camera = new OrthographicCamera();
		camera.zoom = GameState.MAP_SIZE;
		
		camera.update();
		
		INPUT.setCamera(camera);
		
		batch = new SpriteBatch(200);
		
		Util.ATLAS = new TextureAtlas("assets.pack");
		
		Util.loadRegions();
		
		backgroundSprite = new Sprite(Util.BACKGROUND);
		
		state = new LoadingState(camera);
	}

	public void dispose(){
		PREFERENCES.putBoolean(SoundManager.PREF_SOUND_KEY, SOUND_MANAGER.isPlaying());
		PREFERENCES.flush();
		
		batch.dispose();
		FONT.dispose();
		//TEXTURE.dispose();
		SOUND_MANAGER.dispose();
		Util.ATLAS.dispose();
	}
	
	public void render(){
		updateTimer += Gdx.graphics.getDeltaTime();
		while(updateTimer > SECONDS_PER_UPDATE){
			updateTimer -= SECONDS_PER_UPDATE;
			
			update();
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		backgroundSprite.draw(batch);
		
		state.render(batch);
		if(PARTICLE_HANDLER.isLoaded()) PARTICLE_HANDLER.render(batch);
		
		if(FONT != null){
			FONT.setColor(1, 0, 0, 1);
			FONT.draw(batch, "FPS: " + (int) Gdx.graphics.getFramesPerSecond(),
					-camera.viewportWidth * camera.zoom / 2f, camera.viewportHeight * camera.zoom / 2f);
		}
		
		batch.end();
	}
	
	public void update(){
		if(INPUT.escape.isPressed()) Gdx.app.exit();
		if(INPUT.fullscreen.isJustPressed()) Gdx.graphics.setDisplayMode(1920, 1080, true);//for recording, change later
			
		state.update();
		if(!paused && SOUND_MANAGER.isLoaded()) SOUND_MANAGER.update();
		if(PARTICLE_HANDLER.isLoaded()) PARTICLE_HANDLER.update();
		INPUT.update();
		if(TWEEN_MANAGER != null) TWEEN_MANAGER.update(SECONDS_PER_UPDATE);
		
		gameTime++;
		
		//System.out.println(batch.maxSpritesInBatch);
		//batch.maxSpritesInBatch = 0;
	}
	
	public static void setState(State state){
		DodgeGame.state = state;
		state.resize();
	}
	
	public void resize(int width, int height){
		if(width > height){
			camera.viewportWidth = 2f * width / height;
			camera.viewportHeight = 2f;
		}else{
			camera.viewportWidth = 2f;
			camera.viewportHeight = 2f * height / width;
		}
		
		camera.update();
		
		backgroundSprite.setPosition(-camera.viewportWidth * camera.zoom * 0.5f,
				-camera.viewportHeight * camera.zoom * 0.5f);
		backgroundSprite.setSize(camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
		
		state.resize();
	}
	
	public void pause(){
		paused = true;
		
		state.pause();
	}
	
	public void resume(){
		paused = false;
	}
}
