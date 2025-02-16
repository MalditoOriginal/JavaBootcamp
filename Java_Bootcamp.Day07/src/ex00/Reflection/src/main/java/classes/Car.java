package classes;

import java.util.StringJoiner;

public class Car {
   private String model;
   private int year;
   private double price;
   private Long serialNumber;

   public Car() {
       this.model = "Default model";
       this.year = 2000;
       this.price = 0.0;
       this.serialNumber = 0L;
   }

   public Car(String model, int year, double price) {
       this.model = model;
       this.year = year;
       this.price = price;
       this.serialNumber = 0L;
   }

   public double increasePrice(double amount) {
       this.price += amount;
       return price;
   }

   @Override
   public String toString() {
       return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
               .add("model='" + model + "'")
               .add("year=" + year)
               .add("price=" + price)
               .add("serialNumber=" + serialNumber)
               .toString();
   }
}
