package ldjam.hamorigami.gamemode;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.behavior.TreeBehavior;
import ldjam.hamorigami.behavior.TreeHealthBindingBehavior;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.entity.*;
import ldjam.hamorigami.input.Proceedable;
import ldjam.hamorigami.input.ingame.IngameControllerAdapter;
import ldjam.hamorigami.input.ingame.IngameKeyboardAdapter;
import ldjam.hamorigami.input.ingame.ProceedableControllerAdapter;
import ldjam.hamorigami.model.HealthData;
import ldjam.hamorigami.model.Movement;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.setup.GameplaySetup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ldjam.hamorigami.Assets.Musics.BACKGROUND_01;
import static ldjam.hamorigami.model.SpiritType.*;

public class GameplayPhase implements GamePhase, Proceedable {

   private static final List<SpiritType> CANDIDATES = new ArrayList<SpiritType>();

   static {
      for (SpiritType type : SpiritType.values()) {
         if (type != SPIRIT_EARTH) {
            CANDIDATES.add(type);
         }
      }
   }

   private Music music;
   private Music cityscapeMusic;

   private GameObject playerObject;
   private GameObject treeObject;
   private AttackHandler attackHandler;
   private SpiritSpawner spawner;

   private boolean gameOver;

   private HamorigamiContext context;

   private final GamePhaseHandler gamePhaseHandler;
   private SpiritSpawnPool spiritSpawnPool;
   private final GameplaySetup setup;

   public GameplayPhase(GamePhaseHandler gamePhaseHandler, GameplaySetup setup) {
      this.gamePhaseHandler = gamePhaseHandler;
      this.setup = setup;
   }

   @Override
   public void disable(HamorigamiContext context, GameObject treeObject) {
      music.stop();
      cityscapeMusic.setVolume(0.4f);
   }

   @Override
   public void enable(final HamorigamiContext context, final GameObject treeObject) {
      setup.resetDay();
      Asset.get(Assets.Musics.MENU, Music.class).stop();
      gameOver = false;
      this.context = context;
      this.treeObject = treeObject;
      this.music = Asset.get(BACKGROUND_01, Music.class);
      cityscapeMusic = Asset.get(Assets.Musics.CITYSCAPE, Music.class);
      cityscapeMusic.setVolume(0.2f);
      music.setLooping(true);
      music.setVolume(0.1f);
      music.play();

      EntityFactory entityFactory = new EntityFactory(context);

      this.playerObject = null;
      for (GameObject existingSpirit : context.getGameWorld().getGroup("spirits")) {
         if (existingSpirit.getType() == SPIRIT_EARTH) {
            this.playerObject = existingSpirit;
            context.getBehaviorManager().apply(context.getCameraParallaxor(), playerObject);
            break;
         }
      }
      if (playerObject == null) {
         // add player
         this.playerObject = entityFactory.spawnSpirit(
               SpiritType.SPIRIT_EARTH,
               context.getGameCamera().getScaledCameraWidth() / 2f, 0f
         );
         this.playerObject.getColor().a = 0f;
         final String playerId = playerObject.getId();
         Tween.to(this.playerObject.getColor(), ColorTween.A, 3f)
               .target(1f)
               .start(SharedTweenManager.getInstance());
         playerObject.setDimensions(64f, 64f);
         ColorTransition colorTransition = new ColorTransition();
         colorTransition.setColor(Color.valueOf("9cd2ff"));
      }

      // Spirit spawning
      this.spiritSpawnPool = new SpiritSpawnPool();

      for (Map.Entry<Float, SpiritType[]> entry : setup.getCurrentDaySetup().getSpawns().entrySet()) {
         spiritSpawnPool.addSpawnWave(entry.getKey(), entry.getValue());
      }

      spawner = new SpiritSpawner(spiritSpawnPool, entityFactory, context, treeObject);

      attackHandler = new AttackHandler(playerObject, entityFactory, context.getAudioManager());
      context.getBehaviorManager().apply(new SpiritedAway(context));

      setupInput(context);

      context.getBehaviorManager().apply(new TreeBehavior(), treeObject);
      context.getBehaviorManager().apply(new TreeHealthBindingBehavior(this.treeObject, context.getAudioManager(), gamePhaseHandler, context), this.playerObject);
      context.getBehaviorManager().apply(playerObject.getAttribute(Movement.class), playerObject);
   }

   @Override
   public boolean isFinished() {
      return gameOver || treeObject.getAttribute(HealthData.class).isDead();
   }

   @Override
   public void update(float delta) {
      if (gameOver) {
         return;
      }
      setup.update(delta);
      spawner.update(delta);
      attackHandler.update(delta);

      if (setup.isEndOfDay() && !spawner.canSpawn() && !gameOver) {
         // check that the only spirit left is the player
         Array<GameObject> objects = context.getGameWorld().getObjects();
         boolean spiritStillAlive = false;
         for (GameObject object : objects) {
            if (CANDIDATES.contains(object.getType())) {
               spiritStillAlive = true;
               break;
            }
         }

         // game successful!
         if (!spiritStillAlive) {
            kilAllSpirits(true);
            gameOver = true;
            if (setup.getCurrentDaySetup().getEndCutscene() == null) {
               setup.triggerNextDay();
            }
            gamePhaseHandler.changePhase(Phases.CUTSCENE);
         }
      }
   }

   private void kilAllSpirits(boolean excludePlayer) {
      // let all spirits fade away
      for (GameObject spirit : context.getGameWorld().getGroup("spirits")) {
         if (excludePlayer && spirit.getType() == SPIRIT_EARTH) {
            continue;
         }
         final String spiritId = spirit.getId();
         SharedTweenManager.getInstance().killTarget(spirit);
         SharedTweenManager.getInstance().killTarget(spirit.getColor());
         Tween.to(spirit, GameObjectTween.OFFSET_Y, 0.5f)
               .target(180)
               .start(SharedTweenManager.getInstance());
         Tween.to(spirit.getColor(), ColorTween.A, 0.5f)
               .target(0f)
               .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int type, BaseTween<?> tween) {
                     context.getGameWorld().remove(spiritId);
                  }
               })
               .setCallbackTriggers(TweenCallback.COMPLETE)
               .ease(TweenEquations.easeOutCubic)
               .start(SharedTweenManager.getInstance());
      }
      playerObject = null;
   }

   private void setupInput(GameContext2D context) {
      context.getInputManager().register(new IngameKeyboardAdapter(playerObject, attackHandler, this));
      context.getInputManager().register(new IngameControllerAdapter(playerObject, attackHandler));
      context.getInputManager().register(new ProceedableControllerAdapter(this));
   }

   @Override
   public void proceed() {

   }

   @Override
   public void skip() {
      kilAllSpirits(false);
      gameOver = true;
      gamePhaseHandler.changePhase(Phases.CREDITS);
   }
}
