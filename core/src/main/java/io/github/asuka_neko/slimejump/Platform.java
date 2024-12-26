package io.github.asuka_neko.slimejump;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
    private Texture texture;
    private Rectangle bounds;

    public Platform(float x, float y, float width, float height) {
        texture = new Texture("ty/platform.png"); // Текстура платформы
        bounds = new Rectangle(x, y, width, height);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }
    public void setY(float y) {
        this.bounds.y = y;
        bounds.setY(y); // Обновляем границы после изменения позиции
    }
}
