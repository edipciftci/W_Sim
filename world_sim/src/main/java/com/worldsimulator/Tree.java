package com.worldsimulator;

import java.util.ArrayList;

public class Tree extends Organism {

private final ArrayList<Organism> organisms;

	public Tree(float posX, float posY, int expectedLifeHours, float size, int treeCount, ArrayList<Organism> organisms) {
		super(posX, posY, expectedLifeHours, size);

		this.name = "Tree " + treeCount;
		this.organisms = organisms;

		System.out.printf(
			"%s is born at the coordinates:%n"
			+ "\tX:\t%.2f%n"
			+ "\tY:\t%.2f%n",
			this.name,
			posX,
			posY
		);
	}

	@Override
	public void update(float deltaTime) {
		ageInHours += deltaTime;

		healthUpdate(deltaTime);
	}

	private float clamp01(float value) {return Math.max(0f,Math.min(1f, value));}

	private float exponentialCurve(float value,float steepness) {
		value = clamp01(value);

		return (float) ((Math.exp(steepness * value) - 1) / (Math.exp(steepness) - 1));
	}

	private float regeneration() {
		float lifeRatio =
		ageInHours / expectedLifeHours;

		if (lifeRatio >= 1f) {
		return 0f;
		}

		float progress =
		(lifeRatio - 0.75f) / 0.25f;

		return 1f - exponentialCurve(
		progress,
		4f
		);
	}

	private float agingHealthLoss() {
	float lifeRatio =
	ageInHours / expectedLifeHours;

	float progress =
	(lifeRatio - 0.60f) / 0.40f;

	return exponentialCurve(
	progress,
	4f
	);
	}

	private void healthUpdate(float deltaTime) {
	float regen =
	regeneration()
	* (float) Math.random()
	* 0.5f;

	float aging =
	agingHealthLoss()
	* (float) Math.random()
	* 20f;

	health += (regen - aging) * deltaTime;

	if (health > 100f) {
	health = 100f;
	}

	if (health < 0f) {
	health = 0f;
	}
	}
}