package io.github.asuka_neko.slimejump;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlatformGenerator {
    private List<Platform> platforms; // Список текущих платформ
    private float platformWidth = 128; // Ширина платформы
    private float platformHeight = 32; // Высота платформы
    private float screenWidth;
    private float screenHeight;
    private float platformGap = 150; // Расстояние между платформами

    public PlatformGenerator(float screenWidth, float screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        platforms = new ArrayList<>();

        // Генерация начальных платформ
        float y = platformGap;
        for (int i = 0; i < 20; i++) {
            float x = MathUtils.random(0, screenWidth - platformWidth);
            platforms.add(new Platform(x, y, platformWidth, platformHeight));
            y += platformGap;
        }
    }

    // Метод для генерации новых платформ
    public void generate(float playerY) {
        Iterator<Platform> iterator = platforms.iterator();

        // Удаление платформ ниже экрана
        while (iterator.hasNext()) {
            Platform platform = iterator.next();
            if (platform.getBounds().y + platformHeight < playerY - screenHeight / 2) {
                iterator.remove();
            }
        }

        // Добавление новых платформ выше игрока
        while (platforms.size() < 20) {
            float lastY = platforms.get(platforms.size() - 1).getBounds().y;
            float x = MathUtils.random(0, screenWidth - platformWidth);
            platforms.add(new Platform(x, lastY + platformGap, platformWidth, platformHeight));
        }
    }

    // Метод для смещения платформ (скроллинг)
    public void scrollPlatforms(float offset) {
        for (Platform platform : platforms) {
            platform.setY(platform.getBounds().y - offset); // Смещаем платформы вниз
        }
    }

    public void render(SpriteBatch batch) {
        for (Platform platform : platforms) {
            platform.render(batch);
        }
    }

    public void dispose() {
        for (Platform platform : platforms) {
            platform.dispose();
        }
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }
}
