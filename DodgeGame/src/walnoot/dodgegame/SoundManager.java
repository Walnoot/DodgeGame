package walnoot.dodgegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public class SoundManager{
	private static final String[] musicPaths = {
		"Blobby Samba.mp3",
		"Comparsa.mp3",
		"CumbiaNoFrillsFaster.mp3",
		"No Frills Salsa.mp3",
		"Notanico Merengue.mp3",
		"Peppy Pepe.mp3"
	};
	private static final String[] soundPaths = {
		"apple.wav",
		"apple2.wav",
		"bite.mp3",
		"bite2.wav",
		"burp.wav"
	};
	public static final String SOUND_PREF_NAME = "SoundOn";
	
	private Music currentSong;
	private int songIndex;
	private FileHandle musicFolder, soundFolder;
	private boolean soundOn;
	private Sound[] sounds = new Sound[soundPaths.length];
	
	public void init(){
		musicFolder = Gdx.files.internal("music/");
		soundFolder = Gdx.files.internal("sounds/");
		
		songIndex = MathUtils.random(musicPaths.length - 1);
		currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
		
		if(DodgeGame.PREFERENCES.getBoolean(SOUND_PREF_NAME, true)){
			currentSong.play();
			soundOn = true;
		}
		
		for(int i = 0; i < sounds.length; i++){
			sounds[i] = Gdx.audio.newSound(soundFolder.child(soundPaths[i]));
		}
	}
	
	public void update(){
		if(!currentSong.isPlaying() && soundOn){
			currentSong.dispose();
			
			songIndex++;
			if(songIndex >= musicPaths.length) songIndex = 0;
			
			currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
			currentSong.play();
		}
		
		if(DodgeGame.INPUT.toggleSound.isJustPressed()){
			if(soundOn) pause();
			else resume();
		}
	}
	
	public void playRandomSound(){
		if(soundOn) sounds[MathUtils.random(0, sounds.length - 1)].play();
	}
	
	public void pause(){
		soundOn = false;
		
		currentSong.pause();
	}
	
	public void resume(){
		soundOn = true;
		
		currentSong.play();
	}
	
	public boolean isPlaying(){
		return soundOn;
	}
	
	public void dispose(){
		currentSong.dispose();
	}
}
