package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerImpl;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.setup.GameplaySetup;
import ldjam.hamorigami.weather.WeatherManager;

import static ldjam.hamorigami.GameColors.*;

public class AtmosphericParticleRenderLayer extends RenderLayer2D {

   private final GameplaySetup gameplaySetup;

   private final ParticleManagerImpl manager;

   private final float scattering;

   private final WeatherManager weatherManager;

   public AtmosphericParticleRenderLayer(ParticleManagerImpl manager, WeatherManager weatherManager, GameplaySetup gameplaySetup, float scattering) {
      this.manager = manager;
      this.gameplaySetup = gameplaySetup;
      this.scattering = scattering;
      this.weatherManager = weatherManager;
   }

   @Override
   public void render(Batch batch, float delta) {
      float eveningFactor = (float) (1f - Math.sin(Math.PI * gameplaySetup.getDayProgress()));
      float dayFactor = 1f - eveningFactor;

      ShaderProgram program = Asset.get(Assets.Shaders.TINT_SCATTERING, ShaderProgram.class);
      batch.setShader(program);
      batch.begin();
      program.setUniformf("scatterColor", getScatteringColor(weatherManager, eveningFactor, dayFactor));
      program.setUniformf("tintColor", getAmbientColor(weatherManager, eveningFactor, dayFactor));
      program.setUniformf("scatterIntensity", scattering);
      program.setUniformf("tintIntensity", 0.7f);
      manager.draw(batch, delta);
      batch.end();
      batch.setShader(null);
   }
}
