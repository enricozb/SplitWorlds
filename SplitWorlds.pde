import java.awt.Rectangle;
import fisica.*;

final String SPIKE = "Spike";
final String DOORBUTTON = "DoorButton";
final String MOVINGPLATFORM ="mPlatform";

FWorld world;

Man man;
Man wman;

color currentBackground = color(255);
color newBackground = color(255);
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

void setup() 
{
	size(1200,600);
	textAlign(CENTER,CENTER);
	rectMode(CENTER);
	initFisicaWorld();

	state = LAUNCHER;
	transitionTime = 0.0;
	reader = createReader("level" + level + ".txt");
	level = 0;
	drawLauncher();
}

void draw() 
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

void clearWorld()
{
	world.clear();
	world.setEdges();
}

void continueTransition()
{
	float a = map(transitionTime, 0, MAX_TRANSITION, 0, width * 2);
	pushStyle();
	noStroke();
	fill(newBackground);
	rect(transitionVector.x, transitionVector.y, a,a);
	popStyle();
	transitionTime += .01;

	if(transitionTime >= MAX_TRANSITION)
	{
		currentBackground = newBackground;
		state = PLAYING;
		clearWorld();
		drawLevel();
		updateLevel();
	}
}

void updateLevel()
{
	reader = createReader("level" + level + ".txt");
	background(255);
	initFisicaWorld();
	drawLevel();
}

void initTransition(FBody a, FBody b)
{
	transitionTime = 0;
	state = TRANSITION;
	transitionVector = new PVector((a.getX() + b.getX())/2, (a.getY() + b.getY())/2);
	newBackground = lerpColor(a.getFillColor(), b.getFillColor(), .5);
}

void drawLauncher()
{
	Platform ptemp;
	man = new Man(width/2,lerp(0,height,.75) - 20, 20, 20);

	ptemp = new Platform(lerp(0,width,.25),height/2,100,50,true);
	ptemp.box.setName("PLAY");
	ptemp.box.setFill(75,182,192);
	ptemp = new Platform(lerp(0,width,.5),height/2,100,50,true);
	ptemp.box.setName("HELP");
	ptemp.box.setFill(75,182,192);
	ptemp = new Platform(lerp(0,width,.75),height/2,100,50,true);
	ptemp.box.setName("ABOUT");
	ptemp.box.setFill(75,182,192);
	new Platform(width/2, lerp(0,height,.75),width,20,true);
}

//Method to check for user decisions on Launcher options
void checkForChoice()
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

void checkForFinish()
{
	if(man != null && wman != null && man.box.isTouchingBody(wman.box))
	{
		initTransition(man.box,wman.box);
		level++;
	}
}

void initFisicaWorld()
{
	Fisica.init(this);
	world = new FWorld();
	world.setGrabbable(true);
	world.setEdges();
	world.setGravity(0, 1e3);
}

//Key press events, simultaneous key presses working.

boolean rPressed = false;
boolean lPressed = false;
boolean uPressed = false;
boolean dPressed = false;

void keyPressed()
{
	if(key == CODED)
	{
		if(keyCode == RIGHT) rPressed = true;
		if(keyCode == LEFT)  lPressed = true;
		if(keyCode == UP)    uPressed = true;
		if(keyCode == DOWN)  dPressed = true;
	}
}

void keyReleased()
{
	if(key == CODED)
	{
		if(keyCode == RIGHT) rPressed = false;
		if(keyCode == LEFT)  lPressed = false;
		if(keyCode == UP)    uPressed = false;
		if(keyCode == DOWN)  dPressed = false;
	}
}

void updateWorld()
{
	world.step();
}

void upDrawObjects()
{
	if(man != null)	 man.move(1);
	if(wman != null) wman.move(-1);
	if(state != TRANSITION)
	{
		updateWorld();
	}
	world.draw();
}

// Format : ClassName xpos ypos sx sy

void drawLevel()
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
					new Platform(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]), boolean(ch[5]));
				else if(ch[0].equals("Platform(c)")) {
					float width = abs(float(ch[1]) - float(ch[3]));
					float height = abs(float(ch[2]) - float(ch[4]));
					float x = (float(ch[1]) + float(ch[3])) / 2;
					float y = (float(ch[2]) + float(ch[4])) / 2;
					new Platform(x,y,width,height, boolean(ch[5]));
				}
				else if(ch[0].equals("Spikes(c)")) {
					float width = abs(float(ch[1]) - float(ch[3]));
					float height = abs(float(ch[2]) - float(ch[4]));
					float x = (float(ch[1]) + float(ch[3])) / 2;
					float y = (float(ch[2]) + float(ch[4])) / 2;
					new Spikes(x,y,width,height);
				}
				else if(ch[0].equals("Man"))
					man = new Man(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]));
				else if(ch[0].equals("Woman"))
					wman = new Man(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]));
				else if(ch[0].equals("Spikes"))
					new Spikes(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]));

			} catch(IOException e) 
			{
			}
		}
	}
	while(line != null);	
	man.box.setFriction(0);
	wman.box.setFriction(0);

}
 void mousePressed() {
	
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
		int num = int(sx/X_REPEAT_SIZE);
		for(float i = x - X_REPEAT_SIZE*num/2; i <= x + X_REPEAT_SIZE*num/2; i += X_REPEAT_SIZE)
		{
			mainBody.addBody(getTriangle(i,y,X_REPEAT_SIZE,sy));
		}
		mainBody.setName(SPIKE);
		mainBody.setStatic(true);
		world.add(mainBody);
		
	}

	FPoly getTriangle(float x, float y, float sx, float sy)
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
	MovingPlatform(float x, float y, float sx, float sy, float xoff, float yoff, float speed)
	{
		super(x,y,sx,sy,true);
		moveTime = 0;
		this.speed = 0;
		this.xoff = xoff;
		this.yoff = yoff;
		ix = x;
		iy = y;
	}
	void move()
	{
		box.setName(MOVINGPLATFORM);
		box.setPosition(ix + (xoff - ix) * sin(speed), iy + (yoff - iy) * sin(speed));
		moveTime += speed;
	}
};

class Door extends Platform
{
	FBox buttonBox;
	boolean active;

	Door(float x, float y, float sx, float sy, float bx, float by, float bsx, float bsy)
	{
		super(x, y, sx, sy, true);
		box.setFillColor(color(0,0,255));

		buttonBox = new FBox(bsx, bsy);
		buttonBox.setPosition(bx, by);
		buttonBox.setSensor(true);
		buttonBox.setStatic(true);
		buttonBox.setFillColor(color(0, 255, 0));

		buttonBox.setNoStroke();

		world.add(buttonBox);
	}

	void activate()
	{
		buttonBox.setPosition(buttonBox.getX(),buttonBox.getY() + 1);
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

	void die()
	{
		box.setPosition(ix,iy);
		box.setVelocity(0,0);
	}

	void move(int sign)
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
