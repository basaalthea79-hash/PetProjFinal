package model;

public class Rabbit extends Pet {
    public Rabbit(String name, int weight, int age) {
        super(name, weight, age);
    }

    @Override
    public String getPetType() {
        return "Rabbit";
    }

    @Override
    public int getSizeSurcharge() {
        return surchargeFor(3, 50);
    }
}