package ldjam.hamorigami.context;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.context.GameContext2DImpl;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerImpl;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.util.ViewportFactory;
import ldjam.hamorigami.anchor.AnchorManager;
import ldjam.hamorigami.behavior.CameraParallaxor;
import ldjam.hamorigami.cutscene.emotes.EmoteManager;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.weather.WeatherManager;

public class HamorigamiContext extends GameContext2DImpl {

   private final EntityFactory entityFactory;
   private final EmoteManager emoteManager;
   private final AnchorManager anchorManager;
   private final WeatherManager weatherManager;
   private final ParticleManagerImpl backgroundParticleManager;
   private final CameraParallaxor parallaxor;

   public HamorigamiContext(ViewportFactory viewportFactory,
                            ShaderConfig shaderConfig,
                            BrainGdxGame game,
                            AbstractScreen<?, ?> screen) {
      super(viewportFactory, shaderConfig, game, screen);
      this.entityFactory = new EntityFactory(this);
      this.emoteManager = new EmoteManager(this);
      this.anchorManager = new AnchorManager();
      this.weatherManager = new WeatherManager(this);
      this.backgroundParticleManager = new ParticleManagerImpl(getBehaviorManager(), getSettings().getGraphics());
      this.parallaxor = new CameraParallaxor(getGameCamera());
      getRenderPipeline().putBefore(
            RenderPipeIds.WORLD,
            "background-particles",
            new ParticleManagerRenderLayer(backgroundParticleManager));


   }

   @Override
   public void dispose() {
      super.dispose();
      backgroundParticleManager.dispose();
   }

   public CameraParallaxor getCameraParallaxor() {
      return parallaxor;
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
   
   public WeatherManager getWeatherManager() {
      return weatherManager;
   }

   public ParticleManager getBackgroundParticleManager() {
      return backgroundParticleManager;
   }
}
