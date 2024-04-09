package main.java.model.objects;

public class VertexWeight {
	private int boneID;
	private int vertexID;
	private float weight;
	
	public VertexWeight(int boneId, int vertexID, float weight) {
		this.boneID = boneId;
		this.vertexID = vertexID;
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Vertex ID: " + vertexID);
		sb.append(", Weight: " + weight);
		return sb.toString();
	}
	
	public int vertexID() {
		return vertexID;
	}
	
	public int boneID() {
		return boneID;
	}
	
	public float weight() {
		return weight;
	}
}
