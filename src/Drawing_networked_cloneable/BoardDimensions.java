package Drawing_networked_cloneable;

import java.awt.Dimension;
import java.awt.Point;

public class BoardDimensions {

	private int screen_width=-1, screen_height=-1;
	private double scale_width=0.0, scale_height=0.0;
	private double generic_size = 1000000.0; // One million
		
	public void setParms(Dimension dimension)
	{
		screen_width = dimension.width;
		screen_height = dimension.height;
		scale_width = generic_size/screen_width;
		scale_height = generic_size/screen_height;
	}
	Point toGeneric(Point p)
	{
		int x = (int)(p.x * scale_width);
		int y = (int)(p.y * scale_height);
		return new Point(x,y);
	}
	Point toPixels(Point p)
	{
		int x = (int)(p.x / scale_width);
		int y = (int)(p.y / scale_height);
		return new Point(x,y);
	}
	

}
