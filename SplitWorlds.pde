import java.awt.Rectangle;
import fisica.*;

FWorld world;

Man man;
Man wman;

//ArrayList<GameObject> gos = new ArrayList<GameObject>();

int level;
boolean isLevelLoaded;

BufferedReader reader;

void setup() 
{
	size(1200,600);
	rectMode(CENTER);
	initFisicaWorld();

	reader = createReader("level" + level + ".txt");
	level = 0;
	drawLevel();
}

void draw() 
{
	checkForFinish();
	background(60);
	upDrawObjects();
	checkForFinish();
}

void checkForFinish()
{
	if(man != null && wman != null && man.box.isTouchingBody(wman.box))
	{
		isLevelLoaded = false;
		level++;
		reader = createReader("level" + level + ".txt");
		background(0);
		initFisicaWorld();
		drawLevel();
	}
}

void initFisicaWorld()
{
	Fisica.init(this);
	world = new FWorld();
	world.setGrabbable(false);
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

void upDrawObjects()
{
	man.move(-1);
	wman.move(1);
	world.step();
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
				else if(ch[0].equals("Man"))
					man = new Man(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]));
				else if(ch[0].equals("Woman"))
					wman = new Man(float(ch[1]),float(ch[2]),float(ch[3]),float(ch[4]));

			} catch(IOException e) 
			{
			}
		}
	}
	while(line != null);	
	man.box.setFriction(0);
	wman.box.setFriction(0);
	wman.box.setFill(204,42,65);
	man.box.setFill(100,144,138);
}

//**********Classes***********

final String SPIKE = "Spike";
final String DOORBUTTON = "DoorButton";

final PVector G = new PVector(0,1);
final PVector UP_VECTOR = new PVector(0,-5);

abstract class GameObject 
{
	FBox box;
 	GameObject(float x, float y, float sx, float sy)
 	{
		box = new FBox(sx, sy);
		box.setPosition(x, y);
		box.setNoStroke();
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

class Spikes
{
	final float X_REPEAT_SIZE = 20;
	FCompound mainBody;

	Spikes(float x, float y, float sx, float sy)
	{
		mainBody = new FCompound();
		int num = int(sx/X_REPEAT_SIZE);
		for(float i = x - X_REPEAT_SIZE*num/2; i <= x + X_REPEAT_SIZE*num/2; i += X_REPEAT_SIZE)
		{
			mainBody.addBody(getTriangle(i,y,X_REPEAT_SIZE,sy));
		}
		mainBody.setName(SPIKE);

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
				if(fb.getY() > box.getY() && !fb.isSensor())
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
		if(rPressed) box.setVelocity(sign * 100, box.getVelocityY());
		if(lPressed) box.setVelocity(sign *-100, box.getVelocityY());
		if(!lPressed && !rPressed && temp.size() != 0)
			box.setVelocity(0,box.getVelocityY());

	}
};
