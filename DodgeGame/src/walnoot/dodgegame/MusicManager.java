package walnoot.dodgegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public class MusicManager{
	private static final String[] songPaths = {
		"Blobby Samba.mp3",
		"Comparsa.mp3",
		"CumbiaNoFrillsFaster.mp3",
		"No Frills Salsa.mp3",
		"Notanico Merengue.mp3",
		"Peppy Pepe.mp3"
	};
	public static final String SOUND_PREF_NAME = "SoundOn";
	
	private Music currentSong;
	private int songIndex;
	private FileHandle songFolder;
	private boolean playing;
	
	public void init(){
		songFolder = Gdx.files.internal("music/");
		
		songIndex = MathUtils.random(songPaths.length - 1);
		currentSong = Gdx.audio.newMusic(songFolder.child(songPaths[songIndex]));
		
		if(DodgeGame.PREFERENCES.getBoolean(SOUND_PREF_NAME, true)){
			currentSong.play();
			playing = true;
		}
	}
	
	public void update(){
		if(!currentSong.isPlaying() && playing){
			currentSong.dispose();
			
			songIndex++;
			if(songIndex >= songPaths.length) songIndex = 0;
			
			currentSong = Gdx.audio.newMusic(songFolder.child(songPaths[songIndex]));
			currentSong.play();
		}
		
		if(DodgeGame.INPUT.toggleSound.isJustPressed()){
			if(playing) pause();
			else resume();
		}
	}
	
	public void pause(){
		playing = false;
		
		currentSong.pause();
	}
	
	public void resume(){
		playing = true;
		
		currentSong.play();
	}
	
	public boolean isPlaying(){
		return playing;
	}
	
	public void dispose(){
		currentSong.dispose();
	}
}
