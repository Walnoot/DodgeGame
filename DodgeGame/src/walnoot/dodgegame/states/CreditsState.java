package walnoot.dodgegame.states;

import walnoot.dodgegame.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CreditsState extends State{
	private String text;
	
	public CreditsState(OrthographicCamera camera){
		super(camera);
		
		text = Gdx.files.internal("credits.txt").readString();
		
		Label label = new Label(text, Util.SKIN);
		label.setWrap(true);
		
		ScrollPane pane = new ScrollPane(label, Util.SKIN);
		pane.setFillParent(true);
		
		Table container = new Table();
		container.setFillParent(true);
		container.add(pane).expand().fill();
		
		stage.addActor(container);
		
		Util.addQuitButton(stage, camera);
	}
	
	public void update(){
	}
	
	public void render(SpriteBatch batch){
	}
	
	public void dispose(){
	}
}
