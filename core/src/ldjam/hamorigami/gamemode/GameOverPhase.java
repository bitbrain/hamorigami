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
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.i18n.Messages;
import ldjam.hamorigami.input.Proceedable;
import ldjam.hamorigami.input.ingame.ProceedableControllerAdapter;
import ldjam.hamorigami.ui.Styles;

public class GameOverPhase implements GamePhase, Proceedable {

   private boolean exiting;

   private GameContext2D context;

   private final GamePhaseHandler phaseHandler;
   private Table layout;

   public GameOverPhase(GamePhaseHandler phaseHandler) {
      this.phaseHandler = phaseHandler;
   }

   @Override
   public void disable(final GameContext2D context, GameObject treeObject) {
      SharedAssetManager.getInstance().get(Assets.Musics.FAIL, Music.class).stop();
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
   public void enable(GameContext2D context, GameObject treeObject) {
      this.context = context;
      context.getInputManager().register(new ProceedableControllerAdapter(this));
      SharedAssetManager.getInstance().get(Assets.Musics.FAIL, Music.class).play();
      exiting = false;
      this.layout = new Table();
      layout.setFillParent(true);
      layout.getColor().a = 0f;
      Tween.to(layout, ActorTween.ALPHA, 3f)
            .target(1f)
            .start(SharedTweenManager.getInstance());

      Label logo = new Label(Bundle.get(Messages.GAME_OVER), Styles.LABEL_CAPTION);
      layout.add(logo).row();

      Label pressAnyButton = new Label(Bundle.get(Messages.PLAY_AGAIN), Styles.DIALOG_TEXT);
      layout.add(pressAnyButton).padTop(120f).row();

      context.getWorldStage().addActor(layout);

      context.getGameCamera().setZoom(800, GameCamera.ZoomMode.TO_WIDTH);

      pressAnyButton.getColor().a = 0f;
      Tween.to(pressAnyButton, ActorTween.ALPHA, 3f).target(1f).delay(2f)
            .ease(TweenEquations.easeInCubic)
            .start(context.getTweenManager());

      Tween.to(pressAnyButton, ActorTween.ALPHA, 1f).target(1f).delay(3f)
            .target(1f)
            .ease(TweenEquations.easeInCubic)
            .repeatYoyo(Tween.INFINITY, 0f)
            .start(context.getTweenManager());
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
      if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
         proceed();
      }
   }

   @Override
   public void proceed() {
      Array<GameObject> spirits = context.getGameWorld().getGroup("spirits");
      for (GameObject o : spirits) {
         SharedTweenManager.getInstance().killTarget(o);
         context.getGameWorld().remove(o);
      }
      exiting = true;
      phaseHandler.changePhase(Phases.GAMEPLAY);
   }

   @Override
   public void skip() {
      Gdx.app.exit();
   }
}
