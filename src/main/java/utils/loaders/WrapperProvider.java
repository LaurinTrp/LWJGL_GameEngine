package main.java.utils.loaders;

import java.nio.ByteBuffer;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import jassimp.AiColor;
import jassimp.AiNode;
import jassimp.AiQuaternion;
import jassimp.AiWrapperProvider;

public class WrapperProvider implements AiWrapperProvider<Vec3, Mat4, AiColor, AiNode, AiQuaternion>{

	@Override
	public Vec3 wrapVector3f(ByteBuffer buffer, int offset, int numComponents) {
		float[] data = new float[3];
		data[0] = buffer.getFloat(offset);
		data[1] = buffer.getFloat(offset+4);
		data[2] = buffer.getFloat(offset+8);
		Vec3 vec = new Vec3(data);
		return vec;
	}

	@Override
	public Mat4 wrapMatrix4f(float[] data) {
		Mat4 matrix = new Mat4(data);
		return matrix;
	}

	@Override
	public AiColor wrapColor(ByteBuffer buffer, int offset) {
        return new AiColor(buffer, offset);
	}

	@Override
	public AiNode wrapSceneNode(Object parent, Object matrix, int[] meshReferences, String name) {
        return new AiNode((AiNode) parent, matrix, meshReferences, name);
	}

	@Override
	public AiQuaternion wrapQuaternion(ByteBuffer buffer, int offset) {
        return new AiQuaternion(buffer, offset);
	}
}	
	
