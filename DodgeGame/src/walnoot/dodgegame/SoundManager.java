package walnoot.dodgegame;

import walnoot.dodgegame.states.GameState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class SoundManager{
//	private static final String[] musicPaths = {"Comparsa.mp3", "CumbiaNoFrillsFaster.mp3", "No Frills Salsa.mp3",
//			"Notanico Merengue.mp3", "Peppy Pepe.mp3"};
	private static final String MENU_SONG_NAME = "Slow Ska Game Loop.ogg";
	private static final String GAME_SONG_NAME = "Funk Game Loop.ogg";
	private static final String[] grabSoundPaths = {"register.mp3"};
	public static final String PREF_SOUND_KEY = "SoundVolume", PREF_MUSIC_KEY = "musicVolume";
	private static final float VOLUME_THRESHOLD = 0.05f;
	
	private Music menuSong, gameSong;
//	private int songIndex;
	private FileHandle musicFolder, soundFolder;
//	private boolean soundOn;
	private Sound[] grabSounds = new Sound[grabSoundPaths.length];
	private Sound clickSound;
	private boolean loaded;
	private float soundVolume, musicVolume;
	
	private float transistion = 0f;
	
//	private boolean disposed;
	
	public void init(){
		musicFolder = Gdx.files.internal("music/");
		soundFolder = Gdx.files.internal("sounds/");
		
//		songIndex = MathUtils.random(musicPaths.length - 1);
//		currentSong = Gdx.audio.newMusic(musicFolder.child(musicPaths[songIndex]));
//		menuSong = Gdx.audio.newMusic(musicFolder.child("Slow Ska Game Loop.ogg"));
//		currentSong = Gdx.audio.newMusic(musicFolder.child("Funk Game Loop.ogg"));
		
		soundVolume = DodgeGame.PREFERENCES.getFloat(PREF_SOUND_KEY, 1f);
		musicVolume = DodgeGame.PREFERENCES.getFloat(PREF_MUSIC_KEY, 1f);
		
//		if(musicVolume > VOLUME_THRESHOLD){
//			menuSong.play();
//			menuSong.setLooping(true);
//			menuSong.setVolume(soundVolume);
//		}
		
		for(int i = 0; i < grabSounds.length; i++){
			grabSounds[i] = Gdx.audio.newSound(soundFolder.child(grabSoundPaths[i]));
		}
		
		clickSound = Gdx.audio.newSound(soundFolder.child("click.wav"));
		
		loaded = true;
	}
	
	public void update(){
		if(musicVolume > VOLUME_THRESHOLD){
			if(DodgeGame.state instanceof GameState){
				if(gameSong == null){
					gameSong = Gdx.audio.newMusic(musicFolder.child(GAME_SONG_NAME));
					gameSong.setVolume(transistion * musicVolume);
					gameSong.setLooping(true);
					gameSong.play();
				}
				
				transistion += DodgeGame.SECONDS_PER_UPDATE;
				
				if(transistion > 1f){
					transistion = 1f;
					
					if(menuSong != null){
						menuSong.dispose();
						menuSong = null;
					}
				}else{
					menuSong.setVolume(Interpolation.sine.apply(0f, musicVolume, 1f - transistion));
					gameSong.setVolume(Interpolation.sine.apply(0f, musicVolume, transistion));
				}
			}else{
				if(menuSong == null){
					menuSong = Gdx.audio.newMusic(musicFolder.child(MENU_SONG_NAME));
					menuSong.setVolume((1f - transistion) * musicVolume);
					menuSong.setLooping(true);
					menuSong.play();
				}
				
				transistion -= DodgeGame.SECONDS_PER_UPDATE;
				
				if(transistion < 0f){
					transistion = 0f;
					
					if(gameSong != null){
						gameSong.dispose();
						gameSong = null;
					}
				}else{
					menuSong.setVolume(Interpolation.sine.apply(0f, musicVolume, 1f - transistion));
					gameSong.setVolume(Interpolation.sine.apply(0f, musicVolume, transistion));
				}
			}
		}
	}
	
	public void playRandomGrabSound(){
		if(soundVolume > VOLUME_THRESHOLD) grabSounds[MathUtils.random(0, grabSounds.length - 1)].play(soundVolume);
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
			if(menuSong != null){
				menuSong.dispose();
				menuSong = null;
			}
		}else{
			if(menuSong == null){
				menuSong = Gdx.audio.newMusic(musicFolder.child(MENU_SONG_NAME));
				menuSong.setLooping(true);
				menuSong.play();
			}
			
			menuSong.setVolume(musicVolume);
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
		if(menuSong != null) menuSong.dispose();
		if(gameSong != null) gameSong.dispose();
	}
}
