package beachballPong;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class MyUserInterface extends JFrame implements GameNet_UserInterface, ActionListener {


    GamePlayer myGamePlayer;
    String myName;
    MyGameInput myGameInput = new MyGameInput();

    private JMenuBar jMenuBar;
    private JMenu options;
    private JMenu file;
    private JMenuItem quit;
    private JMenuItem parameters;
    private JPanel topPanel;
    static JLabel scoreLabel;

    public GamePanel gamePanel;

    OptionsWindow optionsWindow;

    @Override
    public void startUserInterface(GamePlayer player) {
        myGamePlayer = player;
        myName = myGamePlayer.getPlayerName();
        myGameInput.setName(myName);
        myGameInput.setCmd(MyGameInput.CONNECTING);
        myGamePlayer.sendMessage(myGameInput);

        updateGamePanel();

        ArrayList mySounds= new ArrayList();
        mySounds.add("Timeless Techno DEMO.wav");

        SoundPlayer mySoundPlayer = new SoundPlayer(mySounds, true);

    }

    public void updateGamePanel() {
        gamePanel.myGameInput = this.myGameInput;
        gamePanel.myName = this.myName;
        gamePanel.myGamePlayer = this.myGamePlayer;



            System.out.println(myName);
//        System.out.println(MyGame.clients);
//
//        if (MyGame.clients.size() > 0){
//            scoreLabel.setText(MyGame.clients.get(0) + "'s" + " Score: " + 0 + " " + "Player 2's" + " Score: " + 0);
////
//        } else if (MyGame.clients.size() > 1){
//            scoreLabel.setText(MyGame.clients.get(0) + "'s" + " Score: " + 0 + " " + MyGame.clients.get(1) + "'s" + " Score: " + 0);
//        }


    }


    public MyUserInterface() {
        super("My Pong Game");
        setSize(1200, 800);
        setResizable(false);
        addWindowListener(new Termination());

         optionsWindow = new OptionsWindow();
        setLayout(new BorderLayout());


        topPanel = new JPanel();
        topPanel.setBackground(Color.white);

        gamePanel = new GamePanel(this);
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        add(gamePanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        jMenuBar = new JMenuBar();
        add(jMenuBar, BorderLayout.SOUTH);

        file = new JMenu("File");
        options = new JMenu("Options");

        quit = new JMenuItem("Quit");
        quit.addActionListener(this);

        file.add(quit);


        parameters = new JMenuItem("Parameters");
        parameters.addActionListener(this);

        options.add(parameters);

        jMenuBar.add(file);
        jMenuBar.add(options);

        scoreLabel = new JLabel();

        Font scoreFont = new Font("Courier", Font.PLAIN, 20);
        scoreLabel.setFont(scoreFont);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setText("Player 1 Score: 0 Player 2 Score: 0");

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

    @Override
    public void actionPerformed(ActionEvent e) {

        String buttonString = e.getActionCommand();

        if (buttonString == "Parameters"){
            optionsWindow.setVisible(true);

        } else if (buttonString == "Quit"){
            exitProgram();

        }


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
