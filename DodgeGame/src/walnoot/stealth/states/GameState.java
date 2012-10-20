package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.Map;
import walnoot.stealth.components.MoveComponent;
import walnoot.stealth.components.ObjectModifierComponent;
import walnoot.stealth.components.ObjectModifierComponent.ModifierType;
import walnoot.stealth.components.PlayerComponent;
import walnoot.stealth.components.SpriteComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class GameState extends State{
	public static final int SPAWN_RATE = 20;
	public static final float MAP_SIZE = 8;
	public static final int GROW_OBJECT_CHANGE = 40, SHRINK_OBJECT_CHANGE = 40; //out of 100
	
	private Map map;
	private int enemySpawnTimer = SPAWN_RATE; //an enemy spawn when this is zero
	
	private int gameOverTimer; //delays the switch to game over state
	private boolean gameOver;
	
	public GameState(OrthographicCamera camera){
		super(camera);
		
		Entity playerEntity = new Entity(0, 0, 0);
		playerEntity.addComponent(new SpriteComponent(playerEntity, DodgeGame.TEXTURES[0][0], Color.BLACK));
		PlayerComponent playerComponent = new PlayerComponent(playerEntity, camera);
		playerEntity.addComponent(playerComponent);
		
		map = new Map(playerComponent);
		map.addEntity(playerEntity);
	}
	
	public void update(){
		enemySpawnTimer--;
		if(enemySpawnTimer == 0){
			enemySpawnTimer = SPAWN_RATE;
			
			float x, y, rotation = 0;
			
			int side = MathUtils.random(0, 3);
			
			if(side == 0 || side == 2){
				x = MathUtils.random(-MAP_SIZE, MAP_SIZE);
				y = side == 0 ? -MAP_SIZE : MAP_SIZE;
				
				if(side == 0) rotation = MathUtils.random(45f, 135f);
				if(side == 2) rotation = MathUtils.random(-135f, -45f);
			}else{
				y = MathUtils.random(-MAP_SIZE, MAP_SIZE);
				x = side == 1 ? -MAP_SIZE : MAP_SIZE;
				
				if(side == 1) rotation = MathUtils.random(-45f, 45f);
				if(side == 3) rotation = MathUtils.random(135f, 225f);
			}
			
			Entity object = new Entity(x, y, rotation);
			object.addComponent(new SpriteComponent(object, DodgeGame.TEXTURES[0][0]));
			object.addComponent(new MoveComponent(object));
			
			ModifierType type;
			int randomInt = MathUtils.random(0, 99);
			
			if(randomInt < GROW_OBJECT_CHANGE) type = ModifierType.GROW;
			else if(randomInt < GROW_OBJECT_CHANGE + SHRINK_OBJECT_CHANGE) type = ModifierType.SHRINK;
			else type = ModifierType.DEATH;
			
			object.addComponent(new ObjectModifierComponent(object, type));
			
			map.addEntity(object);
		}
		
		if(gameOver){
			gameOverTimer++;
			
			if(gameOverTimer == (int) DodgeGame.UPDATES_PER_SECOND) DodgeGame.setState(new GameOverState(this));
		}
		
		map.update();
	}

	public void gameOver(){
		gameOver = true;
	}
	
	public void render(SpriteBatch batch){
		camera.zoom = MAP_SIZE;
		map.render(batch);
	}
	
	public void dispose(){
	}
}
