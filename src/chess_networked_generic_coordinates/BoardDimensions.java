package chess_networked_generic_coordinates;

import java.awt.Graphics;

public class BoardDimensions {

	private int xstart=-1, ystart=-1;
	double square_width=-1, square_height=-1;
	
	
	public void setParms(int xstart, int ystart, double square_width, double square_height)
	{
		this.xstart = xstart;
		this.ystart = ystart;
		this.square_width = square_width;
		this.square_height = square_height;
	}
	DPoint getDpoint(int x, int y)
	{
		double dx = (x - xstart)/square_width;
		double dy = (y - ystart)/square_height;
		if (dx < 0.0 || dy < 0.0 || dx >= 8.0 || dy >= 8.0)
			return null;
		else
			return new DPoint(dx, dy);
	}
	public void boardDraw(Graphics g, PieceType pieceType, ColorType color, double x, double y)
    {
        ChessPiece chessPiece = ChessPiece.chessPieces[color.ordinal()][pieceType.ordinal()];
        int xPixel = xstart+(int)(square_width * x) ;
        int yPixel = ystart+(int)(square_height * y);
        chessPiece.draw(g, xPixel, yPixel, (int)square_width, (int)square_height);
    }
}
