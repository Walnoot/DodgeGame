package walnoot.dodgegame;

import walnoot.dodgegame.states.MainMenuState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
	public static TextureRegion DOT;
	public static TextureRegion COIN;
	public static TextureRegion FIELD;
	public static TextureRegion FONT;
	public static TextureRegion FOOD_ONE;
	public static TextureRegion FOOD_TWO;
	public static TextureRegion FOOD_THREE;
	public static TextureRegion ICON_FALSE;
	public static TextureRegion ICON_TRUE;
	public static TextureRegion BOMB;
	public static TextureRegion BACKGROUND;
	public static TextureRegion SHINE;
	public static TextureRegion PAUSE;
	public static TextureRegion RESUME;
	public static TextureRegion SLIDER;
	public static TextureRegion SLIDER_BIG;
	public static TextureRegion SLIDER_KNOB;
	
	public static NinePatch PATCH_SLIDER;
	public static NinePatch PATCH_SLIDER_BIG;
	
	public static ParticleEffect SHINE_EFFECT;
	
	private static ParticleEffect getShineEffect(){
		ParticleEffect effect = new ParticleEffect();
		effect.loadEmitters(Gdx.files.internal("effects/shine.dat"));
		effect.getEmitters().get(0).setSprite(new Sprite(Util.SHINE));
		
		return effect;
	}
	
	public static void loadRegions(){
		HEART = ATLAS.findRegion("heart");
		HAND = ATLAS.findRegion("hand");
		HAND_CLOSED = ATLAS.findRegion("hand_closed");
		DOT = ATLAS.findRegion("dot");
		COIN = ATLAS.findRegion("coin");
		FIELD = ATLAS.findRegion("field");
		FONT = ATLAS.findRegion("font");
		FOOD_ONE = ATLAS.findRegion("drumstick");
		FOOD_TWO = ATLAS.findRegion("burger");
		FOOD_THREE = ATLAS.findRegion("pizza");
		ICON_FALSE = ATLAS.findRegion("icon_false");
		ICON_TRUE = ATLAS.findRegion("icon_true");
		BOMB = ATLAS.findRegion("bone");
		BACKGROUND = ATLAS.findRegion("background");
		SHINE = ATLAS.findRegion("shine");
		PAUSE = ATLAS.findRegion("pause");
		RESUME = ATLAS.findRegion("resume");
		SLIDER = ATLAS.findRegion("slider");
		SLIDER_BIG = ATLAS.findRegion("slider_big");
		SLIDER_KNOB = ATLAS.findRegion("slider_knob");
		
		PATCH_SLIDER = getNinePatch(SLIDER);
		PATCH_SLIDER_BIG = getNinePatch(SLIDER_BIG);
		
		SHINE_EFFECT = getShineEffect();
	}
	
	public static void setSkin(){
		NinePatchDrawable patchSmall = new NinePatchDrawable(PATCH_SLIDER);
		NinePatchDrawable patchBig = new NinePatchDrawable(PATCH_SLIDER_BIG);
		
		SKIN.add("default-horizontal", new SliderStyle(patchSmall, new TextureRegionDrawable(SLIDER_KNOB)));
		
		TextButtonStyle textButtonStyle = new TextButtonStyle(patchSmall, patchBig, null);
		textButtonStyle.font = DodgeGame.UI_FONT;
		textButtonStyle.fontColor = Color.BLACK;
		SKIN.add("default", textButtonStyle);
		
		SKIN.add("default", new CheckBoxStyle(new TextureRegionDrawable(ICON_FALSE), new TextureRegionDrawable(
				ICON_TRUE), DodgeGame.UI_FONT, Color.BLACK));
		
		SKIN.add("default", new LabelStyle(DodgeGame.UI_FONT, Color.BLACK));
		
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
				DodgeGame.setState(new MainMenuState(camera));
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
