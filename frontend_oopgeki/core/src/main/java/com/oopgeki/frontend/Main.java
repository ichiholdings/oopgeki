package com.oopgeki.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.oopgeki.frontend.commands.Command;
import com.oopgeki.frontend.commands.InputHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private List<GameObject> entities;
    private InputHandler inputHandler;
    private Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();

        inputHandler = new InputHandler();

        entities = new ArrayList<>();

        player = new Player("Reimu Hakurei");
        entities.add(player);
        entities.add(new Obstacle("Antek-Antek Asing", 100, 430, 30f,50f));
        entities.add(new Obstacle("Antek-Antek Asing", 180, 400, 30f,50f));
        entities.add(new Obstacle("Antek-Antek Asing", 270, 450, 30f,50f));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Handle keyboard input
        List<Command> commands = inputHandler.handleInput(delta);

        // Execute commands on the player
        for (Command command : commands) {
            command.execute(player, delta);
        }

        // Update entities
        for (GameObject entity : entities) {
            entity.update();
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
                for (GameObject other : entities) {
                    if (other instanceof Player player) {
                        if (obstacle.getHitbox().overlaps(player.getHitbox())) {
                            System.out.println("Collision detected with obstacle!");
                            break;
                        }

                    }
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
