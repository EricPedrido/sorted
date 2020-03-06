package com.pedrido.model;

import javafx.beans.property.SimpleStringProperty;

import java.util.TimerTask;

/**
 * Modified from <a href="https://stackoverflow.com/questions/9355303/javafx-stopwatch-timer">
 *     https://stackoverflow.com/questions/9355303/javafx-stopwatch-timer</a>
 */
public class Timer {
    private final static Timer INSTANCE = new Timer();

    // Time Variables
    private SimpleStringProperty sspTime = new SimpleStringProperty("00:00.00");
    private long time;

    // Timer variables

    private java.util.Timer t = new java.util.Timer();
    private TimerTask tt;
    private boolean timing = false;

    private Timer() {
    }

    public static Timer getInstance() {
        return INSTANCE;
    }

    public void startTimer(final long time) {
        this.time = time;
        timing = true;

        tt = new TimerTask() {
            @Override
            public void run() {
                if (!timing) {
                    try {
                        tt.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    updateTime();
                }
            }
        };

        t.scheduleAtFixedRate(tt, 10, 10);
    }

    public void stopTimer() {
        timing = false;
    }

    public void updateTime() {
        this.time++;
        String[] split = getMSMsTimeFormat();

        //Add extra '0' before number if only one digit
        sspTime.set((split[0].length() == 1 ? "0" + split[0] : split[0].substring(0, 2)) + ":" +
                (split[1].length() == 1 ? "0" + split[1] : split[1].substring(0, 2)) + "." +
                (split[2].length() == 1 ? "0" + split[2] : split[2].substring(0, 2)));
    }

    private String[] getMSMsTimeFormat(){
        String[] MSMs = new String[3];

        //Convert elapsed time to minutes, seconds, and milliseconds
        long minutes = (this.time / 6000);
        long seconds = (this.time / 100) % 60;
        long milliseconds = this.time % 100;

        //Put minutes, seconds, milliseconds into string array
        MSMs[0] = Integer.toString((int)minutes);
        MSMs[1] = Integer.toString((int)seconds);
        MSMs[2] = Integer.toString((int)milliseconds);

        return MSMs;
    }

    public long getTime() {
        return time;
    }

    public SimpleStringProperty getSspTime() {
        return sspTime;
    }

}
