import java.awt.Rectangle;
import fisica.*;

FWorld world;

Platform divider;

Man man;
Man wman;


int level;
boolean isLevelLoaded;

BufferedReader reader;

void setup() 
{
	size(1200,800,OPENGL);
	smooth(8);

	rectMode(CENTER);
	initFisicaWorld();

	reader = createReader("level00.txt");
	level = 0;
	drawLevel();
}

void draw() 
{
	background(0);
	upDrawObjects();
}

void initFisicaWorld()
{
	Fisica.init(this);
	world = new FWorld();
	world.setGrabbable(false);
	world.setEdges();
	world.setGravity(0,1e3);

	divider = new Platform(width/2,height/2,20,height,true); //Remove later
	man = new Man(width/4,height/2, 30, 30);
	wman = new Man(3 * width/4,height/2, 30, 30);
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

void upDrawObjects()
{
	man.move();
	wman.move();
	world.step();
	world.draw();
}

// Format : ClassName xpos ypos sx sy
void drawLevel()
{
	String line = null;
	do {
		if(isLevelLoaded != true) { 
			try {
					line = reader.readLine();
					String[] ch = split(line, " ");

					if(ch[0].equals("Platform"))
							new Platform(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]), boolean(ch[5]));
					if(ch[0].equals("Man"))
							new Man(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]));
					if(ch[0].equals("Woman"))
							new Man(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]));
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

	void move()
	{
		if(uPressed) this.box.addImpulse(0,-1e3);
		if(rPressed) this.box.addForce(1e4,0);
		if(lPressed) this.box.addForce(-1e4,0);
	}
}