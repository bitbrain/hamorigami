package ldjam.hamorigami.cutscene;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cutscene {

   private final Map<Float, List<CutsceneStep>> steps;
   private final List<Tween> tweens = new ArrayList<>();
   private final List<BaseTween<?>> executedTweens = new ArrayList<>();

   public Cutscene(Map<Float, List<CutsceneStep>> steps) {
      this.steps = steps;
   }

   public void play() {
      if (tweens.size() > 0f) {
         return;
      }
      for (Map.Entry<Float, List<CutsceneStep>> entry : steps.entrySet()) {
         float delay = entry.getKey();
         for (final CutsceneStep step : entry.getValue()) {
            tweens.add(Tween.call(new TweenCallback() {
               @Override
               public void onEvent(int type, final BaseTween<?> tween) {
                  Gdx.app.postRunnable(new Runnable() {
                     @Override
                     public void run() {
                        step.execute();
                        executedTweens.add(tween);
                     }
                  });
               }
            }).setCallbackTriggers(TweenCallback.COMPLETE)
                  .delay(delay)
                  .start(SharedTweenManager.getInstance()));
         }
      }
   }

   public int size() {
      return steps.size();
   }

   public void stop(GameContext2D context) {
      for (Map.Entry<Float, List<CutsceneStep>> entry : steps.entrySet()) {
         for (CutsceneStep step : entry.getValue()) {
            step.stop();
         }
      }
      for (Tween tween : tweens) {
         tween.free();
      }
      tweens.clear();
   }

   public boolean isOver() {
      return executedTweens.size() == tweens.size();
   }

   @Override
   public String toString() {
      return "Cutscene{" +
            "steps=" + steps +
            '}';
   }
}
