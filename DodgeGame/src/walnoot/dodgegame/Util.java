package walnoot.dodgegame;

import walnoot.dodgegame.states.MenuState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Util{
	private static final float UI_SCALE = 640f;
	
	public static Skin SKIN = new Skin();
	
	public static TextureAtlas ATLAS;
	
	public static TextureRegion HEART;
	public static TextureRegion HAND;
	public static TextureRegion HAND_CLOSED;
	public static TextureRegion HAND_SHORT;
	public static TextureRegion COIN;
	public static TextureRegion FIELD;
	public static TextureRegion FONT;
	public static TextureRegion FONT_NUMBERS;
	public static TextureRegion ICON_FALSE;
	public static TextureRegion ICON_TRUE;
	public static TextureRegion BOMB;
	public static TextureRegion BACKGROUND;
	public static TextureRegion PAUSE;
	public static TextureRegion RESUME;
	public static TextureRegion SLIDER;
	public static TextureRegion SLIDER_BIG;
	public static TextureRegion SLIDER_KNOB;
	public static TextureRegion BORDER;
	public static TextureRegion LOGO;
	
	public static NinePatch PATCH_SLIDER;
	public static NinePatch PATCH_SLIDER_BIG;
	public static NinePatch PATCH_BORDER;
	
	public static void loadRegions(){
		HEART = ATLAS.findRegion("Gameplay/heart");
		HAND = ATLAS.findRegion("Gameplay/hand");
		HAND_CLOSED = ATLAS.findRegion("Gameplay/hand_closed");
		HAND_SHORT = HAND.split(HAND.getRegionWidth(), HAND.getRegionHeight() / 2)[0][0];
		COIN = ATLAS.findRegion("Gameplay/coin");
		FIELD = ATLAS.findRegion("Gameplay/field");
		FONT = ATLAS.findRegion("UI/font");
		FONT_NUMBERS = ATLAS.findRegion("UI/numbers");
		ICON_FALSE = ATLAS.findRegion("UI/icon_false");
		ICON_TRUE = ATLAS.findRegion("UI/icon_true");
		BOMB = ATLAS.findRegion("Gameplay/bone");
		BACKGROUND = ATLAS.findRegion("Gameplay/background");
		PAUSE = ATLAS.findRegion("UI/pause");
		RESUME = ATLAS.findRegion("UI/resume");
		SLIDER = ATLAS.findRegion("UI/slider");
		SLIDER_BIG = ATLAS.findRegion("UI/slider_big");
		SLIDER_KNOB = ATLAS.findRegion("UI/slider_knob");
		BORDER = ATLAS.findRegion("UI/border");
		LOGO = ATLAS.findRegion("UI/logo");
		
		PATCH_SLIDER = getNinePatch(SLIDER);
		PATCH_SLIDER_BIG = getNinePatch(SLIDER_BIG);
		PATCH_BORDER = getNinePatch(BORDER);
	}
	
	public static void setSkin(){
		NinePatchDrawable patchSmall = new NinePatchDrawable(PATCH_SLIDER);
		NinePatchDrawable patchBig = new NinePatchDrawable(PATCH_SLIDER_BIG);
		NinePatchDrawable patchBorder = new NinePatchDrawable(PATCH_BORDER);
		
		SKIN.add("default-horizontal", new SliderStyle(patchSmall, new TextureRegionDrawable(SLIDER_KNOB)));
		
		TextButtonStyle textButtonStyle = new TextButtonStyle(patchSmall, patchBig, null);
		textButtonStyle.font = DodgeGame.UI_FONT;
		textButtonStyle.fontColor = Color.BLACK;
		SKIN.add("default", textButtonStyle);
		
		SKIN.add("default", new ButtonStyle(patchBig, null, null));
		SKIN.add("border", new ButtonStyle(patchBorder, null, null));
		
		SKIN.add("default", new CheckBoxStyle(new TextureRegionDrawable(ICON_FALSE), new TextureRegionDrawable(
				ICON_TRUE), DodgeGame.UI_FONT, Color.BLACK));
		
		SKIN.add("default", new LabelStyle(DodgeGame.UI_FONT, Color.BLACK));
		SKIN.add("numbers", new LabelStyle(DodgeGame.NUMBERS_FONT, Color.WHITE));
		
		SKIN.add("default", new ScrollPaneStyle(null, null, patchSmall, null, patchSmall));
	}
	
	private static NinePatch getNinePatch(TextureRegion region){
		int vert = (region.getRegionHeight() / 2) - 1;
		int hor = (region.getRegionWidth() / 2) - 1;
		
		return new NinePatch(region, hor, hor, vert, vert);
	}
	
	public static void addQuitButton(Stage stage, final OrthographicCamera camera){
		stage.addActor(getQuitButton(camera));
	}
	
	public static TextButton getQuitButton(final OrthographicCamera camera){
		TextButton button = new TextButton("BACK", SKIN);
		button.addListener(new ButtonClickListener(){
			public void click(Actor actor){
				DodgeGame.setState(new MenuState(camera));
			}
		});
		
		return button;
	}
	
	public static Vector2 getStageDimensions(){
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		float minDimension = Math.min(width, height);
		
		return Vector2.tmp.set(width / minDimension * UI_SCALE, height / minDimension * UI_SCALE);
	}
}
