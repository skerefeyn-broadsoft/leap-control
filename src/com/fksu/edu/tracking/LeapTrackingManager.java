package com.fksu.edu.tracking;

import com.fksu.edu.remote.RemoteEventsListener;
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
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Screen;
import com.leapmotion.leap.ScreenList;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;
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
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
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
			FingerList fingers = hand.fingers();
			int fingerCount = fingers.count();

			if(fingerCount >= 4) {
				calculateMouseMovement(frame, hand);
				return;
			}
			
			if(hands.count() > 1) {
				return;
			}
			
			GestureList gestures = frame.gestures();
			for (int i = 0; i < gestures.count(); i++) {
				Gesture gesture = gestures.get(i);

				switch (gesture.type()) {
				case TYPE_CIRCLE:
					CircleGesture circle = new CircleGesture(gesture);
					if (circle.isValid()) {
						// Calculate clock direction using the angle between
						// circle normal and pointable
						if (circle.pointable().direction()
								.angleTo(circle.normal()) <= Math.PI / 4) {
							// Clockwise if angle is less than 90 degrees
							remote.onScroll(RemoteEventsListener.SCROLL_DOWN);
						} else {
							remote.onScroll(RemoteEventsListener.SCROLL_UP);
						}
					}
					break;
				case TYPE_SCREEN_TAP:
					ScreenTapGesture tapGesture = new ScreenTapGesture(gesture);
					if (tapGesture.isValid()) {
						remote.onLeftMouseButtonDown();
					}
					break;
				case TYPE_KEY_TAP:
					KeyTapGesture keyTap = new KeyTapGesture(gesture);
					if (keyTap.isValid()) {
						switch (fingerCount) {
						case 1:
							clickCount++;
							if (!isThreadStarted) {
								new DoubleClickThread().start();
							}
							break;
						case 2:
							remote.onRightMouseButtonClick();
							break;
						case 3:
							remote.onMiddleMouseButtonClick();
							break;
						}
					}
					break;
				default:
					System.out.println("[gestures] Unknown gesture type.");
					break;
				}
			}
			lastFrame = null;

		}

	}

	private float xPast;
	private float yPast;
	private Frame lastFrame;
	
	private synchronized void calculateMouseMovement(Frame frame, Hand hand) {
		if (lastFrame != null) {
			Finger finger = hand.fingers().get(0);
			Vector fingerPosition = finger.stabilizedTipPosition();
			float xNow = fingerPosition.getX();
//			float xSpeed = finger.tipVelocity().getX();
			System.out.println("xNow: " + xNow + " xPast: " + xPast);
			float yNow = fingerPosition.getY();
			System.out.println("yNow: " + yNow + " yPast: " + yPast);
//			float ySpeed = finger.tipVelocity().getY();

			float xMove = 0;
			float yMove = 0;

			xMove = Math.abs(xNow - xPast);
			if (xMove < 1) {
				xMove = 0;
			} 
			xMove *= 15;
			if (xNow < xPast) {
				xMove *= -1;
			}


			yMove = Math.abs(yNow - yPast);
			if (yMove < 1) {
				yMove = 0;
			} 
			yMove *= 5;
			if (yNow > yPast) { //screen is with negative y
				yMove *= -1;
			}

			xPast = xNow;
			yPast = yNow;

			remote.onMouseMove(xMove, yMove);
			System.out.println("Mouse moved with x: " + xMove + " y: " + yMove);
		}
		lastFrame = frame;
	}

	private volatile int clickCount = 0;
	private volatile boolean isThreadStarted = false;

	private class DoubleClickThread extends Thread {
		@Override
		public void run() {
			isThreadStarted = true;
			try {
				sleep(1000);
			} catch (InterruptedException e) {
			}

			if (clickCount == 2) {
				remote.onLeftMouseButtonDoubleClick();
			} else {
				remote.onLeftMouseButtonClick();
			}

			clickCount = 0;
			isThreadStarted = false;
		}
	}
}
