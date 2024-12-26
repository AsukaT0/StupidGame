package io.github.asuka_neko.slimejump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ParalaxBG {
    private static Sprite bg1,bg2,bg3,bg4,bg5;

    public static void create(){
        bg1 = new Sprite(new Texture("ty/b5.png"));
        bg2 = new Sprite(new Texture("ty/b1.png"));
        bg3 = new Sprite(new Texture("ty/b2.png"));
        bg4 = new Sprite(new Texture("ty/b3.png"));
        bg5 = new Sprite(new Texture("ty/b4.png"));
        bg1.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth()*1.352f);
        bg2.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth()*1.352f);
        bg3.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth()*1.352f);
        bg4.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth()*1.352f);
        bg5.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth()*1.352f);
    }
    public static void render(SpriteBatch batch){
        bg1.setPosition(0,bg1.getBoundingRectangle().y-Connector.moveY/120);
        bg1.draw(batch);
        bg2.setPosition(0,bg2.getBoundingRectangle().y-Connector.moveY/90);
        bg2.draw(batch);
        bg3.setPosition(0,bg3.getBoundingRectangle().y-Connector.moveY/60);
        bg3.draw(batch);
        bg4.setPosition(0,bg4.getBoundingRectangle().y-Connector.moveY/30);
        bg4.draw(batch);
        bg5.setPosition(0,bg5.getBoundingRectangle().y-Connector.moveY/10);
        bg5.draw(batch);
    }
    public static void restart(){
        Connector.moveY = 0;
        bg1.setPosition(0,bg1.getBoundingRectangle().y-Connector.moveY/120);
        bg2.setPosition(0,bg2.getBoundingRectangle().y-Connector.moveY/90);
        bg3.setPosition(0,bg3.getBoundingRectangle().y-Connector.moveY/60);
        bg4.setPosition(0,bg4.getBoundingRectangle().y-Connector.moveY/30);
        bg5.setPosition(0,bg5.getBoundingRectangle().y-Connector.moveY/10);
    }
}
