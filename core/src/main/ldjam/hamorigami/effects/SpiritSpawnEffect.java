package ldjam.hamorigami.effects;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.world.GameObject;

public interface SpiritSpawnEffect {

   void onSpawnSpirit(GameObject spirit, GameObject tree, GameContext2D context);
}
