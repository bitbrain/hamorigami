package ldjam.hamorigami.behavior;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.audio.JukeBox;
import ldjam.hamorigami.gamemode.GamePhaseHandler;
import ldjam.hamorigami.gamemode.Phases;
import ldjam.hamorigami.model.HealthData;

public class TreeHealthBindingBehavior extends BehaviorAdapter {

   private final GameObject treeObject;
   private final JukeBox jukeBox;
   private final GamePhaseHandler phaseHandler;
   private final GameContext2D context;

   public TreeHealthBindingBehavior(GameObject treeObject, AudioManager audioManager, GamePhaseHandler phaseHandler, GameContext2D context) {
      this.treeObject = treeObject;
      this.jukeBox = new JukeBox(audioManager, 400, Assets.Sounds.DEATH_01, Assets.Sounds.DEATH_02);
      jukeBox.setVolume(1f);
      this.phaseHandler = phaseHandler;
      this.context = context;
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      HealthData treeHealth = treeObject.getAttribute(HealthData.class);
      source.getColor().a = treeHealth.getHealthPercentage();
      if (treeHealth.isDead()) {
         jukeBox.playSound(source.getLeft(), source.getTop());
         phaseHandler.changePhase(Phases.GAMEOVER);

         // let all spirits fade away
         for (GameObject spirit : context.getGameWorld().getGroup("spirits")) {
            final String spiritId = spirit.getId();
            SharedTweenManager.getInstance().killTarget(spirit);
            SharedTweenManager.getInstance().killTarget(spirit.getColor());
            Tween.to(spirit, GameObjectTween.OFFSET_Y, 1.5f)
                  .target(180)
                  .start(SharedTweenManager.getInstance());
            Tween.to(spirit.getColor(), ColorTween.A, 1.5f)
                  .target(0f)
                  .setCallback(new TweenCallback() {
                     @Override
                     public void onEvent(int type, BaseTween<?> tween) {
                        context.getGameWorld().remove(spiritId);
                     }
                  })
                  .setCallbackTriggers(TweenCallback.COMPLETE)
                  .ease(TweenEquations.easeOutCubic)
                  .start(SharedTweenManager.getInstance());
         }
      }
   }
}
