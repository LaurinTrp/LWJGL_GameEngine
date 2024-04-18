package main.java.data.sounds;

import glm.vec._3.Vec3;

import static org.lwjgl.openal.AL10.*;

import java.util.Arrays;

public class SoundListener {
	
	public SoundListener() {
		this(0,0,0);
	}
	
	public SoundListener(float x, float y, float z) {
		alListener3f(AL_POSITION, x, y, z);
	}
	
	public void setVelocity(float x, float y, float z) {
		alListener3f(AL_VELOCITY, x, y, z);
	}
	
	public void setPosition(float x, float y, float z) {
		alListener3f(AL_POSITION, x, y, z);
	}
	
	public void setOrientation(Vec3 at, Vec3 up) {
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        alListenerfv(AL_ORIENTATION, data);
    }
	
}
