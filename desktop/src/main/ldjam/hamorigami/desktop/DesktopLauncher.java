package ldjam.hamorigami.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ldjam.hamorigami.HamorigamiGame;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "hamorigami";
		config.forceExit = true;
		config.resizable = false;
		new LwjglApplication(new HamorigamiGame(args), config);
	}
}
