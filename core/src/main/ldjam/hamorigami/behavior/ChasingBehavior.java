package ldjam.hamorigami.behavior;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.Movement;
import ldjam.hamorigami.model.SpiritType;

public class ChasingBehavior extends BehaviorAdapter {

   public static final float MIN_DISTANCE = 20f;
   public static final float ATTACK_INTERVAL = 1f;

   public static final float ARRIVAL_HEALTH = 8f;

   private final GameObject target;
   private final Vector2 direction = new Vector2();
   private final Vector2 offset = new Vector2();
   private final DeltaTimer attackTimer = new DeltaTimer();
   private final GameContext2D context;

   private boolean deathInitiated;

   public ChasingBehavior(GameContext2D context, GameObject target, float offsetX, float offsetY) {
      this.context = context;
      this.target = target;
      offset.set(offsetX, offsetY);

   }

   @Override
   public void update(final GameObject source, float delta) {
      super.update(source, delta);
      Movement mover = source.getAttribute(Movement.class);
      if (mover != null) {
         direction.x = (target.getLeft() + offset.x) - source.getLeft();
         direction.y = (target.getTop() + offset.y) - source.getTop();

         if (direction.len() <= MIN_DISTANCE) {
            attackTimer.update(delta);
            if (attackTimer.reached(ATTACK_INTERVAL)) {
               attackTimer.reset();
               SpiritType type = (SpiritType) source.getType();
               type.getAbsorbEffect().applyEffect(target);
            }

            final String entityId = source.getId();
            if (!deathInitiated) {
               deathInitiated = true;
               Tween.call(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> tween) {
                     GameObject obj = context.getGameWorld().getObjectById(entityId);
                     if (obj != null && obj.hasAttribute(HealthData.class)) {
                        obj.getAttribute(HealthData.class).kill();
                     }
                  }
               }).delay(ARRIVAL_HEALTH).start(SharedTweenManager.getInstance());
            }
         } else {
            mover.move(direction);
         }
      }
   }
}
