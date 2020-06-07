package ldjam.hamorigami.effects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.audio.JukeBox;
import ldjam.hamorigami.behavior.ChasingBehavior;

public class SpawnWaterSpiritEffect implements SpiritSpawnEffect {

   @Override
   public void onSpawnSpirit(final GameObject spirit, final GameObject tree, final GameContext2D context) {
      final JukeBox jukeBox = new JukeBox(context.getAudioManager(), 500f,
            Assets.Sounds.WATER_DEATH_01, Assets.Sounds.WATER_DEATH_02, Assets.Sounds.WATER_DEATH_03);
      jukeBox.setVolume(0.2f);

      float top = context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight();
      float left = (float) (context.getGameCamera().getLeft() + Math.random() * (context.getGameCamera().getScaledCameraWidth() - 64f));
      spirit.setPosition(left, top);
      spirit.setActive(false);
      spirit.setAttribute("falling", true);
      spirit.getColor().a = 0f;
      Tween.to(spirit, GameObjectTween.ALPHA, 0.4f)
            .target(1f)
            .start(SharedTweenManager.getInstance());

      Tween.to(spirit, GameObjectTween.POS_Y, 0.6f)
            .target(context.getGameCamera().getTop() + 40f)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  spirit.removeAttribute("falling");
                  spirit.setAttribute("landing", true);
                  jukeBox.playSound(spirit.getLeft(), spirit.getTop());

               }
            })
            .ease(TweenEquations.easeInCubic)
            .start(SharedTweenManager.getInstance());
      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int type, BaseTween<?> source) {
            spirit.setActive(true);
            float padding = 35f;
            spirit.removeAttribute("landing");
            float targetX = padding + (float) Math.random() * (tree.getWidth() - padding * 2f);
            float targetY = padding + (float) Math.random() * (tree.getHeight() - padding * 2f);
            context.getBehaviorManager().apply(new ChasingBehavior(context, tree, targetX, targetY), spirit);
            context.getBehaviorManager().apply(new ChasingBehavior(context, tree, targetX, targetY), spirit);
         }
      }).delay(0.7f).start(SharedTweenManager.getInstance());
   }
}
