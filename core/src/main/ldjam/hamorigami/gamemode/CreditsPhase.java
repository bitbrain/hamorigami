package ldjam.hamorigami.gamemode;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.Cutscene;
import ldjam.hamorigami.cutscene.CutsceneBuilder;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.input.Proceedable;
import ldjam.hamorigami.input.ingame.ProceedableControllerAdapter;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.ui.Styles;

import static ldjam.hamorigami.Assets.Musics.OUTRO;

public class CreditsPhase implements GamePhase, Proceedable {

   private boolean exiting;

   private final GamePhaseHandler phaseHandler;
   private Music music;
   private Table layout;
   private Cutscene cutscene;

   public CreditsPhase(GamePhaseHandler phaseHandler) {
      this.phaseHandler = phaseHandler;
   }

   @Override
   public void disable(final HamorigamiContext context, GameObject treeObject) {
      music.stop();
      Asset.get(Assets.Musics.CITYSCAPE, Music.class).setVolume(0.9f);
      cutscene.stop(context);
      Tween.to(layout, ActorTween.ALPHA, 1f)
            .target(0f)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  context.getWorldStage().getActors().removeValue(layout, true);
               }
            })
            .start(SharedTweenManager.getInstance());
   }

   @Override
   public void enable(HamorigamiContext context, GameObject treeObject) {
      context.getInputManager().register(new ProceedableControllerAdapter(this));
      exiting = false;
      Asset.get(Assets.Musics.CITYSCAPE, Music.class).setVolume(0.05f);
      this.music = Asset.get(OUTRO, Music.class);
      music.setLooping(false);
      music.setVolume(0.1f);
      music.play();

      this.layout = new Table();
      layout.getColor().a = 0f;
      Tween.to(layout, ActorTween.ALPHA, 3f).delay(1f)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      layout.setFillParent(true);

      Label pressAnyButton = new Label(Bundle.get(Messages.THANKS), Styles.DIALOG_TEXT);
      layout.add(pressAnyButton).padTop(120f).row();

      context.getWorldStage().addActor(layout);
      context.getGameCamera().setZoom(800, GameCamera.ZoomMode.TO_WIDTH);


      pressAnyButton.getColor().a = 0f;
      Tween.to(pressAnyButton, ActorTween.ALPHA, 3f).target(1f).delay(3f)
            .ease(TweenEquations.easeInCubic)
            .start(context.getTweenManager());

      Tween.to(pressAnyButton, ActorTween.ALPHA, 1f).target(1f).delay(4f)
            .target(1f)
            .ease(TweenEquations.easeInCubic)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(context.getTweenManager());

      // setup cutscene
      this.cutscene = new CutsceneBuilder(context)
            .spawn("credits1", SpiritType.SPIRIT_SUN, context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f)
            .fadeIn("credits1", 1.5f)
            .wait(1.5f)
            .say("credits.1", "credits1")
            .wait(1f)
            .moveBy("credits1", context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f, 4f)
            .remove("credits1", 1f)

            .spawn("credits2", SpiritType.SPIRIT_EARTH, context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f)
            .fadeIn("credits2", 1.5f)
            .wait(1.5f)
            .say("credits.2", "credits2")
            .wait(2f)
            .moveBy("credits2", -context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f, 4f)
            .remove("credits2", 1f)

            .spawn("credits3", SpiritType.SPIRIT_WATER, context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f)
            .fadeIn("credits3", 1.5f)
            .wait(1.5f)
            .say("credits.3", "credits3")
            .wait(1f)
            .moveBy("credits3", context.getGameCamera().getScaledCameraWidth() / 2f, -context.getGameCamera().getScaledCameraHeight() / 2f, 4f)
            .remove("credits3", 1f)

            .spawn("credits4", SpiritType.SPIRIT_EARTH, context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f)
            .fadeIn("credits4", 1.5f)
            .wait(1.5f)
            .say("credits.4", "credits4")
            .wait(1f)
            .moveBy("credits4", context.getGameCamera().getScaledCameraWidth() / 2f, -context.getGameCamera().getScaledCameraHeight() / 2f, 4f)
            .remove("credits4", 1f)

            .spawn("credits5", SpiritType.SPIRIT_SUN, context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f)
            .fadeIn("credits5", 1.5f)
            .wait(1.5f)
            .say("credits.5", "credits5")
            .wait(1f)
            .moveBy("credits5", context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f, 4f)
            .remove("credits5", 1f)

            .spawn("credits6", SpiritType.SPIRIT_SUN, context.getGameCamera().getScaledCameraWidth() / 2f, context.getGameCamera().getScaledCameraHeight() / 2f)
            .fadeIn("credits6", 1.5f)
            .wait(1.5f)
            .say("credits.6", "credits6")
            .wait(1f)
            .moveBy("credits6", -context.getGameCamera().getScaledCameraWidth() / 2f, -context.getGameCamera().getScaledCameraHeight() / 2f, 4f)
            .remove("credits6", 1f)

            .build();
      this.cutscene.play();
   }

   @Override
   public boolean isFinished() {
      return exiting;
   }

   @Override
   public void update(float delta) {
      if (!exiting && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
         skip();
      }
      if (!exiting && (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         proceed();
      }
   }

   @Override
   public void proceed() {
      exiting = true;
      phaseHandler.changePhase(Phases.TITLE);
   }

   @Override
   public void skip() {
      Gdx.app.exit();
   }
}
