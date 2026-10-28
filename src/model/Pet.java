package model;

public abstract class Pet {
    public static final int MAX_WEIGHT = 50;
    public static final int MAX_AGE = 20;
    public static final String[] PET_TYPES = {"Dog", "Cat", "Rabbit"};

    private String name;
    private int weight;
    private int age;

    public Pet(String name, int weight, int age) {
        if (name == null || name.trim().isEmpty()) {
            this.name = "Unnamed";
        } else {
            this.name = name.trim();
        }
        this.weight = clamp(weight, 1, MAX_WEIGHT);
        this.age = clamp(age, 0, MAX_AGE);
    }
   
    public String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }
    public int getAge() {
        return age;
    }
    
    private static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public abstract String getPetType();
    public abstract int getSizeSurcharge();
    protected int surchargeFor(int limitKg, int fee) {
        if (weight > limitKg) {
            return fee;
        }
        return 0;
    }

    public String getDescription() {return name + " (" + getPetType() + ", " + weight + " kg" + ", " + age + " yrs)";}
    public static Pet create(String type, String name, int weight, int age) {
        if ("Cat".equals(type)) {
            return new Cat(name, weight, age);
        } else if ("Rabbit".equals(type)) {
            return new Rabbit(name, weight, age);
        }
        return new Dog(name, weight, age);
    }
}