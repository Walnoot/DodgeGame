package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.TextButton;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState extends State{
	private final GameState gameState;
	private TextButton button;
	
	public GameOverState(GameState gameState){
		super(gameState.camera);
		this.gameState = gameState;
		
		button = new TextButton("RETRY?", 0, 0){
			public void doAction(){
				DodgeGame.setState(new GameState(camera));
			};
		};
	}
	
	public void update(){
		gameState.update();
		
		button.update();
	}
	
	public void render(SpriteBatch batch){
		gameState.render(batch);
		
		button.render(batch);
	}
	
	public void dispose(){
		
	}
}
