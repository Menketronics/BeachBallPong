package QuickClick;

import java.awt.Color;
import java.io.Serializable;

public class Disk implements Serializable
{
	double vx,vy;
	DPoint dp;
	Color color;
	Disk()
	{
		dp = new DPoint();// generates random location
		vx =-0.5 + Math.random();
		vy = -1.0 + 2*Math.random();
		color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
	}
	boolean isInPlay()
	{
		if (dp.x < 0)
			return false;
		else
			return true;
	}
	boolean insideDisk(DPoint dp_click , double radius)
	{
		if ( dp_click.x <= (dp.x + radius)  &&
			 dp_click.x >=	(dp.x - radius) &&
			 dp_click.y <=	(dp.y + radius) &&
			 dp_click.y >= (dp.y - radius))
		{
			dp.x= MyGame.OUT_OF_BOUNDS; // Take this disk out of play
			return true;  // the click is inside a box that surrounds the disk
		}
		else
			return false;
	}
	void update()
	{
		if (dp.x <= MyGame.OUT_OF_BOUNDS ) return;
		dp.x += vx;
		dp.y += vy;

		// Bounce off top wall
		if (dp.y < MyGame.diskRadius)
		{
			vy = -vy;
			dp.y= MyGame.diskRadius;
		}
		// Bounce off bottom wall
		if (dp.y > MyGame.fieldHeight-MyGame.diskRadius)
		{
			vy = - vy;
			dp.y= MyGame.fieldHeight-MyGame.diskRadius;
		}

		// Check Right Wall
		if (dp.x > MyGame.fieldWidth-MyGame.diskRadius)
		{
			vx = -vx;
			dp.x = MyGame.fieldWidth-MyGame.diskRadius;
		}


		// Check Left Wall
		if (dp.x < MyGame.diskRadius)
		{
			vx = -vx;
			dp.x = MyGame.diskRadius;
		}
	}
}
