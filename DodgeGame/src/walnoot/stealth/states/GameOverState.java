package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.TextButton;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState extends State{
	private static final String HIGH_SCORE_KEY = "highScore";
	
	private final GameState gameState;
	private TextButton retryButton, mainMenuButton;
	private TextElement scoreElement, highScoreElement;
	
	public GameOverState(GameState gameState){
		super(gameState.camera);
		this.gameState = gameState;
		
		retryButton = new TextButton("RETRY" + ((Gdx.app.getType() == ApplicationType.Android) ? "" : " (R)"), 0, -2.5f, 4f, Keys.R){
			public void doAction(){
				DodgeGame.setState(new GameState(camera));
			}
		};
		
		mainMenuButton = new TextButton("MAIN MENU", 0, -5.5f, 4f, Keys.R){
			public void doAction(){
				DodgeGame.setState(new MainMenuState(camera));
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
		
		scoreElement = new TextElement("SCORE: " + score, 0, 4f, 2f);
		highScoreElement = new TextElement("HIGHSCORE: " + highscore + (newHighScore ? "!" : ""), 0, 2f, 2f);
	}
	
	public void update(){
		gameState.update();
		
		retryButton.update();
		mainMenuButton.update();
	}
	
	public void render(SpriteBatch batch){
		gameState.render(batch);
		
		retryButton.render(batch);
		mainMenuButton.render(batch);
		scoreElement.render(batch);
		highScoreElement.render(batch);
	}
	
	public void dispose(){
		
	}
}
