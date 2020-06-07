package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.setup.GameplaySetup;
import ldjam.hamorigami.weather.WeatherManager;

import static ldjam.hamorigami.GameColors.*;
import static ldjam.hamorigami.GameColors.getAmbientColor;

public class DayProgressRenderer extends SpriteRenderer {

   private final GameplaySetup setup;
   private final WeatherManager weatherManager;

   public DayProgressRenderer(GameplaySetup setup, WeatherManager weatherManager, String dayTextureId) {
      super(dayTextureId);
      this.setup = setup;
      this.weatherManager = weatherManager;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      float eveningFactor = (float) (1f - Math.sin(Math.PI * setup.getDayProgress()));
      float dayFactor = 1f - eveningFactor;

      ShaderProgram program = Asset.get(Assets.Shaders.TINT_SCATTERING, ShaderProgram.class);
      batch.setShader(program);
      program.setUniformf("scatterIntensity", 0f);
      program.setUniformf("tintIntensity", 0.5f);
      program.setUniformf("scatterColor", getScatteringColor(weatherManager, eveningFactor, dayFactor));
      program.setUniformf("tintColor", getAmbientColor(weatherManager, eveningFactor, dayFactor));
      super.render(object, batch, delta);
      batch.setShader(null);
   }
}
