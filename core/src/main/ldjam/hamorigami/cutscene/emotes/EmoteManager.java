package ldjam.hamorigami.cutscene.emotes;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.ui.SpeechBubble;

import java.util.*;

public class EmoteManager {

   public static float EMOTE_FADE_IN_DURATION = 0.3f;
   public static float EMOTE_FADE_OUT_DURATION = 0.5f;
   public static float SPEECH_FADE_IN_DURATION = 0.3f;
   public static float SPEECH_FADE_OUT_DURATION = 0.3f;
   public static float EMOTE_DELAY_DURATION = 2f;
   public static float SPEECH_DELAY_DURATION_PER_CHARACTER = 0.05f;

   private class EmoteContainer {
      final String text;
      final Emote emote;

      EmoteContainer(String text) {
         this.emote = null;
         this.text = text;
      }

      EmoteContainer(Emote emote) {
         this.text = null;
         this.emote = emote;
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         EmoteContainer that = (EmoteContainer) o;
         return Objects.equals(text, that.text) &&
               emote == that.emote;
      }

      @Override
      public int hashCode() {
         return Objects.hash(text, emote);
      }
   }

   private final GameContext2D context;
   private final Map<String, SpeechBubble> bubbleMap = new HashMap<>();
   private final Map<String, List<EmoteContainer>> emoteMap = new HashMap<>();

   public EmoteManager(GameContext2D context) {
      this.context = context;
   }

   public void clear() {
      emoteMap.clear();
      for (final SpeechBubble bubble : bubbleMap.values()) {
         context.getWorldStage().getActors().removeValue(bubble, true);
      }
      bubbleMap.clear();
   }

   public void say(String text, GameObject target) {
       addEmote(new EmoteContainer(text), target);
   }

   public void emote(Emote emote, GameObject target) {
      addEmote(new EmoteContainer(emote), target);
   }

   private void addEmote(EmoteContainer emoteContainer, GameObject target) {
      List<EmoteContainer> queue = ensureSpeechQueue(target);
      if (queue.isEmpty()) {
         queue.add(emoteContainer);
         nextText(target);
      } else {
         queue.add(emoteContainer);
      }
   }

   private List<EmoteContainer> ensureSpeechQueue(GameObject object) {
      if (object != null && !emoteMap.containsKey(object.getId())) {
         emoteMap.put(object.getId(), new ArrayList<EmoteContainer>());
      }
      return emoteMap.get(object.getId());
   }

   private void nextText(final GameObject target) {
      List<EmoteContainer> queue = ensureSpeechQueue(target);
      if (queue.isEmpty()) {
         return;
      }
      EmoteContainer container = queue.get(0);
      SpeechBubble bubble = ensureBubble(target);
      if (container.text != null) {
         bubble.setText(container.text, SPEECH_FADE_IN_DURATION, (container.text.length() * SPEECH_DELAY_DURATION_PER_CHARACTER) / 2f);
      }
      if (container.emote != null) {
         bubble.setEmote(container.emote);
      }
      SharedTweenManager.getInstance().killTarget(bubble);
      Tween.to(bubble, ActorTween.ALPHA, container.emote != null ? EMOTE_FADE_IN_DURATION : SPEECH_FADE_IN_DURATION)
            .target(1f)
            .start(SharedTweenManager.getInstance());
      Tween.to(bubble, ActorTween.ALPHA, container.emote != null ? EMOTE_FADE_OUT_DURATION : SPEECH_FADE_OUT_DURATION)
            .delay(container.emote != null ? EMOTE_FADE_IN_DURATION + EMOTE_DELAY_DURATION : SPEECH_FADE_IN_DURATION + container.text.length() * SPEECH_DELAY_DURATION_PER_CHARACTER)
            .target(0f)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int type, BaseTween<?> source) {
                  List<EmoteContainer> queue = ensureSpeechQueue(target);
                  if (!queue.isEmpty()) {
                     queue.remove(0);
                     nextText(target);
                  }
               }
            })
            .start(SharedTweenManager.getInstance());
   }

   private SpeechBubble ensureBubble(GameObject object) {
      if (!bubbleMap.containsKey(object.getId())) {
         SpeechBubble bubble = new SpeechBubble(context, object.getId());
         bubble.getColor().a = 0f;
         context.getWorldStage().addActor(bubble);
         bubbleMap.put(object.getId(), bubble);
      }
      return bubbleMap.get(object.getId());
   }
}
