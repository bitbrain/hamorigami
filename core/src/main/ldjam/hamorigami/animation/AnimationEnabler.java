package ldjam.hamorigami.animation;

import de.bitbrain.braingdx.util.Enabler;
import de.bitbrain.braingdx.world.GameObject;

public class AnimationEnabler implements Enabler<GameObject> {

   private boolean attackSeen;
   @Override
   public boolean isEnabledFor(GameObject target) {
      if (target.hasAttribute("attacking")) {
         attackSeen = true;
      }
      if (attackSeen && !target.hasAttribute("attacking")) {
         attackSeen = false;
         return false;
      }
      return true;
   }
}
