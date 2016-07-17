package beachballPong;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jacobmenke on 7/16/16.
 */
public class GamePanel extends JPanel implements KeyListener {

    Box box = null;
    Image offScreenImage = null;
    Dimension previousSize = null;

    GamePlayer myGamePlayer;
    String myName;
    MyGameInput myGameInput;


    MyUserInterface myUserInterface;


    public BufferedImage image;


    int game_top, game_bottom, game_left, game_right;
    Color[] paddleColors = {Color.green, Color.red};
    BoardDimensions boardDimensions = new BoardDimensions();

    GamePanel(MyUserInterface myUserInterface) {

        this.myUserInterface = myUserInterface;

        Mouser m = new Mouser();
        addMouseMotionListener(m);
        addMouseListener(m);

        addKeyListener(this);

        loadImages();

    }

    private void loadImages() {
        try {

            image = ImageIO.read(new File("beach.jpg"));


        } catch (IOException ex) {

            System.out.println("Error reading in images: +ex");
        }
    }

    public void paint(Graphics theScreen) {
        Dimension d = getSize();
        if (offScreenImage == null || !d.equals(previousSize)) {
            offScreenImage = createImage(d.width, d.height);
            previousSize = d;
        }
        Graphics g = offScreenImage.getGraphics();


        Graphics2D g2d = (Graphics2D)g.create();

        g2d.drawImage(image,0,0,null);


        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        g.setColor(Color.black);

        Insets insets = getInsets();
        int pad = 10;
        boardDimensions.setParms(insets.top + pad, insets.left + pad,
                d.width - insets.left - insets.right - 2 * pad,
                d.height - insets.top - insets.bottom - 2 * pad);

        Font titleFont = new Font("Chalkboard",Font.PLAIN, d.height/10);
        FontMetrics fm = getFontMetrics(titleFont);

        g.setFont(titleFont);
        g.setColor(Color.white);

//        String start = "Click mouse to start";


        if (box == null) {
//            g.drawString(start, d.width/2-fm.stringWidth(start)/2, d.height/2);
        } else {

            if (!box.isRunning()) {
                String restart = "Click Mouse to play";

                g.drawString(restart, d.width/2-fm.stringWidth(restart)/2, d.height/2);


            }

            if (Box.powerShot == true){

                Font powerTitle = new Font("Arial",Font.BOLD, d.height/10);
                fm = getFontMetrics(powerTitle);

                g.setFont(powerTitle);
                g.setColor(Color.white);

                String powerShotTitle = "Power Shot!@!";

                g.drawString(powerShotTitle, d.width/2-fm.stringWidth(powerShotTitle)/2, d.height/2);

            }

            BasicStroke bs = new BasicStroke(d.height/150, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
            g2d.setStroke(bs);
            g2d.setColor(Color.white);

            Point bur = boardDimensions.toPixels(box.boxUpperRight);
            Point bul = boardDimensions.toPixels(box.boxUpperLeft);
            Point blr = boardDimensions.toPixels(box.boxLowerRight);
            Point bll = boardDimensions.toPixels(box.boxLowerLeft);
            Point rightHoleUpper = boardDimensions.toPixels(box.rightHoleUpper);
            Point rightHoleLower = boardDimensions.toPixels(box.rightHoleLower);
            Point leftHoleUpper = boardDimensions.toPixels(box.leftHoleUpper);
            Point leftHoleLower = boardDimensions.toPixels(box.leftHoleLower);

            g.setColor(Color.white);

            g2d.drawLine(bll.x, bll.y, blr.x, blr.y); // lower line
            g2d.drawLine(bul.x, bul.y, bur.x, bur.y);  // top side

            g2d.drawLine(bul.x, bul.y, leftHoleUpper.x, leftHoleUpper.y);   // above hole on left
            g2d.drawLine(bll.x, bll.y, leftHoleLower.x, leftHoleLower.y);   // below hole on left

            g2d.drawLine(bur.x, bur.y, rightHoleUpper.x, rightHoleUpper.y);   // above hole on right
            g2d.drawLine(blr.x, blr.y, rightHoleLower.x, rightHoleLower.y);   // below hole on right

            g2d.setColor(Color.green);

            Point pball = boardDimensions.toPixels(box.ballLoc);
            int r = boardDimensions.toPixels(box.ballRadius);

            Image ball = new ImageIcon("ball.gif").getImage();

            g2d.drawImage(ball,pball.x - r, pball.y - r,null);




//            g2d.fillOval(pball.x - r, pball.y - r, 2 * r, 2 * r);
            BasicStroke paddleStroke = new BasicStroke(d.height/75, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);

            g2d.setStroke(paddleStroke);

            g2d.setColor(new Color(169,69,16));
            int paddleWidth = boardDimensions.toPixels(box.paddleWidth);


            for (int i = 0; i < 2; i++) {
                Point pPaddle = boardDimensions.toPixels(box.paddleLoc[i]);
//                g2d.setColor(paddleColors[i]);
                g2d.drawLine(pPaddle.x, pPaddle.y - paddleWidth / 2,
                        pPaddle.x, pPaddle.y + paddleWidth / 2);
            }
        }

        theScreen.drawImage(offScreenImage, 0, 0, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {




    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_T){
            Box.powerKeyHeld = true;

        }


    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_T){
            Box.powerKeyHeld = false;

        }

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
