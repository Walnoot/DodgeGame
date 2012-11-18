package walnoot.dodgegame.states;

import java.util.ArrayList;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.Map;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.components.HeartComponent;
import walnoot.dodgegame.components.MoveComponent;
import walnoot.dodgegame.components.ObjectModifierComponent;
import walnoot.dodgegame.components.ObjectModifierComponent.ModifierType;
import walnoot.dodgegame.components.PlayerComponent;
import walnoot.dodgegame.components.SpriteComponent;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class GameState extends State{
	public static final float MAP_SIZE = 8;
	public static final int MAX_UNUSED_ENTITIES = 10;
	private static final int GROW_OBJECT_CHANGE = 60, SHRINK_OBJECT_CHANGE = 20; //out of 100
	private static final int INITIAL_SPAWN_RATE = 30;
	private static final int SPAWN_RATE_SCALE = 4;
	private static final float STATUS_TEXT_SCALE = 2f;//scale of the status text at the beginning
	
	private ArrayList<Entity> unusedEntities = new ArrayList<Entity>(MAX_UNUSED_ENTITIES);
	
	private Map map;
	private int enemySpawnTimer = SPAWN_RATE_SCALE; //an enemy spawn when this is zero
	
	private int gameOverTimer; //delays the switch to game over state
	private boolean gameOver;
	
	private String statusText;
	private Color statusColor;
	private int statusTimer;
	private TextBounds statusBounds = new TextBounds();
	
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
	}
	
	public void update(){
		enemySpawnTimer--;
		if(enemySpawnTimer == 0){
			enemySpawnTimer = (int) (INITIAL_SPAWN_RATE - SPAWN_RATE_SCALE * (map.getPlayerComponent().getRadius() - 1f));
			
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
			
			Entity object = getNewObject();
			object.setxPos(x);
			object.setyPos(y);
			object.setRotation(rotation);
			
			ModifierType type;
			int randomInt = MathUtils.random(0, 99);
			
			if(randomInt < GROW_OBJECT_CHANGE) type = ModifierType.GROW;
			else if(randomInt < GROW_OBJECT_CHANGE + SHRINK_OBJECT_CHANGE) type = ModifierType.SHRINK;
			else type = ModifierType.DEATH;
			
			object.getComponent(ObjectModifierComponent.class).init(type);
			object.getComponent(MoveComponent.class).init();
			
			map.addEntity(object);
		}
		
		if(gameOver){
			gameOverTimer++;
			
			if(gameOverTimer == (int) DodgeGame.UPDATES_PER_SECOND) DodgeGame.setState(new GameOverState(this));
		}
		
		if(statusTimer != 0){
			statusTimer--;
		}
		
		map.update();
		
		if(DodgeGame.INPUT.pause.isJustPressed() && !gameOver){
			DodgeGame.setState(new PauseState(camera, this));
		}
	}
	
	private Entity getNewObject(){
		if(unusedEntities.isEmpty()){
			Entity e = new Entity(map, 0, 0, 0);
			
			e.addComponent(new MoveComponent(e));
			e.addComponent(new ObjectModifierComponent(e, this));
			
			return e;
		}else{
			Entity e = unusedEntities.get(0);
			unusedEntities.remove(0);
			e.setRemoved(false);
			
			return e;
		}
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
	}
	
	public void setStatusText(String status, Color color){
		statusText = status;
		statusColor = color;
		statusTimer = (int) DodgeGame.UPDATES_PER_SECOND;
		
		DodgeGame.FONT.setScale(STATUS_TEXT_SCALE);
		statusBounds = DodgeGame.FONT.getBounds(statusText, statusBounds);
	}
	
	public Map getMap(){
		return map;
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
