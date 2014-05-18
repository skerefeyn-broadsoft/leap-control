package com.fksu.edu.tracking;

import com.fksu.edu.tracking.LeapTrackingManager;

import java.io.IOException;

import com.leapmotion.leap.Controller;

public class Main {
    public static void main(String[] args) {
        // Create a sample listener and controller
        LeapTrackingManager listener = new LeapTrackingManager();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}
