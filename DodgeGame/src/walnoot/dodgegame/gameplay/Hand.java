package walnoot.dodgegame.gameplay;

import walnoot.dodgegame.Util;
import walnoot.dodgegame.components.PlayerComponent;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Hand{
	public boolean died;
	public Sprite sprite;
	public Vector2 pos;
	
	public Hand(){
		pos = new Vector2();
		
		sprite = new Sprite(Util.HAND);
		sprite.setSize(1f, 2f);
		sprite.setOrigin(0.5f, 0f);
	}
	
	public void update(float rotation){
		sprite.setRotation(-rotation);
		
		Vector2 normPos = Vector2.tmp.set(MathUtils.sinDeg(rotation), MathUtils.cosDeg(rotation));
		
//		float handDist = 0.7f - handCloseTimer / 50f;
		float handDist = 0.7f;
		sprite.setPosition(normPos.x * handDist - 0.5f, normPos.y * handDist);
		
		pos.set(Vector2.tmp.mul(PlayerComponent.MOVE_RADIUS));
	}
	
	public void die(){
		died = true;
	}
}
