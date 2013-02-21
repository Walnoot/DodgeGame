package walnoot.dodgegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public class SoundManager{
	private static final String[] musicPaths = {"Comparsa.mp3", "CumbiaNoFrillsFaster.mp3", "No Frills Salsa.mp3",
			"Notanico Merengue.mp3", "Peppy Pepe.mp3"};
//	private static final String[] eatSoundPaths = {"apple.wav", "apple2.wav", "bite.mp3", "bite2.wav", "burp.wav"};
	private static final String[] eatSoundPaths = {"register.mp3"};
	public static final String PREF_SOUND_KEY = "SoundVolume";
	private static final float VOLUME_THRESHOLD = 0.05f;
	
	private Music currentSong;
	private int songIndex;
	private FileHandle musicFolder, soundFolder;
	private boolean soundOn;
	private Sound[] eatSounds = new Sound[eatSoundPaths.length];
	private Sound clickSound;
	private boolean loaded;
	private float volume;
	private boolean disposed;
	
	public void init(){
		musicFolder = Gdx.files.internal("music/");
		soundFolder = Gdx.files.internal("sounds/");
		
		songIndex = MathUtils.random(musicPaths.length - 1);
		currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
		
		volume = DodgeGame.PREFERENCES.getFloat(PREF_SOUND_KEY, 1f);
		
		if(volume > VOLUME_THRESHOLD){
			currentSong.play();
			currentSong.setVolume(volume);
			soundOn = true;
		}
		
		for(int i = 0; i < eatSounds.length; i++){
			eatSounds[i] = Gdx.audio.newSound(soundFolder.child(eatSoundPaths[i]));
		}
		
		clickSound = Gdx.audio.newSound(soundFolder.child("click.wav"));
		
		loaded = true;
	}
	
	public void update(){
		if(soundOn){
			if(disposed){//returning from disposal
				currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
				currentSong.setVolume(volume);
				
				currentSong.play();

				disposed = false;
			}else if(!currentSong.isPlaying()){
				currentSong.dispose();
				
				songIndex++;
				if(songIndex >= musicPaths.length) songIndex = 0;
				
				currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
				currentSong.setVolume(volume);
				currentSong.play();
			}
		}
	}
	
	public void playRandomEatSound(){
		if(soundOn) eatSounds[MathUtils.random(0, eatSounds.length - 1)].play(volume);
	}
	
	public void playClickSound(){
		if(soundOn) clickSound.play(volume);
	}
	
	public void pause(){
		soundOn = false;
		
		currentSong.pause();
	}
	
	public void resume(){
		soundOn = true;
		
		currentSong.play();
	}
	
	public void setVolume(float volume){
		currentSong.setVolume(volume);
		this.volume = volume;
		
		if(volume < VOLUME_THRESHOLD){
			this.volume = 0f;
			if(soundOn) pause();
		}else{
			if(!soundOn) resume();
		}
		
		DodgeGame.PREFERENCES.putFloat(PREF_SOUND_KEY, this.volume);
	}
	
	public float getVolume(){
		return volume;
	}
	
	public boolean isPlaying(){
		return soundOn;
	}
	
	public boolean isLoaded(){
		return loaded;
	}
	
	public void dispose(){
		disposed = true;
		
		currentSong.stop();

		currentSong.dispose();
	}
}
