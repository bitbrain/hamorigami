package ldjam.hamorigami.setup.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import ldjam.hamorigami.context.HamorigamiContext;
import ldjam.hamorigami.cutscene.CutsceneBuilder;
import ldjam.hamorigami.cutscene.emotes.Emote;
import ldjam.hamorigami.model.SpiritType;
import ldjam.hamorigami.setup.DaySetupBuilder;
import ldjam.hamorigami.setup.GameplaySetup;
import ldjam.hamorigami.setup.GameplaySetupBuilder;
import ldjam.hamorigami.setup.loader.commands.Argument;
import ldjam.hamorigami.setup.loader.commands.CommandBehavior;
import ldjam.hamorigami.setup.loader.commands.CommandParser;
import ldjam.hamorigami.setup.loader.commands.CommandParserBuilder;
import ldjam.hamorigami.setup.loader.commands.inputs.NumberInput;
import ldjam.hamorigami.setup.loader.commands.inputs.PositionInput;
import ldjam.hamorigami.setup.loader.commands.inputs.SecondInput;
import ldjam.hamorigami.setup.loader.commands.inputs.StringInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

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
            if (scanMode == ScanMode.GAMEPLAY && (previousScanmode == ScanMode.EVENING_CUTSCENE || previousScanmode == ScanMode.GAMEPLAY) && !morningCutscene) {
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
         if (scanMode == ScanMode.GAMEPLAY && line.toLowerCase().startsWith("at")) {
            morningCutscene = true;
         }
         if (scanMode == ScanMode.GAMEPLAY) {
            CommandParser parser = buildIngameCommandParser(daySetupBuilder);
            parser.parse(line.split(" "));
         }
         if (scanMode == ScanMode.EVENING_CUTSCENE || scanMode == ScanMode.MORNING_CUTSCENE) {
            CommandParser parser = buildCutsceneCommandParser(cutsceneBuilder);
            parser.parse(line.split(" "));
         }
      }
      builder.addDay(daySetupBuilder.build());
      return builder.build();
   }

   private CommandParser buildIngameCommandParser(final DaySetupBuilder daySetupBuilder) {
      return new CommandParserBuilder()
            // at <time> spawn <spirit> times <amount>
            .withCommand("at", new CommandBehavior() {
                     @Override
                     public void apply(Map<String, Object> args) {
                        float seconds = convertToSeconds((String) args.get("at"));
                        String spirit = (String) args.get("spawn");
                        int amount = args.containsKey("times") ? (Integer) args.get("times") : 1;
                        SpiritType[] spirits = convertToSpiritTypes(spirit, amount);
                        daySetupBuilder.addSpawns(seconds, spirits);
                     }
                  }, new StringInput(),
                  new Argument("spawn", new StringInput()),
                  new Argument("times", new NumberInput()))
            // every <time> spawn <spirit> times <amount>
            .withCommand("every", new CommandBehavior() {
                     @Override
                     public void apply(Map<String, Object> args) {
                        float seconds = convertToSeconds((String) args.get("every"));
                        String spirit = (String) args.get("spawn");
                        int amount = args.containsKey("times") ? (Integer) args.get("times") : 1;
                        SpiritType[] spirits = convertToSpiritTypes(spirit, amount);
                        for (float currentSeconds = seconds; currentSeconds <= GameplaySetup.SECONDS_PER_DAY; currentSeconds += seconds) {
                           daySetupBuilder.addSpawns(currentSeconds, spirits);
                        }
                     }
                  }, new StringInput(),
                  new Argument("spawn", new StringInput()),
                  new Argument("times", new NumberInput()))
            .build();
   }

   private CommandParser buildCutsceneCommandParser(final CutsceneBuilder cutsceneBuilder) {
      return new CommandParserBuilder()
            // wait <seconds>
            .withCommand("wait", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  Float time = (Float) args.get("wait");
                  cutsceneBuilder.wait(time);
               }
            }, new SecondInput())
            // reset <id>
            .withCommand("reset", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  String id = (String) args.get("reset");
                  cutsceneBuilder.clearTweens(id);
               }
            }, new StringInput())
            // spawn <id> at <position>
            .withCommand("spawn", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  String id = (String) args.get("spawn");
                  String anchor = (String) args.get("at");
                  SpiritType type = SpiritType.resolveByName(id);
                  if (type == SpiritType.SPIRIT_EARTH) {
                     cutsceneBuilder.spawn(id, type, anchor, true);
                  } else {
                     cutsceneBuilder.spawn(id, type, anchor);
                  }
               }
            }, new StringInput(), new Argument("at", new StringInput()))
            // fadeIn <id> for <seconds>
            .withCommand("fadein", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  String id = (String) args.get("fadein");
                  float duration = (Float) args.get("for");
                  cutsceneBuilder.fadeIn(id, duration);
               }
            }, new StringInput(), new Argument("for", new SecondInput()))
            // fadeOut <id> for <seconds>
            .withCommand("fadeout", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  String id = (String) args.get("fadeout");
                  float duration = (Float) args.get("for");
                  cutsceneBuilder.fadeOut(id, duration);
               }
            }, new StringInput(), new Argument("for", new SecondInput()))
            // say <id> on <id>
            .withCommand("say", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  String sentenceId = (String) args.get("say");
                  String id = (String) args.get("on");
                  cutsceneBuilder.say(sentenceId, id);
               }
            }, new StringInput(), new Argument("on", new StringInput()))
            // emote <id> on <id>
            .withCommand("emote", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  String emoteId = (String) args.get("emote");
                  String id = (String) args.get("on");
                  Emote emote = Emote.valueOf(emoteId.toUpperCase());
                  cutsceneBuilder.emote(emote, id);
               }
            }, new StringInput(), new Argument("on", new StringInput()))
            // start <attribute> on <id>
            .withCommand("start", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  String attribute = (String) args.get("start");
                  String id = (String) args.get("on");
                  cutsceneBuilder.setAttribute(id, attribute);
               }
            }, new StringInput(), new Argument("on", new StringInput()))
            // stop <attribute> on <id>
            .withCommand("stop", new CommandBehavior() {
               @Override
               public void apply(Map<String, Object> args) {
                  String attribute = (String) args.get("stop");
                  String id = (String) args.get("on");
                  cutsceneBuilder.removeAttribute(id, attribute);
               }
            }, new StringInput(), new Argument("on", new StringInput()))
            // move <id> by <position> for <time> second [looped]
            .withCommand("move", new CommandBehavior() {
                     @Override
                     public void apply(Map<String, Object> args) {
                        String id = (String) args.get("move");
                        Vector2 direction = (Vector2) args.get("by");
                        boolean looped = args.containsKey("looped");
                        float duration = args.containsKey("for") ? (float) args.get("for") : 1f;
                        cutsceneBuilder.moveBy(id, direction.x, direction.y, duration, looped);
                     }
                  }, new StringInput(), new Argument("by", new PositionInput()),
                  new Argument("for", new SecondInput()),
                  new Argument("looped", new StringInput()))
            // shake for <seconds> with intensity <intensity>
            .withCommand("shake", new CommandBehavior() {
                     @Override
                     public void apply(Map<String, Object> args) {
                        float seconds = args.containsKey("for") ? (float) args.get("for") : 1f;
                        float intensity = args.containsKey("intensity") ? (float) args.get("intensity") : 1f;
                        cutsceneBuilder.shakeScreen(intensity, seconds);
                     }
                  }, new Argument("for", new NumberInput()),
                  new Argument("intensity", new NumberInput()))
            .build();
   }

   private float convertToSeconds(String secondsOrPercentage) {
      if (secondsOrPercentage.contains("%")) {
         float percentage = Float.parseFloat(secondsOrPercentage.replace("%", ""));
         return GameplaySetup.SECONDS_PER_DAY / 100f * percentage;
      }
      return Float.parseFloat(secondsOrPercentage);
   }

   private SpiritType[] convertToSpiritTypes(String name, int amount) {
      SpiritType[] types = new SpiritType[amount];
      for (int i = 0; i < amount; ++i) {
         types[i] = SpiritType.resolveByName(name);
      }
      return types;
   }
}
