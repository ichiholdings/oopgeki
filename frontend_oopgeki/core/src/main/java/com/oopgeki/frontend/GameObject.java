package com.oopgeki.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject {
    protected float x, y;
    protected float width, height;

    public abstract void update();
    public abstract void render(SpriteBatch batch);

    public boolean isOutOfBounds() {
        if (x < 0 || y < 0 || x + width > Gdx.graphics.getWidth() || y + height > Gdx.graphics.getHeight()) {
            return true;
        } else {
            return false;
        }
    }
}
