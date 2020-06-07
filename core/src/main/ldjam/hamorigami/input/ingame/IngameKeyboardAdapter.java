package ldjam.hamorigami.input.ingame;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.entity.AttackHandler;
import ldjam.hamorigami.input.Proceedable;
import ldjam.hamorigami.model.Movement;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys.*;

public class IngameKeyboardAdapter extends InputAdapter implements Updateable {

   private final GameObject playerObject;
   private final Vector2 moveDirection = new Vector2();
   private final Vector2 speed = new Vector2();
   private final AttackHandler attackHandler;
   private final Proceedable proceedable;

   private boolean buttonUp = false;

   public IngameKeyboardAdapter(GameObject playerObject, AttackHandler attackHandler, Proceedable proceedable) {
      this.playerObject = playerObject;
      this.attackHandler = attackHandler;
      this.proceedable = proceedable;
   }

   @Override
   public void update(float delta) {
      if (!buttonUp && !input.isKeyPressed(ESCAPE)) {
         buttonUp = true;
         return;
      }
      if (buttonUp && input.isKeyPressed(ESCAPE)) {
         proceedable.skip();
      }
      if (input.isKeyPressed(W)) {
         moveDirection.y = 1;
      }
      if (input.isKeyPressed(A)) {
         moveDirection.x = -1;
      }
      if (input.isKeyPressed(S)) {
         moveDirection.y = -1;
      }
      if (input.isKeyPressed(D)) {
         moveDirection.x = 1;
      }
      if (input.isKeyPressed(SPACE)) {
         if (playerObject.hasAttribute(Movement.class)) {
            playerObject.getAttribute(Movement.class).jump();
         }
      }
      if (input.isTouched() || input.isKeyPressed(ENTER)) {
         attackHandler.attack();
      }
      if (playerObject.hasAttribute(Movement.class)) {
         playerObject.getAttribute(Movement.class).move(moveDirection);
      }
      moveDirection.x = 0;
      moveDirection.y = 0;
      speed.x = 0;
      speed.y = 0;
   }
}
