package beachballPong;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class SoundPlayer implements Runnable, LineListener{
    ArrayList sounds;
    AudioInputStream audioInputStream;
    Clip clip;
    boolean looping = false;
    boolean stopThread = false;
    Thread thread=null;

    public SoundPlayer(ArrayList sounds, boolean looping)
    {
        this.looping = looping;
        this.sounds = sounds;
        thread = new Thread(this);
        thread.start();
    }

    public void run()
    {
        int index = 0;
        while (index < sounds.size())
        {
            playSound((String)sounds.get(index));
            index +=1;
            // We now need to wait for completion or user requesting next
            waitForSoundCompletion();
            if (stopThread)
                break;
            // Finished last sound
            if (looping && index == sounds.size())
                index = 0;
        }
        System.out.println("Thread done doing sounds");
    }
    public void next()
    {
        clip.stop();
// The following line will interrup the "wait" call in waitForSoundCompletion.
// We will actually get an "InterruptedException" in this method
        thread.interrupt();
    }
    public void stop()
    {
        stopThread=true;
        next();
    }
    private synchronized void waitForSoundCompletion()
    {
        try {
            wait();
        } catch (InterruptedException e) {
            // Must be wanting to stop this Process
            System.out.println("Thread interrupted");
        }
    }
    private synchronized void wakeup()
    {
// The following will cause "wait" to finish in the waitForSoundCompletion routine
        notify();
    }
    @Override
    public void update(LineEvent event) {
        System.out.println("update event="+event.getType());
        if (event.getType() == LineEvent.Type.STOP)
        {
            try {
                // Not sure this is necessary?
                audioInputStream.close();
                System.out.println("Closing audioInputStream");
            } catch (IOException e) {
                e.printStackTrace();
            }
            wakeup(); // Wakeup thread so it can play the next sound.
        }

    }
    private void playSound(String fileName)
    {
        try {
            System.out.println("Playing: "+ fileName);
            audioInputStream = AudioSystem.getAudioInputStream(
                    new File(fileName));

            clip = AudioSystem.getClip();

            // This will call update when a Clip is done.  update can call wakeup.
            clip.addLineListener(this);

            clip.open(audioInputStream);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound."+ex);
            ex.printStackTrace();
        }
    }



}
