package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.setup.GameplaySetup;

import static ldjam.hamorigami.Assets.Textures.*;
import static ldjam.hamorigami.GameColors.EVENING_COLOR;

public class Cityscape extends RenderLayer2D {

   private final GameplaySetup setup;
   private final HamorigamiContext context;

   private final ParallaxMap cityFront;
   private final ParallaxMap cityBack;
   private final ParallaxMap cityFar;

   public Cityscape(GameplaySetup setup, HamorigamiContext context) {
      this.context = context;
      this.setup = setup;
      cityFront = new ParallaxMap(CITYSCAPE_FRONT, context.getGameCamera(), 1.4f);
      cityFront.scale(1.1f);
      cityBack = new ParallaxMap(CITYSCAPE_MIDDLE, context.getGameCamera(), 1.3f);
      cityBack.scale(1.1f);
      cityFar = new ParallaxMap(CITYSCAPE_FAR, context.getGameCamera(), 1.1f);
      cityFar.scale(1.1f);
   }

   @Override
   public void render(Batch batch, float delta) {
      batch.begin();
      float x = context.getGameCamera().getLeft();
      float y = context.getGameCamera().getTop();

      Texture background = Asset.get(SKY_DAY, Texture.class);
      batch.draw(background, x, y);

      Texture background_noon = Asset.get(SKY_EVENING, Texture.class);
      Color color = batch.getColor();
      float transitionValue = (float) (1f - Math.sin(Math.PI * setup.getDayProgress()));
      batch.setColor(1f, 1f, 1f, transitionValue);
      batch.draw(background_noon, x, y);

      Color eveningColor = Color.WHITE.cpy().lerp(EVENING_COLOR, transitionValue);

      cityFar.setColor(eveningColor);
      cityFar.draw(batch);
      cityBack.setColor(eveningColor);
      cityBack.draw(batch);
      cityFront.setColor(eveningColor);
      cityFront.draw(batch);

      batch.setColor(color);
      batch.end();
   }
}
