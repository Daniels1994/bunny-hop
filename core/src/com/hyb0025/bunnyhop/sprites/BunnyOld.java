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

        flap = Gdx.audio.newSound(Gdx.files.internal("woosh.mp3"));
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
