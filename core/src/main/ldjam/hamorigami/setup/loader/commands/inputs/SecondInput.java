package ldjam.hamorigami.setup.loader.commands.inputs;

import ldjam.hamorigami.setup.loader.commands.InputMapper;

public class SecondInput implements InputMapper {

   @Override
   public Float map(String input) {
      return Float.valueOf(input);
   }
}
