package main;

import java.io.IOException;

import glm.vec._3.Vec3;
import main.java.data.sounds.AudioData;
import main.java.data.sounds.SoundListener;
import main.java.data.sounds.SoundManager;
import main.java.data.sounds.SoundSource;

public class SoundTest {

	public static void main(String[] args) throws Exception {
		SoundManager manager = new SoundManager();
		manager.init();
		playSounds(manager);
	}
	
	public static void playSounds(SoundManager soundManager) throws Exception {
		
		AudioData data = new AudioData("/run/media/laurin/Festplatte/Programmieren/Java/3D-Workbench/LWJGL_GameEngineResource/src/resources/audio/loopmusic.ogg");
		soundManager.addAudioData("Doorbell", data);
		
		SoundSource soundSource = new SoundSource(true, false);
		soundSource.setAudioData(soundManager.getAudioData("Doorbell"));
		soundManager.addSoundSource("Test", soundSource);
		
		SoundListener listener = new SoundListener();
		listener.setPosition(0, 0, 2);
		soundManager.setListener(listener);
		
		soundSource.play();
		
		float xpos = 20;
		soundSource.setPosition(0, 0, 0);

		char c = ' ';
		while(c != 'q') {
			xpos -= 0.03f;
			soundSource.setPosition(xpos, 0, 0);
			Thread.sleep(10);
		}
	}
}
