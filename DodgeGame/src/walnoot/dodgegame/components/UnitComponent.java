package walnoot.dodgegame.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.SpriteAccessor;
import walnoot.dodgegame.Util;
import walnoot.dodgegame.gameplay.Hand;
import walnoot.dodgegame.states.GameState;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class UnitComponent extends Component{
	public static final float FADE_OUT_DURATION = .25f, FADE_IN_DURATION = .25f;//seconds
	private static final float SPIN_SPEED = 6f;
	
	private UnitType type;
	private boolean consumed, hasPlayedSound;
	private int removeTimer;
	
	private final GameState gameState;
	
	public UnitComponent(Entity owner, GameState gameState){
		super(owner);
		this.gameState = gameState;
	}
	
	public void init(UnitType type){
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
		
		Array<Hand> hands = map.getPlayerComponent().getHands();
		
		float shortestLength2 = Float.MAX_VALUE;
		Hand closestHand = null;
		
		for(int i = 0; i < hands.size; i++){
			float length2 = Vector2.tmp2.set(hands.get(i).pos).sub(owner.getPos()).len2();
			
			if(length2 < shortestLength2){
				shortestLength2 = length2;
				closestHand = hands.get(i);
			}
		}
		
		float minDistance = 0.5f + map.getPlayerComponent().getRadius() / 2f;
		if(shortestLength2 < minDistance * minDistance){
			consumed = true;
			
			Tween tween =
					Tween.to(owner.getComponent(SpriteComponent.class).getSprite(), SpriteAccessor.TRANSPARANCY,
							FADE_OUT_DURATION);
			tween.target(0).start(DodgeGame.TWEEN_MANAGER);
			
			switch (type){
				case SCORE:
					map.getPlayerComponent().score();
					closestHand.closeHand();
					
					DodgeGame.PARTICLE_HANDLER.addShineEffect(owner.getxPos(), owner.getyPos());
					
					break;
				case DIE:
					closestHand.die();
					
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
	}
	
	public enum UnitType{
		SCORE(Util.COIN), DIE(Util.BOMB);
		
		private final TextureRegion[] regions;
		
		private UnitType(TextureRegion... regions){
			this.regions = regions;
		}
		
		private TextureRegion getRandomRegion(){
			return regions[MathUtils.random(0, regions.length - 1)];
		}
	}
}
