package walnoot.dodgegame.states;

import java.util.ArrayList;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.components.Entity;
import walnoot.dodgegame.components.FoodComponent;
import walnoot.dodgegame.components.HeartComponent;
import walnoot.dodgegame.components.MoveComponent;
import walnoot.dodgegame.components.PlayerComponent;
import walnoot.dodgegame.components.SpriteComponent;
import walnoot.dodgegame.gameplay.BasicSpawnHandler;
import walnoot.dodgegame.gameplay.Map;
import walnoot.dodgegame.gameplay.SpawnHandler;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameState extends State{
	public static final float MAP_SIZE = 6;
	public static final int MAX_UNUSED_ENTITIES = 10;
	private static final int SPAWN_RATE_SCALE = 4;
	private static final float STATUS_TEXT_SCALE = 2f;//scale of the status text at the beginning
	
	private ArrayList<Entity> unusedEntities = new ArrayList<Entity>(MAX_UNUSED_ENTITIES);
	
	private Map map;
	private int enemySpawnTimer = SPAWN_RATE_SCALE; //an enemy spawn when this is zero
	
	private int gameOverTimer; //delays the switch to game over state
	private boolean gameOver;
	
	private int time;
	
	private String statusText;
	private Color statusColor;
	private int statusTimer;
	private TextBounds statusBounds = new TextBounds();
	
	private TextElement multiplierElement, scoreElement;
	
	private SpawnHandler spawnHandler = new BasicSpawnHandler();
	private int spawnHandlerTimer = spawnHandler.getDuration();
	
	public GameState(OrthographicCamera camera){
		super(camera);
		
		map = new Map();
		
		Entity playerEntity = new Entity(map, 0, 0, 0);
		playerEntity.addComponent(new SpriteComponent(playerEntity, Util.DOT, Color.BLACK));
		
		PlayerComponent playerComponent = new PlayerComponent(playerEntity, this);
		playerEntity.addComponent(playerComponent);
		
		map.addEntity(playerEntity);
		map.setPlayerComponent(playerComponent);
		
		for(int i = 0; i < PlayerComponent.NUM_START_LIVES; i++){
			Entity heart = new Entity(map, 0, 0, 0);
			
			heart.addComponent(new SpriteComponent(heart, Util.HEART, HeartComponent.SCALE));
			heart.addComponent(new HeartComponent(heart, i));
			
			map.addEntity(heart);
		}
		
		multiplierElement = new TextElement(Integer.toString(playerComponent.getScoreMultiplier()), -MAP_SIZE, 0f, 3f);
		scoreElement = new TextElement(Integer.toString(playerComponent.getScore()), -MAP_SIZE, 3f, 3f);
		
		Image image = new Image(Util.PAUSE);
		image.setSize(160f, 160f);
		image.addListener(new EventListener(){
			public boolean handle(Event event){
				if(event instanceof InputEvent){
					if(((InputEvent) event).getType() == Type.touchDown) pause();
				}
				return false;
			}
		});
		stage.addActor(image);
	}
	
	public void update(){
		time++;
		
		checkFoodSpawn();
		
		if(gameOver){
			gameOverTimer++;
			
			if(gameOverTimer == (int) DodgeGame.UPDATES_PER_SECOND) DodgeGame.setState(new GameOverState(this));
		}
		
		if(statusTimer != 0){
			statusTimer--;
		}
		
		map.update();
		
		if(DodgeGame.INPUT.pause.isJustPressed() && !gameOver){
			pause();
		}
		
		multiplierElement.setScale(3f + MathUtils.cosDeg(DodgeGame.gameTime * 3 * map.getPlayerComponent()
				.getScoreMultiplier()));
	}
	
	private void checkFoodSpawn(){
		enemySpawnTimer--;
		if(enemySpawnTimer == 0){
			enemySpawnTimer = spawnHandler.getPauseTicks(time);
			
			Entity food = getNewFood();
			
			spawnHandler.spawn(food, spawnHandlerTimer);
			
			map.addEntity(food);
		}
		
		spawnHandlerTimer--;
		if(spawnHandlerTimer == 0){
			spawnHandler = spawnHandler.getNextHandler();
			spawnHandler.init();
			spawnHandlerTimer = spawnHandler.getDuration();
		}
	}
	
	public void pause(){
		DodgeGame.setState(new PauseState(camera, this));
	}
	
	private Entity getNewFood(){
		/*if(unusedEntities.isEmpty()){
			Entity e = new Entity(map, 0, 0, 0);
			
			e.addComponent(new MoveComponent(e));
			e.addComponent(new FoodComponent(e, this));
			
			return e;
		}else{
			Entity e = unusedEntities.get(0);
			unusedEntities.remove(0);
			e.setRemoved(false);
			
			return e;
		}*/
		
		Entity e = new Entity(map, 0, 0, 0);
		
		e.addComponent(new MoveComponent(e));
		e.addComponent(new FoodComponent(e, this));
		
		return e;
	}
	
	public void render(SpriteBatch batch){
		map.render(batch);
		
		if(statusTimer != 0){
			float completeness = (statusTimer / DodgeGame.UPDATES_PER_SECOND);//scale of status thing between 0 and 1
			
			DodgeGame.FONT.setScale(DodgeGame.FONT_SCALE * STATUS_TEXT_SCALE * completeness);
			DodgeGame.FONT.setColor(statusColor);
			DodgeGame.FONT.draw(batch, statusText, -statusBounds.width * completeness * 0.5f, 7f - completeness);
			DodgeGame.FONT.setScale(DodgeGame.FONT_SCALE);
		}
		
		multiplierElement.render(batch);
		scoreElement.render(batch);
	}
	
	public void setStatusText(String status, Color color){
		statusText = status;
		statusColor = color;
		statusTimer = (int) DodgeGame.UPDATES_PER_SECOND;
		
		DodgeGame.FONT.setScale(STATUS_TEXT_SCALE);
		statusBounds = DodgeGame.FONT.getBounds(statusText, statusBounds);
	}
	
	public void resize(){
	}
	
	public Map getMap(){
		return map;
	}
	
	public TextElement getMultiplierElement(){
		return multiplierElement;
	}
	
	public TextElement getScoreElement(){
		return scoreElement;
	}
	
	public ArrayList<Entity> getUnusedEntities(){
		return unusedEntities;
	}
	
	public void gameOver(){
		gameOver = true;
	}
	
	public void dispose(){
	}
}
