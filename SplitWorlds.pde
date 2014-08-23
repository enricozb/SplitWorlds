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

	FBox box;

 	GameObject(float x, float y, float sx, float sy)
 	{
		box = FBox(sx,sy);
		box.setPosition(x,y);
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