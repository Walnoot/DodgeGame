package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Stat;
import walnoot.dodgegame.ui.TextButton;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState extends State{
	public static final String HIGH_SCORE_KEY = "highScore";
	
	private final GameState gameState;
	private TextButton retryButton, mainMenuButton;
	private TextElement scoreElement, highScoreElement;
	
	public GameOverState(GameState gameState){
		super(gameState.camera);
		this.gameState = gameState;
		
		retryButton = new TextButton("RETRY", 0, -2.5f, 3f, Keys.R){
			public void doAction(){
				DodgeGame.setState(new GameState(camera));
			}
		};
		
		mainMenuButton = new TextButton("MAIN MENU", 0, -4.5f, 3f){
			public void doAction(){
				DodgeGame.setState(new MenuState(camera));
			}
		};
		
		int highscore = Stat.HIGH_SCORE.getInt();
		int score = gameState.getMap().getPlayerComponent().getScore();
		
		scoreElement = new TextElement("SCORE: " + score, 0, 4f, 1.5f);
		highScoreElement = new TextElement("HIGHSCORE: " + highscore, 0, 2f, 1.5f);
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
	
	public void resize(){
		super.resize();
		
		gameState.resize();
	}
	
	public void dispose(){
		
	}
}
