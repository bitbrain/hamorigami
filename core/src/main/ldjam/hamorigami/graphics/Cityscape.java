package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.setup.GameplaySetup;

import static ldjam.hamorigami.Assets.Textures.*;

public class Cityscape extends RenderLayer2D {

   private final GameplaySetup setup;
   private final HamorigamiContext context;

   private final ParallaxMap cityFront;
   private final ParallaxMap cityBack;
   private final ParallaxMap cityFar;
   private final ParallaxMap cityFrontEvening;
   private final ParallaxMap cityBackEvening;
   private final ParallaxMap cityFarEvening;

   public Cityscape(GameplaySetup setup, HamorigamiContext context) {
      this.context = context;
      this.setup = setup;
      cityFront = new ParallaxMap(CITYSCAPE_DAY_FRONT, context.getGameCamera(), 1.4f);
      cityFront.scale(1.1f);
      cityFrontEvening = new ParallaxMap(CITYSCAPE_EVENING_FRONT, context.getGameCamera(), 1.4f);
      cityFrontEvening.scale(1.1f);
      cityBack = new ParallaxMap(CITYSCAPE_DAY_MIDDLE, context.getGameCamera(), 1.2f);
      cityBack.scale(1.1f);
      cityBackEvening = new ParallaxMap(CITYSCAPE_EVENING_MIDDLE, context.getGameCamera(), 1.2f);
      cityBackEvening.scale(1.1f);
      cityFar = new ParallaxMap(CITYSCAPE_DAY_FAR, context.getGameCamera(), 1.01f);
      cityFar.scale(1.1f);
      cityFarEvening = new ParallaxMap(CITYSCAPE_EVENING_FAR, context.getGameCamera(), 1.01f);
      cityFarEvening.scale(1.1f);
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
      float alpha = (float) (1f - Math.sin(Math.PI * setup.getDayProgress()));
      batch.setColor(1f, 1f, 1f, alpha);
      batch.draw(background_noon, x, y);
      batch.setColor(color);

      cityFar.draw(batch);
      cityFarEvening.setAlpha(alpha);
      cityFarEvening.draw(batch);
      cityBack.draw(batch);
      cityBackEvening.setAlpha(alpha);
      cityBackEvening.draw(batch);
      cityFront.draw(batch);
      cityFrontEvening.setAlpha(alpha);
      cityFrontEvening.draw(batch);
      batch.end();
   }
}
