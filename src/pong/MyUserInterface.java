package pong;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

public class MyUserInterface extends JFrame implements GameNet_UserInterface {


    GamePlayer myGamePlayer;
    String myName;
    MyGameInput myGameInput = new MyGameInput();

    private JMenuBar jMenuBar;
    private JMenu options;
    private JMenu file;
    private JMenuItem quit;
    private JMenuItem parameters;
    private JPanel topPanel;
    public JLabel scoreLabel;

    public GamePanel gamePanel;

    ArrayList<String> clients;



    @Override
    public void startUserInterface(GamePlayer player) {
        myGamePlayer = player;
        myName = myGamePlayer.getPlayerName();
        myGameInput.setName(myName);
        myGameInput.setCmd(MyGameInput.CONNECTING);
        myGamePlayer.sendMessage(myGameInput);




        scoreLabel.setText("jacob" + " Score: " + 0 + " " + "Placeholder" + " Score: " + 0);


        updateGamePanel();

    }

    public void updateGamePanel() {
        gamePanel.myGameInput = this.myGameInput;
        gamePanel.myName = this.myName;
        gamePanel.myGamePlayer = this.myGamePlayer;

    }


    public MyUserInterface() {
        super("My Pong Game");
        setSize(800, 400);
        setResizable(false);
        addWindowListener(new Termination());

        setLayout(new BorderLayout());


        topPanel = new JPanel();
        topPanel.setBackground(Color.cyan);

        gamePanel = new GamePanel(this);
        add(gamePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        jMenuBar = new JMenuBar();
        add(jMenuBar, BorderLayout.SOUTH);

        file = new JMenu("File");
        options = new JMenu("Options");
        parameters = new JMenuItem("Parameters");

        options.add(parameters);

        jMenuBar.add(file);
        jMenuBar.add(options);


        scoreLabel = new JLabel();
        topPanel.add(scoreLabel);


        setVisible(true);

    }

    public void receivedMessage(Object ob) {
        MyGameOutput myGameOutput = (MyGameOutput) ob;
        // Check to see we were accepted and connected


        if (myGamePlayer != null) {
            if (myGameOutput.myGame.getMyIndex(myName) < 0) {
                System.out.println("Not allowed to connect to the game");
                exitProgram();
            } else {
                gamePanel.box = myGameOutput.myGame.box;


                gamePanel.repaint();


            }
        } else
            System.out.println("Getting outputs before we are ready");
    }


    private void exitProgram() {
        if (myGamePlayer != null)

        {
            myGameInput.setCmd(MyGameInput.DISCONNECTING);
            myGamePlayer.sendMessage(myGameInput); // Let the game know that we are leaving

            myGamePlayer.doneWithGame(); // clean up sockets
        }
        System.exit(0);
    }

    //*******************************************
    // An Inner class
    //*******************************************

    //*******************************************
    // Another Inner class
    //*******************************************
    class Termination extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.out.println("Client is exitting game");
            exitProgram();
        }
    }

    //****** Done with Inner Classes ***************
}
