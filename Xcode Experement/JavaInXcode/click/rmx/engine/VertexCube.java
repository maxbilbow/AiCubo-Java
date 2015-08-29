package click.rmx.engine;

import click.rmx.engine.gl.Mesh;
import click.rmx.engine.math.Vector3;

public class VertexCube extends Geometry {

	public VertexCube(Mesh mesh) {
		super(mesh);
		mesh.vertices = new Vector3[] {
				new Vector3( 1, 1,-1),
				new Vector3(-1, 1,-1),
				new Vector3(-1, 1, 1),
				new Vector3( 1, 1, 1),
				
				new Vector3( 1,-1, 1),
				new Vector3(-1,-1, 1),
				new Vector3(-1,-1,-1),
				new Vector3( 1,-1,-1),

				new Vector3( 1, 1, 1),
				new Vector3(-1, 1, 1),
				new Vector3(-1,-1, 1),
				new Vector3( 1,-1, 1),
				
				new Vector3( 1,-1,-1),
				new Vector3(-1,-1,-1),
				new Vector3(-1, 1,-1),
				new Vector3( 1, 1,-1),
				
				new Vector3(-1, 1, 1),
				new Vector3(-1, 1,-1),
				new Vector3(-1,-1,-1),
				new Vector3(-1,-1, 1),
				
				new Vector3( 1, 1,-1),
				new Vector3( 1, 1, 1),
				new Vector3( 1,-1, 1),
				new Vector3( 1,-1,-1),
				

		};
		        
	}

	@Override
	protected void drawWithScale(float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}

}
