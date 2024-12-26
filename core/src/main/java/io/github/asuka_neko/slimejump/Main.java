package io.github.asuka_neko.slimejump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements InputProcessor,Screen {
    private SpriteBatch batch;
    Sprite ground;
    private Player player;
    private PlatformGenerator platformGenerator;
    private float screenWidth;
    private float screenHeight;
    public static BitmapFont font,font2,font3;

    @Override
    public void create() {
        batch = new SpriteBatch();
        ground = new Sprite(new Texture("ty/black.png"));
        ground.setSize(Gdx.graphics.getWidth(), (float) Gdx.graphics.getWidth() /8);
//        image = new Texture("libgdx.png");
        Gdx.input.setInputProcessor(this);
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        player = new Player(screenWidth / 2, 0);
        platformGenerator = new PlatformGenerator(screenWidth, screenHeight);
        RestartScreen.create();
        ParalaxBG.create();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("joystix monospace.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32; // Установите нужный размер шрифта
        font = generator.generateFont(parameter); // Создание шрифта
        parameter.size = 48; // Установите нужный размер шрифта
        font2 = generator.generateFont(parameter);
        parameter.size = 64; // Установите нужный размер шрифта
        font3 = generator.generateFont(parameter);
        generator.dispose(); //
    }

    @Override
    public void render() {
        ScreenUtils.clear(86/255f, 116/255f, 186/255f, 255/255f);
        player.update(Gdx.graphics.getDeltaTime(),platformGenerator);

        // Генерация новых платформ
        platformGenerator.generate(player.getPosition().y);
        // Отрисовка объектов
        batch.begin();
        ParalaxBG.render(batch);
        player.render(batch);
        platformGenerator.render(batch);
        font.draw(batch, "SCORE:"+((int)Connector.Score/100), 50, 50); // Вывод текста на экран
        RestartScreen.render(batch);
        batch.end();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(86/255f, 116/255f, 186/255f, 255/255f);
        platformGenerator.generate(player.getPosition().y);
        player.update(delta,platformGenerator);
        batch.begin();
        ParalaxBG.render(batch);
        player.render(batch);
        platformGenerator.render(batch);
        font.draw(batch, ""+((int)Connector.Score/100), 0, 0); // Вывод текста на экран
        batch.end();

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
//        image.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Начало ввода (нажатие мыши или пальца)
        player.startDrag(screenX, screenHeight - screenY); // Инвертируем Y для координат экрана
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Конец ввода (отпускание мыши или пальца)
        RestartScreen.onTouchUp(screenX, (int) (screenHeight - screenY));
        player.endDrag(screenX, screenHeight - screenY); // Инвертируем Y для координат экрана
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        player.Dragged(screenX,screenY);
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
