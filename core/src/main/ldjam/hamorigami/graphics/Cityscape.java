package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.GameColors;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.setup.GameplaySetup;
import ldjam.hamorigami.weather.WeatherManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ldjam.hamorigami.GameColors.*;

public class Cityscape extends RenderLayer2D {

   public static class Layer {
      public final float parallaxity;
      public final float scattering;
      public final String texture;

      public Layer(float parallaxity, float scattering, String texture) {
         this.parallaxity = parallaxity;
         this.scattering = scattering;
         this.texture = texture;
      }
   }

   private final GameplaySetup setup;

   private final List<Layer> layers = new ArrayList<>();
   private final Map<Layer, ParallaxMap> parallaxMap = new HashMap<Layer, ParallaxMap>();
   private final WeatherManager weatherManager;

   public Cityscape(GameplaySetup setup, HamorigamiContext context, Layer... parallaxLayers) {
      this.setup = setup;
      this.weatherManager = context.getWeatherManager();
      for (Layer layer : parallaxLayers) {
         layers.add(layer);
         ParallaxMap map = new ParallaxMap(layer.texture, context.getGameCamera(), layer.parallaxity);
         map.scale(1.1f);
         parallaxMap.put(layer, map);
      }
   }

   @Override
   public void render(Batch batch, float delta) {

      float eveningFactor = (float) (1f - Math.sin(Math.PI * setup.getDayProgress()));
      float dayFactor = 1f - eveningFactor;

      ShaderProgram program = Asset.get(Assets.Shaders.TINT_SCATTERING, ShaderProgram.class);
      batch.setShader(program);
      for (Layer layer : layers) {
         batch.begin();
         program.setUniformf("scatterColor", getScatteringColor(weatherManager, eveningFactor, dayFactor));
         program.setUniformf("tintColor", getAmbientColor(weatherManager, eveningFactor, dayFactor));
         program.setUniformf("tintIntensity", 0.7f);
         program.setUniformf("scatterIntensity", layer.scattering);
         parallaxMap.get(layer).draw(batch);
         batch.end();
      }
      batch.setShader(null);
   }
}
