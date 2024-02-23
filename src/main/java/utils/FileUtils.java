package main.java.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

public class FileUtils {
	public static String removeExtension(File file) {
		if(file.isFile()) {
			return file.getName().substring(0, file.getName().lastIndexOf("."));
		}
		return "";
	}

	public static String getExtension(File file) {
		if(file.isFile()) {
			return file.getName().substring(file.getName().lastIndexOf(".") + 1);
		}
		return "";
	}

	public static ArrayList<String> getFileContent(File file) throws IOException {
		ArrayList<String> content = new ArrayList<>();
		FileInputStream fis = new FileInputStream(file);
		String contentString = new String(fis.readAllBytes());
		for (String string : contentString.split("\n")) {
			content.add(string);
		}
		return content;
	}
	public static String getFileContentAsString(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		String contentString = new String(fis.readAllBytes());
		return contentString;
	}
	public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (url == null)
            throw new IOException("Classpath resource not found: " + resource);
        File file = new File(url.getFile());
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            fc.close();
            fis.close();
        } else {
            buffer = BufferUtils.createByteBuffer(bufferSize);
            InputStream source = url.openStream();
            if (source == null)
                throw new FileNotFoundException(resource);
            try {
                byte[] buf = new byte[8192];
                while (true) {
                    int bytes = source.read(buf, 0, buf.length);
                    if (bytes == -1)
                        break;
                    if (buffer.remaining() < bytes)
                        buffer = resizeBuffer(buffer, Math.max(buffer.capacity() * 2, buffer.capacity() - buffer.remaining() + bytes));
                    buffer.put(buf, 0, bytes);
                }
                buffer.flip();
            } finally {
                source.close();
            }
        }
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
