package ldjam.hamorigami;

import com.badlogic.gdx.graphics.Color;
import ldjam.hamorigami.weather.WeatherManager;

public final class GameColors {

   public static final Color MIDDAY_ATMOSPHERIC_COLOR = Color.valueOf("bdf7ff");
   public static final Color MIDDAY_AMBIENT_COLOR = Color.valueOf("d6eeff");

   public static final Color EVENING_ATMOSPHERE_COLOR = Color.valueOf("ffcaad");
   public static final Color EVENING_AMBIENT_COLOR = Color.valueOf("ffbebd");

   public static final Color RAIN_ATMOSPHERE_COLOR = Color.valueOf("404c4f");
   public static final Color RAIN_AMBIENT_COLOR = Color.valueOf("3a3e47");

   public static Color getScatteringColor(WeatherManager weatherManager, float eveningFactor, float dayFactor) {
      if (weatherManager.getRainIntensity() > 0f) {
         return RAIN_ATMOSPHERE_COLOR;
      }
      Color middayAtmosphericColor = Color.WHITE.cpy().lerp(MIDDAY_ATMOSPHERIC_COLOR, dayFactor);
      return middayAtmosphericColor.lerp(EVENING_ATMOSPHERE_COLOR, eveningFactor);
   }

   public static Color getAmbientColor(WeatherManager weatherManager, float eveningFactor, float dayFactor) {
      if (weatherManager.getRainIntensity() > 0f) {
         return RAIN_AMBIENT_COLOR;
      }
      Color middayAmbientColor = Color.WHITE.cpy().lerp(MIDDAY_AMBIENT_COLOR, dayFactor);
      return middayAmbientColor.lerp(EVENING_AMBIENT_COLOR, eveningFactor);
   }
}
