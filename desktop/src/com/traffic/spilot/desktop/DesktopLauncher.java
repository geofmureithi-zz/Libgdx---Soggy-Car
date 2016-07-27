package com.traffic.spilot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.traffic.spilot.TrafficGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = TrafficGame.G_TITLE;

//		config.width = 320;
//		config.height = 480;

		config.width = 480;
		config.height = 800;

//		config.width = 720;
//		config.height = 1280;

//		config.width = 1080;
//		config.height = 1920;

		new LwjglApplication(new TrafficGame(new DesktopInterface()), config);
	}
}
