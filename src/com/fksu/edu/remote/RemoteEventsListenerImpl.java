package com.fksu.edu.remote;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;


public class RemoteEventsListenerImpl implements RemoteEventsListener {

	private Robot robot;

	public RemoteEventsListenerImpl() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onMouseMove(float x, float y) {
		System.out.println("[remote] mouse move");
		PointerInfo info = MouseInfo.getPointerInfo();
		if (info != null) {
			java.awt.Point p = info.getLocation();
			robot.mouseMove(p.x + Math.round(x), p.y + Math.round(y));
		}
	}

	@Override
	public synchronized void onLeftMouseButtonClick() {
		System.out.println("[remote] left click");
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	@Override
	public synchronized void onZoomOut() {
		robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		
		onMouseMove(0, -30);
		
		robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		onMouseMove(0, 30);
	}

	@Override
	public synchronized void onZoomIn() {
		robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		
		onMouseMove(0, 30);
		
		robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		onMouseMove(0, -30);
	}

	private boolean isSceneMoved = false;
	
	@Override
	public synchronized void onSceneMove() {
		if(!isSceneMoved) {
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			isSceneMoved = true;
		}
	}
	
	@Override
	public synchronized void onSceneRelease() {
		if(isSceneMoved) {
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
			isSceneMoved = false;
		}
	}
}
