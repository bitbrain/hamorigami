package ldjam.hamorigami.setup;

import ldjam.hamorigami.cutscene.Cutscene;
import ldjam.hamorigami.model.SpiritType;

import java.util.Map;

public class DaySetup {

   private final Map<Float, SpiritType[]> spawns;
   private Cutscene startCutscene, endCutsccene;

   public DaySetup(Map<Float, SpiritType[]> spawns, Cutscene startCutscene, Cutscene endCutsccene) {
      this.spawns = spawns;
      this.startCutscene = startCutscene;
      this.endCutsccene = endCutsccene;
   }

   public Map<Float, SpiritType[]> getSpawns() {
      return spawns;
   }

   public Cutscene getStartCutscene() {
      return startCutscene;
   }

   public Cutscene getEndCutscene() {
      return endCutsccene;
   }
}
