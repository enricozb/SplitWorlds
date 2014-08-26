import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.Rectangle; 
import fisica.*; 
import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SplitWorlds extends PApplet {



//import Minim library


final String SPIKE = "Spike";
final String DOORBUTTON = "DoorButton";
final String MOVINGPLATFORM ="mPlatform";

Minim minim;
//to make it play song files
AudioPlayer song;

FWorld world;

Man man;
Man wman;

ArrayList<GameObject> gos = new ArrayList<GameObject>();
ArrayList<TextObject> tos = new ArrayList<TextObject>();

int level;
<<<<<<< HEAD
int STARTING_LEVEL = 10;
=======
int STARTING_LEVEL = 8;
>>>>>>> FETCH_HEAD
int MAX_LEVELS = 20;

int[][] colors = new int[MAX_LEVELS][5];

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
	initMinim();
	initColors();
	state = LAUNCHER;
	transitionTime = 0.0f;
<<<<<<< HEAD
	level = 8;
	reader = createReader("level" + level + ".txt");
=======
>>>>>>> origin/master
	reader = createReader("level" + level + ".txt");
	level = STARTING_LEVEL - 1; //Adjust for launcher appearance
	drawLauncher();

}
public void draw() 
{

	background(colors[level][0]);
	if(state == LAUNCHER)
	{
		upDrawObjects();
		drawLauncherText();
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
		if(level == STARTING_LEVEL - 1)
			drawLauncherText();
		continueTransition();
	}
}

public void initMinim()
{
	minim = new Minim(this);
	song = minim.loadFile("bgs.wav");
	song.play();
	song.loop();
}

public void initColors()
{
	for(int i = 0; i < MAX_LEVELS; i++)
	{
		colors[i] = new int[] {color(42,54,59),color(255,132,124),color(254,206,168),color(232,74,95),color(153,184,152)};
	}
}

public void clearWorld()
{
	man = null;
	wman = null;
	world.clear();
	world.setEdges(colors[level][1]);
	gos.clear();
	tos.clear();
}

public void continueTransition()
{
	float a = map(transitionTime, 0, MAX_TRANSITION, 0, width * 2);
	pushStyle();
	noStroke();
	fill(colors[level + 1][0]);
	rect(transitionVector.x, transitionVector.y, a, a);
	popStyle();
	transitionTime += .01f;

	if(transitionTime >= MAX_TRANSITION)
	{
		level++;
		state = PLAYING;
		updateLevel();
	}
}

public void initTransition(FBody a, FBody b)
{
	transitionTime = 0;
	state = TRANSITION;
	transitionVector = new PVector((a.getX() + b.getX())/2, (a.getY() + b.getY())/2);
}

public void drawLauncherText()
{
	//text("PLAY",lerp(0,width,.25),height/2);
	text("PLAY",lerp(0,width,.5f),height/2);
	//text("HELP",lerp(0,width,.75),height/2);
}

public void drawLauncher()
{
	Platform ptemp;
	man = new Man(width/2,lerp(0,height,.75f) - 20, 20, 20);
	man.box.setFillColor(colors[level][4]);
	//ptemp = new Platform(lerp(0,width,.25),height/2,100,50,true);
	//ptemp.box.setFillColor(colors[level][1]);
	ptemp = new Platform(lerp(0,width,.5f),height/2,100,50,true);
	ptemp.box.setName("PLAY");
	ptemp.box.setFillColor(colors[level][1]);
	//ptemp = new Platform(lerp(0,width,.75),height/2,100,50,true);
	//ptemp.box.setFillColor(colors[level][1]);
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
	}
}

public void initFisicaWorld()
{
	Fisica.init(this);
	world = new FWorld();
	world.setGrabbable(true);
	world.setGravity(0, 1e3f);
	clearWorld();
}

//Play Sounds

public void playDeathSound()
{
	AudioPlayer sound;
	sound = minim.loadFile("die.wav");
	sound.play();
}

public void playClickSound()
{
	AudioPlayer sound;
	sound = minim.loadFile("click.mp3");
	sound.play();
}

//Key press events, simultaneous key presses working.

boolean rPressed = false;
boolean lPressed = false;
boolean uPressed = false;
boolean dPressed = false;

public void restartLevel()
{
	if(state != PLAYING)
		return;
	updateLevel();
}

public void keyPressed()
{
	if(key == CODED)
	{
		if(keyCode == RIGHT) rPressed = true;
		if(keyCode == LEFT)  lPressed = true;
		if(keyCode == UP)    uPressed = true;
		if(keyCode == DOWN)  dPressed = true;
	}
	else if(key == 'r')
	{
		println("DOING");
		restartLevel();
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
			if(go.box.getTouching().size() > 0)
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
	for(TextObject to : tos)
	{
		to.draw();
	}
	world.draw();
}

public void updateLevel()
{
	reader = createReader("level" + level + ".txt");
	clearWorld();
	drawLevel();
// Format : ClassName xpos ypos sx sy
}
public void mouseClicked() {
	for(GameObject go : gos) {
		if(go instanceof Door) {
			Door d = (Door) go;
			print("Door " + d.door.box.getX() + " " + d.door.box.getY() + " " + d.door.box.getWidth() + " " + d.door.box.getHeight() + " " + d.door.xoff +  " " + d.door.yoff + " " + d.door.speed + " ");
			println(go.box.getX() + " " + go.box.getY() + " " + go.box.getWidth() + " " + go.box.getHeight());
		}
		else
			println(go.getClass().getName().replace("SplitWorlds$", "") + " " + go.box.getX() + " " + go.box.getY() + " " + go.box.getWidth() + " " + go.box.getHeight());
	}

	println( "Man" + " " + man.box.getX() + " " + man.box.getY() + " " + man.box.getWidth() + " " + man.box.getHeight());
	try {
		println("Woman" + " " + wman.box.getX() + " " + wman.box.getY() + " " + wman.box.getWidth() + " " + wman.box.getHeight());
	}
	catch(NullPointerException e)
	{}

	println("END");
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
					gos.add(new Platform(PApplet.parseInt(ch[1]),PApplet.parseInt(ch[2]),PApplet.parseInt(ch[3]),PApplet.parseInt(ch[4]), PApplet.parseBoolean(ch[5])));
				else if(ch[0].equals("Spikes"))
					gos.add(new Spikes(PApplet.parseInt(ch[1]),PApplet.parseInt(ch[2]),PApplet.parseInt(ch[3]),PApplet.parseInt(ch[4])));
				else if(ch[0].equals("Moving"))
					gos.add(new MovingPlatform(PApplet.parseInt(ch[1]),PApplet.parseInt(ch[2]),PApplet.parseInt(ch[3]),PApplet.parseInt(ch[4]),PApplet.parseInt(ch[5]),PApplet.parseInt(ch[6]),PApplet.parseInt(ch[7])));
				else if(ch[0].equals("Man"))
					man = new Man(PApplet.parseInt(ch[1]),PApplet.parseInt(ch[2]),PApplet.parseInt(ch[3]),PApplet.parseInt(ch[4]));
				else if(ch[0].equals("Woman"))
					wman = new Man(PApplet.parseInt(ch[1]),PApplet.parseInt(ch[2]),PApplet.parseInt(ch[3]),PApplet.parseInt(ch[4]));
				else if(ch[0].equals("Text"))
					tos.add(new TextObject(PApplet.parseInt(ch[1]), PApplet.parseInt(ch[2]), ch[3]));
				else if(ch[0].equals("Door"))
					gos.add(new Door(PApplet.parseFloat(ch[1]),PApplet.parseFloat(ch[2]),PApplet.parseFloat(ch[3]),PApplet.parseFloat(ch[4]),PApplet.parseFloat(ch[5]),PApplet.parseFloat(ch[6]),PApplet.parseFloat(ch[7]),PApplet.parseFloat(ch[8]),PApplet.parseFloat(ch[9]),PApplet.parseFloat(ch[10]),PApplet.parseFloat(ch[11])));

			} catch(NullPointerException e ) {
				System.exit(0);
			} catch (IOException e){
				System.exit(0);
			}
		}
	}
	while(line != null);
	if(man == null)
	{
		System.exit(0);
	}
	man.box.setFillColor(colors[level][4]);
	wman.box.setFillColor(colors[level][3]);
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
		box.setFillColor(colors[level][1]);
		world.add(box);
	}
}

class TextObject
{
	String message;
	float x;
	float y;

	TextObject(float x, float y, String message)
	{
		this.message = message.replace("_"," ");
		this.x = x;
		this.y = y;
	}

	public void draw()
	{
		text(message, x, y);
	}

};

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
		if(!active)
			playClickSound();
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
		box.setFillColor(color(255,255,255));
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
		playDeathSound();
		box.setPosition(ix,iy);
		box.setVelocity(0,0);
	}

	boolean lastJump = false;
	int jumpCallCount = 0;
	final int JUMP_CALL_COUNT_MAX = 40;
	public void move(int sign)
	{	
		//Made to prevent stride jumps
		if(lastJump)
		{
			if(jumpCallCount >= JUMP_CALL_COUNT_MAX)
			{
				jumpCallCount = 0;
				lastJump = false;
			}
			jumpCallCount++;
		}
		ArrayList<FBody> temp = box.getTouching();
		for(FBody fb : temp)
		{
			if(uPressed) 
			{
				if(fb.getY() > box.getY() && !fb.isSensor() && !lastJump) //FIX FOR CERTAIN CASES
				{
					lastJump = true;
					box.addImpulse(0, -500);
					break;
				}	
			}
			else
			{
				lastJump = false;
				jumpCallCount = 0;
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
