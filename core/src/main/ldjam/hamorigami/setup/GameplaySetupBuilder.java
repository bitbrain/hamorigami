package ldjam.hamorigami.setup;

import java.util.ArrayList;
import java.util.List;

public class GameplaySetupBuilder {

   private final List<DaySetup> daySetups = new ArrayList<>();

   public GameplaySetupBuilder addDay(DaySetup daySetup) {
      daySetups.add(daySetup);
      return this;
   }

   public GameplaySetup build() {
      return new GameplaySetup(daySetups);
   }
}
