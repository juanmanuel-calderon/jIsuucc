package com.jmc.jisuucc.render.impl;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Start;

import com.jmc.jisuucc.render.api.Renderer;
import com.jmc.jisuucc.render.api.Texture;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.TextureIO;

@Component
public class RendererImpl implements Renderer {
	
	static GLProfile GL_PROFILE = GLProfile.getDefault();
	private Queue<DrawingElement> drawingQueue = new ConcurrentLinkedQueue<>();
	private Map<Integer, com.jogamp.opengl.util.texture.Texture> textures = new HashMap<>();
	
	private class DrawingElement {
		final Texture tex;
		final int x;
		final int y;
		
		DrawingElement(Texture tex, int x, int y) {
			this.tex = tex;
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "DrawingElement [tex=" + tex.id() + ", x=" + x + ", y=" + y + "]";
		}
		
	}
	
	@Start
	public void start() {
		new Window();
   	}
	
	private class Window extends JFrame implements GLEventListener {
		private static final long serialVersionUID = -1050882102096410964L;
		private final int WIDTH = 1024;
		private final int HEIGHT = 768;
		private final String shadersFilepath = "assets" + File.separator + "shaders" + File.separator; 
		
		private GLCanvas canvas;
		private GLSLProgramObject programObject; 
		private int textureUnLoc;
		
		private int[] vertexBufferObject = new int[1]; 
	    private int[] vertexArrayObject = new int[1]; 
	    private float[] vertexData = new float[] { 
	    				0.25f, 0.25f, 0.75f, 1.0f, 
	    				0.25f, -0.25f, 0.75f, 1.0f, 
	    				-0.25f, -0.25f, 0.75f, 1.0f, 
	    				-0.25f, 0.25f, 0.75f, 1.0f, 
	    				1.0f, 1.0f, 
	    				1.0f, 0.0f, 
	    				0.0f, 0.0f, 
	    				0.0f, 1.0f}; 

		Window() {
			init();
		}
		
		private void init() {
			GLCapabilities capabilities = new GLCapabilities(GL_PROFILE);
			canvas = new GLCanvas(capabilities);
			canvas.setSize(WIDTH, HEIGHT);
			canvas.addGLEventListener(this);
			
			setTitle("Map Test");
			getContentPane().add(canvas);
			
			setSize(WIDTH, HEIGHT);
			setLocationRelativeTo(null);
			//setUndecorated(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			canvas.requestFocusInWindow();
		}

		@Override
		public void display(GLAutoDrawable d) {
			
	        GL3 gl3 = d.getGL().getGL3(); 

	        gl3.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
	        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT); 

	        programObject.bind(gl3); 
	        { 
	            gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertexBufferObject[0]); 

	            gl3.glEnableVertexAttribArray(0); 
	            gl3.glEnableVertexAttribArray(1); 
	            { 
	                gl3.glActiveTexture(GL3.GL_TEXTURE0);
	                DrawingElement[] des = drawingQueue.toArray(new DrawingElement[0]);
	                for(int i = 0; i < des.length; i++) {
	                	DrawingElement de = des[i];
	                	if(!textures.containsKey(de.tex.id())) {
	                		textures.put(de.tex.id(), TextureIO.newTexture(de.tex.texture()));
	                	}
	                	
	                	com.jogamp.opengl.util.texture.Texture texture = textures.get(de.tex.id());
		                texture.enable(gl3); 
		                texture.bind(gl3); 
		                gl3.glUniform1i(textureUnLoc, 0); 
		                
		                gl3.glVertexAttribPointer(0, 4, GL3.GL_FLOAT, false, 0, 0); 
		                gl3.glVertexAttribPointer(1, 2, GL3.GL_FLOAT, false, 0, 4 * 4 * 4); 
	
		                gl3.glDrawArrays(GL3.GL_QUADS, 0, 4); 
		                
		                texture.disable(gl3);
	                }
	            } 
	            gl3.glDisableVertexAttribArray(0); 
	            gl3.glDisableVertexAttribArray(1); 
	        } 
	        programObject.unbind(gl3); 

	        d.swapBuffers(); 
		}

		@Override
		public void dispose(GLAutoDrawable d) { }

		@Override
		public void init(GLAutoDrawable d) { 
			GL3 gl3 = d.getGL().getGL3();
			canvas.setAutoSwapBufferMode(false);
			try {
				buildShaders(gl3);
			} catch(Exception e) {
				e.printStackTrace();
			}

	        initializeVertexBuffer(gl3); 

	        gl3.glGenVertexArrays(1, IntBuffer.wrap(vertexArrayObject)); 
	        gl3.glBindVertexArray(vertexArrayObject[0]); 

	        gl3.glEnable(GL3.GL_CULL_FACE); 
	        gl3.glCullFace(GL3.GL_BACK); 
	        gl3.glFrontFace(GL3.GL_CW);
		}

		@Override
		public void reshape(GLAutoDrawable d, int x, int y, int w, int h) { }
		
		private void buildShaders(GL3 gl3) throws Exception { 
	        programObject = new GLSLProgramObject(gl3); 
	        programObject.attachVertexShader(gl3, shadersFilepath + "OrthoWithOffset_VS.glsl"); 
	        programObject.attachFragmentShader(gl3, shadersFilepath + "StandardColor_FS.glsl"); 
	        programObject.initializeProgram(gl3, true); 

	        textureUnLoc = gl3.glGetUniformLocation(programObject.getProgId(), "myTexture"); 
	    }
		
		private void initializeVertexBuffer(GL3 gl3) { 
	        gl3.glGenBuffers(1, IntBuffer.wrap(vertexBufferObject)); 

	        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vertexBufferObject[0]); 
	        { 
	            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(vertexData); 

	            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, vertexData.length * 4, buffer, GL3.GL_STATIC_DRAW); 
	        } 
	        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0); 
	    }
	}
	
	@Override
	public void drawImage(Texture texture, int x, int y) {
		drawingQueue.add(new DrawingElement(texture, x, y));
	}

}
