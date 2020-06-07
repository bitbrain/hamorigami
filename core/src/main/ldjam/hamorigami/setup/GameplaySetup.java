package ldjam.hamorigami.setup;

import java.util.List;

public class GameplaySetup {

   public static float SECONDS_PER_DAY = 60.0f;

   private int currentDay;
   private List<DaySetup> daySetups;
   private float currentSeconds = 0f;

   public GameplaySetup(List<DaySetup> daySetups) {
      this.daySetups = daySetups;
      this.currentDay = 1;
   }

   public boolean isStartOfDay() {
      return getDayProgress() == 0f;
   }

   public boolean isEndOfDay() {
      return getDayProgress() == 1f;
   }

   public int getCurrentDay() {
      return currentDay;
   }

   public int getNumberOfDays() {
      return daySetups.size();
   }

   public float getDayProgress() {
      return currentSeconds / SECONDS_PER_DAY;
   }

   public DaySetup triggerNextDay() {
      if (!isLastDay()) {
         currentSeconds = 0f;
         currentDay++;
      } else {
         currentSeconds = SECONDS_PER_DAY;
      }
      return getCurrentDaySetup();
   }

   public DaySetup resetDay() {
      this.currentSeconds = 0f;
      return getCurrentDaySetup();
   }

   public void update(float delta) {
      this.currentSeconds += delta;
      this.currentSeconds = Math.min(SECONDS_PER_DAY, this.currentSeconds);
   }

   public boolean isLastDay() {
      return currentDay == daySetups.size();
   }

   public DaySetup getCurrentDaySetup() {
      if (daySetups.isEmpty()) {
         return null;
      }
      return daySetups.get(currentDay - 1);
   }

   public void resetAll() {
      resetDay();
      this.currentDay = 1;
   }
}
