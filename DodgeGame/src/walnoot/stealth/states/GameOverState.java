package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.TextButton;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState extends State{
	private static final String HIGH_SCORE_KEY = "highScore";
	
	private final GameState gameState;
	private TextButton button;
	private TextElement scoreElement, highScoreElement;
	
	public GameOverState(GameState gameState){
		super(gameState.camera);
		this.gameState = gameState;
		
		button = new TextButton("RETRY (R)", 0, -1f, Keys.R){
			public void doAction(){
				DodgeGame.setState(new GameState(camera));
			}
		};
		
		int highscore = DodgeGame.PREFERENCES.getInteger(HIGH_SCORE_KEY, 0);
		int score = gameState.getMap().getPlayerComponent().getScore();
		boolean newHighScore = false;
		
		if(score > highscore){
			highscore = score;
			DodgeGame.PREFERENCES.putInteger(HIGH_SCORE_KEY, highscore);
			
			newHighScore = true;
		}
		
		scoreElement = new TextElement("Score: " + score, 0, 2f);
		highScoreElement = new TextElement("Highscore: " + highscore + (newHighScore ? "!" : ""), 0, 1f);
	}
	
	public void update(){
		gameState.update();
		
		button.update();
	}
	
	public void render(SpriteBatch batch){
		gameState.render(batch);
		
		DodgeGame.FONT.setColor(Color.BLACK);
		
		button.render(batch);
		scoreElement.render(batch);
		highScoreElement.render(batch);
	}
	
	public void dispose(){
		
	}
}
