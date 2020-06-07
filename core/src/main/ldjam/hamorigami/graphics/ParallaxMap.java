package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.GameCamera;

public class ParallaxMap {

   private final float initialOffsetX;
   private final float initialOffsetY;
   private Sprite sprite;

   private GameCamera camera;

   private double parallaxity;

   private float width, height;

   private float scale;

   private Vector2 offset;

   private float alpha = 1f;

   public ParallaxMap(String assetId, GameCamera camera, double parallaxity) {
      this.camera = camera;
      this.sprite = new Sprite(Asset.get(assetId, Texture.class));
      this.parallaxity = parallaxity;
      this.width = sprite.getWidth();
      this.height = sprite.getHeight();

      double targetX = getTarget(camera.getLeft());
      double targetY = getTarget(camera.getTop());
      this.initialOffsetX = (float) (targetX - camera.getLeft());
      this.initialOffsetY = (float) (targetY - camera.getTop());
   }

   public float getParallaxity() {
      return (float) parallaxity;
   }

   public void scale(float scale) {
      this.scale = scale;
   }

   public void setColor(Color color) {
      sprite.setColor(color);
   }

   public void draw(Batch batch) {
      float scaleOffetX = -(width * this.scale - width) / 2f;
      float scaleOffetY = -(height * this.scale - height) / 2f;

      float baseX = camera.getLeft() - camera.getShake().x;
      float baseY = camera.getTop() - camera.getShake().y;
      // correct X position
      double targetX = getTarget(baseX);
      double targetY = getTarget(baseY);
      sprite.setPosition(
            (float)(targetX - initialOffsetX + scaleOffetX),
            (float)targetY - initialOffsetY + scaleOffetY);
      sprite.setSize(width * this.scale, height * this.scale);
      sprite.draw(batch, alpha);
   }

   private double getTarget(double focus) {
      return focus / parallaxity;
   }
}
