package ldjam.hamorigami.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.bitbrain.braingdx.assets.Asset;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import ldjam.hamorigami.Assets;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.setup.GameplaySetup;

import static ldjam.hamorigami.Assets.Textures.*;
import static ldjam.hamorigami.GameColors.*;

public class Cityscape extends RenderLayer2D {

   private final GameplaySetup setup;
   private final HamorigamiContext context;

   private final ParallaxMap cityFront;
   private final ParallaxMap cityBack;
   private final ParallaxMap cityFar;
   public Cityscape(GameplaySetup setup, HamorigamiContext context) {
      this.context = context;
      this.setup = setup;
      cityFront = new ParallaxMap(CITYSCAPE_FRONT, context.getGameCamera(), 1.5f);
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
      Color middayAtmosphericColor = Color.WHITE.cpy().lerp(MIDDAY_ATMOSPHERIC_COLOR, dayFactor);
      Color eveningAtmosphericColor = middayAtmosphericColor.lerp(EVENING_ATMOSPHERE_COLOR, eveningFactor);
      Color middayAmbientColor = Color.WHITE.cpy().lerp(MIDDAY_AMBIENT_COLOR, dayFactor);
      Color eveningAmbientColor = middayAmbientColor.lerp(EVENING_AMBIENT_COLOR, eveningFactor);

      ShaderProgram program = Asset.get(Assets.Shaders.TINT_SCATTERING, ShaderProgram.class);
      batch.setShader(program);
      program.setUniformf("scatterColor", eveningAtmosphericColor);
      program.setUniformf("tintColor", eveningAmbientColor);
      program.setUniformf("tintIntensity", 0.7f);
      drawDistanceLayer(cityFar, batch, program, 0.9f);
      batch.end();
      batch.begin();
      drawDistanceLayer(cityBack, batch, program, 0.5f);
      batch.end();
      batch.begin();
      drawDistanceLayer(cityFront, batch, program, 0.3f);
      batch.end();
      batch.setShader(null);
   }

   private void drawDistanceLayer(ParallaxMap map, Batch batch, ShaderProgram program, float scattering) {
      program.setUniformf("scatterIntensity", scattering);
      map.draw(batch);
   }
}
