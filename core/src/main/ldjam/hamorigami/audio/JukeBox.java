package ldjam.hamorigami.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.audio.AudioManager;

import java.util.Random;

public class JukeBox {

   public static float MINIMUM_INTERVAL_MILLIS = 400f;

   private final AudioManager audioManager;
   private final String[] audioFiles;
   private float pitchVariation = 0.1f;
   private final float range;
   private long interval = -1;
   private float volume = 0.3f;
   private float basePitch = 1f;
   private float minimumIntervalMillis = MINIMUM_INTERVAL_MILLIS;
   private Random random = new Random();

   private boolean threedeesound = true;

   public JukeBox(AudioManager audioManager, float range, String... audioFiles) {
      this.audioManager = audioManager;
      this.range = range;
      this.audioFiles = audioFiles;
   }

   public void setSeed(String seed) {
      if (seed != null) {
         this.random = new Random(seed.hashCode());
      } else {
         this.random = new Random();
      }
   }

   public void setBasePitch(float basePitch) {
      this.basePitch = basePitch;
   }

   public void setVolume(float volume) {
      this.volume = volume;
   }

   public void setPitchVariation(float pitchVariation) {
      this.pitchVariation = pitchVariation;
   }

   public void setMinimumIntervalMillis(float minimumIntervalMillis) {
      this.minimumIntervalMillis = minimumIntervalMillis;
   }

   public void set3DSound(boolean enabled) {
      this.threedeesound = enabled;
   }

   public void playSound(float x, float y) {
      if (audioFiles == null || audioFiles.length == 0) {
         Gdx.app.error("SOUND", "Missing audio files in JukeBox!");
         return;
      }
      if (interval >= 0 && System.currentTimeMillis() - interval < minimumIntervalMillis) {
         return;
      }
      String audioFile = audioFiles[(int) (audioFiles.length * random.nextFloat())];
      if (threedeesound) {
         float pitch = (float) ((basePitch - pitchVariation / 2f) + (pitchVariation * Math.random()));
         audioManager.spawnSound(audioFile, x, y, pitch, volume, range);
      } else {
         Asset.get(audioFile, Sound.class).play(1f);
      }
      interval = System.currentTimeMillis();
   }
}