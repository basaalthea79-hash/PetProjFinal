package model;

public class Cat extends Pet {
    public Cat(String name, int weight, int age) {
        super(name, weight, age);
    }

    @Override
    public String getPetType() {
        return "Cat";
    }

    @Override
    public int getSizeSurcharge() {
        return surchargeFor(5, 80);
    }
}