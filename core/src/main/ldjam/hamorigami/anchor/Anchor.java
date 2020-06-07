package ldjam.hamorigami.anchor;

import de.bitbrain.braingdx.world.GameObject;

public class Anchor {

   private final GameObject object;

   public Anchor(GameObject object) {
      this.object = object;
   }

   /**
    * Retrieves the horizontal position based on the mask provided.
    */
   public float getX(int mask) {
      if (isSet(mask, AnchorMask.EAST)) {
         return object.getRight();
      } else if (isSet(mask, AnchorMask.WEST)) {
         return object.getLeft();
      }
      // LEFT IS DEFAULT
      return object.getLeft() + object.getWidth() / 2f;
   }

   public float getY(int mask) {
      if (isSet(mask, AnchorMask.NORTH)) {
         return object.getBottom();
      } else if (isSet(mask, AnchorMask.SOUTH)) {
         return object.getTop();
      }
      // TOP IS DEFAULT
      return object.getTop() + object.getHeight() / 2f;
   }

   private static boolean isSet(int mask, AnchorMask orientation) {
      return orientation.getByte() == (mask & orientation.getByte());
   }
}
