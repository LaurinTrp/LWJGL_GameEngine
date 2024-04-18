package main.java.data.sounds;

import static org.lwjgl.openal.AL10.*;

import java.util.Arrays;

import glm.vec._3.Vec3;

public class SoundSource {

	private final int sourceID;

	public SoundSource(boolean loop, boolean relative) {
		sourceID = alGenSources();
		alSourcei(sourceID, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
		alSourcei(sourceID, AL_SOURCE_RELATIVE, relative ? AL_TRUE : AL_FALSE);
	}

	public void setAudioData(AudioData audioData) {
		stop();
		alSourcei(sourceID, AL_BUFFER, audioData.getBufferID());
	}

	public void setPosition(float x, float y, float z) {
		alSource3f(sourceID, AL_POSITION, x, y, z);
		
		float[] f1 = new float[1], f2 = new float[1], f3 = new float[1];
		alGetSource3f(sourceID, AL_POSITION, f1, f2, f3);
		System.out.println(Arrays.toString(f1) + "\t" + Arrays.toString(f2) + "\t" + Arrays.toString(f3));
	}

	public void setVelocity(float x, float y, float z) {
		alSource3f(sourceID, AL_VELOCITY, x, y, z);
	}

	public void setGain(float gain) {
		alSourcef(sourceID, AL_GAIN, gain);
	}

	public void setProperty(int param, float value) {
		alSourcef(sourceID, param, value);
	}

	public void play() {
		alSourcePlay(sourceID);
	}

	public boolean isPlaying() {
		return alGetSourcei(sourceID, AL_SOURCE_STATE) == AL_PLAYING;
	}

	public void pause() {
		alSourcePause(sourceID);
	}

	public void stop() {
		alSourceStop(sourceID);
	}

	public void dispose() {
		stop();
		alDeleteSources(sourceID);
	}
}
