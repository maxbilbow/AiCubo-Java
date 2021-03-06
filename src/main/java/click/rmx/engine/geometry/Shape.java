package click.rmx.engine.geometry;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexAttribBinding;

import static org.lwjgl.system.MemoryUtil.memAddress;
public interface Shape {
	float[] vertices();
	float[] normals();
	short[] indices();
	
	default int vertexSize() {
		return this.vertices().length;
	}
	
	default int normalsSize() {
		return this.normals().length;
	}
	
	default int indexSize() {
		return this.indices().length;
	}
	
	default void initBuffers() {
		FloatBuffer vBuffer = BufferUtils.createFloatBuffer(this.vertexSize());
		FloatBuffer nBuffer = BufferUtils.createFloatBuffer(this.normalsSize());
		ByteBuffer iBuffer = BufferUtils.createByteBuffer(this.indexSize() * 2);
		
		vBuffer.put(this.vertices());
		nBuffer.put(this.normals());
		for (short i : this.indices())
			iBuffer.putShort(i);
		
		vBuffer.flip();
		nBuffer.flip();
		iBuffer.flip();
		
		this.setVertexBuffer(vBuffer);
		this.setNormalsBuffer(nBuffer);
		this.setIndexBuffer(iBuffer);
	}
	
	Geometry newGeometry();
	/**
	 * Called but initBuffers 
	 * @param buffer
	 */
	void setVertexBuffer(Buffer buffer);
	
	/**
	 * Called but initBuffers 
	 * @param buffer
	 */
	void setIndexBuffer(Buffer buffer);
	
	/**
	 * Called but initBuffers 
	 * @param buffer
	 */
	void setNormalsBuffer(Buffer buffer);
	
	FloatBuffer getVertexBuffer();
	
	ByteBuffer getIndexBuffer();
	
	FloatBuffer getNormalsBuffer();
	
	default long vertexBufferPointer() {
		return memAddress(getVertexBuffer());
	}
	
	default long normalsBufferPointer() {
		return memAddress(getNormalsBuffer());
	}
	
	default long indexBufferPointer() {
		return memAddress(getIndexBuffer());
	}
	default void bindToBuffer(int buffer) {
		System.err.println("Not tested or safe");
		System.exit(-1);
		 ARBVertexAttribBinding.glBindVertexBuffer(
				 (int) this.vertexBufferPointer(),
				 buffer,
				 0,
				 4
		    );
		 ARBVertexAttribBinding.glBindVertexBuffer(
				 (int) this.normalsBufferPointer(),
				 buffer,
				 0,
				 4
		    );
		 ARBVertexAttribBinding.glBindVertexBuffer(
				 (int) this.indexBufferPointer(),
				 buffer,
				 0,
				 1
		    );
	}
	
	String getName();
	
}



