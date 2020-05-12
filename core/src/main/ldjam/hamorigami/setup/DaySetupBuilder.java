package ldjam.hamorigami.setup;

import ldjam.hamorigami.cutscene.Cutscene;
import ldjam.hamorigami.model.SpiritType;

import java.util.HashMap;
import java.util.Map;

public class DaySetupBuilder {

   private final Map<Float, SpiritType[]> spawns = new HashMap<>();
   private Cutscene startCutscene, endCutscene;

   public DaySetupBuilder startOfDayCutscene( Cutscene cutscene) {
      this.startCutscene = cutscene;
      return this;
   }

   public DaySetupBuilder endOfDayCutscene( Cutscene cutscene) {
      this.endCutscene = cutscene;
      return this;
   }


   public DaySetupBuilder addSpawns(float seconds, SpiritType ... spirits) {
      spawns.put(seconds, spirits);
      return this;
   }

   public DaySetup build() {
      return new DaySetup(spawns, startCutscene, endCutscene);
   }
}
