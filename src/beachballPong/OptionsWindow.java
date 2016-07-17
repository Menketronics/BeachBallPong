package beachballPong;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by jacobmenke on 7/16/16.
 */
public class OptionsWindow extends JFrame implements ActionListener, ChangeListener {

    private JLabel speedLabel;
    private JSlider speedSliderY;
    private JSlider speedSliderX;
    private JButton saveChangesButton;
    private JLabel speedValueLabelX;
    private JLabel speedValueLabelY;

    String valueX;
    String valueY;


    OptionsWindow() {
        super("Options Window");
        setSize(400, 400);
        setLayout(new GridLayout(6,1));
        setVisible(false);

        Font optionsFont = new Font("Arial", Font.PLAIN, 24);

        speedLabel = new JLabel("Speed - Adjust with Sliders");
        speedLabel.setFont(optionsFont);
        speedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(speedLabel);

        speedValueLabelX = new JLabel("Speed in X: 25");
        speedValueLabelX.setFont(optionsFont);
        speedValueLabelX.setHorizontalAlignment(SwingConstants.CENTER);
        add(speedValueLabelX);

        speedSliderX = new JSlider(0, 100, 25);
        add(speedSliderX);


        speedValueLabelY = new JLabel("Speed in Y: 25");
        speedValueLabelY.setFont(optionsFont);
        speedValueLabelY.setHorizontalAlignment(SwingConstants.CENTER);
        add(speedValueLabelY);

        speedSliderY = new JSlider(0, 100, 25);

        add(speedSliderY);

        speedSliderY.addChangeListener(this);
        speedSliderX.addChangeListener(this);


        saveChangesButton = new JButton("Save Changes");

        add(saveChangesButton);


        saveChangesButton.addActionListener(this);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() == "Save Changes") {
            System.out.println(speedSliderY.getValue());
            Box.ballVx = speedSliderX.getValue();
            Box.ballVy = speedSliderY.getValue();

            Box.firstTime = false;


        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {

        valueX = "Speed in X: " + (speedSliderX.getValue());
        speedValueLabelX.setText(valueX);

        valueY = "Speed in Y: " + (speedSliderY.getValue());
        speedValueLabelY.setText(valueY);

        if (e.getSource() == speedSliderX){

            if (Box.ballVx < 0){
                Box.ballVx = speedSliderX.getValue()*-1;

            } else {
                Box.ballVx = speedSliderX.getValue();
            }

        } else if (e.getSource() == speedSliderY){

            if (Box.ballVy < 0){
                Box.ballVy = speedSliderY.getValue()*-1;

            } else {
                Box.ballVy = speedSliderY.getValue();
            }
        }


        Box.firstTime = false;


    }
}
