package com.worldsimulator;

public abstract class Organism {

    protected String name;

    protected float health;

    protected float posX;
    protected float posY;

    protected int expectedLifeHours;
    protected float ageInHours;

    protected float size;

    public Organism(
            float posX,
            float posY,
            int expectedLifeHours,
            float size
    ) {
        this.posX = posX;
        this.posY = posY;

        this.expectedLifeHours = expectedLifeHours;
        this.size = size;

        this.health = 100f;
        this.ageInHours = 0f;
    }

    public abstract void update(float deltaTime);

    public boolean isAlive() {
        return health > 0;
    }

    public void takeDamage(float damage) {
        health -= damage;

        if (health < 0) {
            health = 0;
        }
    }

    public String getName() {
        return name;
    }

    public float getHealth() {
        return health;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getExpectedLifeHours() {
        return expectedLifeHours;
    }

    public float getAge() {
        return ageInHours;
    }

    public float getSize() {
        return size;
    }
}