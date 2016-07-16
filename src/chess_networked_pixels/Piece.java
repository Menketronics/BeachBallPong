package chess_networked_pixels;

import java.awt.Graphics;
import java.io.Serializable;

enum ColorType {black, white};
enum PieceType {Pawn, Rook, Knight, Bishop, Queen, King};



class Piece implements Serializable
{    
    int xSquare, ySquare;
    PieceType pieceType;  
    ColorType color;
    
    
    Piece (PieceType p, ColorType c, int xSquare, int ySquare)
    {
        this.pieceType=p;
        this.color =c;
        this.xSquare = xSquare;
        this.ySquare = ySquare;
    }
    
    void moveLoc(int x, int y)
    {
        xSquare = x;
        ySquare = y;
    }
    boolean areYouHere(int xSelectLoc, int ySelectLoc)
    {
        if (xSelectLoc == xSquare && ySelectLoc == ySquare)
            return true;
        else
            return false;
    }
    
     void drawInPosition(Graphics g, BoardDimensions b)
     {
         dragDraw(g, b, 0,0);
     }
     // The following will be used while we are dragging piece around
     void dragDraw(Graphics g, BoardDimensions b,
             int xDelta, int yDelta)
     {
         ChessPiece chessPiece = ChessPiece.chessPieces[color.ordinal()][pieceType.ordinal()];
         int x = b.xstart+xSquare*b.square_width + xDelta;
         int y = b.ystart+ySquare*b.square_height+yDelta;
         chessPiece.draw(g,x,y,b.square_width,b.square_height);
     }
}

