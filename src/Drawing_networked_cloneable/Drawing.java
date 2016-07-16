package Drawing_networked_cloneable;

import gameNet_cloneable.CloneableObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.ArrayList;

enum DrawType {scribble, oval,  rectangle, polygon, line};

class DrawingProperties extends CloneableObject implements Serializable
{
    DrawType drawType;
    boolean filled;
    Color color;
    public  void myCloneMethod( Object obj)
    {
    	DrawingProperties dp = (DrawingProperties)obj;
    	dp.color = new Color(dp.color.getRGB());
    }
    
    DrawingProperties(DrawType drawType, Color color, boolean filled)
    {
        this.drawType = drawType;
        this.color = color;
        this.filled = filled;
    }
}

public class Drawing  extends CloneableObject implements Serializable{
    DrawingProperties drawingProperties = new DrawingProperties(DrawType.rectangle, Color.blue, false);
    ArrayList<Shape> shapeArr = new ArrayList<Shape>();
    Shape inProgress = null;
    
    public  void myCloneMethod( Object obj)
    {
    	Drawing drw = (Drawing)obj;
    	drw.drawingProperties = (DrawingProperties)drw.drawingProperties.clone();
    	if (drw.inProgress != null)
    		drw.inProgress = (Shape)drw.inProgress.clone();
    	drw.shapeArr = (ArrayList<Shape>)drw.shapeArr.clone();
    }

    public void draw(Graphics g, BoardDimensions boardDimensions)
    {
        for (int i=0; i < shapeArr.size(); i++)
        {
            Shape s = shapeArr.get(i);
            s.draw(g, boardDimensions);
        }
        if (inProgress != null)
            inProgress.draw(g, boardDimensions);
    }

    public void setColor(Color color)
    {
        drawingProperties.color = color;

        //The following only gets used for Polygons
        if (inProgress != null)
            inProgress.setColor(color);
    }

    public void setFilled(boolean filled)
    {
        drawingProperties.filled = filled;
        
        // The following code is only needed for Polygons
        if (inProgress != null && 
                drawingProperties.drawType == DrawType.polygon)
        {
            PolygonShape p = (PolygonShape)inProgress;
            p.setFilled(filled);
        }
    }
    public void setDrawType(DrawType drawType)
    {
        // Most of this code is dealing with the Polygon case
        if (drawingProperties.drawType == DrawType.polygon)
            exitPolygonMode();

        drawingProperties.drawType = drawType;
    }
    

    private void exitPolygonMode()
    {
        if (inProgress != null)
            shapeArr.add(inProgress);
        inProgress = null;
    }

    public void mousePressed(Point p)
    {
        if (drawingProperties.drawType == DrawType.polygon)
        {
            if (inProgress == null)
            {
                inProgress = new PolygonShape(drawingProperties.color, drawingProperties.filled);
            }
            inProgress.subsequentPoint(p);
        }
        else
        {
            switch(drawingProperties.drawType)
            {
            case rectangle:
                inProgress = new Rectangle(drawingProperties.color, drawingProperties.filled);
                break;
            case oval:
                inProgress = new Oval(drawingProperties.color, drawingProperties.filled);
                break;
            case line:
                inProgress = new Line(drawingProperties.color);
                break;
            case scribble:
                inProgress = new Scribble(drawingProperties.color);
                break;
            }
            inProgress.firstPoint(p);
        }

    }
    public void mouseDragged(Point p)
    {
        switch(drawingProperties.drawType)
        {
        case rectangle:
        case oval:
        case scribble:
        case line:
            inProgress.subsequentPoint(p);
            break;
        }
    }
    public void mouseReleased(Point p)
    {
        if (drawingProperties.drawType != DrawType.polygon)
        {
            inProgress.subsequentPoint(p);
            shapeArr.add(inProgress);
            inProgress = null;
        }
    }

}

abstract class Shape  extends CloneableObject implements Serializable
{
    Color color;
    public  void myCloneMethod( Object obj)
    {
    	Shape shape = (Shape)obj;
    	if (shape.color != null)
    		shape.color = new Color(shape.color.getRGB());
    }
    Shape ( Color c)
    {
        color =c;
    }
    void setColor(Color c)
    {
        color = c;
    }
    abstract void firstPoint(Point p);
    abstract void draw(Graphics g, BoardDimensions boardDimensions);
    abstract void subsequentPoint(Point p);
}



class Rectangle extends Shape  implements Serializable
{
    boolean filled=false;
    Point start;
    Point lastPoint;
    public  void myCloneMethod( Object obj)
    {
    	super.myCloneMethod(obj);
    	Rectangle r = (Rectangle)obj;
    	r.lastPoint = (Point)r.lastPoint.clone();
    	r.start = (Point)r.start.clone();
    }
    Rectangle(Color c, boolean filled)
    {
        super(c);
        lastPoint = start;
        this.filled = filled;
    }
    void firstPoint(Point p)
    {
        start = p;
        lastPoint =p;
    }
    void subsequentPoint(Point p)
    {
        lastPoint = p;
    }
    void draw(Graphics g, BoardDimensions boardDimensions)
    {
        g.setColor(color);
        Point s = boardDimensions.toPixels(start);
        Point l = boardDimensions.toPixels(lastPoint);
        int x = Math.min(s.x, l.x);
        int y = Math.min(s.y, l.y);
        int w = Math.abs(s.x - l.x);
        int h = Math.abs(s.y - l.y);
        if (filled)
            g.fillRect(x, y, w, h);
        else
            g.drawRect(x, y, w, h);
    }
}

class Line extends Shape  implements Serializable
{
    Point start;
    Point lastPoint;
    
    public  void myCloneMethod( Object obj)
    {
    	super.myCloneMethod(obj);
    	Line l = (Line)obj;
    	l.lastPoint = (Point)l.lastPoint.clone();
    	l.start = (Point)l.start.clone();
    }
    Line(Color c)
    {
        super(c);
    }
    void firstPoint(Point p)
    {
        start = p;
        lastPoint =p;
    }
    void subsequentPoint(Point p)
    {
        lastPoint = p;
    }
    void draw(Graphics g, BoardDimensions boardDimensions)
    {
        g.setColor(color);
        Point s = boardDimensions.toPixels(start);
        Point l = boardDimensions.toPixels(lastPoint);
        g.drawLine(s.x,  s.y,  l.x,  l.y);
    }
}

class Oval extends Shape  implements Serializable
{
    boolean filled=false;
    Point start;
    Point lastPoint;
    
    public  void myCloneMethod( Object obj)
    {
    	super.myCloneMethod(obj);
    	Oval o = (Oval)obj;
    	o.lastPoint = (Point)o.lastPoint.clone();
    	o.start = (Point)o.start.clone();
    }
    Oval(Color c, boolean filled)
    {
        super(c);
        this.filled = filled;
    }
    void firstPoint(Point p)
    {
        start = p;
        lastPoint =p;
    }
    void subsequentPoint(Point p)
    {
        lastPoint = p;
    }
    void draw(Graphics g, BoardDimensions boardDimensions)
    {
        g.setColor(color);
        Point s = boardDimensions.toPixels(start);
        Point l = boardDimensions.toPixels(lastPoint);
        int x = Math.min(s.x, l.x);
        int y = Math.min(s.y, l.y);
        int w = Math.abs(s.x - l.x);
        int h = Math.abs(s.y - l.y);
        if (filled)
            g.fillOval(x, y, w, h);
        else
            g.drawOval(x, y, w, h);
    }
}


class Scribble extends Shape  implements Serializable
{
    ArrayList<Point> points= new ArrayList<Point>();
    
    public  void myCloneMethod( Object obj)
    {
    	super.myCloneMethod(obj);
    	Scribble s = (Scribble)obj;
    	s.points = (ArrayList<Point>)s.points.clone();
    }
    Scribble(Color c)
    {
        super(c);
    }
    void firstPoint(Point p)
    {
        points.add(p);
    }
    void subsequentPoint(Point p)
    {
        points.add(p);
    }
    void draw(Graphics g, BoardDimensions boardDimensions)
    {
        g.setColor(color);
        for (int i=1; i < points.size(); i++)
        {    
            Point f = boardDimensions.toPixels(points.get(i-1));
            Point n = boardDimensions.toPixels(points.get(i)); 
            g.drawLine(f.x, f.y, n.x, n.y);
        }
    }
}

class PolygonShape extends Shape  implements Serializable
{
    ArrayList<Point> points= new ArrayList<Point>();
    boolean filled;
    
    public  void myCloneMethod( Object obj)
    {
    	super.myCloneMethod(obj);
    	PolygonShape p = (PolygonShape)obj;
    	p.points = (ArrayList<Point>)p.points.clone();
    }
    PolygonShape(Color c, boolean filled)
    {
        super(c);
        this.filled = filled;
    }
    void setFilled(boolean filled)
    {
        this.filled = filled;
    }
    void firstPoint(Point p)
    { // currently not called
        points.add(p);      
    }
    void draw(Graphics g, BoardDimensions boardDimensions)
    {
        int[] y = new int[points.size()];
        int[] x = new int[points.size()];
        g.setColor(color);
        
        for (int i=0; i < points.size(); i++)
        {
        	Point p = boardDimensions.toPixels(points.get(i));
            x[i]=p.x;
            y[i]=p.y;
        }
        Polygon poly = new Polygon(x, y, x.length);
        if (filled)
            g.fillPolygon(poly);
        else
            g.drawPolygon(poly);
    }
    void subsequentPoint(Point p)
    {
        points.add(p);        
    }
}