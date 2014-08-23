import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SplitWorlds extends PApplet {

Man m;
Man w;

BufferedReader reader;

public void setup() 
{
	size(800,800,OPENGL);
	smooth(8);
	rectMode(CENTER);
	noStroke();
	//reader = createReader();
}

public void draw() 
{
	background(0);
	upDrawObjects();
	drawOverlay();
}

public void keyPressed()
{
	if(key == CODED)
	{
		m.move(keyCode);
		w.move(keyCode);
	}
}

public void upDrawObjects()
{

}

public void drawOverlay()
{
	pushStyle();
	fill(255);
	rect(width/2,height/2,20,height);
	popStyle();
}

//**********Classes***********

final PVector G = new PVector(0,1);

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

	public abstract void upDraw();
}

class Man extends GameObject
{
	Man(float x, float y, float dx, float dy)
	{
		super(x, y, dx, dy);
	}

	public void upDraw()
	{

	}

	public void move(int dir)
	{

	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "SplitWorlds" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
