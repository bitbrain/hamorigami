package ldjam.hamorigami.model;

public class TreeStatus {

   private float soilWaterLevel = 0.3f;
   // -1 = super dry
   // 0  = perfect
   // 1  = over-watered
   private float treeWateredLevel = 0.0f;

   public void decreaseSoilWater(float water) {
      this.soilWaterLevel = Math.max(this.soilWaterLevel - water, 0);
   }

   public void increaseSoilWater(float water) {
      this.soilWaterLevel = Math.min(this.soilWaterLevel + water, 1f);
   }

   public float getTreeWateredLevel() {
      return treeWateredLevel;
   }

   public float getSoilWaterLevel() {
      return soilWaterLevel;
   }

   public void decreaseTreeWateredLevel(float water) {
      this.treeWateredLevel = Math.max(this.treeWateredLevel - water, -1);
   }

   public void increaseTreeWateredLevel(float water) {
      this.treeWateredLevel = Math.min(this.treeWateredLevel + water, 1f);
   }

   public void reset() {
      this.soilWaterLevel = 0.3f;
      this.treeWateredLevel = 0f;
   }
}
