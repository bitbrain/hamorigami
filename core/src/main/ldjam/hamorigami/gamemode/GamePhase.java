package ldjam.hamorigami.gamemode;

import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.context.HamorigamiContext;

public interface GamePhase extends Updateable {
   void disable(HamorigamiContext context, GameObject treeObject);
   void enable(HamorigamiContext context, GameObject treeObject);
   boolean isFinished();
}
