package ldjam.hamorigami.screens;

import com.badlogic.gdx.utils.GdxRuntimeException;
import de.bitbrain.braingdx.debug.DebugMetric;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerImpl;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.gamemode.*;
import ldjam.hamorigami.graphics.AtmosphericParticleRenderLayer;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.TreeStatus;
import ldjam.hamorigami.setup.GameplaySetup;
import ldjam.hamorigami.setup.loader.GameplaySetupLoader;

import java.io.IOException;

public class StoryModeScreen extends BaseScreen {

   private GamePhaseHandler phaseHandler;

   public StoryModeScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final HamorigamiContext context) {
      super.onCreate(context);
      this.phaseHandler = new GamePhaseHandler(context, treeObject);
      this.phaseHandler.addPhase(Phases.TITLE, new TitlePhase(phaseHandler, setup));
      this.phaseHandler.addPhase(Phases.CUTSCENE, new CutscenePhase(phaseHandler, setup));
      this.phaseHandler.addPhase(Phases.GAMEPLAY, new GameplayPhase(phaseHandler, setup));
      this.phaseHandler.addPhase(Phases.GAMEOVER, new GameOverPhase(phaseHandler));
      this.phaseHandler.addPhase(Phases.CREDITS, new CreditsPhase(phaseHandler));

      setupDebugUi(context);

      context.getRenderPipeline().put(HamorigamiContext.FURTHER_BACKGROUND_PARTICLES_LAYER,
            new AtmosphericParticleRenderLayer((ParticleManagerImpl) context.getFurtherBackgroundParticleManager(), setup, 3f));
      context.getRenderPipeline().put(HamorigamiContext.BACKGROUND_PARTICLES_LAYER,
            new AtmosphericParticleRenderLayer((ParticleManagerImpl) context.getBackgroundParticleManager(), setup, 1f));
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      phaseHandler.update(delta);

   }

   @Override
   protected GameplaySetup buildGameplaySetup(HamorigamiContext context) {
      GameplaySetupLoader loader = new GameplaySetupLoader(context);
      try {
         return loader.load("game.play");
      } catch (IOException e) {
         throw new GdxRuntimeException(e);
      }
   }

   private void setupDebugUi(HamorigamiContext context) {
      context.getDebugPanel().addMetric("tree watered level", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(treeObject.getAttribute(TreeStatus.class).getTreeWateredLevel());
         }
      });
      context.getDebugPanel().addMetric("soil water level", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return String.valueOf(treeObject.getAttribute(TreeStatus.class).getSoilWaterLevel());
         }
      });
      context.getDebugPanel().addMetric("tree health", new DebugMetric() {
         @Override
         public String getCurrentValue() {
            return (treeObject.getAttribute(HealthData.class).getHealthPercentage() * 100f) + "%";
         }
      });
   }
}
