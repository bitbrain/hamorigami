package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.setup.GameplaySetup;

import static ldjam.hamorigami.GameColors.EVENING_AMBIENT_COLOR;
import static ldjam.hamorigami.GameColors.EVENING_ATMOSPHERE_COLOR;

public class DayProgressRenderer extends SpriteRenderer {

   private final GameplaySetup setup;

   public DayProgressRenderer(GameplaySetup setup, String dayTextureId) {
      super(dayTextureId);
      this.setup = setup;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      float curentProgress = setup.getDayProgress();
      float interpolationValue = 1f - (float) Math.sin(Math.PI * curentProgress);
      Color color = Color.WHITE.cpy().lerp(EVENING_AMBIENT_COLOR, interpolationValue);
      ShaderProgram program = Asset.get(Assets.Shaders.TINT_SCATTERING, ShaderProgram.class);
      batch.setShader(program);
      program.setUniformf("scatterColor", Color.WHITE);
      program.setUniformf("scatterIntensity", 0f);
      program.setUniformf("tintColor", color);
      program.setUniformf("tintIntensity", 1f);
      super.render(object, batch, delta);
      batch.setShader(null);
   }
}
