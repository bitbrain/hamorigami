package ldjam.hamorigami.entity;

import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.SpiritType;

public class SpiritSpawner implements Updateable {

   private final EntityFactory entityFactory;
   private final GameContext2D context;
   private final GameObject treeObject;
   private final SpiritSpawnPool spawnPool;

   private SpiritSpawnPool.SpiritSpawn currentSpawn;
   private final DeltaTimer deltaTimer = new DeltaTimer();
   private boolean gameOver = false;

   public SpiritSpawner(SpiritSpawnPool spawnPool, EntityFactory entityFactory, GameContext2D context, GameObject treeObject) {
      this.spawnPool = spawnPool;
      this.entityFactory = entityFactory;
      this.context = context;
      this.treeObject = treeObject;
      currentSpawn = spawnPool.getNext();
   }

   public boolean canSpawn() {
      return !gameOver;
   }

   @Override
   public void update(float delta) {
      if (gameOver) {
         return;
      }
      deltaTimer.update(delta);
      if (currentSpawn == null || deltaTimer.reached(currentSpawn.durationUntil)) {
         if (currentSpawn != null) {
            for (SpiritType type : currentSpawn.spawns) {
               spawnSpirit(type);
            }
         }
         currentSpawn = spawnPool.getNext();
         if (currentSpawn == null) {
            gameOver = true;
         }
      }
   }

   private void spawnSpirit(SpiritType type) {
      GameObject spirit = entityFactory.spawnSpirit(type, -32, -64);
      type.getSpawnEffect().onSpawnSpirit(spirit, treeObject, context);
   }
}
