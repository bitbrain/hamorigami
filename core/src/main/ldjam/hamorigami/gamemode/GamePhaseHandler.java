package ldjam.hamorigami.gamemode;

import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.TreeStatus;

import java.util.HashMap;
import java.util.Map;

public class GamePhaseHandler implements Updateable {

   private String currentPhase;
   private String nextPhase;

   private final Map<String, GamePhase> phases = new HashMap<>();
   private final HamorigamiContext context;
   private final GameObject treeObject;

   private boolean disabling = false;
   private boolean nextPhaseTriggered = false;

   public GamePhaseHandler(HamorigamiContext context, GameObject treeObject) {
      this.context = context;
      this.treeObject = treeObject;
   }

   public void addPhase(String id, GamePhase phase) {
      this.phases.put(id, phase);
      if (this.phases.size() == 1) {
         changePhase(id);
      }
   }

   public void changePhase(String id) {
      this.nextPhase = id;
      if (currentPhase == null) {
         currentPhase = nextPhase;
         phases.get(currentPhase).enable(context, treeObject);
      }
   }

   @Override
   public void update(float delta) {
      if (nextPhaseTriggered && phases.containsKey(currentPhase)) {
         phases.get(currentPhase).enable(context, treeObject);
         nextPhaseTriggered = false;
         return;
      }
      if (currentPhase != null && !phases.get(currentPhase).isFinished()) {
         phases.get(currentPhase).update(delta);
      }
      if (nextPhase == null || currentPhase == null) {
         return;
      }
      if (!currentPhase.equals(nextPhase)) {
         GamePhase phase = phases.get(currentPhase);
         if (!disabling) {
            phase.disable(context, treeObject);
            disabling = true;
         }
         if (phase.isFinished()) {
            disabling = false;
            nextPhaseTriggered = true;
            context.getBehaviorManager().clear();
            context.getInputManager().clear();
            currentPhase = nextPhase;
            treeObject.getAttribute(HealthData.class).reset();
            treeObject.getAttribute(TreeStatus.class).reset();
         }
      }
   }
}
