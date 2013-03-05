package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class MenuState extends State{
	private Sprite[] sprites = new Sprite[32];
	private float time;
	
	public MenuState(final OrthographicCamera camera){
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
		
		Menu.MAIN.setupStage(stage, this, camera);
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
	
	public void setMenu(Menu menu){
		stage.clear();
		
		menu.setupStage(stage, this, camera);
	}
	
	public void dispose(){
	}
}
