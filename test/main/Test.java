package main;

import java.util.Arrays;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import main.java.data.light.LightManager;
import main.java.data.light.Light;
import main.java.data.sounds.AudioData;
import main.java.data.sounds.SoundManager;
import main.java.utils.math.MoellerTrumbore;

public class Test {
	public static void main(String[] args) {
		MoellerTrumbore ml = new MoellerTrumbore();
		
		float[] origin = {0,0,0};
		float[] direction = {1, 0, 0};
		float[] triangle = {
				1, -1, -1,
				1, 1, -1, 
				1, 0, 1
		};
		
//		float[] intersect = ml.intersect(origin, direction, triangle);
//		System.out.println(Arrays.toString(intersect));
		
	}
}
