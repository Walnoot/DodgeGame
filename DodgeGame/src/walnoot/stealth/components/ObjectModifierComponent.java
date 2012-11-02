package walnoot.stealth.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Entity;
import walnoot.dodgegame.SpriteAccessor;
import walnoot.stealth.states.GameState;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class ObjectModifierComponent extends Component{
	public static final float FADE_OUT_DURATION = .25f, FADE_IN_DURATION = .25f;//seconds
	private static final float SPIN_SPEED = 6f;
	
	private ModifierType type;
	private boolean consumed, hasPlayedSound;
	private int removeTimer;
	
	private final GameState gameState;
	
	public ObjectModifierComponent(Entity owner, GameState gameState){
		super(owner);
		this.gameState = gameState;
	}
	
	public void init(ModifierType type){
		//i hate pooling
		
		this.type = type;
		
		SpriteComponent spriteComponent = (SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT);
		if(spriteComponent == null){
			spriteComponent = new SpriteComponent(owner, type.getRandomRegion());
			spriteComponent.setSpinSpeed(SPIN_SPEED * MathUtils.random(-1f, 1f));
			owner.addComponent(spriteComponent);
		}else{
			spriteComponent.newSprite(type.getRandomRegion());
		}
		
		if(type == ModifierType.SHRINK) spriteComponent.getSprite().setColor(Color.RED);
		else if(type == ModifierType.DEATH) spriteComponent.getSprite().setColor(Color.MAGENTA);
		
		Tween tween = Tween.from(
				((SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT)).getSprite(),
				SpriteAccessor.TRANSPARANCY, FADE_IN_DURATION);
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
			
			Tween tween = Tween.to(
					((SpriteComponent) owner.getComponent(ComponentIdentifier.SPRITE_COMPONENT)).getSprite(),
					SpriteAccessor.TRANSPARANCY, FADE_OUT_DURATION);
			tween.target(0).start(DodgeGame.TWEEN_MANAGER);
			
			switch (type){
				case GROW:
					map.getPlayerComponent().grow();
					break;
				case SHRINK:
					map.getPlayerComponent().shrink();
					break;
				case DEATH:
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
	
	public Component getCopy(Entity owner){
		return new ObjectModifierComponent(owner, gameState);
	}
	
	public ComponentIdentifier getIdentifier(){
		return ComponentIdentifier.OBJECT_MODIFIER_COMPONENT;
	}
	
	public enum ModifierType{
		GROW(new TextureRegion(DodgeGame.TEXTURE, 512, 0, 128, 128)), SHRINK(new TextureRegion(DodgeGame.TEXTURE, 0, 0,
				256, 256)), DEATH(new TextureRegion(DodgeGame.TEXTURE, 0, 0, 256, 256));
		
		private final TextureRegion[] regions;
		
		private ModifierType(TextureRegion... regions){
			this.regions = regions;
		}
		
		private TextureRegion getRandomRegion(){
			return regions[MathUtils.random(0, regions.length - 1)];
		}
	}
}
