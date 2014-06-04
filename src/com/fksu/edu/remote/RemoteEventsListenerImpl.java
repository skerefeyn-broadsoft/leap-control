package com.fksu.edu.remote;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;


public class RemoteEventsListenerImpl implements RemoteEventsListener {

	private Robot robot;

	private boolean isItemTranslated = false;

	private boolean isItemRotated = false;
	
	public RemoteEventsListenerImpl() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void onMouseMove(float x, float y) {
//		System.out.println("[remote] mouse move");
		PointerInfo info = MouseInfo.getPointerInfo();
		if (info != null) {
			java.awt.Point p = info.getLocation();
			robot.mouseMove(p.x + Math.round(x), p.y + Math.round(y));
		}
	}

	@Override
	public synchronized void onLeftMouseButtonClick() {
		System.out.println("[remote] onLeftMouseButtonClick");
		robot.mousePress(InputEvent.BUTTON1_MASK);
		System.out.println("[remote] button 1 press");
		robot.delay(10);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		System.out.println("[remote] button 1 release");
	}

	@Override
	public synchronized void onZoomOut() {
		System.out.println("[remote] onZoomOut");
		robot.mousePress(InputEvent.BUTTON2_MASK);
		System.out.println("[remote] button 2 press");
		robot.mousePress(InputEvent.BUTTON1_MASK);
		System.out.println("[remote] button 1 press");
		onMouseMove(0, -30);
		
		robot.mouseRelease(InputEvent.BUTTON2_MASK);
		System.out.println("[remote] button 2 release");
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		System.out.println("[remote] button 1 release");
		
		onMouseMove(0, 30);
	}

	@Override
	public synchronized void onZoomIn() {
		System.out.println("[remote] onZoomIn");
		robot.mousePress(InputEvent.BUTTON2_MASK);
		System.out.println("[remote] button 2 press");
		robot.mousePress(InputEvent.BUTTON1_MASK);
		System.out.println("[remote] button 1 press");
		onMouseMove(0, 30);
		
		robot.mouseRelease(InputEvent.BUTTON2_MASK);
		System.out.println("[remote] button 2 release");
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		System.out.println("[remote] button 1 release");
		
		onMouseMove(0, -30);
	}
	
	@Override
	public synchronized void onItemTranslationStart() {
		System.out.println("[remote] onItemTranslation");
		if(!isItemTranslated) {
			robot.mousePress(InputEvent.BUTTON2_MASK);
			System.out.println("[remote] button 2 press");
			isItemTranslated = true;
		}
	}
	
	@Override
	public synchronized void onItemTranslationEnd() {
		System.out.println("[remote] onItemTranslationEnd");
		if(isItemTranslated) {
			robot.mouseRelease(InputEvent.BUTTON2_MASK);
			System.out.println("[remote] button 2 press");
			isItemTranslated = false;
		}
	}

	@Override
	public synchronized void onItemRotationStart() {
		System.out.println("[remote] onItemRotation");
		if(!isItemRotated ) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			System.out.println("[remote] button 1 press");
			isItemRotated = true;
		}
	}

	@Override
	public synchronized void onItemRotationEnd() {
		System.out.println("[remote] onItemRotationEnd");
		if(isItemRotated) {
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			System.out.println("[remote] button 1 release");
			isItemRotated = false;
		}
	}
}
