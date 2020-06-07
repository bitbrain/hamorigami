package ldjam.hamorigami.i18n;

public enum Messages {

   PLAY_GAME("play.game"),
   ANY_KEY("any.key"),
   THANKS("thanks"),
   STORY_INTRO_1("story.intro.01"),
   STORY_INTRO_2("story.intro.02"),
   STORY_INTRO_3("story.intro.03"),
   STORY_INTRO_4("story.intro.04"),
   STORY_INTRO_5("story.intro.05"),
   STORY_INTRO_6("story.intro.06"),
   STORY_OUTRO_1("story.outro.01"),
   STORY_OUTRO_2("story.outro.02"),
   STORY_OUTRO_3("story.outro.03"),
   STORY_OUTRO_4("story.outro.04"),
   CREDITS_1("credits.1"),
   CREDITS_2("credits.2"),
   CREDITS_3("credits.3"),
   CREDITS_4("credits.4"),
   CREDITS_5("credits.5"),
   CREDITS_6("credits.6"),
   GAME_OVER("game.over"),
   PLAY_AGAIN("play.again");

   private final String key;

   Messages(String key) {
      this.key = key;
   }

   public String getKey() {
      return key;
   }
}