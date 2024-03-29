package main.java.utils;

public class Shapes {

	// @formatter:off

	public static class Triangle {
		public static final int triangleCount = 3;
		public static final float[] buffer = {
				// Bottom Left
				-1.0f, -1.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// Bottom Right
				1.0f, -1.0f, 0.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// Top
				0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f, };
	}

	public static class Rectangle {

		public static final int triangleCount = 6;

		public static final float[] buffer = {
				// Bottom Left
				-1.0f, -1.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// Bottom Right
				1.0f, -1.0f, 0.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// Top Right
				1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// Bottom Left
				-1.0f, -1.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// Top Right
				1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// Top Left
				-1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
			};

		public static final float vertices[] = {
			     0.5f,  0.5f, 0.0f,  // top right
			     0.5f, -0.5f, 0.0f,  // bottom right
			    -0.5f, -0.5f, 0.0f,  // bottom left
			    -0.5f,  0.5f, 0.0f   // top left 
			};
		public static final int indices[] = {  // note that we start from 0!
			    0, 1, 3,   // first triangle
			    1, 2, 3    // second triangle
			}; 
	}

	public static class Cube {

		public static final int triangleCount = 36;

		private static final float[] normals = {
				// Coord, color, uvs, normal
			    -0.5f, -0.5f, -0.5f, 1.0f,		0.0f, 0.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		 0.0f,  0.0f, -1.0f, 1.0f,
			     0.5f, -0.5f, -0.5f, 1.0f,		0.0f, 0.0f, 1.0f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		 0.0f,  0.0f, -1.0f, 1.0f,
			     0.5f,  0.5f, -0.5f, 1.0f,		0.0f, 0.0f, 1.0f, 1.0f,	 	1.0f, 1.0f, 0.0f, 1.0f,		 0.0f,  0.0f, -1.0f, 1.0f,
			     0.5f,  0.5f, -0.5f, 1.0f,		0.0f, 0.0f, 1.0f, 1.0f,	 	1.0f, 1.0f, 0.0f, 1.0f,		 0.0f,  0.0f, -1.0f, 1.0f,
			    -0.5f,  0.5f, -0.5f, 1.0f,		0.0f, 0.0f, 1.0f, 1.0f,	 	0.0f, 1.0f, 0.0f, 1.0f,		 0.0f,  0.0f, -1.0f, 1.0f,
			    -0.5f, -0.5f, -0.5f, 1.0f,		0.0f, 0.0f, 1.0f, 1.0f,	 	0.0f, 0.0f, 0.0f, 1.0f,		 0.0f,  0.0f, -1.0f, 1.0f,

			    -0.5f, -0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		 0.0f,  0.0f, 1.0f, 1.0f,
			     0.5f, -0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		1.0f, 0.0f, 0.0f, 1.0f,	  	 0.0f,  0.0f, 1.0f, 1.0f,
			     0.5f,  0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		 0.0f,  0.0f, 1.0f, 1.0f,
			     0.5f,  0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		 0.0f,  0.0f, 1.0f, 1.0f,
			    -0.5f,  0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		 0.0f,  0.0f, 1.0f, 1.0f,
			    -0.5f, -0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		 0.0f,  0.0f, 1.0f, 1.0f,

			    -0.5f,  0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 1.0f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		 -1.0f,  0.0f,  0.0f, 1.0f,
			    -0.5f,  0.5f, -0.5f, 1.0f,		0.0f, 1.0f, 1.0f, 1.0f,		1.0f, 0.0f, 0.0f, 1.0f,		 -1.0f,  0.0f,  0.0f, 1.0f,
			    -0.5f, -0.5f, -0.5f, 1.0f,		0.0f, 1.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		 -1.0f,  0.0f,  0.0f, 1.0f,
			    -0.5f, -0.5f, -0.5f, 1.0f,		0.0f, 1.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		 -1.0f,  0.0f,  0.0f, 1.0f,
			    -0.5f, -0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 1.0f, 1.0f,		0.0f, 1.0f, 0.0f, 1.0f,		 -1.0f,  0.0f,  0.0f, 1.0f,
			    -0.5f,  0.5f,  0.5f, 1.0f,		0.0f, 1.0f, 1.0f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		 -1.0f,  0.0f,  0.0f, 1.0f,

			     0.5f,  0.5f,  0.5f, 1.0f,		1.0f, 0.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  1.0f,  0.0f,  0.0f, 1.0f,
			     0.5f,  0.5f, -0.5f, 1.0f,		1.0f, 0.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  1.0f,  0.0f,  0.0f, 1.0f,
			     0.5f, -0.5f, -0.5f, 1.0f,		1.0f, 0.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  1.0f,  0.0f,  0.0f, 1.0f,
			     0.5f, -0.5f, -0.5f, 1.0f,		1.0f, 0.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  1.0f,  0.0f,  0.0f, 1.0f,
			     0.5f, -0.5f,  0.5f, 1.0f,		1.0f, 0.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  1.0f,  0.0f,  0.0f, 1.0f,
			     0.5f,  0.5f,  0.5f, 1.0f,		1.0f, 0.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  1.0f,  0.0f,  0.0f, 1.0f,

			    -0.5f, -0.5f, -0.5f, 1.0f,		1.0f, 0.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f, -1.0f,  0.0f, 1.0f,
			     0.5f, -0.5f, -0.5f, 1.0f,		1.0f, 0.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f, -1.0f,  0.0f, 1.0f,
			     0.5f, -0.5f,  0.5f, 1.0f,		1.0f, 0.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f, -1.0f,  0.0f, 1.0f,
			     0.5f, -0.5f,  0.5f, 1.0f,		1.0f, 0.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f, -1.0f,  0.0f, 1.0f,
			    -0.5f, -0.5f,  0.5f, 1.0f,		1.0f, 0.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f, -1.0f,  0.0f, 1.0f,
			    -0.5f, -0.5f, -0.5f, 1.0f,		1.0f, 0.0f, 1.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f, -1.0f,  0.0f, 1.0f,

			    -0.5f,  0.5f, -0.5f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f,  1.0f,  0.0f, 1.0f,
			     0.5f,  0.5f, -0.5f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f,  1.0f,  0.0f, 1.0f,
			     0.5f,  0.5f,  0.5f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f,  1.0f,  0.0f, 1.0f,
			     0.5f,  0.5f,  0.5f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f,  1.0f,  0.0f, 1.0f,
			    -0.5f,  0.5f,  0.5f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f,  1.0f,  0.0f, 1.0f,
			    -0.5f,  0.5f, -0.5f, 1.0f,		1.0f, 1.0f, 0.0f, 1.0f,		0.0f, 0.0f, 0.0f, 1.0f,		  0.0f,  1.0f,  0.0f, 1.0f,
			};

		private static final float[] basicCube = {
				// front
				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 0.5f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 0.5f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 0.5f, 	0.0f, 0.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// left
				-1.0f, -1.0f, 1.0f, 1.0f,	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, 1.0f, 1.0f,	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// right
				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// back
				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// top
				-1.0f, 1.0f, -1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				-1.0f, 1.0f, -1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				// bottom
				-1.0f, -1.0f, -1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, -1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, -1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
		};

		private static final float[] cube = {
				// front
				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				// left
				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				// right
				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				// back
				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				// top
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,

				// bottom
				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,
				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f, 	1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,
		};

		public static final float[] cubeNoTexture = {
				// front
				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f,

				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				// left
				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,

				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,

				// right
				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f,

				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f,

				// back
				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f,

				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,

				// top
				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f,

				-1.0f, 1.0f, -1.0f, 1.0f, 	0.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 	1.0f, 1.0f, 0.0f, 1.0f,

				// bottom
				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f,

				-1.0f, -1.0f, -1.0f, 1.0f, 	0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 	1.0f, 0.0f, 1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f, 1.0f,
		};
		public static final float[] cubeOnlyVertices = {
				// front
				-1.0f, -1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 

				-1.0f, -1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 
				-1.0f, 1.0f, -1.0f, 1.0f,

				// left
				-1.0f, -1.0f, 1.0f, 1.0f,
				-1.0f, -1.0f, -1.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f,

				-1.0f, -1.0f, 1.0f, 1.0f,
				-1.0f, 1.0f, -1.0f, 1.0f,
				-1.0f, 1.0f, 1.0f, 1.0f, 

				// right
				1.0f, -1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 
				1.0f, 1.0f, 1.0f, 1.0f, 

				1.0f, -1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 
				1.0f, 1.0f, -1.0f, 1.0f, 

				// back
				-1.0f, -1.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 
				1.0f, 1.0f, 1.0f, 1.0f, 

				-1.0f, -1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 
				-1.0f, 1.0f, 1.0f, 1.0f, 

				// top
				-1.0f, 1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, -1.0f, 1.0f, 
				1.0f, 1.0f, 1.0f, 1.0f, 

				-1.0f, 1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f, 1.0f, 
				-1.0f, 1.0f, 1.0f, 1.0f, 

				// bottom
				-1.0f, -1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 

				-1.0f, -1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f, 1.0f, 
				-1.0f, -1.0f, 1.0f, 1.0f,
		};

		public static final float[] buffer = cube;

		public static final float[] bufferBC = basicCube;

		public static final float[] bufferNormals = normals;
		
		public static final float[] vertices = cubeOnlyVertices;

	}

	public static class SimpleCube {
		public static float[] vertices = {
				// Front face
				-1.0f, -1.0f, 1.0f, 1.0f, 	// Bottom Left
				1.0f, -1.0f, 1.0f, 1.0f,	// Bottom Right
				-1.0f, 1.0f, 1.0f, 1.0f,	// Top Left
				1.0f, 1.0f, 1.0f, 1.0f,		// Top Right
				// Back face
				-1.0f, -1.0f, -1.0f, 1.0f, 	// Bottom Left
				1.0f, -1.0f, -1.0f, 1.0f,	// Bottom Right
				-1.0f, 1.0f, -1.0f, 1.0f,	// Top Left
				1.0f, 1.0f, -1.0f, 1.0f,	// Top Right
				
		};
		public static float[] texCoords = {
				// Front face
				0.0f, 0.0f, 0.0f, 1.0f, // V0
				1.0f, 0.0f, 0.0f, 1.0f, // V1
				1.0f, 1.0f, 0.0f, 1.0f, // V2
				0.0f, 1.0f, 0.0f, 1.0f, // V3
				// Back face 
				1.0f, 0.0f, 0.0f, 1.0f, // V4
				1.0f, 1.0f, 0.0f, 1.0f, // V5
				0.0f, 1.0f, 0.0f, 1.0f, // V6
				0.0f, 0.0f, 0.0f, 1.0f, // V7
		};
		public static float[] normals = {
				// Front face
				0.0f, 0.0f, 1.0f, 1.0f,	// V0
				0.0f, 0.0f, 1.0f, 1.0f,	// V1
				0.0f, 0.0f, 1.0f, 1.0f,	// V2
				0.0f, 0.0f, 1.0f, 1.0f,	// V3
				// Back face       
				0.0f, 0.0f, -1.0f, 1.0f, // V4
				0.0f, 0.0f, -1.0f, 1.0f, // V5
				0.0f, 0.0f, -1.0f, 1.0f, // V6
				0.0f, 0.0f, -1.0f, 1.0f, // V7
		};
		public static int[] indices = {
			    // Front face
			    0, 1, 2,
			    1, 3, 2,
			    // Right face
			    1, 7, 3, 
			    7, 1, 5, 
			    // Back face
			    6, 7, 5, 
			    5, 4, 6, 
			    // Left face
			    4, 0, 6,
			    6, 0, 2,
			    // Bottom face
			    4, 5, 0, 
			    1, 0, 5, 
			    // Top face
			    3, 7, 6, 
			    6, 2, 3  
			};

	}
	
	public static class SimpleCube2{
		public static float[] vertices = {
			    // Front face
			    -1.0f, -1.0f, 1.0f,   // V0
			    1.0f, -1.0f, 1.0f,    // V1
			    1.0f, 1.0f, 1.0f,     // V2
			    -1.0f, 1.0f, 1.0f,    // V3
			    // Back face
			    -1.0f, -1.0f, -1.0f,  // V4
			    1.0f, -1.0f, -1.0f,   // V5
			    1.0f, 1.0f, -1.0f,    // V6
			    -1.0f, 1.0f, -1.0f    // V7
			};
		public static float[] texCoords = {
			    // Front face
			    0.0f, 0.0f,  // V0
			    1.0f, 0.0f,  // V1
			    1.0f, 1.0f,  // V2
			    0.0f, 1.0f,  // V3
			    // Back face
			    1.0f, 0.0f,  // V4
			    1.0f, 1.0f,  // V5
			    0.0f, 1.0f,  // V6
			    0.0f, 0.0f,  // V7
			};
		public static float[] normals = {
			    // Front face
			    0.0f, 0.0f, 1.0f,  // V0
			    0.0f, 0.0f, 1.0f,  // V1
			    0.0f, 0.0f, 1.0f,  // V2
			    0.0f, 0.0f, 1.0f,  // V3
			    // Back face
			    0.0f, 0.0f, -1.0f, // V4
			    0.0f, 0.0f, -1.0f, // V5
			    0.0f, 0.0f, -1.0f, // V6
			    0.0f, 0.0f, -1.0f, // V7
			};
		public static int[] indices = {
			    // Front face
			    0, 1, 2,  // V0-V1-V2
			    2, 3, 0,  // V2-V3-V0
			    // Right face
			    1, 5, 6,  // V1-V5-V6
			    6, 2, 1,  // V6-V2-V1
			    // Back face
			    7, 6, 5,  // V7-V6-V5
			    5, 4, 7,  // V5-V4-V7
			    // Left face
			    4, 0, 3,  // V4-V0-V3
			    3, 7, 4,  // V3-V7-V4
			    // Bottom face
			    4, 5, 1,  // V4-V5-V1
			    1, 0, 4,  // V1-V0-V4
			    // Top face
			    3, 2, 6,  // V3-V2-V6
			    6, 7, 3   // V6-V7-V3
			};

	}
	// @formatter:on
}
