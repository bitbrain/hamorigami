package ldjam.hamorigami.cutscene.toast;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Updateable;
import ldjam.hamorigami.context.HamorigamiContext;

public class ToastManager implements Updateable {

   public static final float TOAST_DURATION = 5f;

   private final Label label;
   private final HamorigamiContext context;

   public ToastManager(HamorigamiContext context, Label.LabelStyle labelStyle) {
      this.context = context;
      this.label = new Label("", labelStyle);
      this.label.getColor().a = 0f;
      context.getWorldStage().addActor(label);
   }

   public void stop() {
      label.setText("");
      SharedTweenManager.getInstance().killTarget(label);
      Tween.to(label, ActorTween.ALPHA, TOAST_DURATION / 3f)
            .target(0f)
            .ease(TweenEquations.easeInOutQuad)
            .start(SharedTweenManager.getInstance());
   }

   public void doToast(final String text) {
      SharedTweenManager.getInstance().killTarget(label);
      Tween.to(label, ActorTween.ALPHA, 0.5f)
            .target(0f)
            .ease(TweenEquations.easeOutQuad)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  label.setText(text);
                  Tween.to(label, ActorTween.ALPHA, 0.5f)
                        .target(1f)
                        .ease(TweenEquations.easeInOutQuad)
                        .start(SharedTweenManager.getInstance());
               }
            })
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .start(SharedTweenManager.getInstance());
      Tween.to(label, ActorTween.ALPHA, TOAST_DURATION / 3f)
            .delay(TOAST_DURATION / 3f * 2f)
            .target(0f)
            .ease(TweenEquations.easeInOutQuad)
            .start(SharedTweenManager.getInstance());
   }

   @Override
   public void update(float delta) {
      label.setPosition(
            context.getGameCamera().getLeft() + context.getGameCamera().getScaledCameraWidth() / 2f - label.getPrefWidth() / 2f,
               context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight() / 1.3f - label.getPrefHeight() / 2f);
   }
}
