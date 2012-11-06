package walnoot.dodgegame.states;

import walnoot.dodgegame.ui.ReturnButton;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class OptionsState extends State{
	private ReturnButton returnButton;

	public OptionsState(OrthographicCamera camera){
		super(camera);
		
		returnButton = new ReturnButton(camera, new MainMenuState(camera));
	}
	
	public void update(){
		returnButton.update();
	}
	
	public void render(SpriteBatch batch){
		returnButton.render(batch);
	}
	
	public void dispose(){
	}
}
