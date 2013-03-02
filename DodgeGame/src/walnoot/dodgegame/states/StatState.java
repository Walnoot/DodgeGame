package walnoot.dodgegame.states;

import walnoot.dodgegame.Stat;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class StatState extends State{
	public StatState(OrthographicCamera camera){
		super(camera);
		
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		
		addRow(Stat.HIGH_SCORE, table);
		addRow(Stat.NUM_DEATHS, table);
		addRow(Stat.NUM_FOOD_EATEN, table);
		addRow(Stat.TICKS_PLAYED, table);
		addRow(Stat.NUM_TIMES_PLAYED, table);
		
		Util.addQuitButton(stage, camera);
	}
	
	private void addRow(Stat stat, Table table){
		table.add(new Label(stat.getName(), Util.SKIN)).left();
		table.add(new Label(stat.getValue(), Util.SKIN)).right();
		table.row();
	}
	
	public void update(){
	}
	
	public void render(SpriteBatch batch){
	}
	
	public void dispose(){
	}
}
