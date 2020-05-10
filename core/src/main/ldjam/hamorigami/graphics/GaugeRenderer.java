package ldjam.hamorigami.graphics;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.model.TreeStatus;

public class GaugeRenderer extends SpriteRenderer {

   private final GameObject treeObject;
   private final Sprite waterLevelSprite;
   private final Sprite waterLevelTopSprite;
   private final Sprite gaugeGrassSprite;
   private final ValueProvider valueProvider = new ValueProvider();

   private float oldLevel = -999f;

   public GaugeRenderer(GameObject treeObject) {
      super(Assets.Textures.GAUGE_OVERLAY);
      this.treeObject = treeObject;
      valueProvider.setValue(0.3f);
      waterLevelSprite = new Sprite(Asset.get(Assets.Textures.GAUGE_WATER, Texture.class));
      waterLevelTopSprite = new Sprite(Asset.get(Assets.Textures.GAUGE_WATER_TOP, Texture.class));
      gaugeGrassSprite = new Sprite(Asset.get(Assets.Textures.GAUGE_GRASS, Texture.class));
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      float currentLevel = treeObject.getAttribute(TreeStatus.class).getSoilWaterLevel() * 1.05f;

      if (oldLevel < -1 || oldLevel != currentLevel) {
         SharedTweenManager.getInstance().killTarget(valueProvider);
         Tween.to(valueProvider, ValueTween.VALUE, 0.3f)
               .target(currentLevel)
               .start(SharedTweenManager.getInstance());
      }
      oldLevel = currentLevel;
      float level = valueProvider.getValue();
      if (level > 0.85f) {
         level = 0.85f;
      }
      if (level > 0.07f) {
         waterLevelSprite.setPosition(object.getLeft(), object.getTop());
         waterLevelSprite.setSize(object.getWidth(), object.getHeight() * level);
         waterLevelSprite.draw(batch, 1f);
         int height = (int) (object.getHeight() * level);
         waterLevelSprite.setRegion(0, height, 16, -height);
         waterLevelTopSprite.setPosition(object.getLeft(), object.getTop() + object.getHeight() * level - 4f);
         waterLevelTopSprite.setSize(16f, 8f);
         waterLevelTopSprite.draw(batch, 1f);
      }
      super.render(object, batch, delta);
      gaugeGrassSprite.setPosition(object.getLeft() - 5f, object.getTop() -4f);
      gaugeGrassSprite.setSize(24f, 16f);
      gaugeGrassSprite.draw(batch, 1f);
   }
}
