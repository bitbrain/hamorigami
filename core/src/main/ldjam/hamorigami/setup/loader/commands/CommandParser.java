package ldjam.hamorigami.setup.loader.commands;

import com.badlogic.gdx.Gdx;

import java.util.*;

public class CommandParser {

   private final Map<String, CommandBehavior> behaviorMap;
   private final Map<String, Argument[]> argumentMap;
   private final Map<String, InputMapper> inputMap;

   public CommandParser(Map<String, CommandBehavior> behaviorMap,
                        Map<String, Argument[]> argumentMap,
                        Map<String, InputMapper> inputMap) {
      this.behaviorMap = behaviorMap;
      this.argumentMap = argumentMap;
      this.inputMap = inputMap;
   }

   public void parse(String[] args) {
      String command = getCommand(args);
      Argument[] allArguments = getArguments(command);
      if (command == null) {
         Gdx.app.error("COMMAND", "Unrecognised commands: " + Arrays.toString(args));
         return;
      }
      Map<Argument, String> argumentMap = collectArgumentsAndInputs(args, allArguments);
      // transform into objects
      Map<String, Object> inputs = transformIntoInputs(argumentMap);
      InputMapper inputMapper = inputMap.get(command);
      if (inputMapper != null && args.length > 1) {
         inputs.put(command, inputMapper.map(args[1]));
      }
      if (behaviorMap.get(command) != null) {
         behaviorMap.get(command).apply(inputs);
      } else {
         Gdx.app.error("COMMAND", "Unrecognised commands: " + Arrays.toString(args));
      }
   }

   private Argument[] getArguments(String command) {
      Argument[] args = argumentMap.get(command);
      return args != null ? args : new Argument[0];
   }

   private Map<String, Object> transformIntoInputs(Map<Argument, String> argumentMap) {
      Map<String, Object> transformedMap = new HashMap<String, Object>();
      for (Map.Entry<Argument, String> entry : argumentMap.entrySet()) {
         Argument argument = entry.getKey();
         Object transformedValue = argument.getInputMapper().map(entry.getValue());
         transformedMap.put(argument.getName(), transformedValue);
      }
      return transformedMap;
   }

   private String getCommand(String[] args) {
      for (String arg : args) {
         if (argumentMap.containsKey(arg.toLowerCase())) {
            return arg.toLowerCase();
         }
      }
      return args.length > 0 ? args[0] : null;
   }

   private Map<Argument, String> collectArgumentsAndInputs(String[] args, Argument[] allArguments) {
      Map<Argument, String> argumentMap = new HashMap<Argument, String>();
      for (int i = 0; i < args.length; ++i) {
         String arg = args[i];
         for (Argument argument : allArguments) {
            if (arg.toLowerCase().equals(argument.getName())) {
               String input = i < args.length - 1 ? args[i + 1] : "";
               argumentMap.put(argument, input);
            }
         }
      }
      return argumentMap;
   }
}
