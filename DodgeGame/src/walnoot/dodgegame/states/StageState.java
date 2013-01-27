package walnoot.dodgegame.states;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;

public class StageState extends State{
	public static final float UI_SCALE = 640f;
	
	private Stage stage;
	private Table table;
	
	public StageState(OrthographicCamera camera){
		super(camera);
		
		setupStage();
	}
	
	private void setupStage(){
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		float minDimension = Math.min(width, height);
		
		stage = new Stage(width / minDimension * UI_SCALE, height / minDimension * UI_SCALE, false);
		
		System.out.printf("%f, %f", stage.width(), stage.height());
		
		DodgeGame.inputs.addProcessor(stage);
		
		table = new Table();
		table.right();
		table.y = stage.height() / 2;
		table.x = stage.width();
		
		stage.addActor(table);
		
		BitmapFont font = new BitmapFont(Gdx.files.internal("komika_axis.fnt"), Util.FONT, false);
		//font.setScale(Gdx.graphics.getHeight() / 640f);
		LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
		
		table.add(new Label("COMBO:", labelStyle)).left();
		table.add(new Label("15X", labelStyle)).left();
		table.row();
		table.add(new Label("SCORE:", labelStyle)).left();
		table.add(new Label("14235", labelStyle)).left();
		table.row();
		
		table.add(new CheckBox("KSDJF", new CheckBoxStyle(Util.ICON_FALSE, Util.ICON_TRUE, font, Color.BLACK)))
				.colspan(2);
		table.row();
		
		NinePatch sliderPatch = new NinePatch(Util.SLIDER, 15, 15, 15, 15);
		Slider slider = new Slider(0f, 1f, 1f, new SliderStyle(sliderPatch, Util.DOT));
		table.add(slider).colspan(2).width(400);
		table.row();
		
		table.add(
				new TextField("DASGF", new TextFieldStyle(font, Color.BLACK, font, Color.BLACK, sliderPatch, Util.DOT,
						sliderPatch))).colspan(2).fill();
		
		table.debug();
	}
	
	public void update(){
		stage.act(DodgeGame.SECONDS_PER_UPDATE);
	}
	
	public void render(SpriteBatch batch){
	}
	
	public void renderUI(){
		stage.draw();
		Table.drawDebug(stage);
	}
	
	public void resize(){
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		float minDimension = Math.min(width, height);
		
		stage.setViewport(width / minDimension * UI_SCALE, height / minDimension * UI_SCALE, false);
		
		table.y = stage.height() / 2;
		table.x = stage.width();
	}
	
	public void dispose(){
		
	}
}
