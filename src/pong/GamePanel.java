package pong;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by jacobmenke on 7/16/16.
 */
public class GamePanel extends JPanel implements GameNet_UserInterface {

    Box box = null;
    Image offScreenImage = null;
    Dimension previousSize = null;

    GamePlayer myGamePlayer;
    String myName;
    MyGameInput myGameInput;

    JLabel scoreLabel;

    MyUserInterface myUserInterface;



    int game_top, game_bottom, game_left, game_right;
    Color[] paddleColors = {Color.green, Color.red};
    BoardDimensions boardDimensions = new BoardDimensions();

    GamePanel(MyUserInterface myUserInterface) {

        this.myUserInterface = myUserInterface;

        Mouser m = new Mouser();
        addMouseMotionListener(m);
        addMouseListener(m);

    }


    public void paint(Graphics theScreen) {
        Dimension d = getSize();
        if (offScreenImage == null || !d.equals(previousSize)) {
            offScreenImage = createImage(d.width, d.height);
            previousSize = d;
        }
        Graphics g = offScreenImage.getGraphics();

        g.setColor(Color.white);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.black);

        Insets insets = getInsets();
        int pad = 10;
        boardDimensions.setParms(insets.top + pad, insets.left + pad,
                d.width - insets.left - insets.right - 2 * pad,
                d.height - insets.top - insets.bottom - 2 * pad);
        if (box == null) {
            g.drawString("Click Mouse to start", 100, 100);
        } else {

            if (!box.isRunning()) {
                String str = "Success count=" +
                        box.playerScore + " Click Mouse to restart";
                myUserInterface.scoreLabel.setText("Player 1 Score: " + 0 + " " + myName  + "'s Score: " + box.playerScore);
                g.drawString(str, 100, 100);

            }

            Point bur = boardDimensions.toPixels(box.boxUpperRight);
            Point bul = boardDimensions.toPixels(box.boxUpperLeft);
            Point blr = boardDimensions.toPixels(box.boxLowerRight);
            Point bll = boardDimensions.toPixels(box.boxLowerLeft);
            Point rightHoleUpper = boardDimensions.toPixels(box.rightHoleUpper);
            Point rightHoleLower = boardDimensions.toPixels(box.rightHoleLower);
            Point leftHoleUpper = boardDimensions.toPixels(box.leftHoleUpper);
            Point leftHoleLower = boardDimensions.toPixels(box.leftHoleLower);

            g.setColor(Color.blue);
            g.drawLine(bll.x, bll.y, blr.x, blr.y); // lower line
            g.drawLine(bul.x, bul.y, bur.x, bur.y);  // top side

            g.drawLine(bul.x, bul.y, leftHoleUpper.x, leftHoleUpper.y);   // above hole on left
            g.drawLine(bll.x, bll.y, leftHoleLower.x, leftHoleLower.y);   // below hole on left

            g.drawLine(bur.x, bur.y, rightHoleUpper.x, rightHoleUpper.y);   // above hole on right
            g.drawLine(blr.x, blr.y, rightHoleLower.x, rightHoleLower.y);   // below hole on right

            g.setColor(Color.green);
            Point pball = boardDimensions.toPixels(box.ballLoc);
            int r = boardDimensions.toPixels(box.ballRadius);
            g.fillOval(pball.x - r, pball.y - r, 2 * r, 2 * r);


            int paddleWidth = boardDimensions.toPixels(box.paddleWidth);
            for (int i = 0; i < 2; i++) {
                Point pPaddle = boardDimensions.toPixels(box.paddleLoc[i]);
                g.setColor(paddleColors[i]);
                g.drawLine(pPaddle.x, pPaddle.y - paddleWidth / 2,
                        pPaddle.x, pPaddle.y + paddleWidth / 2);
            }
        }

        theScreen.drawImage(offScreenImage, 0, 0, this);
    }

    @Override
    public void receivedMessage(Object objectReceived) {

    }

    @Override
    public void startUserInterface(GamePlayer gamePlayer) {

    }

    class Mouser extends MouseAdapter {
        public void mouseMoved(MouseEvent e) {
            int y = e.getY();

            if (box != null) {
                myGameInput.setLocation(boardDimensions.toGenericY(y));
                if (myGamePlayer != null)
                    myGamePlayer.sendMessage(myGameInput);

            }

        }

        public void mousePressed(MouseEvent e) {

            if (myGamePlayer != null) {
                myGameInput.setCmd(MyGameInput.MOUSE_PRESSED);
                if (myGamePlayer != null)
                    myGamePlayer.sendMessage(myGameInput);

            }
        }

    }

}
