package io.github.asuka_neko.slimejump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class RestartScreen {
    private static Sprite background, restart, saveButton;
    private static float alphaBg = 0;
    private static Rectangle boundsRestart, boundsSave;
    private static String playerName = ""; // Для ввода имени игрока
    private static boolean showInputField = false; // Флаг для отображения ввода имени

    public static void create() {
        background = new Sprite(new Texture("ty/black.png"));
        restart = new Sprite(new Texture("ty/restart.png"));
        saveButton = new Sprite(new Texture("ty/save.png")); // Кнопка "Сохранить"

        background.setAlpha(0);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        restart.setSize(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4, (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4) / 4);
        saveButton.setSize(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4, (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4) / 4);

        background.setPosition(-Gdx.graphics.getWidth(), 0);
        restart.setPosition(-Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6);
        saveButton.setPosition(-Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 3);

        boundsRestart = new Rectangle(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4, (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4) / 4, -Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 3);
        boundsSave = new Rectangle(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4, (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4) / 4, -Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 6);

        // Инициализируем базу данных
        new DatabaseTest();
    }

    public static void render(SpriteBatch batch) {
        if (Connector.isDead) {
            background.setPosition(0, 0);
            restart.setPosition(Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 6);
            saveButton.setPosition(Gdx.graphics.getWidth() / 8, Gdx.graphics.getHeight() / 3);

            alphaBg += Gdx.graphics.getDeltaTime() * 10;
            if (alphaBg > 1f) {
                alphaBg = 1f;
            }
            background.draw(batch);
            background.setAlpha(alphaBg);
            restart.draw(batch);
            saveButton.draw(batch);

            Main.font3.draw(batch, "You died", Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() * 5f / 6f);
            Main.font2.draw(batch, "Your score: " + Connector.Score / 100, Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() * 5f / 6f - 100);

            if (showInputField) {
                Main.font2.draw(batch, "Enter your name: " + playerName, Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 3);
                handleInput();
            }

            // Отображение списка лидеров
            Main.font2.draw(batch, "Leaderboard:", Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() * 5f / 6f - 200);
            ArrayList<String> leaderboard = DatabaseTest.getLeaderboard();
            if (leaderboard.isEmpty()) {
                Main.font2.draw(batch, "No leaders yet.", Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() * 5f / 6f - 200 - 30);
            } else {
                for (int i = 0; i < leaderboard.size(); i++) {
                    Main.font2.draw(batch, leaderboard.get(i), Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() * 5f / 6f - 200 - (i + 1) * 50);
                }
            }

            boundsRestart.x = Gdx.graphics.getWidth() / 8;
            boundsSave.x = Gdx.graphics.getWidth() / 8;
        }
    }

    public static void onTouchUp(int screenX, int screenY) {
        if (!Connector.isDead) return;
        if (restart.getBoundingRectangle().contains(screenX, screenY)) {
            Connector.isDead = false;
            Connector.isDeadZero = false;
            alphaBg = 0f;
            background.setAlpha(0);
            background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            restart.setSize(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4, (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4) / 4);
            background.setPosition(-Gdx.graphics.getWidth(), 0);
            restart.setPosition(-Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 3);
            boundsRestart = new Rectangle(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4, (Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4) / 4, -Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 3);
            ParalaxBG.restart();
            Connector.Score = 0;
        } else if (saveButton.getBoundingRectangle().contains(screenX, screenY)) {
            showInputField = true;
        }
    }

    public static void handleInput() {
        if (showInputField) {
            // Запускаем диалог системной клавиатуры
            Gdx.input.getTextInput(new TextInputListener() {
                @Override
                public void input(String text) {
                    playerName = text; // Сохраняем введённое имя
                    DatabaseTest.saveScore(playerName, Connector.Score / 100); // Сохраняем результат в базу данных
                    showInputField = false; // Закрываем поле ввода
                }

                @Override
                public void canceled() {
                    showInputField = false; // Закрываем поле ввода, если пользователь отменил
                }
            },"","","");

            // Отключаем поле, чтобы не запускать клавиатуру повторно
            showInputField = false;
        }
    }
}
