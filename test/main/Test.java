package main;

import java.util.Random;

import glm.vec._2.Vec2;

public class Test {
	public static void main(String[] args) {
		int value = 1;
		
		int a = (value >> 24) & 0xFF;
		int r = (value >> 16) & 0xFF;
		int g = (value >> 8) & 0xFF;
		int b = value & 0xFF;
		
		System.out.println((r + "\t" + g + "\t" + b + "\t" + a));
		
		int convertedBack = (a << 24) | (r << 16) | (g << 8) | b;
		System.out.println(convertedBack);
	}
}
