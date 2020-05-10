package ldjam.hamorigami.cutscene.emotes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.assets.Asset;
import ldjam.hamorigami.Assets;

public enum Emote {
   SMILE(0),
   CONFUSION(1),
   SADNESS(2),
   SURPRISE(3),
   DISAGREE(4),
   AGREE(5);

   private Animation<TextureRegion> animation;
   private final int yIndex;

   Emote(int yIndex) {
      this.yIndex = yIndex;
   }

   public Animation<TextureRegion> getAnimation() {
      if (this.animation == null) {
         final int frames = 8;
         final int size = 32;
         final float duration = 0.1f;
         final Texture texture = Asset.get(Assets.Textures.EMOTE_SPRITESHEET, Texture.class);
         Array<TextureRegion> regions = new Array<TextureRegion>();
         for (int i = 0; i < frames; ++i) {
            regions.add(new TextureRegion(texture, i * size, yIndex * size, size, size));
         }
         this.animation = new Animation<TextureRegion>(duration, regions, Animation.PlayMode.LOOP_PINGPONG);
      }
      return animation;
   }
}
