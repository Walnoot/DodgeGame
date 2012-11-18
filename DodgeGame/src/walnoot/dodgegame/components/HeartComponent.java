package walnoot.dodgegame.components;

import walnoot.dodgegame.Entity;
import walnoot.dodgegame.states.GameState;

/**
 * component that represents the heart icons at the top of the screen
 */
public class HeartComponent extends Component{
	public static final float SCALE = 2f;
	
	private final int index;
	
	public HeartComponent(Entity owner, int index){
		super(owner);
		this.index = index;
		
		owner.setyPos(GameState.MAP_SIZE - (SCALE / 2f));
	}
	
	public void update(){
		if(map.getPlayerComponent().getLives() <= index){
			owner.remove();
			return;
		}
		
		float targetX = (index + 0.5f - map.getPlayerComponent().getLives() / 2f) * SCALE;
		
		owner.translate((targetX - owner.getxPos()) * 0.1f, 0);
	}
}
