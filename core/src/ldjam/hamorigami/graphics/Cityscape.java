package ldjam.hamorigami.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import ldjam.hamorigami.effects.DayProgress;

import static ldjam.hamorigami.Assets.Textures.CITY_DAY;
import static ldjam.hamorigami.Assets.Textures.CITY_EVENING;

public class Cityscape extends RenderLayer2D {

   private final DayProgress dayProgress;

   public Cityscape(DayProgress dayProgress) {
      this.dayProgress = dayProgress;
   }

   @Override
   public void render(Batch batch, float delta) {
      batch.begin();
      float x = Gdx.graphics.getWidth() / 2f - 400;
      float y = Gdx.graphics.getHeight() / 2f - 300;
      Texture background = SharedAssetManager.getInstance().get(CITY_DAY, Texture.class);
      batch.draw(background, x, y);
      if (dayProgress.getCurrentProgress() > 0f) {
         Texture background_noon = SharedAssetManager.getInstance().get(CITY_EVENING, Texture.class);
         Color color = batch.getColor();
         batch.setColor(1f, 1f, 1f, dayProgress.getCurrentProgress());
         batch.draw(background_noon, x, y);
         batch.setColor(color);
      }
      batch.end();
   }
}
