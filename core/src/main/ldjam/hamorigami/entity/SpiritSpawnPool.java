package ldjam.hamorigami.entity;

import ldjam.hamorigami.model.SpiritType;

import java.util.*;

public class SpiritSpawnPool {

   private final Comparator<SpiritSpawn> spawnComparator = new Comparator<SpiritSpawn>() {
      @Override
      public int compare(SpiritSpawn o1, SpiritSpawn o2) {
         return (int) (o1.durationUntil - o2.durationUntil);
      }
   };

   public class SpiritSpawn {
      public SpiritType[] spawns;
      public final float durationUntil;

      public SpiritSpawn(float durationUntil, SpiritType[] spawns) {
         this.durationUntil = durationUntil;
         this.spawns = spawns;
      }

      private void addSpawns(SpiritType ... spawns) {
         int aLen = this.spawns.length;
         int bLen = spawns.length;
         SpiritType[] result = new SpiritType[aLen + bLen];
         System.arraycopy(this.spawns, 0, result, 0, aLen);
         System.arraycopy(spawns, 0, result, aLen, bLen);
         this.spawns = result;
      }

      @Override
      public String toString() {
         return "SpiritSpawn{" +
               "spawns=" + Arrays.toString(spawns) +
               ", durationUntil=" + durationUntil +
               '}';
      }
   }

   private final Map<Float, SpiritSpawn> spawnMap = new HashMap<Float, SpiritSpawn>();
   private final List<SpiritSpawn> spawnList = new ArrayList<>();

   private int currentIndex = 0;

   public void addSpawnWave(float durationUntilNext, SpiritType... spawns) {
      SpiritSpawn spawn = spawnMap.get(durationUntilNext);
      if (spawn == null) {
         spawnMap.put(durationUntilNext, new SpiritSpawn(durationUntilNext, spawns));
         spawnList.add(spawnMap.get(durationUntilNext));
      } else {
         spawnMap.get(durationUntilNext).addSpawns(spawns);
      }
      Collections.sort(spawnList, spawnComparator);
   }

   public SpiritSpawn getNext() {
      if (spawnMap.isEmpty() || currentIndex >= spawnList.size()) {
         return null;
      }
      SpiritSpawn current = spawnList.get(currentIndex);
      currentIndex++;
      if (currentIndex == spawnList.size()) {
         return null;
      }
      return current;
   }
}
