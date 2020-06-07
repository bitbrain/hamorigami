package ldjam.hamorigami;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameSettings;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.debug.BrainGdxDebug;
import de.bitbrain.braingdx.event.GameEventManagerImpl;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.GraphicsSettings;
import de.bitbrain.braingdx.graphics.postprocessing.filters.RadialBlur;
import de.bitbrain.braingdx.screens.AbstractScreen;
import ldjam.hamorigami.graphics.PhasedGFX;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.screens.StoryModeScreen;
import ldjam.hamorigami.ui.Styles;
import org.apache.commons.lang.SystemUtils;

public class HamorigamiGame extends BrainGdxGame {

   private ScreenMode screenMode = ScreenMode.FULLSCREEN;

   private enum ScreenMode {
      SCREENSHOT,
      GIF,
      FULLSCREEN
   }

   private boolean debug;

   public HamorigamiGame(String[] args) {
      screenMode = ScreenMode.FULLSCREEN;
      for (String arg : args) {
         if (arg.equals("screenshot")) {
            screenMode = ScreenMode.SCREENSHOT;
         }
         if (arg.equals("gif")) {
            screenMode = ScreenMode.GIF;
         }
         if (arg.equals("debug")) {
            debug = true;
         }
      }
   }

   public boolean isDebug() {
      return debug;
   }

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new SmartAssetLoader(Assets.class);
   }

   @Override
   protected AbstractScreen<?, ?> getInitialScreen() {
      PhasedGFX.setTexture(GraphicsFactory.createTexture(1, 1, Color.WHITE));
      Gdx.input.setCursorCatched(true);
      Bundle.load();
      Styles.init();
      BrainGdxDebug.setLabelStyle(Styles.LABEL_DEBUG);
      configureSettings();
      Music cityscape = Asset.get(Assets.Musics.CITYSCAPE, Music.class);
      cityscape.setLooping(true);
      cityscape.setVolume(0.1f);
      cityscape.play();
      return new StoryModeScreen(this);
   }

   private void configureSettings() {
      GameSettings settings = new GameSettings(new GameEventManagerImpl());
      GraphicsSettings graphicsSettings = settings.getGraphics();
      if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
         if (screenMode == ScreenMode.SCREENSHOT) {
            Gdx.graphics.setWindowedMode(3840, 2160);
         } else if (screenMode == ScreenMode.GIF) {
            Gdx.graphics.setWindowedMode(1248, 770);
         } else if (SystemUtils.IS_OS_WINDOWS) {
            Gdx.graphics.setUndecorated(true);
            Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
         } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
         }
         graphicsSettings.setRadialBlurQuality(RadialBlur.Quality.High);
         graphicsSettings.setRenderScale(0.1f);
         graphicsSettings.setParticleMultiplier(0.7f);
         graphicsSettings.save();
      }
   }
}
