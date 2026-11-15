package com.oopgeki.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Obstacle extends GameObject {
    private String name;
    private int hp;
    private float speed;
    private static Texture whitePixel;
    private float x, y;
    private float width, height;

    public Obstacle(String name) {
        this.name = name;
        this.x = 160;
        this.y = 430;
        this.width = 30f;
        this.height = 50f;
        this.speed = 120f;

        if (whitePixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            whitePixel = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    public Obstacle(String name, float x, float y, float width, float height, float speed) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;

        if (whitePixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            whitePixel = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    public void update() {
        // Move downwards over time (falling)
        y -= speed * Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch batch) {
        // Render obstacles in red to distinguish them from the player
        batch.setColor(Color.RED);
        batch.draw(whitePixel, x, y, width, height);
        batch.setColor(Color.WHITE); // Reset color
    }

    public boolean isOutOfBounds() {
        // Check if the obstacle has fallen completely off the bottom of the screen
        return y + height < 0;
    }
}
