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

Exit mExit;
Exit wExit;

//ArrayList<GameObject> gos = new ArrayList<GameObject>();

int level;
boolean isLevelLoaded;

BufferedReader reader;

public void setup() 
{
	size(1200, 800.0f);

	rectMode(CENTER);
	initFisicaWorld();

	//reader = createReader("level00.txt");
	//level = 0;
	//drawLevel();
}

public void draw() 
{
	background(0);
	upDrawObjects();
	if(checkForFinish())
	{
		//pass
	}
}

public boolean checkForFinish()
{
	return man.box.isTouchingBody(mExit.box) && wman.box.isTouchingBody(wExit.box);
}

public void initFisicaWorld()
{
	Fisica.init(this);
	world = new FWorld();
	world.setGrabbable(false);
	world.setEdges();
	world.setGravity(0, 1e3f);

	divider = new Platform(width/2,height/2,20,height,true); //Remove later
	//new Platform(width/2,height,width,50,true);
	man = new Man(width/4, height/2, 20, 20);
	wman = new Man(3 * width/4, height/2, 20, 20);
	mExit = new Exit(width/4, 700, 20, 20);
	wExit = new Exit(3 * width/4 + 20, 700, 20, 20);
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
/*
void drawLevel()
{
	String line;
	do 
	{
		try
		{
			if(isLevelLoaded != true) 
			{ 
				String line;
				do 
				{
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
		}
	}
	while(line != null);
		while(line != null) 
		{
			try
			{
				line = reader.readLine();
			} catch(IOException e) 
			{
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

//**********Classes***********

final PVector G = new PVector(0,1);
final PVector UP_VECTOR = new PVector(0,-5);

abstract class GameObject 
{
	FBox box;

 	GameObject(float x, float y, float sx, float sy)
 	{
		box = new FBox(sx, sy);
		box.setPosition(x, y);
		world.add(box);
 	}
}

class Platform extends GameObject
{
	Platform(float x, float y, float sx, float sy, boolean isStatic)
	{
		super(x, y, sx, sy);
		this.box.setStatic(isStatic);
	}
};

class Exit extends Platform
{
	Exit(float x, float y, float sx, float sy)
	{
		super(x, y, sx, sy, true);
		box.setSensor(true);
		box.setFillColor(color(255,0,0));
	}
};

class Door extends Platform
{
	FBox buttonBox;
	Door(float x, float y, float bx, float by, float sx, float sy, float bsx, float bsy)
	{
		super(x, y, sx, sy, true);
		buttonBox = new FBox(bsx, bsy);
		buttonBox.setPosition(bx, by);
		buttonBox.setSensor(true);
		buttonBox.setFill(color(0, 255, 0));
		world.add(buttonBox);
	}
};

class Man extends GameObject
{
	Man(float x, float y, float sx, float sy)
	{
		super(x, y, sx, sy);
	}

	public void move()
	{
		if(uPressed) 
		{
			ArrayList<FBody> temp = box.getTouching();
			for(FBody fb : temp)
			{
				if(fb.getY() > box.getY() && !fb.isSensor())
				{
					box.addImpulse(0, -250);
					break;
				}
			}
		}
		if(rPressed) box.setVelocity(100, box.getVelocityY());
		if(lPressed) box.setVelocity(-100, box.getVelocityY());
	}
};
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "SplitWorlds" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
