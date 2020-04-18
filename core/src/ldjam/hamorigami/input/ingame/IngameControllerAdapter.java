package ldjam.hamorigami.input.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.Movement;

public class IngameControllerAdapter extends ControllerAdapter implements Updateable {

   private final Vector2 moveDirection = new Vector2();

   private final GameObject playerObject;
   private float horizontalValue;
   private float verticalValue;

   public IngameControllerAdapter(GameObject playerObject) {
      this.playerObject = playerObject;
   }

   @Override
   public void update(float delta) {
      moveDirection.set(horizontalValue, verticalValue);
      if (moveDirection.len() > 0.2f) {
         playerObject.getAttribute(Movement.class).move(moveDirection);
      }
   }

   @Override
   public boolean axisMoved(Controller controller, int axisIndex, float value) {
      this.horizontalValue = controller.getAxis(Xbox.L_STICK_HORIZONTAL_AXIS);
      this.verticalValue = -controller.getAxis(Xbox.L_STICK_VERTICAL_AXIS);
      return true;
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      if (buttonIndex == getEscapeButton(controller)) {
         Gdx.app.exit();
         return true;
      }
      return false;
   }

   private int getEscapeButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.START;
      }
      return -1;
   }
}
