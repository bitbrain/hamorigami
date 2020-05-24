package ldjam.hamorigami.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.screen.AbstractBrainGdxScreen2D;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.util.ArgumentFactory;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.GameColors;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.graphics.*;
import ldjam.hamorigami.model.ObjectType;
import ldjam.hamorigami.model.SpiritAnimationType;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.model.TreeStatus;
import ldjam.hamorigami.setup.GameplaySetup;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static ldjam.hamorigami.Assets.Textures.*;
import static ldjam.hamorigami.model.SpiritType.SPIRIT_SUN;
import static ldjam.hamorigami.model.SpiritType.SPIRIT_WATER;

public abstract class BaseScreen extends AbstractBrainGdxScreen2D<HamorigamiGame, HamorigamiContext> {

   protected GameObject treeObject;
   protected GameplaySetup setup;
   private HamorigamiContext context;

   public BaseScreen(final HamorigamiGame game) {
      super(game, new ArgumentFactory<AbstractScreen<HamorigamiGame, HamorigamiContext>, HamorigamiContext>() {
         @Override
         public HamorigamiContext create(AbstractScreen<HamorigamiGame, HamorigamiContext> supplier) {
            return new HamorigamiContext(supplier.getViewportFactory(), new ShaderConfig(), game, supplier);
         }
      });
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
      if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
         context.getWeatherManager().thunder();
      }
   }

   @Override
   protected void onCreate(final HamorigamiContext context) {
      this.context = context;
      context.getGameCamera().setStickToWorldBounds(false);
      context.getGameCamera().setZoom(800, GameCamera.ZoomMode.TO_WIDTH);
      ColorTransition colorTransition = new ColorTransition();
      colorTransition.setColor(Color.WHITE.cpy());
      context.setBackgroundColor(GameColors.EVENING_AMBIENT_COLOR);
      context.setDebug(getGame().isDebug());
      setupLevel(context);
      this.setup = buildGameplaySetup(context);
      setupGraphics(context);
      context.getRenderPipeline().put(RenderPipeIds.BACKGROUND, new RenderLayer2D() {
         @Override
         public void render(Batch batch, float delta) {
            float x = context.getGameCamera().getLeft();
            float y = context.getGameCamera().getTop();

            Color color = batch.getColor();
            if (context.getWeatherManager().getRainIntensity() == 0f) {
               Texture background = Asset.get(SKY_DAY, Texture.class);

               batch.setColor(Color.WHITE);
               batch.begin();
               batch.draw(background, x, y);

               Texture background_noon = Asset.get(SKY_EVENING, Texture.class);
               float eveningFactor = (float) (1f - Math.sin(Math.PI * setup.getDayProgress()));
               batch.setColor(1f, 1f, 1f, eveningFactor);
               batch.draw(background_noon, x, y);
               batch.end();
            } else {
               Texture background = Asset.get(SKY_RAIN, Texture.class);
               batch.setColor(Color.WHITE);
               batch.begin();
               batch.draw(background, x, y);
               batch.end();
            }
            batch.setColor(color);
         }
      });
      context.getRenderPipeline().putBefore(HamorigamiContext.FURTHER_BACKGROUND_PARTICLES_LAYER, "cityscape-back", new Cityscape(setup, context,
            new Cityscape.Layer(1.1f, 1f, CITYSCAPE_FAR)));
      context.getRenderPipeline().putAfter(HamorigamiContext.FURTHER_BACKGROUND_PARTICLES_LAYER, "cityscape-front", new Cityscape(setup, context,
            new Cityscape.Layer(1.3f, 0.6f, CITYSCAPE_MIDDLE),
            new Cityscape.Layer(1.5f, 0.3f, CITYSCAPE_FRONT)));
   }

   private void setupLevel(HamorigamiContext context) {

      // add tree
      this.treeObject = context.getEntityFactory().spawnTree();

      // add floor
      GameObject floorObject = context.getEntityFactory().spawnFloor();

      // add gauge
      GameObject gaugeObject = context.getEntityFactory().spawnGauge(360, 65);

      // register anchors
      context.getAnchorManager().addAnchor("gauge", gaugeObject);
      context.getAnchorManager().addAnchor("floor", floorObject);
      context.getAnchorManager().addAnchor("tree", treeObject);
   }

   private void setupGraphics(HamorigamiContext context) {
      context.getRenderManager().setRenderOrderComparator(new EntityOrderComparator());
      AnimationSpriteSheet kodamaSpritesheet = new AnimationSpriteSheet(
            SPIRIT_EARTH_KODAMA_SRITESHEET, 64, 64
      );
      AnimationSpriteSheet hiSpritesheet = new AnimationSpriteSheet(
            SPIRIT_FIRE_HI_SPRITESHEET, 32, 64
      );
      AnimationSpriteSheet ameSpritesheet = new AnimationSpriteSheet(
            SPIRIT_WATER_AME_SPRITESHEET, 32, 64
      );

      context.getRenderManager().register(SpiritType.SPIRIT_EARTH, new SpiritRenderer(context.getGameCamera(), kodamaSpritesheet, AnimationConfig.builder()
            .registerFrames(SpiritAnimationType.SWIPING_EAST, AnimationFrames.builder()
                  .origin(0, 6)
                  .frames(6)
                  .duration(0.1f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.SWIPING_WEST, AnimationFrames.builder()
                  .origin(0, 7)
                  .frames(6)
                  .duration(0.1f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.ATTACKING_EAST, AnimationFrames.builder()
                  .origin(0, 5)
                  .frames(4)
                  .duration(0.05f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.ATTACKING_WEST, AnimationFrames.builder()
                  .origin(0, 4)
                  .frames(4)
                  .duration(0.05f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_EAST, AnimationFrames.builder()
                  .origin(0, 1)
                  .frames(5)
                  .duration(0.1f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_WEST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(5)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_EAST, AnimationFrames.builder()
                  .origin(0, 3)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_WEST, AnimationFrames.builder()
                  .origin(0, 2)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .build()) {
         @Override
         public void render(GameObject object, Batch batch, float delta) {
            TreeStatus treeStatus = treeObject.getAttribute(TreeStatus.class);
            float alpha = object.getColor().a;
            if (treeStatus.getTreeWateredLevel() < 0f) {
               Color health = Color.RED.cpy().lerp(Color.WHITE, 1f - treeStatus.getTreeWateredLevel() / -1f);
               health.a = alpha;
               object.setColor(health);
            } else {
               Color health = Color.BLUE.cpy().lerp(Color.WHITE, 1f - treeStatus.getTreeWateredLevel() / 1f);
               health.a = alpha;
               object.setColor(health);
            }
            super.render(object, batch, delta);
         }
      });
      context.getRenderManager().register(SPIRIT_WATER, new SpiritRenderer(context.getGameCamera(), ameSpritesheet, AnimationConfig.builder()
            .registerFrames(SpiritAnimationType.LANDING_WEST, AnimationFrames.builder()
                  .origin(0, 2)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.LANDING_EAST, AnimationFrames.builder()
                  .origin(0, 3)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_EAST, AnimationFrames.builder()
                  .origin(0, 5)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_WEST, AnimationFrames.builder()
                  .origin(0, 4)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_EAST, AnimationFrames.builder()
                  .origin(0, 4)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_WEST, AnimationFrames.builder()
                  .origin(0, 5)
                  .frames(4)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.FALLING_EAST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.FALLING_WEST, AnimationFrames.builder()
                  .origin(0, 1)
                  .frames(1)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .build()));
      context.getRenderManager().register(SPIRIT_SUN, new SpiritRenderer(context.getGameCamera(), hiSpritesheet, AnimationConfig.builder()
            .registerFrames(SpiritAnimationType.IDLE_EAST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.IDLE_WEST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_EAST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.HOVERING_WEST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.FALLING_WEST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .registerFrames(SpiritAnimationType.FALLING_EAST, AnimationFrames.builder()
                  .origin(0, 0)
                  .frames(8)
                  .duration(0.2f)
                  .playMode(LOOP)
                  .build())
            .build()));

      context.getRenderManager().register(ObjectType.TREE, new DayProgressRenderer(setup, context.getWeatherManager(), TREE));
      context.getRenderManager().register(ObjectType.FLOOR, new DayProgressRenderer(setup, context.getWeatherManager(), BACKGROUND_FLOOR));
      context.getRenderManager().register(ObjectType.GAUGE, new GaugeRenderer(treeObject));
   }

   protected abstract GameplaySetup buildGameplaySetup(HamorigamiContext context);
}
