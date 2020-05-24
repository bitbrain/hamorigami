package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerImpl;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.util.Colors;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.setup.GameplaySetup;

import static ldjam.hamorigami.GameColors.*;
import static ldjam.hamorigami.GameColors.EVENING_AMBIENT_COLOR;

public class AtmosphericParticleRenderLayer extends RenderLayer2D {

   private final GameplaySetup gameplaySetup;

   private final ParticleManagerImpl manager;

   private final float scattering;

   public AtmosphericParticleRenderLayer(ParticleManagerImpl manager, GameplaySetup gameplaySetup, float scattering) {
      this.manager = manager;
      this.gameplaySetup = gameplaySetup;
      this.scattering = scattering;
   }

   @Override
   public void render(Batch batch, float delta) {
      float eveningFactor = (float) (1f - Math.sin(Math.PI * gameplaySetup.getDayProgress()));
      float dayFactor = 1f - eveningFactor;

      Color middayAtmosphericColor = Color.WHITE.cpy().lerp(MIDDAY_ATMOSPHERIC_COLOR, dayFactor);
      Color eveningAtmosphericColor = middayAtmosphericColor.lerp(EVENING_ATMOSPHERE_COLOR, eveningFactor);
      Color middayAmbientColor = Color.WHITE.cpy().lerp(MIDDAY_AMBIENT_COLOR, dayFactor);
      Color eveningAmbientColor = middayAmbientColor.lerp(EVENING_AMBIENT_COLOR, eveningFactor);

      ShaderProgram program = Asset.get(Assets.Shaders.TINT_SCATTERING, ShaderProgram.class);
      batch.setShader(program);
      batch.begin();
      program.setUniformf("scatterColor", eveningAtmosphericColor);
      program.setUniformf("scatterIntensity", scattering);
      program.setUniformf("tintColor", Colors.lighten(eveningAmbientColor, 1.5f));
      program.setUniformf("tintIntensity", 0.7f);
      manager.draw(batch, delta);
      batch.end();
      batch.setShader(null);
   }
}
