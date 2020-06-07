package ldjam.hamorigami.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.world.GameObject;

public class Movement extends BehaviorAdapter {

   private final Vector2 lookDirection = new Vector2(0f, 1f);
   private final Vector2 moveDirection = new Vector2(0f, 1f);
   private final Vector3 tmp = new Vector3();
   private final GameCamera gameCamera;
   private final float maxSpeed;
   private Vector2 movement = new Vector2();
   private GameObject gameObject;

   public Movement(float maxSpeed, GameCamera gameCamera) {
      this.maxSpeed = maxSpeed;
      this.gameCamera = gameCamera;

   }

   public void move(Vector2 direction, float amount) {
      if (gameObject == null) {
         return;
      }
      if (!gameObject.hasAttribute("gravity") || (gameObject.hasAttribute("gravity") && gameObject.getTop() == gameCamera.getTop())) {
         moveDirection.x = direction.x;
         moveDirection.y = direction.y;
         moveDirection.nor();
         movement.x += moveDirection.x * amount;
         movement.y += moveDirection.y * amount;
      }
   }

   public void move(Vector2 direction) {
      move(direction, maxSpeed);
   }

   public Vector2 getLookDirection() {
      return lookDirection;
   }

   public Vector2 getMoveDirection() {
      return moveDirection;
   }

   public Vector2 getMovement() {
      return movement;
   }

   public void lookAtScreen(float screenX, float screenY) {
      tmp.set(screenX, screenY, 0f);
      gameCamera.getInternalCamera().unproject(tmp);
      lookAtWorld(tmp.x, tmp.y);
   }

   public void lookAtWorld(float worldX, float worldY) {
      if (gameObject != null) {
         lookDirection.x = gameObject.getLeft() + gameObject.getWidth() / 2f - worldX;
         lookDirection.y = gameObject.getTop() + gameObject.getHeight() / 2f - worldY;
         lookDirection.nor();
      }
   }

   public void jump() {
      if (gameObject == null) {
         return;
      }
      if (gameObject.hasAttribute("gravity") && gameObject.getTop() == gameCamera.getTop()) {
         movement.y += 1000;
      }
   }

   @Override
   public void onAttach(GameObject source) {
      this.gameObject = source;
   }

   @Override
   public void update(GameObject source, float delta) {
      float moveX = movement.x * delta;
      float moveY = movement.y * delta;
      gameObject.move(moveX, moveY);
      if (gameObject.hasAttribute("gravity")) {
         if (gameObject.getTop() > gameCamera.getTop()) {
            movement.y *= 0.9f;
            gameObject.move(0f, -(410 - gameObject.getTop()) * delta);
         } else {
            movement.scl(0.9f);
         }
      } else {
         movement.scl(0.9f);
      }

      if (!gameObject.hasAttribute("falling")) {
         // Avoid clipping outside of the screen
         if (gameObject.getLeft() < gameCamera.getLeft()) {
            gameObject.setPosition(gameCamera.getLeft(), gameObject.getTop());
         }
         if (gameObject.getRight() > gameCamera.getLeft() + gameCamera.getScaledCameraWidth()) {
            gameObject.setPosition(gameCamera.getLeft() + gameCamera.getScaledCameraWidth() - gameObject.getWidth(), gameObject.getTop());
         }
         if (gameObject.getTop() < gameCamera.getTop() + 40f) {
            gameObject.setPosition(gameObject.getLeft(), gameCamera.getTop() + 40f);
         }
         if (gameObject.getBottom() > gameCamera.getTop() + gameCamera.getScaledCameraHeight()) {
            gameObject.setPosition(gameObject.getLeft(), gameCamera.getTop() + gameCamera.getScaledCameraHeight() - gameObject.getHeight());
         }
      }
   }
}
