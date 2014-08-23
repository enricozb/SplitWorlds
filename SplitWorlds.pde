import java.awt.Rectangle;
Man man;
Man wman;
ArrayList<GameObject> gos = new ArrayList<GameObject>();

int level;

BufferedReader reader;

void setup() 
{
	size(800,800,OPENGL);
	smooth(8);

	rectMode(CENTER);
	noStroke();
	reader = createReader("level00.txt");
	level = 0;
	drawLevel();

}

void draw() 
{
	background(0);
	upDrawObjects();
	drawOverlay();
}

void keyPressed()
{
	if(key == CODED)
	{
		man.move(keyCode);
		wman.move(keyCode);
	}
}

void upDrawObjects()
{

}

void drawLevel()
{
	String line;
	do {
		line = reader.readLine();
	}
	while(line != null) {
		try{
			line = reader.readLine();
		} catch(IOException e) {
			e.printStackTrace();
			line = null;
		}
		String[] ch = split(line, " ");
		
		for(Sting go: ch) 
		{
			switch (go) 
			{
				case "Platform":
					gos.add(new Platform());
				case "ManW":
					gos.



				
			}

		}	
		
	}	

}
boolean checkCollision(GameObject go1, GameObject go2) {
	Rectangle r1 = new Rectangle((int) go1.x, (int) go1.y, (int) go1.sx, (int) go1.sy);
    Rectangle r2 = new Rectangle((int) go2.x, (int) go2.y, (int) go2.sx, (int) go2.sy);
    return r1.intersects(r2);
}


void drawOverlay()
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
	float x, y;
	float sx, sy;

	PVector v;
	PVector p;

 	GameObject(float x, float y, float sx, float sy)
 	{
		this.x = x;
 		this.y = y;
 		this.sx = sx;
 		this.sy = sy;
 	}

	abstract void upDraw();
}

class Platform extends GameObject
{
	Platform(float x, float y, float sx, float sy)
	{
		super(x,y,sx,sy);
	}

	void upDraw()
	{
		rect(x,y,sx,sy);
	}
}

class Man extends GameObject
{
	Man(float x, float y, float sx, float sy)
	{
		super(x, y, sx, sy);
	}

	void upDraw()
	{
		v.add(g)
	}

	void move(int dir)
	{
		switch(dir)
		{
			case UP: v.add(UP_VECTOR);
			break;
			case RIGHT: p.add(new PVector(1,0));
			break;
			case LEFT: p.add(new PVector(-1,0));
			break;
		}
	}
}