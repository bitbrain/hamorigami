package ldjam.hamorigami.setup.loader.commands.inputs;

import ldjam.hamorigami.setup.loader.commands.InputMapper;

public class StringInput implements InputMapper {

   @Override
   public String map(String input) {
      return input;
   }
}
