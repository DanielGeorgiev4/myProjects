package testingPackage;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Audio {

	private Media media;
	private MediaPlayer mediaPlayer;
	
	public Audio() {
		
	}
	public Audio(String audioName){
		loadAudio(audioName);
	}
	public void loadAudio(String audioName) {
		media = new Media(new File(audioName).toURI().toString());
		mediaPlayer = new MediaPlayer(media);
	}

	public void play() {
		mediaPlayer.play();
	}

	public void repeat() {
		mediaPlayer.seek(Duration.ZERO);
		play();
	}
	
}
