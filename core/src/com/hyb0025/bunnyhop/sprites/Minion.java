package com.hyb0025.bunnyhop.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class Minion
{
    private Vector3 position;
    private Texture texture;
    private Animation animation;
    private float width;
    private float height;
    private Rectangle bounds;
    private Sound deathSound;
    private Sound spawnSound;

    public Minion(float posX, float posY)
    {
        position = new Vector3(posX, posY, 0);
        deathSound = Gdx.audio.newSound(Gdx.files.internal("woya.wav"));

        String texture_filename;
        String spawnSound_filename;
        int rand = (int) Math.floor(Math.random() * 3);

        switch(rand)
        {
            case 0:
                texture_filename = "bat.png";
                break;
            case 1:
                texture_filename = "bat.png";
                break;
            case 2:
                texture_filename = "bat.png";
                break;
            default:
                texture_filename = "bat.png";
                break;
        }

        if (Math.floor(Math.random() * 2) == 0)
        {
            spawnSound = Gdx.audio.newSound(Gdx.files.internal("laugh3.ogg"));
            spawnSound.play();
        }

        texture = new Texture(texture_filename);
        animation = new Animation(new TextureRegion(texture), 4, 0.5f);
        width = 55;
        height = 55;
        bounds = new Rectangle(posX, posY, width, height);
    }

    public void update(float dt, Bunny bird)
    {
        animation.update(dt);
        if (bird.getPosition().y > getPosition().y)
        {
            position.y += .5;
        }
        else
        {
            position.y -= .5;
        }
        bounds.setPosition(position.x, position.y);
    }

    public boolean collides(Rectangle player)
    {
        return player.overlaps(bounds);
    }

    public void die()
    {
        deathSound.play();
        dispose();

    }

    public void dispose()
    {
        texture.dispose();
    }

    public TextureRegion getTexture() { return animation.getFrame(); }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public Vector3 getPosition() { return position; }
    public Rectangle getBounds() { return bounds; }
}
