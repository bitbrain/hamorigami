package ldjam.hamorigami.weather;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.context.HamorigamiContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeatherManager {

   private final HamorigamiContext context;

   private float rainIntensity;
   private List<ParticleEffect> effects = new ArrayList<ParticleEffect>();

   public WeatherManager(HamorigamiContext context) {
      this.context = context;
   }

   public float getRainIntensity() {
      return rainIntensity;
   }

   public void setRainIntensity(float intensity) {
      this.rainIntensity = intensity;
      if (intensity > 0f && effects.isEmpty()) {
         effects.add(context.getFurtherBackgroundParticleManager().spawnEffect(Assets.Particles.RAIN_DISTANT,
               context.getGameCamera().getLeft(),
               context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight() + 50f));
         effects.add(context.getBackgroundParticleManager().spawnEffect(Assets.Particles.RAIN,
               context.getGameCamera().getLeft(),
               context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight() + 100f));
      }
      if (intensity == 0f) {

      }
   }
}
