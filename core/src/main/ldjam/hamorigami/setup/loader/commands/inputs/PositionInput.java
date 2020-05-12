package ldjam.hamorigami.setup.loader.commands.inputs;

import com.badlogic.gdx.math.Vector2;
import ldjam.hamorigami.setup.loader.commands.InputMapper;

public class PositionInput implements InputMapper {

   @Override
   public Vector2 map(String input) {
      float x;
      float y = 0;
      if (input.contains(",")) {
         String[] args = input.split(",");
         x = Float.parseFloat(args[0]);
         y = Float.parseFloat(args[1]);
      } else {
         x = Float.parseFloat(input);
      }
      return new Vector2(x, y);
   }
}
