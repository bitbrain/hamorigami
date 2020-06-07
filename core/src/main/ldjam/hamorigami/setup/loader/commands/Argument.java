package ldjam.hamorigami.setup.loader.commands;

import java.util.Objects;

public class Argument {

   private final String name;
   private final boolean required;
   private final InputMapper inputMapper;

   public Argument(String name, InputMapper inputMapper) {
      this(name, inputMapper, true);
   }

   public Argument(String name, InputMapper inputMapper, boolean required) {
      this.name = name;
      this.inputMapper = inputMapper;
      this.required = required;
   }

   public String getName() {
      return name;
   }

   public boolean isRequired() {
      return required;
   }

   public InputMapper getInputMapper() {
      return inputMapper;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Argument argument = (Argument) o;
      return Objects.equals(name, argument.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name);
   }
}
