package ldjam.hamorigami.weather;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.context.HamorigamiContext;

public class WeatherManager {

   private final HamorigamiContext context;

   private float rainIntensity;
   private ParticleEffect effect;

   public WeatherManager(HamorigamiContext context) {
      this.context = context;
   }

   public float getRainIntensity() {
      return rainIntensity;
   }

   public void setRainIntensity(float intensity) {
      this.rainIntensity = intensity;
      if (intensity > 0f && effect == null) {
         effect = context.getBackgroundParticleManager().spawnEffect(Assets.Particles.RAIN,
               context.getGameCamera().getLeft(),
               context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight() + 100f);
      }
      if (intensity == 0f) {

      }
   }
}
