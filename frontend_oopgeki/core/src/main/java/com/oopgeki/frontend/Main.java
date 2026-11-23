package com.oopgeki.frontend;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.oopgeki.frontend.commands.Command;
import com.oopgeki.frontend.commands.InputHandler;
import com.oopgeki.frontend.systems.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private List<GameObject> entities;
    private InputHandler inputHandler;
    private Player player;

    private Random random = new Random();

    private float spawnTimer = 0f;
    private float spawnInterval = 0.5f; // Spawn every 1.5 seconds

    private float obstacleWidth = 30f;
    private float obstacleHeight = 100f;

    private void spawnObstacle() {
        float screenWidth = Gdx.graphics.getWidth();

        int lanes = 8;

        float laneWidth = screenWidth / lanes;

        int randomLane = random.nextInt(lanes);
        float randomX = randomLane * laneWidth + (laneWidth - obstacleWidth) / 2f;
        entities.add(new Obstacle("Obs", randomX, Gdx.graphics.getHeight(), obstacleWidth, obstacleHeight));
        entities.add(new Notes(300, randomX, 850, 50f, 35f, 250f));
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        inputHandler = new InputHandler();

        entities = new ArrayList<>();

        player = new Player("Reimu Hakurei");
        entities.add(player);

        SoundManager.getInstance().init();
        SoundManager.getInstance().playBGM();

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

        // Handle obstacle spawning
        spawnTimer += delta;

        if (spawnTimer >= spawnInterval) {
            spawnObstacle();
            spawnTimer = 0f;
        }

        // Update entities
        for (GameObject entity : entities) {
            entity.update();
        }

        for (GameObject entity : entities) {
            if (entity instanceof Obstacle obstacle) {
                for (GameObject other : entities) {
                    if (other instanceof Player player && obstacle.getHitbox().overlaps(player.getHitbox())) {
                        System.out.println("Collision detected with obstacle!");
                        SoundManager.getInstance().playItemPickup();
                        break;
                    }
                }
            }
        }

        for (GameObject entity : entities) {
            if (entity instanceof Notes notes) {
                if (Gdx.input.isKeyPressed(Input.Keys.Q) && notes.hitboxInRange()){
                    System.out.println("MASUK");
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
