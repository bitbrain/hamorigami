package ldjam.hamorigami.cutscene;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.GameCameraTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.anchor.Anchor;
import ldjam.hamorigami.anchor.AnchorMask;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.emotes.Emote;
import ldjam.hamorigami.i18n.Bundle;
import ldjam.hamorigami.model.SpiritType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ldjam.hamorigami.cutscene.emotes.EmoteManager.*;

public class CutsceneBuilder {

   private final HamorigamiContext context;
   private final Map<Float, List<CutsceneStep>> steps = new HashMap<>();
   private float currentTime = 0f;
   private float emoteTime = 0f;


   public CutsceneBuilder(HamorigamiContext context) {
      this.context = context;
   }

   public CutsceneBuilder fadeIn(final String id, final float duration) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            if (obj != null) {
               obj.getColor().a = 0f;
               Tween.to(obj, GameObjectTween.ALPHA, duration)
                     .target(1f)
                     .start(SharedTweenManager.getInstance());
            }
         }

         @Override
         public void stop() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            SharedTweenManager.getInstance().killTarget(obj);
         }
      });
      return this;
   }

   public CutsceneBuilder fadeOut(final String id, final float duration) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            if (obj != null) {
               obj.getColor().a = 0f;
               Tween.to(obj, GameObjectTween.ALPHA, duration)
                     .target(0f)
                     .start(SharedTweenManager.getInstance());
            }
         }

         @Override
         public void stop() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            SharedTweenManager.getInstance().killTarget(obj);
         }
      });
      return this;
   }

   public CutsceneBuilder removeAttribute(final String id, final String attribute) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            obj.removeAttribute(attribute);
         }

         @Override
         public void stop() {

         }
      });
      return this;
   }


   public CutsceneBuilder setAttribute(final String id, final String attribute) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            obj.setAttribute(attribute, true);
         }

         @Override
         public void stop() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            if (obj != null) {
               obj.removeAttribute(attribute);
            }
         }
      });
      return this;
   }

   public CutsceneBuilder clearTweens(final String id) {
      getCurrentSteps().add(new CutsceneStep() {

         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            SharedTweenManager.getInstance().killTarget(obj);
         }

         @Override
         public void stop() {

         }
      });
      return this;
   }

   public CutsceneBuilder moveBy(final String id, final float x, final float y, final float duration) {
      return moveBy(id, x, y, duration, false);
   }

   public CutsceneBuilder moveByYoyo(final String id, final float x, final float y, final float duration) {
      return moveBy(id, x, y, duration, true);
   }

   public CutsceneBuilder moveBy(final String id, final float x, final float y, final float duration, final boolean yoyo) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            if (x != 0) {
               float currentX = obj.getLeft();
               Tween tween = Tween.to(obj, GameObjectTween.POS_X, duration)
                     .target(currentX + x)
                     .ease(TweenEquations.easeInOutCubic);
               if (yoyo) {
                  tween = tween.repeatYoyo(Tween.INFINITY, 0f);
               }
               tween.start(SharedTweenManager.getInstance());
            }
            if (y != 0) {
               float currentY = obj.getTop();
               Tween tween = Tween.to(obj, GameObjectTween.POS_Y, duration)
                     .target(currentY + y)
                     .ease(TweenEquations.easeInOutCubic)
                     .start(SharedTweenManager.getInstance());
               if (yoyo) {
                  tween = tween.repeatYoyo(Tween.INFINITY, 0f);
               }
               tween.start(SharedTweenManager.getInstance());
            }
         }

         @Override
         public void stop() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            SharedTweenManager.getInstance().killTarget(obj);
         }
      });
      return this;
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, String anchorName) {
      return spawn(id, spirit, anchorName, AnchorMask.CENTER.getByte());
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, String anchorName, float offsetX, float offsetY, final boolean persistent) {
      return spawn(id, spirit, anchorName, AnchorMask.CENTER.getByte(), offsetX, offsetY, persistent);
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, String anchorName, final boolean persistent) {
      return spawn(id, spirit, anchorName, AnchorMask.CENTER.getByte(), 0f, 0f, persistent);
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, String anchorName, int mask) {
      return spawn(id, spirit, anchorName, mask, 0f, 0f, false);
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, String anchorName, int mask, final boolean persistent) {
      return spawn(id, spirit, anchorName, mask, 0f, 0f, persistent);
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, String anchorName, int mask, float offsetX, float offsetY) {
      return spawn(id, spirit, anchorName, mask, offsetX, offsetY, false);
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, String anchorName, int mask, float offsetX, float offsetY, final boolean persistent) {
      Anchor anchor = context.getAnchorManager().getAnchor(anchorName);
      if (anchor == null) {
         Gdx.app.error("ANCHOR", "Anchor with name '" + anchorName + "' not found.");
         return spawn(id, spirit, offsetX, offsetY, persistent);
      }
      return spawn(
            id,
            spirit,
            anchor.getX(mask) + offsetX - context.getGameCamera().getLeft(),
            anchor.getY(mask) + offsetY - context.getGameCamera().getTop(),
            persistent);
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, final float x, final float y) {
      return spawn(id, spirit, x, y, false);
   }

   public CutsceneBuilder spawn(final String id, final SpiritType spirit, final float x, final float y, final boolean persistent) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            Mutator<GameObject> mutator = new Mutator<GameObject>() {
               @Override
               public void mutate(GameObject target) {
                  target.setId(id);
               }
            };
            if (context.getGameWorld().getObjectById(id) != null) {
               context.getGameWorld().getObjectById(id).setPosition(x, y);
            } else {
               if (persistent) {
                  context.getEntityFactory().spawnSpirit(spirit, x, y, mutator);
               } else {
                  context.getEntityFactory().spawnSpirit(spirit, x, y, mutator, "cutscene");
               }
            }
         }

         @Override
         public void stop() {
            if (!persistent) {
               context.getGameWorld().remove(id);
            }
         }
      });
      return this;
   }

   public CutsceneBuilder positionCamera(final float x, final float y) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getGameCamera().setPosition(x, y);
         }

         @Override
         public void stop() {

         }
      });
      return this;
   }

   public CutsceneBuilder moveCameraTo(float x, float y, float duration) {
      // TODO add braingdx tween support for this
      return this;
   }

   public CutsceneBuilder wait(float seconds) {
      currentTime += seconds + emoteTime;
      emoteTime = 0;
      return this;
   }

   public CutsceneBuilder say(final String text, final String id) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            context.getEmoteManager().say(Bundle.get(text), obj);
         }

         @Override
         public void stop() {

         }
      });
      emoteTime += SPEECH_FADE_IN_DURATION + SPEECH_DELAY_DURATION_PER_CHARACTER * text.length() + SPEECH_FADE_OUT_DURATION;
      return this;
   }

   public CutsceneBuilder emote(final Emote emote, final String id) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            context.getEmoteManager().emote(emote, obj);
         }

         @Override
         public void stop() {

         }
      });
      emoteTime += EMOTE_FADE_IN_DURATION + EMOTE_DELAY_DURATION + EMOTE_FADE_OUT_DURATION;
      return this;
   }

   public CutsceneBuilder cameraZoom(final float value, final GameCamera.ZoomMode zoomMode, final float seconds) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            if (zoomMode == GameCamera.ZoomMode.TO_WIDTH) {
               Tween.to(context.getGameCamera(), GameCameraTween.ZOOM_WIDTH, seconds)
                     .target(value)
                     .start(SharedTweenManager.getInstance());
            }
            if (zoomMode == GameCamera.ZoomMode.TO_HEIGHT) {
               Tween.to(context.getGameCamera(), GameCameraTween.ZOOM_HEIGHT, seconds)
                     .target(value)
                     .start(SharedTweenManager.getInstance());
            } else {
               Tween.to(context.getGameCamera(), GameCameraTween.DEFAULT_ZOOM_FACTOR, seconds)
                     .target(value)
                     .start(SharedTweenManager.getInstance());
            }
         }

         @Override
         public void stop() {
            SharedTweenManager.getInstance().killTarget(context.getGameCamera());
         }
      });
      return this;
   }

   public CutsceneBuilder fadeScreenIn(final float seconds) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getScreenTransitions().in(seconds);
         }

         @Override
         public void stop() {

         }
      });
      return this;
   }

   public CutsceneBuilder fadeScreenOut(final float seconds) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getScreenTransitions().out(seconds);
         }

         @Override
         public void stop() {

         }
      });
      return this;
   }

   public CutsceneBuilder shakeScreen(final float intensity, final float seconds) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getGameCamera().shake(intensity, seconds);
         }

         @Override
         public void stop() {

         }
      });
      return this;
   }

   public CutsceneBuilder playSound(final String soundPath, final float x, final float y) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getAudioManager().spawnSound(soundPath, x, y, 1f, 1f, 300);
         }

         @Override
         public void stop() {

         }
      });
      return this;
   }

   public CutsceneBuilder playSound(String soundPath, GameObject target) {
      return playSound(soundPath, target.getLeft() + target.getWidth() / 2f, target.getTop() + target.getHeight() / 2f);
   }

   public CutsceneBuilder playMusic(final String musicPath) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getAudioManager().playMusic(musicPath);
         }

         @Override
         public void stop() {
            context.getAudioManager().stopMusic(musicPath);
         }
      });
      return this;
   }

   public CutsceneBuilder stopMusic(final String musicPath) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            context.getAudioManager().stopMusic(musicPath);
         }

         @Override
         public void stop() {

         }
      });
      return this;
   }


   public CutsceneBuilder remove(final String id, final float delay) {
      getCurrentSteps().add(new CutsceneStep() {
         @Override
         public void execute() {
            GameObject obj = context.getGameWorld().getObjectById(id);
            Tween.to(obj, GameObjectTween.ALPHA, delay)
                  .target(0f)
                  .setCallbackTriggers(TweenCallback.COMPLETE)
                  .setCallback(new TweenCallback() {
                     @Override
                     public void onEvent(int type, BaseTween<?> source) {
                        context.getGameWorld().remove(id);
                     }
                  })
                  .start(SharedTweenManager.getInstance());
         }

         @Override
         public void stop() {

         }
      });
      currentTime += delay;
      return this;
   }

   public Cutscene build() {
      return new Cutscene(steps);
   }

   private List<CutsceneStep> getCurrentSteps() {
      if (!steps.containsKey(currentTime)) {
         steps.put(currentTime, new ArrayList<CutsceneStep>());
      }
      return steps.get(currentTime);
   }
}