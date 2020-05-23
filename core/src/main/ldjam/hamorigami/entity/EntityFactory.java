package ldjam.hamorigami.entity;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.TweenUtils;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.model.*;

import static ldjam.hamorigami.model.ObjectType.GAUGE;

public class EntityFactory {

   private final HamorigamiContext context;

   public EntityFactory(HamorigamiContext context) {
      this.context = context;
   }

   public GameObject spawnGauge(float x, float y) {
      GameObject object = context.getGameWorld().addObject();
      object.setPosition(context.getGameCamera().getLeft() + x, context.getGameCamera().getTop() + y);
      object.setDimensions(16f, 64f);
      object.setType(GAUGE);
      object.setActive(false);
      return object;
   }

   public GameObject spawnSpirit(SpiritType spiritType, float x, float y, Mutator<GameObject> mutator) {
      return spawnSpirit(spiritType, x, y, mutator, "spirits");
   }

   public GameObject spawnSpirit(SpiritType spiritType, float x, float y, Mutator<GameObject> mutator, String group) {
      GameObject object = mutator != null
            ? context.getGameWorld().addObject(group, mutator)
            : context.getGameWorld().addObject(group);
      object.setZIndex(3);
      object.setType(spiritType);
      object.setPosition(context.getGameCamera().getLeft() + x, context.getGameCamera().getTop() + y);
      object.setDimensions(spiritType == SpiritType.SPIRIT_EARTH ? 64f : 32f, 64f);
      object.setAttribute(HealthData.class, new HealthData(spiritType.getHealth()));
      object.setAttribute(Movement.class, new Movement(spiritType.getMaxSpeed(), context.getGameCamera()));
      context.getBehaviorManager().apply(object.getAttribute(Movement.class), object);
      if (spiritType.getParticleId() != null) {
         if (spiritType == SpiritType.SPIRIT_EARTH) {
            context.getBehaviorManager().apply(context.getCameraParallaxor(), object);
            context.getParticleManager().attachEffect(spiritType.getParticleId(), object, 32f, 32f);
         } else {
            context.getParticleManager().attachEffect(spiritType.getParticleId(), object, 16f, 32f);
         }
      }
      return object;
   }


   public GameObject spawnSpirit(SpiritType spiritType, float x, float y) {
      return spawnSpirit(spiritType, x, y, null);
   }

   public GameObject spawnTree() {
      GameObject object = context.getGameWorld().addObject();
      object.setType(ObjectType.TREE);
      object.setZIndex(2);
      object.setDimensions(300, 230);
      object.setPosition(
            context.getGameCamera().getLeft() + (context.getGameCamera().getScaledCameraWidth() / 2f - object.getWidth() / 2f),
            context.getGameCamera().getTop() + 31);
      object.setAttribute(TreeStatus.class, new TreeStatus());
      object.setAttribute(HealthData.class, new HealthData(500));
      //Light light = context.getLightingManager().createPointLight(500, Color.valueOf("e5a656"));
      //context.getLightingManager().attach(light, object, true);
      return object;
   }

   public GameObject spawnFloor() {
      GameObject object = context.getGameWorld().addObject();
      object.setType(ObjectType.FLOOR);
      object.setZIndex(1);
      object.setPosition(
            context.getGameCamera().getLeft() + context.getGameCamera().getScaledCameraWidth() / 2f - 500,
            context.getGameCamera().getTop());
      object.setDimensions(1000, 50);
      return object;
   }

   public GameObject spawnDamageTelegraph(final GameObject owner, final float centerX, final float centerY, final float width, final float height, final float rotation) {
      final GameObject damage = context.getGameWorld().addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setType("DAMAGE");
            target.setPosition(centerX, centerY);
            target.setRotation(rotation);
            target.setDimensions(width, height);
            target.setAttribute("owner", owner);
         }
      }, false);
      Tween.call(new TweenCallback() {
         @Override
         public void onEvent(int type, BaseTween<?> source) {
            context.getGameWorld().remove(damage);
         }
      }).delay(0.1f).start(SharedTweenManager.getInstance());
      context.getBehaviorManager().apply(new BehaviorAdapter() {

         private boolean applicableForRemoval;

         @Override
         public void update(GameObject source, float delta) {
            super.update(source, delta);
            if (applicableForRemoval) {
               context.getGameWorld().remove(source);
            }
         }

         @Override
         public void update(GameObject source, GameObject target, float delta) {
            super.update(source, target, delta);
            if ("DAMAGE".equals(source.getType()) && source.collidesWith(target)) {
               if (target.equals(source.getAttribute("owner"))) {
                  // let's not hit ourselves ;)
                  return;
               }
               if (target.hasAttribute(HealthData.class) && target.getType() != ObjectType.TREE) {
                  if (!target.getAttribute(HealthData.class).isDead()) {
                     target.getAttribute(HealthData.class).reduceHealth(20);
                     target.setColor(1f, 1f, 1f, 0.5f);
                     TweenUtils.toColor(target.getColor(), Color.WHITE.cpy(), 1f);
                     applicableForRemoval = true;
                  }
               }
            }
         }
      }, damage);
      return damage;
   }
}
