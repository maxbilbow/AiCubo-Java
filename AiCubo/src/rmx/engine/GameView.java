package rmx.engine;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
//import org.lwjgl.util.Display;
//import org.lwjgl.util.glu.GLU;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

import static rmx.RMX.*;

import rmx.Bugger;
import rmx.RMXObject;
import rmx.gl.GLView;
import rmx.gl.KeyCallback;

public class GameView extends RMXObject implements GLView{
 
	private Node _pointOfView;
	private int _height = 340, _width = 600;
	
	
	 // The indices of the vertex position and colour attributes, we use these attributes in the vertex shader
    private static final int VERTEX_POSITION = 1, VERTEX_COLOUR = 0;
    // The error callback function for GLFW
//    private static GLFWErrorCallback errorCallback;
    private static GLFWCursorPosCallback cursorCallback;
    // The window handle
    private static long windowID;
    // The Vertex Array Object (VAO):  stores the of bindings between Vertex Attributes and vertex data
    private static int vertexArrayObject;
    // The Vertex Buffer Object (VBO): stores vertex position and colour data
    private static int vertexBufferObject;
    // The Index Buffer Object (IBO): stores the indices of the data in the VBO, used by glDrawElements
    private static int indexBufferObject;
    // The OpenGL shader program handle
    private static int shaderProgram;
    private static int uniformModelviewProjection;
    // In LWJGL we store vertex and index data using Buffers, because they most resemble C/C++ data arrays
    private static ByteBuffer vertexData;// = BufferUtils.createDoubleBuffer(20);
    private static ShortBuffer indexData;// = BufferUtils.createShortBuffer(6);
    private static Matrix4f modelviewMatrix = new Matrix4f();
    private static Matrix4f projectionMatrix = new Matrix4f();
    private static FloatBuffer mvpMatrix = BufferUtils.createFloatBuffer(16);
    private static Vector4f translate = new Vector4f(0, 0, -5, 1);
    private static int mouseX = -9999, mouseY = -9999;
    
    static {
//    	vertexData = Geometry.cube().vertices();
//    	indexData = Geometry.cube().indexData();
    }
    
    // We need to strongly reference callback instances.
    private GLFWErrorCallback _errorCallback;
    private GLFWKeyCallback   _keyCallback;
 
    // The window handle
    private long _window;
 
    
 
    
    @Override
	public void initGL() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(_errorCallback = errorCallbackPrint(System.err));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
 
        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
 

 
        // Create the window
        _window = glfwCreateWindow(_width, _height, "Hello World!", NULL, NULL);
        if ( _window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
 
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(_window, _keyCallback = rmx.gl.KeyCallback.getInstance());
 
        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            _window,
            (GLFWvidmode.width(vidmode) - _width) / 2,
            (GLFWvidmode.height(vidmode) - _height) / 2
        );
 
        // Make the OpenGL context current
        glfwMakeContextCurrent(_window);
        // Enable v-sync
        glfwSwapInterval(1);
 
        // Make the window visible
        glfwShowWindow(_window);
       
//        more();
    }
 
    private void _more() {
    	 
    	GLContext.createFromCurrent();
    	// >> Vertex Array Objects (VAO) are OpenGL Objects that store the
        // >> set of bindings between Vertex Attributes and the user's source
        // >> vertex data. (http://www.opengl.org/wiki/Vertex_Array_Object)
        // >> glGenVertexArrays returns n vertex array object names in arrays.
        // Create a VAO and store the handle in vertexArrayObject
        vertexArrayObject = glGenVertexArrays();
        // >> glGenBuffers returns n buffer object names in buffers.
        // >> No buffer objects are associated with the returned buffer object names
        // >> until they are first bound by calling glBindBuffer.
        vertexBufferObject = glGenBuffers();
        indexBufferObject = glGenBuffers();

        // >> glBindVertexArray binds the vertex array object with name array.
        // Bind the VAO to OpenGL
        glBindVertexArray(vertexArrayObject);
        // >> glBindBuffer binds a buffer object to the specified buffer binding point.
        // >> Vertex Buffer Objects (VBOs) are Buffer Objects that are used for
        // >> vertex data. (VBO = GL_ARRAY_BUFFER)
        // Bind our buffer object to GL_ARRAY_BUFFER, thus making it a VBO.
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject);

        // >> glBufferData creates a new data store for the buffer object currently bound
        // >> to target. Any pre-existing data store is deleted. The new data store is created
        // >> with the specified size in bytes and usage. If data is not NULL, the data
        // >> store is initialized with data from this pointer. In its initial state, the
        // >> new data store is not mapped, it has a NULL mapped pointer, and its mapped
        // >> access is GL_READ_WRITE.
        // Store the vertex data (position and colour) in the VBO.
       
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        // Store the vertex index data in the IBO.
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);

    }
 
   
    @Override
	public void enterGameLoop() {
//    	glfwGenuffers(1, frameBuffer);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();
        
        
     // >> glEnableVertexAttribArray enables the generic vertex attribute array specified by index.
        // >> glDisableVertexAttribArray disables the generic vertex attribute array specified by
        // >> index. By default, all client-side capabilities are disabled, including all generic
        // >> vertex attribute arrays. If enabled, the values in the generic vertex attribute array
        // >> will be accessed and used for rendering when calls are made to vertex array commands
        // >> such as glDrawArrays, glDrawElements, glDrawRangeElements, glMultiDrawElements, or glMultiDrawArrays.
        // Enable the vertex position vertex attribute.
//        glEnableVertexAttribArray(VERTEX_POSITION);
        // Enable the vertex colour vertex attribute.;
//        glEnableVertexAttribArray(VERTEX_COLOUR);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
        GL11.glClearDepth(1.0); 
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL); 

//        GL11.glMatrixMode(GL11.GL_PROJECTION); 
//        GL11.glLoadIdentity();

        // >> glVertexAttribPointer and glVertexAttribIPointer specify the location and data format of the
        // >> array of generic vertex attributes at index index to use when rendering. size specifies
        // >> the number of components per attribute and must be 1, 2, 3, 4, or GL_BGRA. type specifies
        // >> the data type of each component, and stride specifies the byte stride from one attribute
        // >> to the next, allowing vertices and attributes to be packed into a single array or stored
        // >> in separate arrays.
        // Tell OpenGL where to find the vertex position data (inside the VBO).
//        glVertexAttribPointer(VERTEX_POSITION, 2, GL_DOUBLE, false, 0, 0);
        // Tell OpenGL where to find the vertex colour data (inside the VBO).
//        glVertexAttribPointer(VERTEX_COLOUR, 3, GL_DOUBLE, false, 0, 8 * 4 * 2);
        


//        GL11.glMatrixMode(GL11.GL_MODELVIEW);
//        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        

       
//        glClearColor(8.0f, 0.0f, 0.0f, 0.0f);
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( glfwWindowShouldClose(_window) == GL_FALSE ) {
        	Scene scene = Scene.getCurrent();
        	Camera camera = pointOfView().camera();
             
            if (this.delegate != null) 
        		this.delegate.updateBeforeScene();
            
        	scene.updateSceneLogic();
        	
        	glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            camera.perspective(this);
            
             
        	
            glClearColor(0.3f, 0.3f, 0.3f, 0.3f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            glMatrixMode(GL_MODELVIEW);
            glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
            GL11.glLoadIdentity();  
            
//            camera.look();
        	scene.renderScene(camera);
        	
        	glfwSwapBuffers(_window);

        	glMatrixMode(GL_PROJECTION);
        	 // swap the color buffers
        	 if (this.delegate != null) 
         		this.delegate.updateAfterScene();
        	
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            this.didCauseEvent(END_OF_GAMELOOP);
        }
    }
 
    private RenderDelegate delegate = null;
    
	

	public boolean setPointOfView(Node pointOfView) {
		if (pointOfView.camera() == null) {
			throw new IllegalArgumentException("PointOfView musy have a camera != NULL");
		} else if (_pointOfView == pointOfView) 
			return false;
		else if (_pointOfView != null)
			_pointOfView.camera().stopListening();
		
		_pointOfView = pointOfView;
		_pointOfView.camera().startListening();
		return true;
	}

	public RenderDelegate getDelegate() {
		return this.delegate;
	}

	@Override
	public void setDelegate(RenderDelegate delegate) {
		this.delegate = delegate;
	}

	
	

	@Override
	public long window() {
		// TODO Auto-generated method stub
		return _window;
	}

	@Override
	public GLFWErrorCallback errorCallback() {
		// TODO Auto-generated method stub
		return _errorCallback;
	}

	@Override
	public GLFWKeyCallback keyCallback() {
		// TODO Auto-generated method stub
		return _keyCallback;
	}

	public int width() {
		return _width;
	}

	public void setWidth(int width) {
		this._width = width;
	}

	public int height() {
		return _height;
	}

	public void setHeight(int height) {
		this._height = height;
	}

	@Override
	public Node pointOfView() {
		if (_pointOfView != null || this.setPointOfView(Node.getCurrent()))
			return _pointOfView;
		Bugger.logAndPrint("ERROR: Could Not Set _pointOfView", true);
		return null;
	}
 
}