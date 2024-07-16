package main.java.data.light;

public enum LightType {
	DIRECTIONAL(0),
	POINT(1),
	SPOT(2);
	
	private final int index;
	private LightType(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
}
