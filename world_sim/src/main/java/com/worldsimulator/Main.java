package com.worldsimulator;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    private static final ArrayList<Organism> organisms = new ArrayList<>();

    public static final int windowWidth = 1920;
    public static final int windowHeight = 1080;

    public static final int FPS = 5;
    public int treeCount = 1;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config =
                new Lwjgl3ApplicationConfiguration();

        config.setTitle("World Simulator");
        config.setWindowedMode(windowWidth, windowHeight);
        config.setResizable(true);
        config.setForegroundFPS(FPS);

        new Lwjgl3Application(new Main(), config);
    }

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime() * FPS;

        updateWorld(deltaTime);
        drawWorld();
    }

    private void updateWorld(float deltaTime) {
        organisms.stream()
                .filter(organism -> organism instanceof Tree)
                .forEach(organism -> organism.update(deltaTime));

        organisms.removeIf(organism -> !organism.isAlive());

        if (Math.random() > 0.85) {
            createTree();
        }
    }

    private void drawWorld() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.GREEN);

        for (Organism organism : organisms) {
            shapeRenderer.circle(
                    organism.getPosX(),
                    organism.getPosY(),
                    organism.getSize()
            );
        }

        shapeRenderer.end();

        drawTreePanel();
    }

    private void drawTreePanel() {
        float panelWidth = 350;
        float panelHeight = 280;
        float margin = 20;

        float panelX =
                Gdx.graphics.getWidth() - panelWidth - margin;

        float panelY =
                Gdx.graphics.getHeight() - panelHeight - margin;

        Gdx.gl.glEnable(GL20.GL_BLEND);

        Gdx.gl.glBlendFunc(
                GL20.GL_SRC_ALPHA,
                GL20.GL_ONE_MINUS_SRC_ALPHA
        );

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 0.65f);
        shapeRenderer.rect(
                panelX,
                panelY,
                panelWidth,
                panelHeight
        );

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        font.setColor(Color.WHITE);

        font.draw(
                batch,
                "TREE INFORMATION",
                panelX + 15,
                panelY + panelHeight - 20
        );

        float textY = panelY + panelHeight - 50;
        int shownTreeCount = 0;

        for (Organism organism : organisms) {
            if (organism instanceof Tree tree) {
                String text = String.format(
                        "%s | Health: %.1f | Age: %.1f",
                        tree.getName(),
                        tree.getHealth(),
                        tree.getAge()
                );

                font.draw(
                        batch,
                        text,
                        panelX + 15,
                        textY
                );

                textY -= 22;
                shownTreeCount++;

                if (shownTreeCount >= 10) {
                    break;
                }
            }
        }

        batch.end();
    }

    public void createTree() {
        int expectedTreeLife = 100;
        int size = (int) ((Math.random() * 3) + 4);

        float posX =
                (float) (Math.random() * windowWidth);

        float posY =
                (float) (Math.random() * windowHeight);

        boolean collided = false;

        for (Organism organism : organisms) {
            if (isColliding(organism, posX, posY, size)) {
                collided = true;
                break;
            }
        }

        if (collided) {
            createTree();
        } else {
            Organism tree = new Tree(
                    posX,
                    posY,
                    expectedTreeLife,
                    size,
                    treeCount,
                    organisms
            );

            treeCount++;
            organisms.add(tree);
        }
    }

    private boolean isColliding(
            Organism organism,
            float posX,
            float posY,
            float size
    ) {
        float dx = posX - organism.getPosX();
        float dy = posY - organism.getPosY();

        float minimumDistance =
                size + organism.getSize();

        return dx * dx + dy * dy
                < minimumDistance * minimumDistance;
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }
}