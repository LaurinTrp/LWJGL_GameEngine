package resources;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

public class ResourceLoader {

	static ResourceLoader rl = new ResourceLoader();

	public static Image loadImage(String imageName) {
		return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource("images/" + imageName));
	}

	public static InputStream loadFileAsStream(String fileName) {
		InputStream is = rl.getClass().getResourceAsStream(fileName);
		return is;
	}

	public static InputStream loadShader(String parent, String fileName) {
		InputStream is = rl.getClass().getResourceAsStream("Shader/" + parent + "/" + fileName);
		return is;
	}

	public static String loadShaderAsString(String parent, String fileName) {
		try {
			return new String(loadShader(parent, fileName).readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ByteBuffer loadTexture(String fileName) {
		InputStream imageFile = ResourceLoader.class.getResourceAsStream("Textures/" + fileName);
		byte[] imageData;
		try {
			imageData = IOUtils.toByteArray(imageFile);
			ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageData.length);
			imageBuffer.put(imageData);
			imageBuffer.flip();
			return imageBuffer;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<String> loadObjFile(String parentFolder, String fileName){
		InputStream modelFile = ResourceLoader.class.getResourceAsStream("Models/" + parentFolder + "/" + fileName);
		try {
			String content = new String(modelFile.readAllBytes());
			ArrayList<String> list = new ArrayList<>();
			for (String line : content.split("\n")) {
				list.add(line);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<String> loadMaterialFile(String parentFolder, String fileName){
		InputStream modelFile = ResourceLoader.class.getResourceAsStream("Models/" + parentFolder + "/" + fileName);
		try {
			String content = new String(modelFile.readAllBytes());
			ArrayList<String> list = new ArrayList<>();
			for (String line : content.split("\n")) {
				list.add(line);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		ByteBuffer b = ResourceLoader.loadTexture("Warn.png");
		for (int i = 0; i < b.capacity(); i++) {
			System.out.println(b.get(i));
		}

	}

}