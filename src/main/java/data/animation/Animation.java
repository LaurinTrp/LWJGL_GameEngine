package main.java.data.animation;

import java.util.List;

public class Animation {
	private String name;
	private List<AnimatedFrame> frames;
	private double duration;

	public Animation(String name, List<AnimatedFrame> frames, double duration) {
		this.name = name;
		this.frames = frames;
		this.duration = duration;
	}
	
	public String getName() {
		return name;
	}
	
	public List<AnimatedFrame> getFrames() {
		return frames;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Animation: Name: " + name + ", Duration: " + duration);
		return sb.toString();
	}
}
