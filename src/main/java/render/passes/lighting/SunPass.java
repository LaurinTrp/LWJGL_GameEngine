package main.java.render.passes.lighting;

import glm.glm.mat._4.Mat4;
import glm.glm.vec._3.Vec3;
import glm.glm.vec._4.Vec4;

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
		if (angle < Math.PI / 2f) {
			r = -0.2326f * Math.pow(angle*4, 4) + 2.0523 * Math.pow(angle*4, 3) - 5.7 * Math.pow(angle*4, 2)
					+ 4.767 * angle*4*4 + 255;
			r = Math.max(0, Math.min(255, r));
			
			g = 0.263 * Math.pow(angle*4, 4) - 4.14 * Math.pow(angle*4, 3) + 16.338 * Math.pow(angle*4, 2) - 34.866 * angle*4 + 255;
			g = Math.max(0, Math.min(255, g));

			b = 0.552 * Math.pow(angle*4, 4) - 7.051 * Math.pow(angle*4, 3) + 36.55 * Math.pow(angle*4, 2) - 127.293 * angle*4 + 255;
			b = Math.max(0, Math.min(255, b));
			

		}
		else if(angle > Math.PI * 2 - Math.PI / 2f) {
			float tempAngle = (float) (angle - Math.PI * 1.5);
			System.out.println("ANGLE: " + (angle - Math.PI * 1.5));
			r = -0.2329f * Math.pow(tempAngle, 4) + 3.788 * Math.pow(tempAngle, 3) - 21.936 * Math.pow(tempAngle, 2)
					+ 53.392 * tempAngle + 209;
			r = Math.max(0, Math.min(255, r));
			
			g = -0.0063 * Math.pow(tempAngle, 4) + 1.1766 * Math.pow(tempAngle, 3) - 15.165 * Math.pow(tempAngle, 2) +80.80 * tempAngle + 64;
			g = Math.max(0, Math.min(255, g));
			
			b = 0.187 * Math.pow(tempAngle, 4)
					- 2.577 * Math.pow(tempAngle, 3)
					+ 19.567 * Math.pow(tempAngle, 2) - 28.446 * angle - Math.PI * 2
					- Math.PI / 2f + 9;
			b = Math.max(0, Math.min(255, b));
		} 
		color = new Vec4(r/255f, g/255f, b/255f, 1.0);
		
//		color.print();
	}
	
	public Vec4 getColor() {
		return color;
	}

}
