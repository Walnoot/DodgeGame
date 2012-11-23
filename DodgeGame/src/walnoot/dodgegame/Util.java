package walnoot.dodgegame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Util{
	public static final TextureRegion HEART = getRegion(0f, 0.5f, 0.25f, 1f);
	public static final TextureRegion DOT = getRegion(0f, 0f, 0.25f, 0.5f);
	public static final TextureRegion FIELD = getRegion(0.25f, 0f, 0.5f, 0.5f);
	public static final TextureRegion FONT = getRegion(0.25f, 0.5f, 0.5f, 1f);
	public static final TextureRegion FOOD_ONE = getRegion(0.5f, 0f, 0.625f, 0.25f);
	public static final TextureRegion FOOD_TWO = getRegion(0.5f, 0.5f, 0.625f, 0.75f);
	public static final TextureRegion FOOD_THREE = getRegion(0.5f, 0.75f, 0.625f, 1f);
	public static final TextureRegion ICON_FALSE = getRegion(0.5f, 0.25f, 0.625f, 0.5f);
	public static final TextureRegion ICON_TRUE = getRegion(0.625f, 0.25f, 0.75f, 0.5f);
	public static final TextureRegion BAD_FOOD_ONE = getRegion(0.625f, 0f, 0.75f, 0.25f);
	public static final TextureRegion BACKGROUND = getRegion(0.75f, 0.25f, 0.875f, 0.5f);
	
	private static TextureRegion getRegion(float x, float y, float width, float height){
		return new TextureRegion(DodgeGame.TEXTURE, x, y, width, height);
	}
}
