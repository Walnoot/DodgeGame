package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.LoadingThread;
import walnoot.dodgegame.ui.TextElement;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoadingState extends State{
	private LoadingThread loadingThread;
	private TextElement loadText;

	public LoadingState(OrthographicCamera camera){
		super(camera);
		
		loadingThread = new LoadingThread();
		new Thread(loadingThread, "Loading thread").start();
		
	}
	
	public void update(){
		if(DodgeGame.FONT != null && loadText == null){
			loadText = new TextElement(loadingThread.getLoadText(), 0, 0);
		}
		
		if(loadingThread.isDone()){
			DodgeGame.setState(new MainMenuState(camera));
		}
	}
	
	public void render(SpriteBatch batch){
		if(loadText != null) loadText.render(batch);
	}
	
	public void dispose(){
	}
}
