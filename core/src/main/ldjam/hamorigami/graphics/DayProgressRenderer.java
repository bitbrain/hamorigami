package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.setup.GameplaySetup;

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
      object.setColor(Color.WHITE.cpy().lerp(EVENING_ATMOSPHERE_COLOR, interpolationValue));
      super.render(object, batch, delta);
   }
}
