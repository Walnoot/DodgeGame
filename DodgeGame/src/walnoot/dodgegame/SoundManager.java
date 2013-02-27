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
	public static final String PREF_SOUND_KEY = "SoundVolume", PREF_MUSIC_KEY = "musicVolume";
	private static final float VOLUME_THRESHOLD = 0.05f;
	
	private Music currentSong;
	private int songIndex;
	private FileHandle musicFolder, soundFolder;
//	private boolean soundOn;
	private Sound[] eatSounds = new Sound[eatSoundPaths.length];
	private Sound clickSound;
	private boolean loaded;
	private float soundVolume, musicVolume;
	private boolean disposed;
	
	public void init(){
		musicFolder = Gdx.files.internal("music/");
		soundFolder = Gdx.files.internal("sounds/");
		
		songIndex = MathUtils.random(musicPaths.length - 1);
		currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
		
		soundVolume = DodgeGame.PREFERENCES.getFloat(PREF_SOUND_KEY, 1f);
		musicVolume = DodgeGame.PREFERENCES.getFloat(PREF_MUSIC_KEY, 1f);
		
		if(musicVolume > VOLUME_THRESHOLD){
			currentSong.play();
			currentSong.setVolume(soundVolume);
		}
		
		for(int i = 0; i < eatSounds.length; i++){
			eatSounds[i] = Gdx.audio.newSound(soundFolder.child(eatSoundPaths[i]));
		}
		
		clickSound = Gdx.audio.newSound(soundFolder.child("click.wav"));
		
		loaded = true;
	}
	
	public void update(){
		if(musicVolume > VOLUME_THRESHOLD){
			if(disposed){//returning from disposal
				currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
				currentSong.setVolume(musicVolume);
				
				currentSong.play();

				disposed = false;
			}else if(!currentSong.isPlaying()){
				currentSong.dispose();
				
				songIndex++;
				if(songIndex >= musicPaths.length) songIndex = 0;
				
				currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
				currentSong.setVolume(musicVolume);
				currentSong.play();
			}
		}
	}
	
	public void playRandomEatSound(){
		if(soundVolume > VOLUME_THRESHOLD) eatSounds[MathUtils.random(0, eatSounds.length - 1)].play(soundVolume);
	}
	
	public void playClickSound(){
		if(soundVolume > VOLUME_THRESHOLD) clickSound.play(soundVolume);
	}
	
	public void setSoundVolume(float volume){
		this.soundVolume = volume;
		
		if(volume < VOLUME_THRESHOLD) this.soundVolume = 0f;
		
		DodgeGame.PREFERENCES.putFloat(PREF_SOUND_KEY, this.soundVolume);
	}
	
	public void setMusicVolume(float volume){
		this.musicVolume = volume;
		
		if(volume < VOLUME_THRESHOLD){
			this.musicVolume = 0f;
			currentSong.pause();
		}else{
			if(!currentSong.isPlaying()) currentSong.play();
			currentSong.setVolume(volume);
		}
		
		DodgeGame.PREFERENCES.putFloat(PREF_MUSIC_KEY, this.musicVolume);
	}
	
	public float getSoundVolume(){
		return soundVolume;
	}
	
	public float getMusicVolume(){
		return musicVolume;
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
