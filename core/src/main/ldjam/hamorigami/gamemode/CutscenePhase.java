package ldjam.hamorigami.gamemode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.Cutscene;
import ldjam.hamorigami.input.Proceedable;
import ldjam.hamorigami.input.ingame.ProceedableControllerAdapter;
import ldjam.hamorigami.setup.GameplaySetup;

public class CutscenePhase implements GamePhase, Proceedable {

   private final GamePhaseHandler phaseHandler;
   private boolean aborted;
   private Cutscene cutscene;
   private final GameplaySetup setup;

   public CutscenePhase(GamePhaseHandler phaseHandler, GameplaySetup setup) {
      this.phaseHandler = phaseHandler;
      this.setup = setup;
   }

   @Override
   public void disable(final HamorigamiContext context, GameObject treeObject) {
      if (cutscene != null) {
         context.getEmoteManager().clear();
         cutscene.stop(context);
      }
   }

   @Override
   public void enable(HamorigamiContext context, GameObject treeObject) {
      aborted = false;
      this.cutscene = resolveCutscene(setup);
      if (cutscene == null) {
         skip();
      } else {
         context.getInputManager().register(new ProceedableControllerAdapter(this));
         cutscene.play();
      }
   }

   @Override
   public boolean isFinished() {
      return aborted;
   }

   @Override
   public void update(float delta) {
      if ((cutscene.isOver() || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) && !aborted) {
         skip();
      }
   }

   @Override
   public void proceed() {

   }

   @Override
   public void skip() {
      aborted = true;
      if (setup.isLastDay() && setup.isEndOfDay()) {
         phaseHandler.changePhase(Phases.CREDITS);
      } else if (setup.isEndOfDay()) {
         setup.triggerNextDay();
         phaseHandler.changePhase(Phases.CUTSCENE);
      } else {
         phaseHandler.changePhase(Phases.GAMEPLAY);
      }

   }

   private Cutscene resolveCutscene(GameplaySetup setup) {
      if (setup.getCurrentDaySetup() == null) {
         return null;
      }
      if (setup.isStartOfDay()) {
         return setup.getCurrentDaySetup().getStartCutscene();
      } else if (setup.isEndOfDay()) {
         return setup.getCurrentDaySetup().getEndCutscene();
      } else {
         return null;
      }
   }
}
