package com.fksu.edu.remote;

public interface RemoteEventsListener {

	public void onZoomOut();
	
	public void onZoomIn();

	public void onMouseMove(float x, float y);

	public void onLeftMouseButtonClick();

	public void onSceneMove();
	
	public void onSceneRelease();
}
