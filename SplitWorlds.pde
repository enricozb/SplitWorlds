import java.awt.Rectangle;
import fisica.*;

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

void setup() 
{
	size(1200,800);

	rectMode(CENTER);
	initFisicaWorld();

	//reader = createReader("level00.txt");
	//level = 0;
	//drawLevel();
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
	man = new Man(width/4,height/2, 20, 20);
	wman = new Man(3 * width/4,height/2, 20, 20);
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

class Exit extends Platform
{
	Exit(float x, float y, float sx, float sy)
	{
		super(x,y,sx,sy,false);
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