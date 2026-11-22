import com.oopgeki.frontend.GameObject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Notes extends GameObject {
    private int score;
    private float x;
    private float y;
    private float width;
    private float height;
    private float speed;

    public Notes(int score, float x, float y, float width, float height, float speed){
        this.score = score;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void update(){
        y -= speed * Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch batch){
        batch.setColor(Color.BLUE);
        batch.draw(whitePixel, x, y, width, height);
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }

    public int getScore(){ return score; }
    public float getX(){ return x; }
    public float getY(){ return y; }
    public float getWidth(){ return width; }
    public float getHeight(){ return height; }
    public float getSpeed(){ return speed; }
    public void setScore(int score) { this.score = score; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setWidth(float width) { this.width = width; }
    public void setHeight(float height) { this.height = height; }
    public void setSpeed(float speed) { this.speed = speed; }
}
