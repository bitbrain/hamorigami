package ldjam.hamorigami.weather;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.audio.JukeBox;
import ldjam.hamorigami.context.HamorigamiContext;

import java.util.ArrayList;
import java.util.List;

import static ldjam.hamorigami.Assets.Musics.RAIN_MEDIUM;

public class WeatherManager {

   public static final float RAIN_TRANSITION_TIME = 3f;

   private final HamorigamiContext context;
   private final ValueProvider rainIntensity = new ValueProvider();
   private final ValueProvider thunderIntensity = new ValueProvider();
   private final List<ParticleEffect> effects = new ArrayList<ParticleEffect>();
   private final JukeBox jukeBox;

   public WeatherManager(HamorigamiContext context) {
      this.context = context;
      this.jukeBox = new JukeBox(context.getAudioManager(), 500,
            Assets.Sounds.THUNDER_01,
            Assets.Sounds.THUNDER_02,
            Assets.Sounds.THUNDER_03,
            Assets.Sounds.THUNDER_04,
            Assets.Sounds.THUNDER_05,
            Assets.Sounds.THUNDER_06);
      jukeBox.setVolume(1f);
      jukeBox.set3DSound(false);
   }

   public float getRainIntensity() {
      return rainIntensity.getValue();
   }

   public float getThunderIntensity() {
      return thunderIntensity.getValue();
   }

   public void thunder() {
      thunderIntensity.setValue(1f);
      Tween.to(thunderIntensity, ValueTween.VALUE, 0.1f)
            .target(0f)
            .start(SharedTweenManager.getInstance());
      jukeBox.playSound(
            context.getGameCamera().getLeft(),
            context.getGameCamera().getTop());
   }

   public void setRainIntensity(float intensity) {
      SharedTweenManager.getInstance().killTarget(rainIntensity);
      Tween.to(rainIntensity, ValueTween.VALUE, RAIN_TRANSITION_TIME)
            .target(intensity)
            .start(SharedTweenManager.getInstance());
      if (intensity > 0f && effects.isEmpty()) {
         Music rain = Asset.get(RAIN_MEDIUM, Music.class);
         rain.setLooping(true);
         rain.setVolume(0.3f);
         rain.play();
         effects.add(context.getFurtherBackgroundParticleManager().spawnEffect(Assets.Particles.RAIN_DISTANT,
               context.getGameCamera().getLeft(),
               context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight() + 50f));
         effects.add(context.getBackgroundParticleManager().spawnEffect(Assets.Particles.RAIN,
               context.getGameCamera().getLeft(),
               context.getGameCamera().getTop() + context.getGameCamera().getScaledCameraHeight() + 100f));
      }
      if (intensity == 0f) {
         Asset.get(RAIN_MEDIUM, Music.class).stop();
         for (ParticleEffect effect : effects) {
            effect.allowCompletion();
         }
         effects.clear();
      }
   }
}
