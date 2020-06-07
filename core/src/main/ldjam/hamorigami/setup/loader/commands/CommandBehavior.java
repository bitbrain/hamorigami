package ldjam.hamorigami.setup.loader.commands;

import java.util.Map;

public interface CommandBehavior {
   void apply(Map<String, Object> args);
}
