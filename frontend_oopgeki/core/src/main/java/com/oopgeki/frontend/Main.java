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
    private List<GameObject> entities;

    @Override
    public void create() {
        batch = new SpriteBatch();

        entities = new ArrayList<>();

        entities.add(new Player("Reimu Hakurei"));
        entities.add(new Obstacle("Antek-Antek Asing", 100, 430, 30f,50f));
        entities.add(new Obstacle("Antek-Antek Asing", 180, 400, 30f,50f));
        entities.add(new Obstacle("Antek-Antek Asing", 270, 450, 30f,50f));
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

            // Check if entity is an Obstacle and collides with the Player
            if (GameObject instanceof Obstacle obstacle) {
                boolean collided = false;
                for (GameObject other : entities) {
                    if (other instanceof Player player) {
                        if (obstacle.getHitbox().overlaps(player.getHitbox())) {
                            System.out.println("Collision detected with obstacle!");
                            // Handle damage, game over, or score penalty here
                            collided = true;
                            break;
                        }

                    }
                }
                // If it hit the player, remove it immediately so it doesn't spam
                if (collided) {
                    iterator.remove();
                    continue; // Skip the out-of-bounds check for this removed item
                }
            }
        }

        // Render Everything
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        for (GameObject GameObject : entities) {
            GameObject.render(batch);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
