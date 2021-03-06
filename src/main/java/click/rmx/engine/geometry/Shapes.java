package click.rmx.engine.geometry;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * @author Max
 *
 */
public enum Shapes implements Shape {

	Cube(
			Data.cubeVertexData,
			Data.cubeNormalData,
			Data.cubeIndexData
			);
	
	private final float[] vertexData;
	private final float[] normalData;
	private final short[] indexData;
	private FloatBuffer vertexBuffer, normalsBuffer;
	private ByteBuffer indexBuffer;

	private Shapes(float[] vertexData, float[] normalData, short[] indexData) {
		this.vertexData = vertexData;
		this.normalData = normalData;
		this.indexData = indexData;
		this.initBuffers();
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.geometry.Shape#vertices()
	 */
	@Override
	public float[] vertices() {
		return vertexData;
	}

	/* (non-Javadoc)
	 * @see click.rmx.engine.geometry.Shape#normals()
	 */
	@Override
	public float[] normals() {
		return normalData;
	}

	/* (non-Javadoc)
	 * @see click.rmx.engine.geometry.Shape#indices()
	 */
	@Override
	public short[] indices() {
		return indexData;
	}
	
	

	@Override
	public FloatBuffer getVertexBuffer() {
		return this.vertexBuffer;
	}

	@Override
	public ByteBuffer getIndexBuffer() {
		// TODO Auto-generated method stub
		return this.indexBuffer;
	}

	@Override
	public FloatBuffer getNormalsBuffer() {
		// TODO Auto-generated method stub
		return this.normalsBuffer;
	}

	@Override
	public void setVertexBuffer(Buffer buffer) {
		this.vertexBuffer = (FloatBuffer) buffer;
	}

	@Override
	public void setIndexBuffer(Buffer buffer) {
		this.indexBuffer = (ByteBuffer) buffer;
	}

	@Override
	public void setNormalsBuffer(Buffer buffer) {
		this.normalsBuffer = (FloatBuffer) buffer;
	}

	public Geometry newGeometry() {
		return new GeometryImpl(this);
	}

	@Override
	public String getName() {
		return this.name();
	}
}

final class Data {
	protected static float [] cubeVertexData = {
			//Front
			-1.0f, -1.0f, 1.0f,1.0f,//        0.0f, 0.0f, 1.0f,
			1.0f, -1.0f, 1.0f,1.0f,//         0.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 1.0f, 1.0f,//         0.0f, 0.0f, 1.0f,
			-1.0f, 1.0f, 1.0f, 1.0f,//        0.0f, 0.0f, 1.0f,

			//back
			1.0f, 1.0f, -1.0f, 1.0f,//        0.0f, 0.0f,-1.0f,
			1.0f, -1.0f, -1.0f, 1.0f,//       0.0f, 0.0f,-1.0f,
			-1.0f, -1.0f, -1.0f, 1.0f,//      0.0f, 0.0f,-1.0f,
			-1.0f, 1.0f, -1.0f, 1.0f,//       0.0f, 0.0f,-1.0f,

			//bottom
			-1.0f, -1.0f, -1.0f,1.0f,//       0.0f,-1.0f, 0.0f,
			1.0f, -1.0f, -1.0f, 1.0f,//       0.0f,-1.0f, 0.0f,
			1.0f, -1.0f, 1.0f,  1.0f,//       0.0f,-1.0f, 0.0f,
			-1.0f, -1.0f, 1.0f, 1.0f,//       0.0f,-1.0f, 0.0f,

			//Top
			1.0f, 1.0f, 1.0f,  1.0f,//        0.0f, 1.0f, 0.0f,
			1.0f, 1.0f, -1.0f,  1.0f,//       0.0f, 1.0f, 0.0f,
			-1.0f, 1.0f, -1.0f, 1.0f,//       0.0f, 1.0f, 0.0f,
			-1.0f, 1.0f, 1.0f, 1.0f,//        0.0f, 1.0f, 0.0f,

			//right
			1.0f, -1.0f,-1.0f,1.0f,//         1.0f, 0.0f, 0.0f,
			1.0f, 1.0f, -1.0f,1.0f,//         1.0f, 0.0f, 0.0f,
			1.0f, 1.0f, 1.0f, 1.0f,//         1.0f, 0.0f, 0.0f,
			1.0f, -1.0f, 1.0f,1.0f,//         1.0f, 0.0f, 0.0f,

			//left
			-1.0f, 1.0f, 1.0f,  1.0f,//      -1.0f, 0.0f, 0.0f,
			-1.0f, 1.0f, -1.0f, 1.0f,//      -1.0f, 0.0f, 0.0f,
			-1.0f, -1.0f, -1.0f, 1.0f,//     -1.0f, 0.0f, 0.0f,
			-1.0f, -1.0f, 1.0f, 1.0f//      -1.0f, 0.0f, 0.0
	};



	static final float[] cubeNormalData = {
			//Front
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,

			//back
			0.0f, 0.0f,-1.0f,
			0.0f, 0.0f,-1.0f,
			0.0f, 0.0f,-1.0f,
			0.0f, 0.0f,-1.0f,

			//bottom
			0.0f,-1.0f, 0.0f,
			0.0f,-1.0f, 0.0f,
			0.0f,-1.0f, 0.0f,
			0.0f,-1.0f, 0.0f,

			//Top
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,

			//right
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,

			//left
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f
	};


	//float cubeColors[] = [
	//              [0.8, 0.8, 0.8, 1.0], // Front face: white
	//              [1.0f, 0.0f, 0.0f, 1.0], // Back face: red
	//              [0.0f, 1.0f, 0.0f, 1.0], // Top face: green
	//              [0.0f, 0.0f, 1.0f, 1.0], // Bottom face: blue
	//              [1.0f, 1.0f, 0.0f, 1.0], // Right face: yellow
	//              [1.0f, 0.0f, 1.0f, 1.0]     // Left face: purple
	//              ];
	//
	//var generatedColors = [];
	//
	//for (j = 0; j < 6; j++) {
	//    var c = colors[j];
	//    
	//    for (var i = 0; i < 4; i++) {
	//        generatedColors = generatedColors.concat(c);
	//    }
	//}


	// This array defines each face as two triangles, using the
	// indices into the vertex array to specify each triangle's
	// position.

	static final short cubeIndexData[] = {
			0,1,2,0,2,3, //Front
			4,5,6,4,6,7, //Back
			8,9,10,8,10,11, //top
			12,13,14,12,14,15, //bottom
			16,17,18,16,18,19, //right
			20,21,22,20,22,23 //left   // left
	};



	static final float triandleVertexData[] = {
			-1.0f, -1.0f, 0.0f, 1.0f,
			-1.0f,  1.0f, 0.0f, 1.0f,
			1.0f, -1.0f, 0.0f, 1.0f,

			1.0f, -1.0f, 0.0f, 1.0f,
			-1.0f,  1.0f, 0.0f, 1.0f,
			1.0f,  1.0f, 0.0f, 1.0f,

			-0.0f, 0.25f, 0.0f, 1.0f,
			-0.25f, -0.25f, 0.0f, 1.0f,
			0.25f, -0.25f, 0.0f, 1.0f
	};



	//let txtCoords: [Float] = [
	//                          //Front
	//                          -1.0f, -1.0f,
	//                          1.0f, -1.0f,
	//                          1.0f, 1.0f,
	//                          -1.0f, 1.0f,
	//                          
	//                          //back
	//                          1.0f, 1.0f,
	//                          1.0f, -1.0f,
	//                          -1.0f, -1.0f,
	//                          -1.0f, 1.0f,
	//                          
	//                          //bottom
	//                          -1.0f, -1.0f,
	//                          1.0f, -1.0f,
	//                          1.0f, -1.0f,
	//                          -1.0f, -1.0f,
	//                          
	//                          //Top
	//                          1.0f, 1.0f,
	//                          1.0f, 1.0f,
	//                          -1.0f, 1.0f,
	//                          -1.0f, 1.0f,
	//                          
	//                          //right
	//                          1.0f, -1.0f,
	//                          1.0f, 1.0f,
	//                          1.0f, 1.0f,
	//                          1.0f, -1.0f,
	//                          
	//                          //left
	//                          -1.0f, 1.0f,
	//                          -1.0f, 1.0f,
	//                          -1.0f, -1.0f,
	//                          -1.0f, -1.0f,
	//                          ]
}


