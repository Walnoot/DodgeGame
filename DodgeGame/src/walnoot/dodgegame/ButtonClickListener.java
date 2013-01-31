package walnoot.dodgegame;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public abstract class ButtonClickListener implements EventListener{
	public boolean handle(Event event){
		if(event instanceof ChangeEvent){
			click(event.getTarget());
			DodgeGame.SOUND_MANAGER.playClickSound();
		}
		
		return false;
	}
	
	public abstract void click(Actor actor);
}
