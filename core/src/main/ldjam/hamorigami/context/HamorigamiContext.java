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
import ldjam.hamorigami.cutscene.toast.ToastManager;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.ui.Styles;
import ldjam.hamorigami.weather.WeatherManager;

public class HamorigamiContext extends GameContext2DImpl {

   public static final String FURTHER_BACKGROUND_PARTICLES_LAYER = "further-background-particles";
   public static final String BACKGROUND_PARTICLES_LAYER = "background-particles";

   private final EntityFactory entityFactory;
   private final EmoteManager emoteManager;
   private final AnchorManager anchorManager;
   private final WeatherManager weatherManager;
   private final ParticleManagerImpl backgroundParticleManager;
   private final ParticleManagerImpl furtherParticleManager;
   private final CameraParallaxor parallaxor;
   private final ToastManager toastManager;

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
      this.furtherParticleManager = new ParticleManagerImpl(getBehaviorManager(), getSettings().getGraphics());
      this.parallaxor = new CameraParallaxor(getGameCamera());
      this.toastManager = new ToastManager(this, Styles.DIALOG_TEXT);
      getRenderPipeline().putBefore(
            RenderPipeIds.WORLD,
            BACKGROUND_PARTICLES_LAYER,
            new ParticleManagerRenderLayer(backgroundParticleManager));
      getRenderPipeline().putAfter(
            RenderPipeIds.BACKGROUND,
            FURTHER_BACKGROUND_PARTICLES_LAYER,
            new ParticleManagerRenderLayer(furtherParticleManager));


   }

   @Override
   public void updateAndRender(float delta) {
      this.toastManager.update(delta);
      super.updateAndRender(delta);
   }

   @Override
   public void dispose() {
      super.dispose();
      backgroundParticleManager.dispose();
   }

   public ToastManager getToastManager() {
      return toastManager;
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

   public ParticleManager getFurtherBackgroundParticleManager() {
      return furtherParticleManager;
   }
}
