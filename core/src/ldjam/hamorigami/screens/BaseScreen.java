package ldjam.hamorigami.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationFrames;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.screen.BrainGdxScreen2D;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.HamorigamiGame;
import ldjam.hamorigami.effects.DayProgress;
import ldjam.hamorigami.entity.EntityFactory;
import ldjam.hamorigami.entity.SpiritSpawnPool;
import ldjam.hamorigami.graphics.*;
import ldjam.hamorigami.model.ObjectType;
import ldjam.hamorigami.model.SpiritAnimationType;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.model.TreeStatus;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static ldjam.hamorigami.Assets.Textures.*;
import static ldjam.hamorigami.model.SpiritType.SPIRIT_SUN;
import static ldjam.hamorigami.model.SpiritType.SPIRIT_WATER;

public abstract class BaseScreen extends BrainGdxScreen2D<HamorigamiGame> {

   protected GameObject treeObject;
   protected Cityscape cityscape;

   public BaseScreen(HamorigamiGame game) {
      super(game);
   }

   @Override
   protected void onCreate(final GameContext2D context) {
      ColorTransition colorTransition = new ColorTransition();
      colorTransition.setColor(Color.WHITE.cpy());
      context.setBackgroundColor(Color.valueOf("7766ff"));
      context.setDebug(getGame().isDebug());
      setupLevel(context);
      setupGraphics(context);
      cityscape = new Cityscape(getDayProgress(context));
      context.getRenderPipeline().putAfter(RenderPipeIds.BACKGROUND, "cityscape", cityscape);
   }

   @Override
   protected void onUpdate(float delta) {
      super.onUpdate(delta);
   }

   private void setupLevel(GameContext2D context) {
      EntityFactory entityFactory = new EntityFactory(context);
      context.getGameCamera().setZoom(800, GameCamera.ZoomMode.TO_WIDTH);

      // add tree
      this.treeObject = entityFactory.spawnTree();

      // add floor
      GameObject floorObject = entityFactory.spawnFloor();

      // add gauge
      GameObject gaugeObject = entityFactory.spawnGauge(360, 65);

      // Spirit spawning
      SpiritSpawnPool spiritSpawnPool = new SpiritSpawnPool();
   }

   private void setupGraphics(GameContext2D context) {
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

      context.getRenderManager().register(ObjectType.TREE, new DayProgressRenderer(getDayProgress(context), TREE, TREE_EVENING));
      context.getRenderManager().register(ObjectType.FLOOR, new DayProgressRenderer(getDayProgress(context), BACKGROUND_FLOOR, BACKGROUND_FLOOR_EVENING));
      context.getRenderManager().register(ObjectType.GAUGE, new GaugeRenderer(treeObject));
   }

   protected abstract DayProgress getDayProgress(GameContext2D context);
}
