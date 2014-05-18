package com.fksu.edu.remote;

public interface RemoteEventsListener {

	int SCROLL_UP = 1;
	int SCROLL_DOWN = 2;

	void onScroll(int direction);

	void onMouseMove(float x, float y);

	void onLeftMouseButtonClick();

	void onLeftMouseButtonDoubleClick();

	void onRightMouseButtonClick();

	void onMiddleMouseButtonClick();

	void onLeftMouseButtonDown();

}
