package ldjam.hamorigami.anchor;

public enum AnchorMask {

   CENTER(0b00000001),
   WEST(0b00000010),
   EAST(0b00000100),
   NORTH(0b00001000),
   SOUTH(0b00010000);

   private final int value;

   AnchorMask(int value) {
      this.value = value;
   }

   public int getByte() {
      return value;
   }
}
