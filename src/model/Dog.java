package model;

public class Dog extends Pet {
    public Dog(String name, int weight, int age) {
        super(name, weight, age);
    }

    @Override
    public String getPetType() {
        return "Dog";
    }

    @Override
    public int getSizeSurcharge() {
        return surchargeFor(8, 100) + surchargeFor(20, 100);
    }

    public String getSizeCategory() {
        if (getWeight() <= 8) {
            return "Small";
        } else if (getWeight() <= 20) {
            return "Medium";
        }
        return "Large";
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " - " + getSizeCategory();
    }
}