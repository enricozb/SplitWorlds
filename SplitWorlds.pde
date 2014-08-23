Man m;
Man w;
int level;
BufferedReader reader;
void setup() 
{
	size(500,500,OPENGL);
	smooth(8);
	reader = createReader("level00.txt");
	level = 0;
	drawLevel();
}

void draw() 
{
	upDrawObjects();
	drawOverlay();
}

void drawOverlay()
{

}

void keyPressed()
{

}
void drawLevel()
{
	String line;
	GameObject[][] grid
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
		for(Sting go: ch) {
			switch (go) {
				case "Platform":
				

			}
		}	

	}
		
	

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