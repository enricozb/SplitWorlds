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




final String SPIKE = "Spike";
final String DOORBUTTON = "DoorButton";
final String MOVINGPLATFORM ="mPlatform";

FWorld world;

Man man;
Man wman;

int currentBackground = color(255);
int newBackground = color(255);
ArrayList<GameObject> gos = new ArrayList<GameObject>();

int level;
boolean isLevelLoaded;

int state;

PVector transitionVector;
float transitionTime;
final float MAX_TRANSITION = 1;

//STATE CONSTANTS
final int LAUNCHER = 0;
final int TRANSITION = 1;
final int PLAYING = 2;

BufferedReader reader;

public void setup() 
{
	size(1200,600);
	textAlign(CENTER,CENTER);
	rectMode(CENTER);
	initFisicaWorld();

	state = LAUNCHER;
	transitionTime = 0.0f;
	reader = createReader("level" + level + ".txt");
	level = 0;
	drawLauncher();
}

public void draw() 
{
	background(currentBackground);
	if(state == LAUNCHER)
	{
		upDrawObjects();
		checkForChoice();
	}
	else if(state == PLAYING)
	{
		upDrawObjects();
		checkForFinish();
	}
	else if(state == TRANSITION)
	{
		upDrawObjects();
		continueTransition();
	}
}

public void initFisicaWorld()
{
	Fisica.init(this);
	world = new FWorld();
	world.setGrabbable(false);
	world.setEdges();
	world.setGravity(0, 1e3f);
}

public void clearWorld()
{
	world.clear();
	world.setEdges();
	gos.clear();
}

public void continueTransition()
{
	float a = map(transitionTime, 0, MAX_TRANSITION, 0, width * 2);
	pushStyle();
	noStroke();
	fill(newBackground);
	rect(transitionVector.x, transitionVector.y, a,a);
	popStyle();
	transitionTime += .01f;

	if(transitionTime >= MAX_TRANSITION)
	{
		currentBackground = newBackground;
		state = PLAYING;
		updateLevel();
	}
}

public void initTransition(FBody a, FBody b)
{
	transitionTime = 0;
	state = TRANSITION;
	transitionVector = new PVector((a.getX() + b.getX())/2, (a.getY() + b.getY())/2);
	newBackground = lerpColor(a.getFillColor(), b.getFillColor(), .5f);
}

public void drawLauncher()
{
	Platform ptemp;
	man = new Man(width/2,lerp(0,height,.75f) - 20, 20, 20);

	ptemp = new Platform(lerp(0,width,.25f),height/2,100,50,true);
	ptemp.box.setName("PLAY");
	ptemp.box.setFill(75,182,192);
	ptemp = new Platform(lerp(0,width,.5f),height/2,100,50,true);
	ptemp.box.setName("HELP");
	ptemp.box.setFill(75,182,192);
	ptemp = new Platform(lerp(0,width,.75f),height/2,100,50,true);
	ptemp.box.setName("ABOUT");
	ptemp.box.setFill(75,182,192);
	new Platform(width/2, lerp(0,height,.75f),width,20,true);
}

//Method to check for user decisions on Launcher options
public void checkForChoice()
{
	ArrayList<FBody> temp = man.box.getTouching();
	for(FBody fb : temp)
	{
		if(fb.getName() == "PLAY")
		{
			state = TRANSITION;
			initTransition(man.box,fb);
		}
		else if(fb.getName() == "HELP")
		{
			state = TRANSITION;
			initTransition(man.box,fb);
		}
		else if(fb.getName() == "ABOUT")
		{
			state = TRANSITION;
			initTransition(man.box,fb);
		}
	}
}

public void checkForFinish()
{
	if(man != null && wman != null && man.box.isTouchingBody(wman.box))
	{
		initTransition(man.box,wman.box);
		level++;
	}
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

public void updateWorld()
{
	for(GameObject go : gos)
	{
		if(go instanceof MovingPlatform)
		{
			((MovingPlatform) go).move();
		}
		if(go instanceof Door)
		{
			((Door) go).move();
		}
		if(go instanceof Button)
		{
			if(man.box.isTouchingBody(go.box) || wman.box.isTouchingBody(go.box))
				((Button) go).activate();
		}
	}
	world.step();
}

public void upDrawObjects()
{
	if(man != null)	 man.move(1);
	if(wman != null) wman.move(-1);
	if(state != TRANSITION)
	{
		updateWorld();
	}
	world.draw();
}

public void updateLevel()
{
	reader = createReader("level" + level + ".txt");
	background(255);
	clearWorld();
	drawLevel();
}

public void drawLevel()
{
	String line = null;
	do 
	{
		if(isLevelLoaded != true ) 
		{ 
			try 
			{
				line = reader.readLine();
				if(line == null)
					break;
				String[] ch = split(line, " ");

				if(ch[0].equals("Platform"))
					gos.add(new Platform(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4]), PApplet.parseBoolean(ch[5])));
				else if(ch[0].equals("Spikes"))
					gos.add(new Spikes(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4])));
				else if(ch[0].equals("Moving"))
					gos.add(new MovingPlatform(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4]),PApplet.parseFloat(ch[5]),PApplet.parseFloat(ch[6]),PApplet.parseFloat(ch[7])));
				else if(ch[0].equals("Man"))
					man = new Man(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4]));
				else if(ch[0].equals("Woman"))
					wman = new Man(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4]));

			} catch(IOException e) 
			{
			}
		}
	}
	while(line != null);
	man.box.setFriction(0);
	wman.box.setFriction(0);
}

//**********Classes***********

abstract class GameObject 
{
	FBox box;
 	GameObject(float x, float y, float sx, float sy)
 	{
		box = new FBox(sx, sy);
		box.setPosition(x, y);
		box.setNoStroke();
		box.setFill(63,63,63);
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

class Spikes extends GameObject
{
	final float X_REPEAT_SIZE = 20;
	FCompound mainBody;

	Spikes(float x, float y, float sx, float sy)
	{
		super(x,y,sx,sy);
		box.setSensor(true);
		box.setNoFill();
		box.setStatic(true);
		mainBody = new FCompound();
		int num = PApplet.parseInt(sx/X_REPEAT_SIZE);
		for(float i = x - X_REPEAT_SIZE*num/2; i <= x + X_REPEAT_SIZE*num/2; i += X_REPEAT_SIZE)
		{
			mainBody.addBody(getTriangle(i,y,X_REPEAT_SIZE,sy));
		}
		mainBody.setName(SPIKE);
		mainBody.setStatic(true);
		world.add(mainBody);
	}

	public FPoly getTriangle(float x, float y, float sx, float sy)
	{
		FPoly ptemp = new FPoly();
		ptemp.vertex(x - sx/2, y + sy/2);
		ptemp.vertex(x + sx/2, y + sy/2);
		ptemp.vertex(x, y - sy/2);
		ptemp.setNoStroke();
		return ptemp;
	}
};

class MovingPlatform extends Platform
{
	float moveTime;
	float speed;
	float xoff;
	float yoff;
	float ix;
	float iy;
	boolean active;

	MovingPlatform(float x, float y, float sx, float sy, float xoff, float yoff, float speed)
	{
		super(x,y,sx,sy,true);
		box.setName(MOVINGPLATFORM);
		moveTime = 0;
		this.speed = speed;
		this.xoff = xoff;
		this.yoff = yoff;
		ix = x;
		iy = y;
		active = true;
	}

	public void move()
	{
		if(!active)
			return;
		box.setPosition(ix + (xoff) * sin(radians(moveTime)), iy + (yoff) * sin(radians(moveTime)));
		moveTime += speed;
	}
};

class Button extends GameObject
{
	boolean active;
	Button(float x, float y, float sx, float sy)
	{
		super(x, y, sx, sy);
		box.setSensor(true);
		box.setStatic(true);
	}
	public void activate()
	{
		active = true;
	}
};

class Door extends Button
{
	MovingPlatform door;
	boolean done;

	Door(float x, float y, float sx, float sy, float xoff, float yoff, float speed, float bx, float by, float bsx, float bsy)
	{
		super(bx, by, bsx, bsy);
		box.setFillColor(color(0,0,255));
		door = new MovingPlatform(x, y, sx, sy, xoff, yoff, speed);
		door.active = true;
	}

	public void move()
	{
		if(done || !active)
			return;
		door.active = true;
		door.move();
		if(door.moveTime >= 90)
		{
			door.active = false;
			done = true;
		}
	}
};

class Man extends GameObject
{
	float ix;
	float iy;

	Man(float x, float y, float sx, float sy)
	{
		super(x, y, sx, sy);
		this.ix = x;
		this.iy = y;
	}

	public void die()
	{
		box.setPosition(ix,iy);
		box.setVelocity(0,0);
	}

	public void move(int sign)
	{	
		ArrayList<FBody> temp = box.getTouching();
		for(FBody fb : temp)
		{
			if(uPressed) 
			{
				if(fb.getY() > box.getY() && !fb.isSensor()) //FIX FOR CERTAIN CASES
				{
					box.addImpulse(0, -250);
					break;
				}	
			}
			if(fb.getName() == SPIKE)
			{
				die();
			}
			if(fb.getName() == DOORBUTTON)
			{
				//UNSUPPORTED
			}
		}
		if(rPressed) box.setVelocity(sign * 150, box.getVelocityY());
		if(lPressed) box.setVelocity(sign *-150, box.getVelocityY());
		if(!lPressed && !rPressed && temp.size() != 0)
			box.setVelocity(0,box.getVelocityY());

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
