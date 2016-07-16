package QuickClick;

import java.awt.Point;


public class BoardDimensions {

	private int left=-1, top=-1;
	int width=-1, height=-1;
	double x_scale=1.0, y_scale=1.0;
	
	// note that width and height are the effective width and height
	public void setParms(int top, int left, int width , int height)
	{
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		x_scale = width /MyGame.fieldWidth;
		y_scale = height/MyGame.fieldHeight;
	}
	
	DPoint toGenericPoint(Point p)
	{
		double dx = (p.x - left)/x_scale;
		double dy = (p.y - top)/y_scale;
		if (dx < 0.0 || dy < 0.0 || dx > MyGame.fieldWidth || dy > MyGame.fieldHeight)
			return null;
		else
			return new DPoint(dx, dy);
	}
	double toPaintScaleX(double x)
	{
		return x*x_scale;
	}
	DPoint toPaintPoint(DPoint dp)
	{
		int x=left + (int)(dp.x*x_scale);
		int y=top+ (int)(dp.y*y_scale);
		return new DPoint(x,y);
	}
}
