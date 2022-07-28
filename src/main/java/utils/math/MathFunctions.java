package main.java.utils.math;

public class MathFunctions {
	public static float map(float value, float istart, float istop, float ostart, float ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart)); 
	}
}
