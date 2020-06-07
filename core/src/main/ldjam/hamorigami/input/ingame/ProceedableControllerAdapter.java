package ldjam.hamorigami.input.ingame;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.mappings.Xbox;
import ldjam.hamorigami.input.Proceedable;

public class ProceedableControllerAdapter extends ControllerAdapter {

   private final Proceedable proceedable;

   public ProceedableControllerAdapter(Proceedable proceedable) {
      this.proceedable = proceedable;
   }



   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      if (buttonIndex == getSkipButton(controller)) {
         proceedable.skip();
         return true;
      }
      proceedable.proceed();
      return true;
   }

   private int getSkipButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.B;
      }
      return -1;
   }
}
