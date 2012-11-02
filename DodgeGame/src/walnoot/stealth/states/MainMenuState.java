package walnoot.stealth.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.ui.TextButton;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuState extends State{
	private TextElement[] textElements;
	
	public MainMenuState(final OrthographicCamera camera){
		super(camera);
		
		TextElement titleElement = new TextElement("Dodge Game", 0, 5f, 4f);
		
		TextButton newGameButton = new TextButton("NEW GAME", 0, 2f, 2f){
			public void doAction(){
				DodgeGame.setState(new GameState(camera));
			}
		};
		
		TextButton quitGameButton = new TextButton("QUIT GAME", 0, 0, 2f){
			public void doAction(){
				Gdx.app.exit();
			}
		};
		
		textElements = new TextElement[] {titleElement, newGameButton, quitGameButton};
	}
	
	public void update(){
		for(int i = 0; i < textElements.length; i++){
			if(textElements[i] instanceof TextButton) ((TextButton) textElements[i]).update();
		}
	}
	
	public void render(SpriteBatch batch){
		for(int i = 0; i < textElements.length; i++){
			textElements[i].render(batch);
		}
	}
	
	public void dispose(){
	}
}
