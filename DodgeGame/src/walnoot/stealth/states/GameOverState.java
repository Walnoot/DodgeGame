package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState extends State{
	private static final CharSequence MAIN_TEXT = "Game Over";
	private final GameState gameState;
	private TextBounds textBounds;
	
	public GameOverState(GameState gameState){
		super(gameState.camera);
		this.gameState = gameState;
		
		textBounds = DodgeGame.FONT.getBounds(MAIN_TEXT);
		
		System.out.println(textBounds.width + " : " + textBounds.height);
	}
	
	public void update(){
		gameState.update();
	}
	
	public void render(SpriteBatch batch){
		gameState.render(batch);
		
		DodgeGame.FONT.setColor(0, 0, 0, 1);
		DodgeGame.FONT.draw(batch, MAIN_TEXT, -(textBounds.width / 2f), 0);
	}
	
	public void dispose(){
		
	}
}
