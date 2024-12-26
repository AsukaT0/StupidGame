package io.github.asuka_neko.slimejump;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    private Texture stay_texture,prejump_texture,arrow_texture;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 size;
    private float gravity = -1600;
    private Vector2 dragStart; // Начало векторного прыжка
    private Vector2 dragEnd;   // Конец векторного прыжка
    private boolean isDragging = false; // Флаг нажатия
    private Rectangle bounds; // Границы игрока для столкновений
    private Sprite person,arrow;

    public Player(float x, float y) {
        stay_texture = new Texture("ty/person_stay.png"); // Текстура игрока
        prejump_texture = new Texture("ty/person_prejump.png"); // Текстура игрока
        arrow_texture = new Texture("ty/arrow.png"); // Текстура игрока

        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        dragStart = new Vector2();
        dragEnd = new Vector2();
        size = new Vector2(100,100);

        person = new Sprite(stay_texture);
        person.setPosition(position.x,position.y);
        person.setSize(size.x,size.y);
        person.setOriginCenter();

        arrow = new Sprite(arrow_texture);
        arrow.setAlpha(0);
        arrow.setSize(0,0);
        bounds = new Rectangle(x, y, size.x, size.y);
    }

    public void render(SpriteBatch batch) {
        person.setPosition(position.x,position.y);
        person.draw(batch);
        arrow.draw(batch);
    }

    public void update(float deltaTime, PlatformGenerator platformGenerator) {
        // Гравитация
        velocity.y += gravity * deltaTime;
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        // Обновление границ
        bounds.setPosition(position);

        // Проверка столкновений с платформами
        for (Platform platform : platformGenerator.getPlatforms()) {
            if (bounds.overlaps(platform.getBounds())) {
                // Проверяем, стоим ли мы на платформе сверху
                if (
                    (position.y > platform.getBounds().y && velocity.y <= 0) && (
                        position.x + size.x >= platform.getBounds().x
                            && position.x - size.x <= platform.getBounds().x + platform.getBounds().width)
                ) {
                    position.y = platform.getBounds().y + platform.getBounds().height;
                    velocity.y = 0; // Останавливаем падение
                    velocity.x = 0;
                }
            }
        }

        // Скроллинг платформ, если игрок поднимается выше половины экрана
        if (position.y > Gdx.graphics.getHeight() / 2) {
            float offset = position.y - Gdx.graphics.getHeight() / 2;
            Connector.moveY = offset;
            Connector.isDeadZero = true;
            Connector.Score+=offset;
            platformGenerator.scrollPlatforms(offset); // Скроллим платформы
            position.y = Gdx.graphics.getHeight() / 2; // Фиксируем игрока на половине экрана
        }

        // Ограничения по краям экрана
        if (position.x < 0) {
            velocity.x = 0;
            position.x = 0;
        }
        if (position.x > Gdx.graphics.getWidth() - size.x) {
            velocity.x = 0;
            position.x = Gdx.graphics.getWidth() - size.x;
        }
        if (position.y < 0 && !Connector.isDeadZero) {
            position.y = 0;
            velocity.x = 0;
            velocity.y = 0;
        }
        if (position.y < 0 && Connector.isDeadZero) {
            Connector.isDead = true;
            System.out.println(Connector.isDead);
        }
    }
    public Vector2 getPosition() {
        return position;
    }

    public void startDrag(float x, float y) {
        dragStart.set(x, y);
        isDragging = true;
        arrow.setAlpha(1f);
        // Устанавливаем текстуру для стадии перетаскивания, если персонаж еще не в прыжке
        if (velocity.x == 0 && velocity.y == 0) {
            person.setTexture(prejump_texture);
        }
    }

    public void endDrag(float x, float y) {
        dragEnd.set(x, y);
        isDragging = false;

        // Рассчитываем вектор прыжка
        Vector2 jumpVector = dragStart.cpy().sub(dragEnd).scl(2); // Увеличиваем силу прыжка
        jumpVector.y = Math.min(jumpVector.y, 1400);

        // Устанавливаем текстуру в состояние "стоя" только если персонаж стоит
        if (velocity.x == 0 && velocity.y == 0) {
            velocity.set(jumpVector);
            person.setTexture(stay_texture); // Сбрасываем текстуру после прыжка
        }
        arrow.setAlpha(0); // Убираем стрелку с экрана
        arrow.setSize(0,0);
    }

    public void Dragged(float x,float y) {
        Vector2 dragPos = new Vector2(x,Gdx.graphics.getHeight()-y);
        float dist = (float) Math.sqrt(Math.pow(dragPos.x - dragStart.x,2)+Math.pow(dragPos.y - dragStart.y,2));
        float angle = dragPos.cpy().sub(dragStart).angle()+90;
        arrow.setPosition(dragPos.x-dist/8*(float) Math.cos(Math.toRadians(angle)), dragPos.y-dist/8*(float) Math.sin(Math.toRadians(angle)));
        arrow.setRotation(angle);
        arrow.setSize(dist/4,dist);

        if (x > dragStart.x) {
            person.setScale(-1, 1); // Поворачиваем вправо
        } else if (x < dragStart.x) {
            person.setScale(1, 1); // Поворачиваем влево
        }
    }
}
