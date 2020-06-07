package ldjam.hamorigami.graphics;

import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.ObjectType;
import ldjam.hamorigami.model.SpiritType;

import java.util.Comparator;

public class EntityOrderComparator implements Comparator<GameObject> {
   @Override
   public int compare(GameObject o1, GameObject o2) {
      if (o1.getType() instanceof SpiritType && o2.getType() instanceof SpiritType) {
         return (int) (o2.getTop() - o1.getTop());
      }
      if (o1.getType() == ObjectType.FLOOR) {
         return -1;
      }
      if (o1.getType() == ObjectType.GAUGE) {
         if (o2.getType() == ObjectType.TREE) {
            return 1;
         }
         if (o2.getType() instanceof SpiritType) {
            return -1;
         }
         return 1;
      }
      if (o1.getType() == ObjectType.TREE) {
         if (o2.getType() == ObjectType.FLOOR) {
            return 1;
         }
         if (o2.getType() == ObjectType.GAUGE) {
            return -1;
         }
         return -1;
      }
      return 0;
   }
}
