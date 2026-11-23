package com.oopgeki.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player extends GameObject {
    private String name;
    private int hp;
    private long score;
    private Color color;
    protected TextureRegion sprite;
    private static Texture whitePixel;

    // Position coordinates
    private float x = 30;
    private float y = 50;
    private float width, height;

    // Speed variable (pixels per second)
    private float speed = 300f;

    public Player(String name){
        this.name = name;
        this.hp = 100;
        this.score = 100;

        if (whitePixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            whitePixel = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    public Player(String name, int hp, long score){
        this.name = name;
        this.hp = hp;
        this.score = score;

        if (whitePixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            whitePixel = new Texture(pixmap);
            pixmap.dispose();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(whitePixel, x, y, 20, 20);
    }

    public void move(float amount, float delta) {
        x += amount * speed * delta;
    }

    @Override
    public void update() {

    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }
}
