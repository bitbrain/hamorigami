package ldjam.hamorigami.animation;

import de.bitbrain.braingdx.graphics.animation.AnimationTypeResolver;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.Movement;
import ldjam.hamorigami.model.SpiritAnimationType;

public class SpiritAnimationTypeResolver implements AnimationTypeResolver<GameObject> {

   private float lastValue = 0f;

   @Override
   public Object getAnimationType(GameObject object) {
      Movement movement = object.getAttribute(Movement.class);
      float directionAngle = movement.getMovement().angle();
      String direction = "EAST";
      if (directionAngle < 90f || directionAngle >= 270) {
         direction = "WEST";
      }
      String type = "HOVERING";
      if (object.hasAttribute("landing")) {
         type = "LANDING";
      } else if (object.hasAttribute("falling")) {
         type = "FALLING";
      } else if (object.hasAttribute("attacking")) {
         type = "ATTACKING";
      } else if (object.hasAttribute("swiping")) {
         if (object.getLeft() < lastValue) {
            direction = "WEST";
         }else {
            direction = "EAST";
         }
         lastValue = object.getLeft();
         type = "SWIPING";
      } else if (movement.getMovement().len() < 25f) {
         type = "IDLE";
      } else if (movement.getMovement().len() < 20f && object.hasAttribute(SpiritAnimationType.class)) {
         return object.getAttribute(SpiritAnimationType.class);
      }
      SpiritAnimationType animationType = SpiritAnimationType.valueOf(type + "_" + direction);
      object.setAttribute(SpiritAnimationType.class, animationType);
      return animationType;
   }
}
