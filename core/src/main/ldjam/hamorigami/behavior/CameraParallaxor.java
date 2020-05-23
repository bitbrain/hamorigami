package ldjam.hamorigami.behavior;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Adjusts the camera position slightly to the player position
 * to achieve a parallax effect.
 */
public class CameraParallaxor extends BehaviorAdapter {

   public static final float FACTOR = 30f;

   private final Vector2 tmp = new Vector2();

   private final GameCamera gameCamera;

   private Vector2 initialPosition;

   public CameraParallaxor(GameCamera camera) {
      this.gameCamera = camera;
   }

   @Override
   public void update(GameObject source, float delta) {
      if (initialPosition == null) {
         initialPosition = new Vector2(gameCamera.getPosition().x, gameCamera.getPosition().y);
      }

      tmp.x = (source.getLeft()) / gameCamera.getScaledCameraWidth();
      tmp.y = (source.getTop()) / gameCamera.getScaledCameraHeight();

      tmp.scl(20f);

      gameCamera.setPosition(initialPosition.x + tmp.x - 45f, initialPosition.y + tmp.y - 35f);
   }
}
