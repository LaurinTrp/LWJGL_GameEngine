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
		System.out.println(SimplexNoise.noise(0, 20));
		System.out.println(SimplexNoise.noise(0, 21));
		System.out.println();
	}
	
	public static void main(String[] args) {
		new TestClass().start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new TestClass().start();
	}
	
}
