package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Stat;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.components.Entity;
import walnoot.dodgegame.components.HeartComponent;
import walnoot.dodgegame.components.PlayerComponent;
import walnoot.dodgegame.components.SpriteComponent;
import walnoot.dodgegame.gameplay.BasicSpawnHandler;
import walnoot.dodgegame.gameplay.EntityPool;
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
import com.badlogic.gdx.utils.Pool;

public class GameState extends State{
	public static final float MAP_SIZE = 6f;
	public static final int MAX_UNUSED_ENTITIES = 10;
	private static final int SPAWN_RATE_SCALE = 4;
	private static final float STATUS_TEXT_SCALE = 1f;//scale of the status text at the beginning
	
	//private ArrayList<Entity> unusedEntities = new ArrayList<Entity>(MAX_UNUSED_ENTITIES);
	
	private Map map = new Map();
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
	
	private Pool<Entity> entityPool = new EntityPool(map, this);

	public GameState(OrthographicCamera camera){
		super(camera);
		
		Entity playerEntity = new Entity(map, 0, 0);
//		playerEntity.addComponent(new SpriteComponent(playerEntity, Util.DOT, Color.BLACK));//for testing purposes
		
		PlayerComponent playerComponent = new PlayerComponent(playerEntity, this);
		playerEntity.addComponent(playerComponent);
		
		map.addEntity(playerEntity);
		map.setPlayerComponent(playerComponent);
		
		for(int i = 0; i < PlayerComponent.NUM_START_LIVES; i++){
			Entity heart = new Entity(map, 0, 0);
			
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
		
		Stat.NUM_TIMES_PLAYED.addInt(1);
	}
	
	public void update(){
		time++;
		

		checkFoodSpawn();
		
		if(gameOver){
			gameOverTimer++;
			
			if(gameOverTimer == (int) DodgeGame.UPDATES_PER_SECOND) DodgeGame.setState(new GameOverState(this));
		}else{
			Stat.TICKS_PLAYED.addInt(1);
		}
		
		if(statusTimer != 0) statusTimer--;
		
		map.update();
		
		if(DodgeGame.INPUT.pause.isJustPressed() && !gameOver) pause();
		
		multiplierElement.setScale(3f + MathUtils.cosDeg(DodgeGame.gameTime * 3
				* map.getPlayerComponent().getScoreMultiplier()));
	}
	
	private void checkFoodSpawn(){
		enemySpawnTimer--;
		if(enemySpawnTimer == 0){
			enemySpawnTimer = spawnHandler.getPauseTicks(time);
			
			Entity food = getNewUnit();
			
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
	
	private Entity getNewUnit(){
		Entity e = entityPool.obtain();
		e.setRemoved(false);
		
		return e;

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
		
		/*Entity e = new Entity(map, 0, 0);
		
		e.addComponent(new MoveComponent(e));
		e.addComponent(new FoodComponent(e, this));
		
		return e;*/
	}
	
	public void render(SpriteBatch batch){
		map.render(batch);
		
		if(statusTimer != 0){
			float completeness = (statusTimer / DodgeGame.UPDATES_PER_SECOND);//scale of status thing between 0 and 1
			
			DodgeGame.SCALE_FONT.setScale(DodgeGame.FONT_SCALE * STATUS_TEXT_SCALE * completeness);
			DodgeGame.SCALE_FONT.setColor(statusColor);
			DodgeGame.SCALE_FONT
					.draw(batch, statusText, -statusBounds.width * 0.5f * completeness, completeness * 0.5f);
			DodgeGame.SCALE_FONT.setScale(DodgeGame.FONT_SCALE);
		}
		
		multiplierElement.render(batch);
		scoreElement.render(batch);
	}
	
	public void setStatusText(String status, Color color){
		statusText = status;
		statusColor = color;
		statusTimer = (int) DodgeGame.UPDATES_PER_SECOND;
		
		DodgeGame.SCALE_FONT.setScale(STATUS_TEXT_SCALE * DodgeGame.FONT_SCALE);
		statusBounds = DodgeGame.SCALE_FONT.getBounds(statusText, statusBounds);
	}
	
	public void resize(){
	}
	
	public Map getMap(){
		return map;
	}
	
	public Pool<Entity> getPool(){
		return entityPool;
	}

	public TextElement getMultiplierElement(){
		return multiplierElement;
	}
	
	public TextElement getScoreElement(){
		return scoreElement;
	}
	
	public void gameOver(){
		gameOver = true;
		
		Stat.saveStats();
	}
	
	public void dispose(){
		DodgeGame.PREFERENCES.flush();//for the stats
	}
}
