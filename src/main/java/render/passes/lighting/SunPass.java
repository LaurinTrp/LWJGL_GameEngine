package main.java.render.passes.lighting;

import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glm.vec._4.Vec4;

public class SunPass {

	private boolean init = false;

	private Vec4 lightPosition = new Vec4(0.0, 5.0, 0.0, 0.0);

	private Vec4 color = new Vec4(1.0, 1.0, 1.0, 1.0);

	private float angle = 0.0f;

	private Mat4 modelMatrix;

	private void init() {
		initMatrixes();
		init = true;
	}

	private void initMatrixes() {
		modelMatrix = new Mat4(1.0f);
		modelMatrix.translate(new Vec3(lightPosition.x, lightPosition.y, lightPosition.z));
	}

	private void rotateSun() {
		modelMatrix.translate(new Vec3(lightPosition).negate().toFa_());
		modelMatrix.rotateX(Math.toRadians(.3));
		modelMatrix.translate(new Vec3(lightPosition).toFa_());
		angle += Math.toRadians(.3);
	}

	public void update() {

		if (!init) {
			init();
		}

		if (!init) {
			return;
		}

		rotateSun();
		angle %= Math.PI * 2;
		calculateLightColor();
	}

	public Vec4 getLightPosition() {
		if (modelMatrix != null) {
			return new Vec4(lightPosition).mul(modelMatrix);
		}
		return lightPosition;
	}

	public void calculateLightColor() {
		double r = 0, g = 0, b = 0;
		color = new Vec4();

		double tempAngle = angle;
		if (Math.toDegrees(angle) < 90  || Math.toDegrees(angle) > 270) {
			if(Math.toDegrees(angle) > 270) {
				tempAngle = Math.PI*2 - angle;
			}
			r = -653.553 * Math.pow(tempAngle, 5) + 2476.668 * Math.pow(tempAngle, 4) - 3329.971 * Math.pow(tempAngle, 3)
					+ 1731.326 * Math.pow(tempAngle, 2) - 285.683 * tempAngle + 255;
			r = Math.max(0, Math.min(255, r));
			
			
			g = 68.078 * Math.pow(tempAngle, 5) - 81.272 * Math.pow(tempAngle, 4) - 192.165 * Math.pow(tempAngle, 3)
					+ 220.796 * Math.pow(tempAngle, 2) - 124.486 * tempAngle + 255;
			g = Math.max(0, Math.min(255, g));
			
			b = -381.239 * Math.pow(tempAngle, 5) + 1360.243 * Math.pow(tempAngle, 4) - 1682.455 * Math.pow(tempAngle, 3)
					+ 1036.009 * Math.pow(tempAngle, 2) - 589.404 * tempAngle + 255;
			b = Math.max(0, Math.min(255, b));
		}

		color = new Vec4(r / 255f, g / 255f, b / 255f, 1.0);

	}

	public Vec4 getColor() {
		return color;
	}

}
