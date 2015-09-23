package click.rmx.engine;

import static org.lwjgl.opengl.GL11.*;

import click.rmx.engine.math.Vector3;
import click.rmx.engine.math.Vector4;

public class LightSource extends ANodeComponent {
	
	public static final LightSource defaultLightSource = new LightSource();
	
	Vector4 specular = new Vector4(0.9f,0.9f,0.9f,1);
	Vector4 ambient = new Vector4(0.1f,0.1f,0.1f,0.1f);
	Vector4 diffuse = new Vector4(0.9f,0.9f,0.9f,0.3f);
	Vector4 material = new Vector4(0.9f,0.9f,0.9f,0.3f);
	public void shine() {
		
		glShadeModel(GL_SMOOTH);
		glMaterialfv(GL_FRONT, GL_SPECULAR, material.getBuffer());				// sets specular material color
		glMaterialf(GL_FRONT, GL_SHININESS, 50.0f);					// sets shininess
		
		Vector3 position = this.transform().position();
		glLightfv(GL_LIGHT0, GL_POSITION, position.getBuffer());				// sets light position
		glLightfv(GL_LIGHT0, GL_SPECULAR, specular.getBuffer());				// sets specular light to white
		glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse.getBuffer());					// sets diffuse light to white
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambient.getBuffer());		// global ambient light 
		
		glEnable(GL_LIGHTING);										// enables lighting
		glEnable(GL_LIGHT0);										// enables light0
		
		glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
	}
}
