package ldjam.hamorigami.setup.loader.commands.inputs;

import ldjam.hamorigami.setup.loader.commands.InputMapper;

public class NumberInput implements InputMapper {

   @Override
   public Integer map(String input) {
      return Integer.parseInt(input);
   }
}
