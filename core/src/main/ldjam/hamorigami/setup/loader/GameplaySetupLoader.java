package ldjam.hamorigami.setup.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.CutsceneBuilder;
import ldjam.hamorigami.cutscene.emotes.Emote;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.setup.DaySetupBuilder;
import ldjam.hamorigami.setup.GameplaySetup;
import ldjam.hamorigami.setup.GameplaySetupBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class GameplaySetupLoader {

   private enum ScanMode {
      MORNING_CUTSCENE,
      EVENING_CUTSCENE,
      GAMEPLAY;
   }

   private final HamorigamiContext context;

   public GameplaySetupLoader(HamorigamiContext context) {
      this.context = context;
   }

   public GameplaySetup load(String gameplayFile) throws IOException {
      FileHandle handle = Gdx.files.internal(gameplayFile);
      BufferedReader br = new BufferedReader(handle.reader("UTF-8"));
      String line;
      GameplaySetupBuilder builder = new GameplaySetupBuilder();
      DaySetupBuilder daySetupBuilder = new DaySetupBuilder();
      CutsceneBuilder cutsceneBuilder = new CutsceneBuilder(context);
      int linesRead = 0;
      ScanMode previousScanmode = ScanMode.EVENING_CUTSCENE;
      ScanMode scanMode = ScanMode.GAMEPLAY;
      boolean morningCutscene = false;
      while ((line = br.readLine()) != null) {
         line = line.trim();
         // skip comments and empty lines
         if (line.isEmpty() || line.startsWith("#")) {
            continue;
         }
         linesRead++;
         // a new day has started
         if (line.toLowerCase().startsWith("day")) {
            if (linesRead > 1) {
               builder.addDay(daySetupBuilder.build());
               daySetupBuilder = new DaySetupBuilder();
               previousScanmode = ScanMode.EVENING_CUTSCENE;
               scanMode = ScanMode.GAMEPLAY;
               morningCutscene = false;
            }
            continue;
         }
         // begin cutscene
         if (line.toLowerCase().startsWith("begin cutscene")) {
            ScanMode oldScanMode = scanMode;
            if (scanMode == ScanMode.GAMEPLAY && previousScanmode == ScanMode.EVENING_CUTSCENE && !morningCutscene) {
               morningCutscene = true;
               scanMode = ScanMode.MORNING_CUTSCENE;
            } else {
               scanMode = ScanMode.EVENING_CUTSCENE;
            }
            previousScanmode = oldScanMode;
            continue;
         }
         // end cutscene
         if (line.toLowerCase().startsWith("end cutscene")) {
            if (scanMode == ScanMode.EVENING_CUTSCENE) {
               daySetupBuilder.endOfDayCutscene(cutsceneBuilder.build());
            } else {
               daySetupBuilder.startOfDayCutscene(cutsceneBuilder.build());
               previousScanmode = scanMode;
               scanMode = ScanMode.GAMEPLAY;
            }
            cutsceneBuilder = new CutsceneBuilder(context);
            continue;
         }
         if (scanMode == ScanMode.GAMEPLAY && line.toLowerCase().startsWith("spawn")) {
            morningCutscene = true;
         }
         if (scanMode == ScanMode.GAMEPLAY) {
            processGameplay(line, daySetupBuilder);
         }
         if (scanMode == ScanMode.EVENING_CUTSCENE || scanMode == ScanMode.MORNING_CUTSCENE) {
            processCutscene(line, cutsceneBuilder);
         }
      }
      builder.addDay(daySetupBuilder.build());
      return builder.build();
   }

   private void processGameplay(String line, DaySetupBuilder daySetupBuilder) {
      final int spawnAmount = getSpawnAmount(line);
      final SpiritType spiritType = getSpiritType(line);
      float timeValue = getTimeValue(line);
      SpiritType[] types = new SpiritType[spawnAmount];
      Arrays.fill(types, spiritType);
      if (line.toLowerCase().contains(" every ")) {
         // spawn every x
         if (timeValue == 0f) {
            throw new GdxRuntimeException("Time interval cannot be 0: " + line);
         }
         if (line.toLowerCase().contains(" second")) {
            for (float currentSeconds = timeValue; currentSeconds <= GameplaySetup.SECONDS_PER_DAY; currentSeconds += timeValue) {
               daySetupBuilder.addSpawns(currentSeconds, types);
            }
         } else if (line.contains("%")) {
            float convertedSeconds = GameplaySetup.SECONDS_PER_DAY / 100f * timeValue;
            for (float currentSeconds = convertedSeconds; currentSeconds <= GameplaySetup.SECONDS_PER_DAY; currentSeconds += convertedSeconds) {
               daySetupBuilder.addSpawns(currentSeconds, types);
            }
         } else {
            throw new GdxRuntimeException("Invalid spawn interval: " + line);
         }
      } else if (line.toLowerCase().contains(" at ")) {
         if (line.toLowerCase().contains(" second")) {
            daySetupBuilder.addSpawns(timeValue, types);
         } else if (line.contains("%")) {
            float convertedSeconds = GameplaySetup.SECONDS_PER_DAY / 100f * timeValue;
            daySetupBuilder.addSpawns(convertedSeconds, types);
         } else {
            throw new GdxRuntimeException("Invalid spawn interval: " + line);
         }
      } else {
         throw new GdxRuntimeException("Invalid line: " + line);
      }
   }

   private SpiritType getSpiritType(String line) {
      int endIndex = line.toLowerCase().indexOf(" at");
      if (endIndex == -1) {
         endIndex = line.toLowerCase().indexOf(" every");
      }
      if (endIndex == -1) {
         throw new GdxRuntimeException("Invalid syntax: " + line);
      }
      int startIndex = line.toLowerCase().indexOf("x ");
      if (startIndex == -1) {
         startIndex = 6;
      } else {
         startIndex += 2;
      }
      String spiritName = line.substring(startIndex, endIndex);
      SpiritType resolvedType = SpiritType.resolveByName(spiritName);
      if (resolvedType == null) {
         throw new GdxRuntimeException("Unsupported spirit type: " + spiritName + " at @ " + line);
      }
      return resolvedType;
   }

   private float getTimeValue(String line) {
      int startIndex = line.toLowerCase().indexOf("every ");
      if (startIndex != -1) {
         startIndex += 6;
      } else {
         startIndex = line.toLowerCase().indexOf("at ");
         if (startIndex != -1) {
            startIndex += 3;
         }
      }
      int endIndex = line.toLowerCase().indexOf(" second");
      if (endIndex == -1) {
         endIndex = line.indexOf("%");
      }
      if (startIndex == -1 || endIndex == -1) {
         throw new GdxRuntimeException("Invalid syntax: " + line);
      }
      String timeValue = line.substring(startIndex, endIndex);
      return Float.parseFloat(timeValue.trim());
   }

   private int getSpawnAmount(String line) {
      int startIndex = line.toLowerCase().indexOf("x ");
      if (startIndex == -1) {
         return 1;
      }
      String number = line.replace("spawn", "").replace(line.substring(startIndex), "");
      return Integer.parseInt(number.trim());
   }

   private void processCutscene(String line, CutsceneBuilder cutsceneBuilder) {
      String[] args = line.split(" ");
      if (line.contains(" spawns at ")) {
         processSpawn(line, args, cutsceneBuilder);
      } else if (line.startsWith("wait ")) {
         processWait(line, args, cutsceneBuilder);
      } else if (line.contains(" fades in for ")) {
         processFadeIn(line, args, cutsceneBuilder);
      } else if (line.contains(" fades out for ")) {
         processFadeOut(line, args, cutsceneBuilder);
      } else if (line.contains(" says ")) {
         processSays(line, args, cutsceneBuilder);
      } else if (line.contains(" emotes with ")) {
         processEmotes(line, args, cutsceneBuilder);
      } else if (line.contains(" starts ") || line.contains(" stops ")) {
         processModifyAttribute(line, args, cutsceneBuilder);
      } else if (line.contains(" moves ")) {
         processMovesBy(line, args, cutsceneBuilder);
      } else if (line.startsWith("reset ")) {
         processReset(line, args, cutsceneBuilder);
      } else if (line.startsWith("shake with intensity ")) {
         processShake(line, args, cutsceneBuilder);
      } else {
         throw new GdxRuntimeException("Invalid syntax: " + line);
      }
   }

   private void processShake(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length != 7) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected: shake with intensity <value> for <time> seconds");
      }
      float intensity = Float.parseFloat(args[3]);
      float duration = Float.parseFloat(args[5]);
      cutsceneBuilder.shakeScreen(intensity, duration);
   }

   private void processSpawn(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length != 4) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", must be <entity> spawns at <x>,<y>");
      }
      String[] positionString = args[3].split(",");
      if (positionString.length != 2) {
         throw new GdxRuntimeException("Invalid position specified: " + line);
      }
      float x = Float.parseFloat(positionString[0]);
      float y = Float.parseFloat(positionString[1]);
      String entityName = args[0];
      if (entityName.equals("player")) {
         cutsceneBuilder.spawn("player", SpiritType.SPIRIT_EARTH, x, y, true);
      } else {
         SpiritType type = SpiritType.resolveByName(entityName);
         if (type == null) {
            throw new GdxRuntimeException("Unsupported spirit type: " + entityName + " @ " + line);
         }
         cutsceneBuilder.spawn(entityName, type, x, y);
      }
   }

   private void processReset(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length != 2) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected: reset <entity>");
      }
      cutsceneBuilder.clearTweens(args[1]);
   }

   private void processWait(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length != 3) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected: wait <number> seconds");
      }
      float seconds = Float.parseFloat(args[1]);
      cutsceneBuilder.wait(seconds);
   }

   private void processFadeIn(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length != 6) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected: <entity> fades in for <time> seconds");
      }
      String entity = args[0];
      float seconds = Float.parseFloat(args[4]);
      cutsceneBuilder.fadeIn(entity, seconds);
   }

   private void processFadeOut(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length != 6) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected: <entity> fades out for <time> seconds");
      }
      String entity = args[0];
      float seconds = Float.parseFloat(args[4]);
      cutsceneBuilder.fadeOut(entity, seconds);
   }

   private void processSays(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      int startIndex = 0;
      int endIndex = line.indexOf(" ");
      String entity = line.substring(startIndex, endIndex);
      String sentence = line.substring(endIndex + 6);
      cutsceneBuilder.say(sentence, entity);
   }

   private void processEmotes(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length != 4) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected: <entityy> emotes with <emote>");
      }
      Emote emote = Emote.valueOf(args[3].toUpperCase().replace(" ", "_"));
      String entity = args[0];
      cutsceneBuilder.emote(emote, entity);
   }

   private void processModifyAttribute(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length != 3) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected <entity> <starts|stops> <attribute>");
      }
      String entity = args[0];
      String mode = args[1];
      String attribute = args[2];

      if (mode.equals("starts")) {
         cutsceneBuilder.setAttribute(entity, attribute);
      } else if (mode.equals("stops")) {
         cutsceneBuilder.removeAttribute(entity, attribute);
      } else {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected <entity> <starts|stops> <attribute>");
      }
   }

   private void processMovesBy(String line, String[] args, CutsceneBuilder cutsceneBuilder) {
      if (args.length < 6 && args.length != 3 && args.length != 4) {
         throw new GdxRuntimeException("Invalid syntax: " + line + ", expected: <entity> moves <x>,<y> [for <unit> seconds] [looped]");
      }
      String entity = args[0];
      String[] positionString = args[2].split(",");
      float duration = args.length > 4 ? Float.parseFloat(args[4]) : 1f;
      float x = Float.parseFloat(positionString[0]);
      float y = positionString.length > 1 ? Float.parseFloat(positionString[1]) : 0f;
      if ((args.length == 7 && args[6].equals("looped"))
            || (args.length == 4 && args[3].equals("looped"))) {
         cutsceneBuilder.moveByYoyo(entity, x, y, duration);
      } else {
         cutsceneBuilder.moveBy(entity, x, y, duration);
      }

   }
}
