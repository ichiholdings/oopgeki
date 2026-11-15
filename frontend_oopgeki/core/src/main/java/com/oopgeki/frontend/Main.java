package com.oopgeki.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private List<GameObject> entities;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");

        entities = new ArrayList<>();

        entities.add(new Player("Reimu Hakurei"));
        entities.add(new Obstacle("Antek-Antek Asing"));
    }

    @Override
    public void render() {
        // Update all entities (handles player movement and obstacle falling)
        for (GameObject GameObject : entities) {
            GameObject.update();
        }

        // Remove off-screen obstacles safely using an Iterator
        Iterator<GameObject> iterator = entities.iterator();
        while (iterator.hasNext()) {
            GameObject GameObject = iterator.next();
            if (GameObject.isOutOfBounds()) {
                iterator.remove();
            }
        }

        // Render Everything
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);

        for (GameObject GameObject : entities) {
            GameObject.render(batch);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
