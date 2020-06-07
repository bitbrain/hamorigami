package ldjam.hamorigami.effects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.behavior.ChasingBehavior;

public class SpawnFireEffect implements SpiritSpawnEffect {

   @Override
   public void onSpawnSpirit(final GameObject spirit, final GameObject tree, final GameContext2D context) {
      float top = context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight();
      float left = (float) (context.getGameCamera().getLeft() + Math.random() * (context.getGameCamera().getScaledCameraWidth() - 64f));
      spirit.setPosition(left, top);
      spirit.setActive(false);
      spirit.setAttribute("falling", true);
      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int type, BaseTween<?> source) {
            spirit.removeAttribute("falling");
            spirit.setActive(true);
         }
      }).delay(0.5f).start(SharedTweenManager.getInstance());
      spirit.getColor().a = 0f;
      Tween.to(spirit, GameObjectTween.ALPHA, 1f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      float padding = 35f;
      float targetX = padding + (float) Math.random() * (tree.getWidth() - padding * 2f);
      float targetY = padding + (float) Math.random() * (tree.getHeight() - padding * 2f);
      context.getBehaviorManager().apply(new ChasingBehavior(context, tree, targetX, targetY), spirit);
      context.getBehaviorManager().apply(new ChasingBehavior(context, tree, targetX, targetY), spirit);
   }
}
