package com.hyb0025.bunnyhop.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class Bunny
{
    private Vector3 position;
    private Vector3 velocity;
    private static final int GRAVITY = -8;
    private int movement = 80;
    private Rectangle bounds;
    private Animation bunnyAnimation;
    private Texture texture;
    private Sound flap;

    private Texture puffTexture;
    private Animation puffAnimation;

    private float textureWidth;
    private float textureHeight;
    private int boundsOffset;

    public Bunny(int x, int y)
    {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("bunny.png");

        textureWidth = 60;
        textureHeight = 60;

        boundsOffset = 10;

        bunnyAnimation = new Animation(new TextureRegion(texture), 4, 0.5f);
        bounds = new Rectangle(x + boundsOffset, y + boundsOffset, textureWidth - (2 * boundsOffset), textureHeight - (2 * boundsOffset));

        //puffTexture = new Texture("puff.png");
        //puffAnimation = new Animation(new TextureRegion(puffTexture), 3, 0.5f);

        flap = Gdx.audio.newSound(Gdx.files.internal("woosh.mp3"));
    }

    public void update(float dt, OrthographicCamera cam)
    {
        bunnyAnimation.update(dt);
        if (position.y > 0)
        {
            velocity.add(0, GRAVITY, 0);
        }

        velocity.scl(dt);
        position.add(movement * dt, velocity.y, 0);

        if (position.y < 0)
        {
            position.y = 0;
        }

        if (position.y > cam.viewportHeight - textureHeight)
        {
            position.y = cam.viewportHeight - textureWidth;
        }


        velocity.scl(1/dt);
        bounds.setPosition(position.x + boundsOffset, position.y + boundsOffset);
    }

    public void increaseMovement()
    {
        movement += 1;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public TextureRegion getTexture()
    {
        return bunnyAnimation.getFrame();
    }

    public float getTextureWidth() { return textureWidth; }
    public float getTextureHeight() { return textureHeight; }

    public void jump()
    {

        velocity.y  = 275;
        flap.play(0.4f);
    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public void dispose()
    {
        texture.dispose();
        flap.dispose();
    }
}
