package main.java.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;

import main.java.gui.Engine_Main;
import resources.ResourceLoader;

public class TestClass extends Thread implements Runnable{

	@Override
	public void run() {
		super.run();
				System.out.println(ResourceLoader.loadFile("Test.txt"));
			System.out.println(ResourceLoader.loadImage("Tex.png"));
		    
		    
//		String path = this.getClass().getClassLoader().getResource("/Test.txt").getFile();

//		String path2 = Engine_Main.class.getClassLoader().getResource("Shader").getFile();
		
//		System.out.println(path2);
		File file = new File("resources/Test.txt");
        if(file.exists())
            System.out.println("Yes");
        else
            System.out.println("No");
	}
	
}
