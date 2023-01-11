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
	}

	public static class Cube {

		public static final int triangleCount = 36;

		private static final float[] normals = {
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
		
		public static final float[] buffer = cube;

		public static final float[] bufferBC = basicCube;
		
		public static final float[] bufferNormals = normals;

		// @formatter:on
	}
}
