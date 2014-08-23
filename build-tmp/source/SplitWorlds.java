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

//ArrayList<GameObject> gos = new ArrayList<GameObject>();

int level;
boolean isLevelLoaded;

BufferedReader reader;

public void setup() 
{
	size(800,800,OPENGL);
	Fisica.init(this);

	smooth(8);

	rectMode(CENTER);
	noStroke();

	world = new FWorld();
	world.setGrabbable(false);
	world.setEdges();
	world.setGravity(0,1e3f);

	divider = new Platform(width/2,height/2,20,height,true);

	man = new Man(width/4,height/2,40,40);
	wman = new Man(3 * width/4,height/2,40,40);
	//reader = createReader("level00.txt");
	//level = 0;
	//drawLevel();

}

public void draw() 
{
	background(0);
	upDrawObjects();
}

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

/*
void drawLevel()
{
	String line;
	do {
		try{
	if(isLevelLoaded != true) { 
		String line;
		do {
			line = reader.readLine();
		}
		String[] ch = split(line, " ");
		
		for(Sting go: ch) 
		{
			switch (go) 
			{
				case "Platform":
					gos.add(new Platform());
				case "ManW":
					gos.add(new Man());
			}
		}
	}
	while(line != null);
		while(line != null) {
			try{
				line = reader.readLine();
			} catch(IOException e) {
				e.printStackTrace();
				line = null;
			}
			String[] ch = split(line, " ");

				switch (ch[0]) 
				{
					case "Platform":
						gos.add(new Platform(ch[1],ch[2],ch[3],ch[4]));
						break;
					case "WMan":
						gos.add(new Man(ch[1],ch[2],ch[3],ch[4]));
						break;
					case "Man":
						gos.add(new Man(ch[1],ch[2],ch[3],ch[4]));
						break;
					case "Door":
						//gos.add(new Door(ch[1],ch[2],ch[3],ch[4])));
						break;
				}	
		
		}
	}	
}
*/


public void drawOverlay()
{
	pushStyle();
	fill(255);
	rect(width/2,height/2,20,height);
	popStyle();
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
		if(uPressed) this.box.addImpulse(0,-1000);
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
