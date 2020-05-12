package ldjam.hamorigami.anchor;

import de.bitbrain.braingdx.world.GameObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Anchors are a way to use instead of a particular position. This allows to
 * define certain positions on the screen without knowing details about exact pixel positions.
 */
public class AnchorManager {

   private final Map<String, Anchor> anchors = new HashMap<>();

   public Anchor addAnchor(String name, GameObject object) {
      if (!anchors.containsKey(name)) {
         anchors.put(name, new Anchor(object));
      }

      return anchors.get(name);
   }

   public Anchor getAnchor(String name) {
      return anchors.get(name);
   }

   public void remove(String id) {
      this.anchors.remove(id);
   }
}
