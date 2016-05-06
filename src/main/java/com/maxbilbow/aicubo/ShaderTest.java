package com.maxbilbow.aicubo;


import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GLContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author TJ
 *         <p>
 *         This code is an LWJGL implementation of the following C-tutorial, that shows
 *         how to render objects with OpenGL 4.0 (and 3.1):
 *         <p>
 *         http://openglbook.com/the-book/chapter-3-index-buffer-objects-and-primitive-types/
 *         <p>
 *         If you want to render objects in a non-deprecated way, then this code
 *         might be very useful to you, because it shows how to set up shaders,
 *         how to set up buffer objects, vertex arrays, how to link buffers to the shader,
 *         etc.
 */
public class ShaderTest
{

  public static final int DISPLAY_HEIGHT = 600; // window width
  public static final int DISPLAY_WIDTH  = 600; // window height

  public static final int POSITION_INDEX = 0; // index of vertex attribute "in_Position"
  public static final int COLOR_INDEX    = 1; // index of vertex attribute "in_Color"

  public static final int FLOAT_NUM_BYTES; // sizeof(float) in bytes
  public static final int INT_NUM_BYTES; // sizeof(int) in bytes
  public static final int VEC4_BYTES; // sizeof(vec4) in bytes

  static
  {
    FLOAT_NUM_BYTES = Float.SIZE / Byte.SIZE;
    INT_NUM_BYTES = Integer.SIZE / Byte.SIZE;
    VEC4_BYTES = 4 * FLOAT_NUM_BYTES;
  }

  // We will create the following OpenGL objects in the code below:
  int VertexShaderId,
          FragmentShaderId,
          ProgramId,
          VaoId,
          VboId,
          IndexBufferId;
  private long window;

  public ShaderTest()
  {
  }

  public void create() throws IOException
  {

//        DisplayMode d = new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, FragmentShaderId, FragmentShaderId);
//        Display.setDisplayMode(d);
//        Display.setFullscreen(false);
//        Display.setTitle("Shaders and buffers in LWJGL");
//        Display.create();
// 
//        //Keyboard
//        Keyboard.create();
// 
//        //Mouse
//        Mouse.setGrabbed(false);
//        Mouse.create();
// 
    //OpenGL
    initGL();
    resizeGL();
  }

  void Cleanup()
  {
    DestroyShaders();
    DestroyVBO();
  }

  void CreateVBO()
  {

    // Create a "vertex array" object.
    // This OpenGL-object will group the "in_Position" and "in_Color"
    // vertex attributes.

    VaoId = glGenVertexArrays();
//        //Util.checkGLError();

    glBindVertexArray(VaoId);
//        //Util.checkGLError();

    //==============================================================
    // Next we create the vertex position and color data that we
    // will pass to the shaders.
    // In the array below, we interleave the position and color data:
    // first vec4 for position, then vec4 for color, vec4 for position, etc.

    float[] VerticesArray = new float[]{
            0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            // Top
            -0.2f, 0.8f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            0.2f, 0.8f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.8f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            // Bottom
            -0.2f, -0.8f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.2f, -0.8f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, -0.8f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            // Left
            -0.8f, -0.2f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            -0.8f, 0.2f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -0.8f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            // Right
            0.8f, -0.2f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.8f, 0.2f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            0.8f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f
    };

    // Now we put this data in a FloatBuffer, that will be passed to LWJGL.
    // Importantly: use the class BufferUtils, because then your FloatBuffer
    // will be "direct" AND the byte ordering will be native (=required).
    // Using native byte ordering is something that you might easily forget!

    FloatBuffer Vertices = BufferUtils.createFloatBuffer(VerticesArray.length);
    Vertices.put(VerticesArray);
    Vertices.rewind(); // rewind, otherwise LWJGL thinks our buffer is empty

    // We now create a new OpenGL object: a "buffer object" to hold all
    // vertex data from above.
    // We bind the new object to the target "GL_ARRAY_BUFFER" so that we can
    // transfer our above data to OpenGL.
    {
      VboId = glGenBuffers();
//            //Util.checkGLError();

      glBindBuffer(GL_ARRAY_BUFFER, VboId);
//            //Util.checkGLError();

      // Transfer all data to the OpenGL-object, saying that it will
      // be used for "static drawing", meaning we will not modify
      // our vertex data in the future (so OpenGL can do optimizations).
      glBufferData(GL_ARRAY_BUFFER, Vertices, GL_STATIC_DRAW);
//            //Util.checkGLError();
    }

    //==============================================================
    // Now we do something similar as above, but now for the indices,
    // and we use the OpenGL target GL_ELEMENT_ARRAY_BUFFER instead
    // of GL_ARRAY_BUFFER.

    int IndicesArray[] = {
            // Top
            0, 1, 3,
            0, 3, 2,
            3, 1, 4,
            3, 4, 2,
            // Bottom
            0, 5, 7,
            0, 7, 6,
            7, 5, 8,
            7, 8, 6,
            // Left
            0, 9, 11,
            0, 11, 10,
            11, 9, 12,
            11, 12, 10,
            // Right
            0, 13, 15,
            0, 15, 14,
            15, 13, 16,
            15, 16, 14
    };

    IntBuffer Indices = BufferUtils.createIntBuffer(IndicesArray.length);
    Indices.put(IndicesArray);
    Indices.rewind();

    IndexBufferId = glGenBuffers();
//        //Util.checkGLError();

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, IndexBufferId);
//        //Util.checkGLError();

    glBufferData(GL_ELEMENT_ARRAY_BUFFER, Indices, GL_STATIC_DRAW);
//        //Util.checkGLError();

    //==============================================================
    // Now we tell OpenGL that we will use POSITION_INDEX and COLOR_INDEX
    // to communicate respectively the vertex positions and vertex colors
    // to our shaders.
    {
      // First we enable the indices. This will affect the vertex
      // array object from above.
      glEnableVertexAttribArray(POSITION_INDEX);
//            //Util.checkGLError();

      glEnableVertexAttribArray(COLOR_INDEX);
//            //Util.checkGLError();

      // Then we tell OpenGL how it should read the GL_ARRAY_BUFFER
      // (to which we have bound our vertex data, see above).

      // The position data starts at the beginning of the vertex data
      glVertexAttribPointer(POSITION_INDEX, 4, GL_FLOAT, false,
                            2 * VEC4_BYTES, 0
      );
//            //Util.checkGLError();

      // The color data starts after the first 4 floats of position data
      glVertexAttribPointer(COLOR_INDEX, 4, GL_FLOAT, false,
                            2 * VEC4_BYTES, VEC4_BYTES
      );
//            //Util.checkGLError();
    }

    // Just to be VERY clean, we will unbind the vertex attribute object
    // and only bind it when we render. This way we cannot accidentally modify
    // it anymore.
    glBindVertexArray(0);
//        //Util.checkGLError();

    // Only after the vertex array is disabled, we unbind the buffers
    // to the GL_ARRAY_BUFFER and GL_ELEMENT_ARRAY_BUFFER targets, because
    // otherwise the vertex array object would become invalid again.
    glBindBuffer(GL_ARRAY_BUFFER, 0);
//        //Util.checkGLError();

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
//        //Util.checkGLError();
  }

  void DestroyVBO()
  {

    glDisableVertexAttribArray(POSITION_INDEX);
    //Util.checkGLError();

    glDisableVertexAttribArray(COLOR_INDEX);
    //Util.checkGLError();

    glBindBuffer(GL_ARRAY_BUFFER, 0);
    //Util.checkGLError();

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    //Util.checkGLError();

    glDeleteBuffers(IndexBufferId);
    //Util.checkGLError();

    glDeleteBuffers(VboId);
    //Util.checkGLError();

    glBindVertexArray(0);
    //Util.checkGLError();

    glDeleteVertexArrays(VaoId);
    //Util.checkGLError();
  }

  void CreateShaders() throws IOException
  {

    // Put the shaders "chap2.vert" and "chap2.frag" in the working
    // directory ...

    //==========================================================
    // Load and compile vertex shader

    String VertexShader = readShaderSourceCode("chap2.vert");

    VertexShaderId = glCreateShader(GL_VERTEX_SHADER);
    //Util.checkGLError();

    glShaderSource(VertexShaderId, VertexShader);
    //Util.checkGLError();

    glCompileShader(VertexShaderId);
    //Util.checkGLError();

    // Print possible compile errors
    System.out.println("Vertex shader compilation:");
//        printShaderLog(VertexShaderId);
    //Util.checkGLError();

    //==========================================================
    // Load and compile fragment shader

    String FragmentShader = readShaderSourceCode("chap2.frag");

    FragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
    //Util.checkGLError();

    glShaderSource(FragmentShaderId, FragmentShader);
    //Util.checkGLError();

    glCompileShader(FragmentShaderId);
    //Util.checkGLError();

    // Print possible compile errors
    System.out.println("Fragment shader compilation:");
//        printShaderLog(FragmentShaderId);

    //==========================================================
    // Link shader program

    ProgramId = glCreateProgram();
    //Util.checkGLError();

    // Attach vertex shader
    glAttachShader(ProgramId, VertexShaderId);
    //Util.checkGLError();

    // Attach fragment shader
    glAttachShader(ProgramId, FragmentShaderId);
    //Util.checkGLError();

    // We tell the program how the vertex attribute indices will map
    // to named "in" variables in the vertex shader. This must be done
    // before compiling.
    glBindAttribLocation(ProgramId, POSITION_INDEX, "in_Position");
    glBindAttribLocation(ProgramId, COLOR_INDEX, "in_Color");

    glLinkProgram(ProgramId);
    //Util.checkGLError();

    // Print possible compile errors
    System.out.println("Program linking:");
//        printProgramLog(ProgramId);

    //Util.checkGLError();
    glUseProgram(ProgramId);
    //Util.checkGLError();

    //Util.checkGLError();
  }

  /**
   * Read shader source code from a file.
   *
   * @param file
   * @return
   * @throws IOException
   */
  public static String readShaderSourceCode(String file) throws IOException
  {
    String code = "";
    String line;

    BufferedReader reader = new BufferedReader(new FileReader(file));
    while ((line = reader.readLine()) != null)
    {
      code += line + "\n";
    }

    return code;
  }

//    /**
//     * Print log of shader object.
//     *
//     * @param id
//     */
//    public static void printShaderLog(int id) {
//        int logLength = glGetShader(id, GL_INFO_LOG_LENGTH);
//        //Util.checkGLError();
// 
//        System.out.println("  Log (length " + logLength + " chars)");
//        String log = glGetShaderInfoLog(id, logLength);
//        //Util.checkGLError();
//        for (String line : log.split("\n")) {
//            System.out.println("  " + line);
//        }
//        System.out.println("");
//    }
// 
//    /**
//     * Print log of program object
//     *
//     * @param programId
//     */
//    public static void printProgramLog(int programId) {
//        int logLength = glGetProgram(programId, GL_INFO_LOG_LENGTH);
//        //Util.checkGLError();
// 
//        System.out.println("  Log (length " + logLength + " chars)");
//        String log = glGetProgramInfoLog(programId, logLength);
//        //Util.checkGLError();
//        for (String line : log.split("\n")) {
//            System.out.println("  " + line);
//        }
//        System.out.println("");
//    }

  void DestroyShaders()
  {

    glUseProgram(0);

    glDetachShader(ProgramId, VertexShaderId);
    glDetachShader(ProgramId, FragmentShaderId);

    glDeleteShader(FragmentShaderId);
    glDeleteShader(VertexShaderId);

    glDeleteProgram(ProgramId);

    //Util.checkGLError();
  }

  public void destroy()
  {
    Cleanup();
//        Mouse.destroy();
//        Keyboard.destroy();
//        Display.destroy();
  }

  public void initGL() throws IOException
  {

    GLContext.createFromCurrent();
    glClearColor(0, 0, 0, 0);
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);

    CreateShaders();
    CreateVBO();
  }

  public void processKeyboard()
  {
  }

  public void processMouse()
  {
  }

  public void render()
  {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    //Util.checkGLError();

    // Use the vertex array object. This will automatically cause OpenGL
    // to read our vertex position/color data and the indices data.
    glBindVertexArray(VaoId);

    // The glDrawElements function will the vertex attribute object
    // that we have bound.
    glDrawElements(
            GL_TRIANGLES,
            48, // = use all 48 indices from the indices buffer
            GL_UNSIGNED_INT,
            0
    );
    //Util.checkGLError();
  }

  public void resizeGL()
  {
    glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
  }

  public void run()
  {
    window = glfwCreateWindow(600, 600, "Hello World!", NULL, NULL);
    while (glfwWindowShouldClose(window) == GL_FALSE)
    {

      processKeyboard();
      processMouse();
      update();
      render();

    }
  }

  public void update()
  {
  }

  public static void start()
  {
    ShaderTest main = null;
    try
    {
      main = new ShaderTest();
      main.create();
      main.run();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      if (main != null)
      {
        main.destroy();
      }
    }
  }
}
