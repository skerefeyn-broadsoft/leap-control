package com.fksu.edu.tracking;

import com.fksu.edu.remote.RemoteEventsListenerImpl;
import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.Vector;

public class LeapTrackingManager extends Listener {

	private RemoteEventsListenerImpl remote = new RemoteEventsListenerImpl();
	//Screen resolution, it should match the current screen resolution for more precise movements
	int SCREEN_X = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	int SCREEN_Y = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;

	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	public void onConnect(Controller controller) {
		System.out.println("Connected");
		// controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
//		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	}

	public void onDisconnect(Controller controller) {
		// Note: not dispatched when running in a debugger.
		System.out.println("Disconnected");

	}

	public void onExit(Controller controller) {
		System.out.println("Exited");
	}

	public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();

		HandList hands = frame.hands();
		if (!hands.isEmpty()) {
			// Get the first hand
			Hand hand = hands.get(0);

			// Check if the hand has any fingers
			if(hands.count() == 1 && hand.isRight()) {
				int fingerCount = extendedFingerCount(hand);
				System.out.println("FingerCount: " + fingerCount);
				if(fingerCount > 3 ) {
					remote.onItemTranslationEnd();
					remote.onItemRotationEnd();
					calculateMouseMovement(frame, hand);
				} else if(fingerCount == 3) {
					remote.onItemTranslationEnd();
					remote.onItemRotationStart();
					calculateMouseMovement(frame, hand);
				} else if(fingerCount == 2){
					remote.onItemRotationEnd();
					remote.onItemTranslationStart();
					calculateMouseMovement(frame, hand);
				} else {	
					remote.onItemTranslationEnd();
					remote.onItemRotationEnd();
					calculateGesture(frame);
				}
			}
		}

	}

	private int extendedFingerCount(Hand hand) {
		int extendedFingerCount = 0;
		
		FingerList fingerList = hand.fingers();
		for(Finger finger : fingerList) {
			if(finger.isExtended()) {
				extendedFingerCount++;
			}
		}
		
		return extendedFingerCount;
	}
	
	private void calculateGesture(Frame frame) {
		GestureList gestures = frame.gestures();
		for (int i = 0; i < gestures.count(); i++) {
			Gesture gesture = gestures.get(i);

			switch (gesture.type()) {
			case TYPE_CIRCLE:
				CircleGesture circle = new CircleGesture(gesture);
				if (circle.isValid()) {
					// Calculate clock direction using the angle between
					// circle normal and pointable
					if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 4) {
						// Clockwise if angle is less than 90 degrees
						remote.onZoomOut();
					} else {
						remote.onZoomIn();
					}
				}
				break;
			case TYPE_SCREEN_TAP:
				ScreenTapGesture tapGesture = new ScreenTapGesture(gesture);
				if (tapGesture.isValid()) {
					remote.onLeftMouseButtonClick();
				}
				break;
			default:
				System.out.println("[gestures] Unknown gesture type.");
				break;
			}
		}
	}

	private float xPast;
	private float yPast;
	private Frame lastFrame;
	
	private synchronized void calculateMouseMovement(Frame frame, Hand hand) {
		if (lastFrame != null) {
            Vector avgPos = Vector.zero();
            FingerList fingers = hand.fingers();
            for (Finger finger : fingers) {
                avgPos = avgPos.plus(finger.stabilizedTipPosition());
            }
            avgPos = avgPos.divide(fingers.count());
			
//			Finger finger = hand.fingers().get(0);
//			Vector avgPos = finger.stabilizedTipPosition();
			float xNow = avgPos.getX();
//			System.out.println("xNow: " + xNow + " xPast: " + xPast);
			float yNow = avgPos.getY();
//			System.out.println("yNow: " + yNow + " yPast: " + yPast);

			float xMove = 0;
			float yMove = 0;

			xMove = Math.abs(xNow - xPast);
			if (xMove < 0.7) {
				xMove = 0;
			} 
			xMove *= 15;
			if (xNow < xPast) {
				xMove *= -1;
			}

//			xMove = xNow*15; yMove = SCREEN_Y - yNow*5;
			
			yMove = Math.abs(yNow - yPast);
			if (yMove < 0.7) {
				yMove = 0;
			} 
			yMove *= 5;
			if (yNow > yPast) { //screen is with negative y
				yMove *= -1;
			}

			xPast = xNow;
			yPast = yNow;

			remote.onMouseMove(xMove, yMove);
//			System.out.println("Mouse moved with x: " + xMove + " y: " + yMove);
		}
		lastFrame = frame;
	}
}
