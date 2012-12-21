package walnoot.dodgegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Util{
	public static TextureAtlas ATLAS;
	
	public static TextureRegion HEART;
	public static TextureRegion DOT;
	public static TextureRegion FIELD;
	public static TextureRegion FONT;
	public static TextureRegion FOOD_ONE;
	public static TextureRegion FOOD_TWO;
	public static TextureRegion FOOD_THREE;
	public static TextureRegion ICON_FALSE;
	public static TextureRegion ICON_TRUE;
	public static TextureRegion BAD_FOOD_ONE;
	public static TextureRegion BACKGROUND;
	public static TextureRegion SHINE;
	
	public static ParticleEffect SHINE_EFFECT;
	
	private static ParticleEffect getShineEffect(){
		ParticleEffect effect = new ParticleEffect();
		effect.loadEmitters(Gdx.files.internal("effects/shine.dat"));
		effect.getEmitters().get(0).setSprite(new Sprite(Util.SHINE));
		
		return effect;
	}

	public static void loadRegions(){
		HEART = ATLAS.findRegion("heart");
		DOT = ATLAS.findRegion("dot");
		FIELD = ATLAS.findRegion("field");
		FONT = ATLAS.findRegion("font");
		FOOD_ONE = ATLAS.findRegion("drumstick");
		FOOD_TWO = ATLAS.findRegion("burger");
		FOOD_THREE = ATLAS.findRegion("pizza");
		ICON_FALSE = ATLAS.findRegion("icon_false");
		ICON_TRUE = ATLAS.findRegion("icon_true");
		BAD_FOOD_ONE = ATLAS.findRegion("bone");
		BACKGROUND = ATLAS.findRegion("background");
		SHINE = ATLAS.findRegion("shine");
		
		SHINE_EFFECT = getShineEffect();
	}
}
