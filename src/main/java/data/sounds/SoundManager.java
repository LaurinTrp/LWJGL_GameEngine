package main.java.data.sounds;

import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import main.java.camera.Camera;

public class SoundManager {

	private long device;

	private long context;

	private SoundListener listener;

	private final Map<String, AudioData> audioDatas;

	private final Map<String, SoundSource> soundSourceMap;

	public SoundManager() {
		audioDatas = new HashMap<>();
		soundSourceMap = new HashMap<>();
	}

	public void init() {
		device = alcOpenDevice((ByteBuffer) null);
		if (device == NULL) {
			throw new IllegalStateException("Failed to open the default OpenAL device");
		}
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		context = alcCreateContext(device, (IntBuffer) null);
		if (context == NULL) {
			throw new IllegalStateException("Failed to create OpenAL context");
		}

		alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
	}

	public void addAudioData(String name, AudioData audioData) {
		audioDatas.put(name, audioData);
	}

	public AudioData getAudioData(String name) {
		AudioData data = audioDatas.get(name);
		if (data == null) {
			throw new IllegalArgumentException("The audio data " + name + " does not exist!");
		}
		return data;
	}

	public void addSoundSource(String name, SoundSource soundSource) {
		this.soundSourceMap.put(name, soundSource);
	}

	public void dispose() {
		soundSourceMap.values().forEach(SoundSource::dispose);
		soundSourceMap.clear();
		audioDatas.values().forEach(AudioData::dispose);
		audioDatas.clear();
		if (context != NULL) {
			alcDestroyContext(context);
		}
		if (device != NULL) {
			alcCloseDevice(device);
		}
	}

	public SoundListener getListener() {
		return this.listener;
	}

	public SoundSource getSoundSource(String name) {
		return this.soundSourceMap.get(name);
	}

	public void playSoundSource(String name) {
		SoundSource soundSource = this.soundSourceMap.get(name);
		if (soundSource != null && !soundSource.isPlaying()) {
			soundSource.play();
		}
	}

	public void removeSoundSource(String name) {
		this.soundSourceMap.remove(name);
	}

	public void setAttenuationModel(int model) {
		alDistanceModel(model);
	}

	public void setListener(SoundListener listener) {
		this.listener = listener;
	}

	public void updateListenerPosition(Camera camera) {
		if(listener == null) {
			return;
		}
		Mat4 viewMatrix = new Mat4(camera.getView());

		Vec3 lookAt = new Vec3(viewMatrix.m20, viewMatrix.m21, viewMatrix.m22);
		
		Vec3 cameraUp = new Vec3(viewMatrix.m10, viewMatrix.m11, viewMatrix.m12);
		listener.setOrientation(lookAt, cameraUp);
	}
}
