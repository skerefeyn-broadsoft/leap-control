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
			// TODO Auto-generated catch block
			e.printStackTrace();
			//inform error!
		}
	}
	
	@Override
	public void onScroll(int direction) {
		if (RemoteEventsListener.SCROLL_UP == direction) {
			robot.mouseWheel(1);
			System.out.println("[remote] scrollUp");
		} else if (RemoteEventsListener.SCROLL_DOWN == direction) {
			System.out.println("[remote] scrollDown");
		}
	}

	@Override
	public void onMouseMove(float x, float y) {
		System.out.println("[remote] mouse move");
		PointerInfo info = MouseInfo.getPointerInfo();
		if (info != null) {
			java.awt.Point p = info.getLocation();
			robot.mouseMove(p.x + Math.round(x), p.y + Math.round(y));
		}

	}

	@Override
	public void onLeftMouseButtonClick() {
		System.out.println("[remote] left click");
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	@Override
	public void onLeftMouseButtonDoubleClick() {
		System.out.println("[remote] left double click");
		onLeftMouseButtonClick();
		robot.delay(30);
		onLeftMouseButtonClick();
	}

	@Override
	public void onRightMouseButtonClick() {
		System.out.println("[remote] right click");
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
	}
	
	@Override
	public void onMiddleMouseButtonClick() {
		System.out.println("[remote] middle click");
		robot.mousePress(InputEvent.BUTTON2_MASK);
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON2_MASK);
	}

	@Override
	public void onLeftMouseButtonDown() {
		System.out.println("[remote] left up/down");
		robot.mousePress(InputEvent.BUTTON1_MASK);
	}
}
