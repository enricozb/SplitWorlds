void setup() 
{
	size(500,500,OPENGL);
	smooth(8);
}

void draw() 
{
	upDrawObjects();
	overlay();
}

void keyPressed()
{

}

//**********Classes***********

final PVector G = new PVector(0,1);

abstract class GameObject 
{
	float x, y;
	PVector v;
	PVector p;

 	GameObject(float x, float y)
 	{
		this.x = x;
 		this.y = y;
 	}

	abstract void upDraw();
}

class Man extends GameObject
{
	Man(float x, float y)
	{
		super(x,y);
	}
	void upDraw()
	{

	}
}