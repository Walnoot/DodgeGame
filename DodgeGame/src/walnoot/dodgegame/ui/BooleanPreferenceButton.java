package walnoot.dodgegame.ui;

import walnoot.dodgegame.DodgeGame;
import walnoot.dodgegame.Util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BooleanPreferenceButton extends TextButton{
	private boolean keyValue;
	private Sprite sprite;
	private final String key;

	public BooleanPreferenceButton(String text, float x, float y, float scale, String key){
		super(text, x, y, scale);
		this.key = key;
		
		keyValue = DodgeGame.PREFERENCES.getBoolean(key, false);
		
		sprite = new Sprite(keyValue ? Util.ICON_TRUE : Util.ICON_FALSE);
		sprite.setPosition(x + getWidth() * 0.5f - scale, y - scale * 0.5f);
		sprite.setSize(scale, scale);
	}
	
	public void render(SpriteBatch batch){
		super.render(batch);
		
		sprite.draw(batch);
	}
	
	public void doAction(){
		keyValue = !keyValue;
		
		sprite.setRegion(keyValue ? Util.ICON_TRUE : Util.ICON_FALSE);
		
		DodgeGame.PREFERENCES.putBoolean(key, keyValue);
		
		setChanges(keyValue);
	}
	
	public abstract void setChanges(boolean value);
	
	protected float getWidth(){
		return super.getWidth() + getScale();
	}
}
