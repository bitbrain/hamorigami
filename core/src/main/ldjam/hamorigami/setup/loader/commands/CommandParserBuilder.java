package ldjam.hamorigami.setup.loader.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandParserBuilder {

   private final Map<String, CommandBehavior> behaviorMap = new HashMap<String, CommandBehavior>();
   private final Map<String, Argument[]> argumentMap = new HashMap<String, Argument[]>();
   private final Map<String, InputMapper> inputMappers = new HashMap<String, InputMapper>();

   public CommandParserBuilder withCommand(String command, CommandBehavior behavior, Argument ... arguments) {
      behaviorMap.put(command, behavior);
      argumentMap.put(command, arguments);
      return this;
   }

   public CommandParserBuilder withCommand(String command, CommandBehavior behavior, InputMapper inputMapper, Argument... arguments) {
      behaviorMap.put(command, behavior);
      argumentMap.put(command, arguments);
      inputMappers.put(command, inputMapper);
      return this;
   }

   public CommandParserBuilder withCommand(String command, CommandBehavior behavior, InputMapper inputMapper) {
      behaviorMap.put(command, behavior);
      inputMappers.put(command, inputMapper);
      return this;
   }

   public CommandParser build() {
      return new CommandParser(behaviorMap, argumentMap, inputMappers);
   }
}
