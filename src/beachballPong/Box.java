package beachballPong;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Point;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Box implements Serializable {

    //generic size
    static final int box_width = 1000;
    static final int box_height = 500;

    Point boxUpperRight;
    Point boxUpperLeft;
    Point boxLowerRight;
    Point boxLowerLeft;

    Point rightHoleUpper;
    Point rightHoleLower;

    Point leftHoleUpper;
    Point leftHoleLower;

    Point ballLoc;

    Point[] paddleLoc;

    int paddleWidth;
    int ballRadius = 30;
    static public int ballVx, ballVy;
    private Random rand = new Random();

    int rightPlayerScore = 0;
    int leftPlayerScore = 0;

    static boolean firstTime = true;

    static boolean powerKeyHeld = false;
    static boolean powerShot = false;


    private boolean running = false;

    int strokeWidthAdjustment = 1;
    int strokeWidthAdjustmentX = 15;
    int powerAmount =60;

    static ArrayList<String> players;


    public boolean isRunning() {
        return running;
    }


    public Box() {


        int box_top = 0;
        int box_bottom = box_height;
        int box_left = 0;
        int box_right = box_width;

        boxUpperRight = new Point(box_right, box_top);
        boxUpperLeft = new Point(box_left, box_top);
        boxLowerRight = new Point(box_right, box_bottom);
        boxLowerLeft = new Point(box_left, box_bottom);

        rightHoleUpper = new Point(box_right, box_top + (box_bottom - box_top) / 4);
        rightHoleLower = new Point(box_right, box_top + 3 * (box_bottom - box_top) / 4);

        leftHoleUpper = new Point(box_left, box_top + (box_bottom - box_top) / 4);
        leftHoleLower = new Point(box_left, box_top + 3 * (box_bottom - box_top) / 4);

        paddleWidth = (rightHoleLower.y - rightHoleUpper.y) / 3;
        setGame(false);

    }

    void setGame(boolean startRunning) {

        powerShot = false;

        int box_top = 0;
        int box_bottom = box_height;
        int box_left = 0;
        int box_right = box_width;

        // Start the ball out at a random spot
        ballLoc = new Point(box_left + rand.nextInt(box_right - box_left),
                box_top + rand.nextInt(box_bottom - box_top));

        // Heuristic for generating random starting velocities ... maybe not the best

        if (firstTime == true) {
            ballVx = (-50 + (int) (100 * Math.random()));
            ballVy = -50 + (int) (100 * Math.random());
        }


        paddleLoc = new Point[2];
        paddleLoc[0] = new Point(box_right, (rightHoleUpper.y + rightHoleLower.y) / 2);
        paddleLoc[1] = new Point(box_left + 1, (leftHoleUpper.y + leftHoleLower.y) / 2);
        if (startRunning)
            running = true;

    }

    public void setPaddleY(int yLoc, int clientIndex) {
        paddleLoc[clientIndex].y = yLoc;
        if (paddleLoc[clientIndex].y - paddleWidth / 2 < rightHoleUpper.y)
            paddleLoc[clientIndex].y = rightHoleUpper.y + paddleWidth / 2;
        if (paddleLoc[clientIndex].y + paddleWidth / 2 > rightHoleLower.y)
            paddleLoc[clientIndex].y = rightHoleLower.y - paddleWidth / 2;

    }

    public void turnOnPowerShotIfKeyHeld(){
        if (powerKeyHeld == true) {
            if (ballVx < 0) {
                ballVx -= powerAmount;
            } else {
                ballVx += powerAmount;
            }

            if (ballVy < 0) {
                ballVy -= powerAmount;
            } else {
                ballVy += powerAmount;
            }
            powerShot = true;
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("TOX_Space_Wah.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();

            } catch (Exception e){
                System.out.println(e);

            }



        }

    }

    public void removePowerSpeed(){
        if (ballVx < 0) {
            ballVx += powerAmount;
        } else {
            ballVx -= powerAmount;
        }

        if (ballVy < 0) {
            ballVy += powerAmount;
        } else {
            ballVy -= powerAmount;
        }
        powerShot = false;
    }

    public void turnOffPowerShot (){


        if (powerShot == true){
            removePowerSpeed();
        }

    }

    public void update() {
        if (!running)
            return;
        ballLoc.x = ballLoc.x + ballVx;
        ballLoc.y = ballLoc.y + ballVy;

//        System.out.println(powerKeyHeld);



        // check against right wall

        if (ballLoc.x + ballRadius > boxUpperRight.x) {
            if (ballLoc.y <= rightHoleUpper.y || ballLoc.y >= rightHoleLower.y) {
                // hits wall
                ballVx *= -1;

               turnOffPowerShot();

                ballLoc.x = boxUpperRight.x - ballRadius;
            } else if (ballLoc.y >= paddleLoc[0].y - paddleWidth / 2 &&
                    ballLoc.y <= paddleLoc[0].y + paddleWidth / 2) {

                // In hole but bounces off right paddle
                ballVx *= -1;

                turnOnPowerShotIfKeyHeld();

                ballLoc.x = boxUpperRight.x - ballRadius;
                System.out.println("In Hole and hits paddle");
            } else {
                // In hole and missed by paddle

                turnOffPowerShot();

                leftPlayerScore += 1;
                MyUserInterface.scoreLabel.setText("Jacob" + "'s" + " Score: " + rightPlayerScore + " " + "Player 2's" + " Score: " + leftPlayerScore);

                running = false;
                System.out.println("In Hole and missed by paddle");
            }
        }


        //checking against left wall

        if (ballLoc.x - ballRadius < boxUpperLeft.x) {
            if (ballLoc.y <= leftHoleUpper.y || ballLoc.y >= leftHoleLower.y) {
                // hits wall
                ballVx *= -1;

               turnOffPowerShot();

                ballLoc.x = boxUpperLeft.x + ballRadius;

                System.out.println("hitting wall");
            } else if (ballLoc.y >= paddleLoc[1].y - paddleWidth / 2 &&
                    ballLoc.y <= paddleLoc[1].y + paddleWidth / 2) {
                // In hole but bounces off left paddle
                ballVx *= -1;

                turnOnPowerShotIfKeyHeld();

                ballLoc.x = boxUpperLeft.x + ballRadius;
                System.out.println("In Hole and hits paddle");
            } else {
                // In hole and missed by paddle

                turnOffPowerShot();

                rightPlayerScore += 1;
                MyUserInterface.scoreLabel.setText("Jacob" + "'s" + " Score: " + rightPlayerScore + " " + "Player 2's" + " Score: " + leftPlayerScore);
                running = false;
                System.out.println("In Hole and missed by paddle");
            }
        }

        // check against the bottom wall
        if (ballLoc.y + ballRadius > boxLowerRight.y) {
            ballVy *= -1;
            ballLoc.y = boxLowerRight.y - ballRadius;
        }

        // check against the top wall
        if (ballLoc.y - ballRadius < boxUpperRight.y) {
            ballVy *= -1;
            ballLoc.y = boxUpperRight.y + ballRadius;
        }

    }

}
