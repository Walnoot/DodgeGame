package walnoot.dodgegame.components;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.states.GameState;

/**
 * component that represents the heart icons at the top of the screen
 */
public class HeartComponent extends Component{
	public static final int FALL_TIME = (int) DodgeGame.UPDATES_PER_SECOND;
	public static final float SCALE = 2f;
	
	private final int index;
	private int fallTimer;
	
	public HeartComponent(Entity owner, int index){
		super(owner);
		this.index = index;
		
		owner.setyPos(GameState.MAP_SIZE - (SCALE / 2f));
	}
	
	public void update(){
		/*if(map.getPlayerComponent().getLives() <= index){
			if(fallTimer == 0){
				Tween tween = Tween.to(owner.getComponent(SpriteComponent.class).getSprite(),
						SpriteAccessor.TRANSPARANCY, FALL_TIME / DodgeGame.UPDATES_PER_SECOND);
				tween.target(0).start(DodgeGame.TWEEN_MANAGER);
			}
			
			fallTimer++;
			
			if(fallTimer == FALL_TIME) owner.remove();
			
			owner.translate(0, -GameState.MAP_SIZE / FALL_TIME);
		}
		
		float targetX = (index + 0.5f - map.getPlayerComponent().getLives() / 2f) * SCALE;
		
		owner.translate((targetX - owner.getxPos()) * 0.1f, 0);*/
	}
}
