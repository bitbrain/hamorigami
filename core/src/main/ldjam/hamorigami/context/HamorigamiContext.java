package ldjam.hamorigami.context;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.context.GameContext2DImpl;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.util.ViewportFactory;
import ldjam.hamorigami.anchor.AnchorManager;
import ldjam.hamorigami.cutscene.emotes.EmoteManager;
import ldjam.hamorigami.entity.EntityFactory;

public class HamorigamiContext extends GameContext2DImpl {

   private final EntityFactory entityFactory;
   private final EmoteManager emoteManager;
   private final AnchorManager anchorManager;

   public HamorigamiContext(ViewportFactory viewportFactory,
                            ShaderConfig shaderConfig,
                            BrainGdxGame game,
                            AbstractScreen<?, ?> screen) {
      super(viewportFactory, shaderConfig, game, screen);
      this.entityFactory = new EntityFactory(this);
      this.emoteManager = new EmoteManager(this);
      this.anchorManager = new AnchorManager();
   }

   public EntityFactory getEntityFactory() {
      return entityFactory;
   }

   public EmoteManager getEmoteManager() {
      return emoteManager;
   }

   public AnchorManager getAnchorManager() {
      return anchorManager;
   }
}
