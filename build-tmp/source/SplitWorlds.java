import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.Rectangle; 
import fisica.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SplitWorlds extends PApplet {




FWorld world;

Platform divider;

Man man;
Man wman;


float level;
boolean isLevelLoaded;

BufferedReader reader;

public void setup() 
{
	size(1200,800,OPENGL);
	smooth(8);

	rectMode(CENTER);
	initFisicaWorld();

	reader = createReader("level00.txt");
	level = 0;
	drawLevel();
}

public void draw() 
{
	background(0);
	upDrawObjects();
}

public void initFisicaWorld()
{
	Fisica.init(this);
	world = new FWorld();
	world.setGrabbable(false);
	world.setEdges();
	world.setGravity(0,1e3f);

	divider = new Platform(width/2,height/2,20,height,true); //Remove later
	man = new Man(width/4,height/2, 30, 30);
	wman = new Man(3 * width/4,height/2, 30, 30);
}

//Key press events, simultaneous key presses working.

boolean rPressed = false;
boolean lPressed = false;
boolean uPressed = false;
boolean dPressed = false;

public void keyPressed()
{
	if(key == CODED)
	{
		if(keyCode == RIGHT) rPressed = true;
		if(keyCode == LEFT)  lPressed = true;
		if(keyCode == UP)    uPressed = true;
		if(keyCode == DOWN)  dPressed = true;
	}
}

public void keyReleased()
{
	if(key == CODED)
	{
		if(keyCode == RIGHT) rPressed = false;
		if(keyCode == LEFT)  lPressed = false;
		if(keyCode == UP)    uPressed = false;
		if(keyCode == DOWN)  dPressed = false;
	}
}

public void upDrawObjects()
{
	man.move();
	wman.move();
	world.step();
	world.draw();
}

// Format : ClassName xpos ypos sx sy
public void drawLevel()
{
	String line = null;
	do {
		if(isLevelLoaded != true) { 
			try {
					line = reader.readLine();
					String[] ch = split(line, " ");

					if(ch[0].equals("Platform"))
							new Platform(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4]), PApplet.parseBoolean(ch[5]));
					if(ch[0].equals("Man"))
							new Man(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4]));
					if(ch[0].equals("Woman"))
							new Man(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4]));
					// if(ch[0].equals("Exit"))
					// 		new Exit(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]));
			} catch(IOException e) {
				e.printStackTrace();
				line = null;
			} catch(NullPointerException e) {
				break;
			}
		}
	}
	while(line != null); 	
}


//**********Classes***********

final PVector G = new PVector(0,1);
final PVector UP_VECTOR = new PVector(0,-5);

abstract class GameObject 
{
	FBox box;

 	GameObject(float x, float y, float sx, float sy)
 	{
		box = new FBox(sx,sy);
		box.setPosition(x,y);
		world.add(box);
 	}
}

class Platform extends GameObject
{
	Platform(float x, float y, float sx, float sy, boolean isStatic)
	{
		super(x,y,sx,sy);
		this.box.setStatic(isStatic);
	}
}

class Man extends GameObject
{
	Man(float x, float y, float sx, float sy)
	{
		super(x, y, sx, sy);
	}

	public void move()
	{
		if(uPressed) this.box.addImpulse(0,-1e3f);
		if(rPressed) this.box.addForce(1e4f,0);
		if(lPressed) this.box.addForce(-1e4f,0);
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SplitWorlds" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
