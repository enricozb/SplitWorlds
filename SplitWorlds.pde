Man m;
Man w;

BufferedReader reader;

void setup() 
{
	size(800,800,OPENGL);
	smooth(8);
	rectMode(CENTER);
	noStroke();
	//reader = createReader();
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
		m.move(keyCode);
		w.move(keyCode);
	}
}

void upDrawObjects()
{

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
	float dx, dy;

	PVector v;
	PVector p;

 	GameObject(float x, float y, float dx, float dy)
 	{
		this.x = x;
 		this.y = y;
 		this.dx = dx;
 		this.dy = dy;
 	}

	abstract void upDraw();
}

class Platform extends GameObject
{
	Platform(float x, float y, float dx, float dy)
	{
		super(x,y,dx,dy);
	}

	void upDraw()
	{
		rect(x,y,dx,dy);
	}
}

class Man extends GameObject
{
	Man(float x, float y, float dx, float dy)
	{
		super(x, y, dx, dy);
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