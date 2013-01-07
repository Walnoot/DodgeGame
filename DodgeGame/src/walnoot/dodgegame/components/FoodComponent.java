package walnoot.dodgegame.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.SpriteAccessor;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.states.GameState;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class FoodComponent extends Component{
	public static final float FADE_OUT_DURATION = .25f, FADE_IN_DURATION = .25f;//seconds
	private static final float SPIN_SPEED = 6f;
	
	private FoodType type;
	private boolean consumed, hasPlayedSound;
	private int removeTimer;
	
	private final GameState gameState;
	
	public FoodComponent(Entity owner, GameState gameState){
		super(owner);
		this.gameState = gameState;
	}
	
	public void init(FoodType type){
		//i hate pooling
		
		this.type = type;
		
		SpriteComponent spriteComponent = owner.getComponent(SpriteComponent.class);
		if(spriteComponent == null){
			spriteComponent = new SpriteComponent(owner, type.getRandomRegion());
			spriteComponent.setSpinSpeed(SPIN_SPEED * MathUtils.random(-1f, 1f));
			owner.addComponent(spriteComponent);
		}else{
			spriteComponent.newSprite(type.getRandomRegion());
		}
		
		Tween tween = Tween.from(spriteComponent.getSprite(), SpriteAccessor.TRANSPARANCY, FADE_IN_DURATION);
		tween.target(0).start(DodgeGame.TWEEN_MANAGER);
		
		consumed = false;
		hasPlayedSound = false;
		removeTimer = 0;
	}
	
	public void update(){
		if(consumed){
			removeTimer++;
			if(removeTimer > FADE_OUT_DURATION * DodgeGame.UPDATES_PER_SECOND){
				owner.remove();
			}
		}
		
		if(map.getPlayerComponent().isGameOver() || consumed) return;
		
		float dx = map.getPlayerComponent().owner.getxPos() - owner.getxPos();
		float dy = map.getPlayerComponent().owner.getyPos() - owner.getyPos();
		
		float minDistance = 0.5f + map.getPlayerComponent().getRadius() / 2f;
		if(dx * dx + dy * dy < minDistance * minDistance){
			consumed = true;
			
			Tween tween = Tween.to(owner.getComponent(SpriteComponent.class).getSprite(), SpriteAccessor.TRANSPARANCY,
					FADE_OUT_DURATION);
			tween.target(0).start(DodgeGame.TWEEN_MANAGER);
			
			switch (type){
				case GROW:
					map.getPlayerComponent().grow();
					break;
				case DIE:
					map.getPlayerComponent().die();
					break;
				default:
					break;
			}
			
			if(!hasPlayedSound){
				DodgeGame.SOUND_MANAGER.playRandomEatSound();
				hasPlayedSound = true;
			}
		}
	}
	
	public void onEntityRemove(){
		if(gameState.getUnusedEntities().size() < GameState.MAX_UNUSED_ENTITIES){
			gameState.getUnusedEntities().add(owner);
		}
	}
	
	public enum FoodType{
		GROW(Util.FOOD_ONE, Util.FOOD_TWO, Util.FOOD_THREE), DIE(Util.BAD_FOOD_ONE);
		
		private final TextureRegion[] regions;
		
		private FoodType(TextureRegion... regions){
			this.regions = regions;
		}
		
		private TextureRegion getRandomRegion(){
			return regions[MathUtils.random(0, regions.length - 1)];
		}
	}
}