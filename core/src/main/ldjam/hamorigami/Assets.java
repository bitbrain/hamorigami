package ldjam.hamorigami;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import de.bitbrain.braingdx.assets.annotations.AssetSource;

public interface Assets {

   @AssetSource(directory = "font", assetClass = FreeTypeFontGenerator.class)
   interface Fonts {
      String VISITOR = "visitor.ttf";
      String PINCHER = "pincher.ttf";
   }

   @AssetSource(directory = "texture", assetClass = Texture.class)
   interface Textures {
      String SPIRIT_EARTH_KODAMA_SRITESHEET = "kodama-sprite-sheet.png";
      String SPIRIT_WATER_AME_SPRITESHEET = "ame-sprite-sheet.png";
      String SPIRIT_FIRE_HI_SPRITESHEET = "hi-sprite-sheet.png";

      String TREE = "tree.png";

      String BACKGROUND_FLOOR = "background-floor.png";

      String LOGO = "logo.png";

      String GAUGE_WATER = "gauge-water.png";
      String GAUGE_WATER_TOP = "gauge-water-top.png";
      String GAUGE_OVERLAY = "gauge-overlay.png";
      String GAUGE_GRASS = "gauge-grass.png";

      String SKY_DAY = "sky-midday-clear.png";
      String SKY_EVENING = "sky-evening-clear.png";
      String SKY_RAIN = "sky-rain.png";

      String CITYSCAPE_FRONT = "cityscape-front.png";
      String CITYSCAPE_MIDDLE = "cityscape-middle.png";
      String CITYSCAPE_FAR = "cityscape-far.png";

      String BUBBLE = "bubble.9.png";
      String BUBBLE_BOTTOM = "bubble-bottom.png";
      String EMOTE_SPRITESHEET = "emote-spritesheet.png";
   }

   @AssetSource(directory = "music", assetClass = Music.class)
   interface Musics {
      String BACKGROUND_01 = "bgm_01.ogg";
      String OUTRO = "outro.ogg";
      String CITYSCAPE = "city_01.ogg";
      String FAIL = "fail.ogg";
      String MENU = "menu.ogg";
   }

   @AssetSource(directory = "sound", assetClass = Sound.class)
   interface Sounds {
      String WATER_DEATH_01 = "waterdeath_01.ogg";
      String WATER_DEATH_02 = "waterdeath_02.ogg";
      String WATER_DEATH_03 = "waterdeath_03.ogg";

      String DEATH_01 = "death_01.ogg";
      String DEATH_02 = "death_02.ogg";

      String BRUSH_01 = "brush_01.ogg";
      String BRUSH_02 = "brush_02.ogg";
      String BRUSH_03 = "brush_03.ogg";

      String DEATH = "woosh.ogg";
      String DEATH_SHORT = "woosh_short.ogg";

      String SPEECH_SPIRIT_EARTH_01 = "speech_main_01.ogg";
      String SPEECH_SPIRIT_EARTH_02 = "speech_main_02.ogg";
      String SPEECH_SPIRIT_EARTH_03 = "speech_main_03.ogg";
      String SPEECH_SPIRIT_EARTH_04 = "speech_main_04.ogg";
      String SPEECH_SPIRIT_EARTH_05 = "speech_main_05.ogg";
      String SPEECH_SPIRIT_EARTH_06 = "speech_main_06.ogg";
      String SPEECH_SPIRIT_EARTH_07 = "speech_main_07.ogg";
      String SPEECH_SPIRIT_EARTH_08 = "speech_main_08.ogg";
      String SPEECH_SPIRIT_EARTH_09 = "speech_main_09.ogg";
      String SPEECH_SPIRIT_EARTH_10 = "speech_main_10.ogg";
      String SPEECH_SPIRIT_EARTH_11 = "speech_main_11.ogg";

      String SPEECH_SPIRIT_WATER_01 = "speech_water_01.ogg";
      String SPEECH_SPIRIT_WATER_02 = "speech_water_02.ogg";
      String SPEECH_SPIRIT_WATER_03 = "speech_water_03.ogg";
      String SPEECH_SPIRIT_WATER_04 = "speech_water_04.ogg";
      String SPEECH_SPIRIT_WATER_05 = "speech_water_05.ogg";
      String SPEECH_SPIRIT_WATER_06 = "speech_water_06.ogg";

      String SPEECH_SPIRIT_SUN_01 = "speech_light_01.ogg";
      String SPEECH_SPIRIT_SUN_02 = "speech_light_02.ogg";
      String SPEECH_SPIRIT_SUN_03 = "speech_light_03.ogg";
      String SPEECH_SPIRIT_SUN_04 = "speech_light_04.ogg";
      String SPEECH_SPIRIT_SUN_05 = "speech_light_05.ogg";
      String SPEECH_SPIRIT_SUN_06 = "speech_light_06.ogg";
      String SPEECH_SPIRIT_SUN_07 = "speech_light_07.ogg";
      String SPEECH_SPIRIT_SUN_08 = "speech_light_08.ogg";
   }

   @AssetSource(directory = "particles", assetClass = ParticleEffect.class)
   interface Particles {
      String EARTH = "earth.p";
      String FIRE = "fire.p";
      String WATER = "water.p";

      String RAIN = "rain.p";
   }
}
