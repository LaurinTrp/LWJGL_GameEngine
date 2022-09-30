package main.java.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;

import main.java.gui.Engine_Main;
import main.java.utils.loaders.ModelLoader;
import main.java.utils.math.ImprovedNoise;
import main.java.utils.math.SimplexNoise;

public class TestClass extends Thread implements Runnable {

	@Override
	public void run() {
		super.run();
		double[] data = new double[20];
		for(int i = 0; i < 20; i++) {
			data[i]=SimplexNoise.noise(i*20, 0);
		}
		double minValue = data[0], maxValue = data[0];
	    for (int i = 0; i < data.length; i++) {
	      minValue = Math.min(data[i], minValue);
	      maxValue = Math.max(data[i], maxValue);
	    }

	    int[] pixelData = new int[20];
	    for (int i = 0; i < data.length; i++) {
	      pixelData[i] = (int) (255 * (data[i] - minValue) / (maxValue - minValue));
	    }
	    System.out.println(Arrays.toString(pixelData));
	}
	
	public static void main(String[] args) {
		new TestClass().start();
	}
	
}
