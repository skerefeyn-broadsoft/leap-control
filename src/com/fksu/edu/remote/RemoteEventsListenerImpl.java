package com.fksu.edu.remote;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;


public class RemoteEventsListenerImpl implements RemoteEventsListener {

	private Robot robot;

	private boolean isSceneMoved = false;

	private boolean isItemMoved = false;
	
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
	public synchronized void onSceneMove() {
		System.out.println("[remote] onSceneMove");
		if(!isSceneMoved) {
			robot.mousePress(InputEvent.BUTTON2_MASK);
			System.out.println("[remote] button 2 press");
			isSceneMoved = true;
		}
	}
	
	@Override
	public synchronized void onSceneRelease() {
		System.out.println("[remote] onSceneRelease");
		if(isSceneMoved) {
			robot.mouseRelease(InputEvent.BUTTON2_MASK);
			System.out.println("[remote] button 2 press");
			isSceneMoved = false;
		}
	}

	@Override
	public synchronized void onItemMove() {
		System.out.println("[remote] onItemMove");
		if(!isItemMoved ) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			System.out.println("[remote] button 1 press");
			isItemMoved = true;
		}
	}

	@Override
	public synchronized void onItemRelease() {
		System.out.println("[remote] onItemRelease");
		if(isItemMoved) {
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			System.out.println("[remote] button 1 release");
			isItemMoved = false;
		}
	}
}
