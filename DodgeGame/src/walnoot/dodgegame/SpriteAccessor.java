package walnoot.dodgegame;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite>{
	public static final int POSITION = 0, COLOR = 1, TRANSPARANCY = 2;
	
	public int getValues(Sprite target, int tweenType, float[] returnValues){
		switch(tweenType){
			case POSITION:
				returnValues[0] = target.getX();
				returnValues[1] = target.getY();
				return 2;
			case COLOR:
				returnValues[0] = target.getColor().r;
				returnValues[1] = target.getColor().g;
				returnValues[2] = target.getColor().b;
				returnValues[3] = target.getColor().a;
				return 4;
			case TRANSPARANCY:
				returnValues[0] = target.getColor().a;
				return 1;
			default:
				return -1;
		}
	}
	
	public void setValues(Sprite target, int tweenType, float[] newValues){
		switch(tweenType){
			case POSITION:
				target.setX(newValues[0]);
				target.setY(newValues[1]);
				break;
			case COLOR:
				target.setColor(newValues[0], newValues[1], newValues[2], newValues[3]);
				break;
			case TRANSPARANCY:
				target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
				break;
			default:
				break;
		}
	}
}
