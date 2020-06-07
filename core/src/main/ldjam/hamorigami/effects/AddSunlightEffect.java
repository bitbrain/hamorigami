package ldjam.hamorigami.effects;

import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.TreeStatus;

public class AddSunlightEffect implements SpiritAbsorbEffect {

   @Override
   public boolean applyEffect(GameObject treeObject) {
      treeObject.getAttribute(TreeStatus.class).decreaseSoilWater(0.02f);
      treeObject.getAttribute(TreeStatus.class).decreaseTreeWateredLevel(0.01f);
      if (treeObject.getAttribute(TreeStatus.class).getSoilWaterLevel() == 0f) {
         // tree dead
         return false;
      }
      return true;
   }
}
