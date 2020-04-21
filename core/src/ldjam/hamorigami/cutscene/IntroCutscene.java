package ldjam.hamorigami.cutscene;

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
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.model.SpiritType;

public class IntroCutscene implements CutsceneSetup {

   private GameObject kodama;
   private JukeBox jukeBox;

   @Override
   public void cleanup(GameContext2D context) {
      SharedTweenManager.getInstance().killTarget(kodama);
      kodama.removeAttribute("swiping");
   }

   @Override
   public void setup(GameContext2D context) {
      jukeBox = new JukeBox(context.getAudioManager(), 700f, Assets.Sounds.BRUSH_01);
      jukeBox.setMinimumMillis(100f);
      EntityFactory entityFactory = new EntityFactory(context);
      kodama = entityFactory.spawnSpirit(SpiritType.SPIRIT_EARTH, context.getGameCamera().getScaledCameraWidth() / 3.5f, 0f);
      kodama.setDimensions(64f, 64f);
      kodama.getColor().a = 0f;
      kodama.setAttribute("swiping", true);
      float currentX = kodama.getLeft();
      Tween.to(kodama, GameObjectTween.ALPHA, 3f).delay(4f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(kodama, GameObjectTween.POS_X, 4f).delay(7f)
            .target(currentX - 200f)
            .repeatYoyo(Tween.INFINITY, 0f)
            .ease(TweenEquations.easeInOutCubic)
            .setCallbackTriggers(TweenCallback.ANY)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  jukeBox.playSound(kodama.getLeft() + 32f, kodama.getTop() + 32f);
               }
            })
            .start(SharedTweenManager.getInstance());
   }
}
