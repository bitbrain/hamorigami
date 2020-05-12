package ldjam.hamorigami.setup.loader.commands.inputs;

import ldjam.hamorigami.anchor.AnchorMask;
import ldjam.hamorigami.setup.loader.commands.InputMapper;

public class AnchorMaskInput implements InputMapper {

   @Override
   public Integer map(String input) {
      String[] args = input.split(",");
      if (args.length > 1) {
         return AnchorMask.valueOf(args[0].toUpperCase()).getByte() |
               AnchorMask.valueOf(args[1].toUpperCase()).getByte();
      }
      return AnchorMask.valueOf(args[0].toUpperCase()).getByte();
   }
}
