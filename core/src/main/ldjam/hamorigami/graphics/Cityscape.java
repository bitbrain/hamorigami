package ldjam.hamorigami.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.setup.GameplaySetup;

import static ldjam.hamorigami.Assets.Textures.*;
import static ldjam.hamorigami.GameColors.EVENING_COLOR;
import static ldjam.hamorigami.GameColors.MIDDAY_COLOR;

public class Cityscape extends RenderLayer2D {

   private final GameplaySetup setup;
   private final HamorigamiContext context;

   private final ParallaxMap cityFront;
   private final ParallaxMap cityBack;
   private final ParallaxMap cityFar;
   public Cityscape(GameplaySetup setup, HamorigamiContext context) {
      this.context = context;
      this.setup = setup;
      cityFront = new ParallaxMap(CITYSCAPE_FRONT, context.getGameCamera(), 1.4f);
      cityFront.scale(1.1f);
      cityBack = new ParallaxMap(CITYSCAPE_MIDDLE, context.getGameCamera(), 1.3f);
      cityBack.scale(1.1f);
      cityFar = new ParallaxMap(CITYSCAPE_FAR, context.getGameCamera(), 1.1f);
      cityFar.scale(1.1f);
   }

   @Override
   public void render(Batch batch, float delta) {
      float x = context.getGameCamera().getLeft();
      float y = context.getGameCamera().getTop();

      Texture background = Asset.get(SKY_DAY, Texture.class);

      batch.begin();
      batch.draw(background, x, y);

      Texture background_noon = Asset.get(SKY_EVENING, Texture.class);
      Color color = batch.getColor();
      float eveningFactor = (float) (1f - Math.sin(Math.PI * setup.getDayProgress()));
      float dayFactor = 1f - eveningFactor;

      batch.setColor(1f, 1f, 1f, eveningFactor);
      batch.draw(background_noon, x, y);

      batch.setColor(color);
      Color foreground = Color.WHITE.cpy().lerp(MIDDAY_COLOR, dayFactor);
      Color eveningColor = foreground.lerp(EVENING_COLOR, eveningFactor);

      ShaderProgram program = Asset.get(Assets.Shaders.TEXTURE_TINT, ShaderProgram.class);
      System.out.println(program.getLog());
      batch.setShader(program);
      program.setUniformf("color", eveningColor);
      drawDistanceLayer(cityFar, batch, program);
      drawDistanceLayer(cityBack, batch, program);
      drawDistanceLayer(cityFront, batch, program);
      batch.end();
      batch.setShader(null);
   }

   private void drawDistanceLayer(ParallaxMap map, Batch batch, ShaderProgram program) {
      program.setUniformf("intensity", 1f);
      map.draw(batch);
   }
}
