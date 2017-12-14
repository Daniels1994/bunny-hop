package com.hyb0025.bunnyhop.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.hyb0025.bunnyhop.helpers.Size;

/**
 * Created by root on 3/24/17.
 */

public class Explosion
{
    private Size size;
    private Vector2 position;

    private Texture texture;
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;

    private Sound swooshSound;
    private float animationTimer;


    public Explosion(float posX, float posY)
    {
        position = new Vector2(posX, posY);
        size = new Size(50, 50);
        swooshSound = Gdx.audio.newSound(Gdx.files.internal("swoosh.wav"));
        texture = new Texture("puff.png");

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(texture, 0, 0, texture.getWidth() / 3, texture.getHeight()));
        frames.add(new TextureRegion(texture, texture.getWidth() / 3, 0, texture.getWidth() / 3, texture.getHeight()));
        frames.add(new TextureRegion(texture, texture.getWidth() / 3 * 2, 0, texture.getWidth() / 3, texture.getHeight()));

        swooshSound.play(.3f);

        animation = new Animation(.1f, frames);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public Vector2 getPosition() { return position; }

    public TextureRegion getCurrentFrame()
    {
        return currentFrame;
    }

    public void update(float dt)
    {
        animationTimer += dt;
        currentFrame = animation.getKeyFrame(animationTimer);
        if (isAnimationFinished())
        {
            texture.dispose();
        }
    }

    public void dispose()
    {
        texture.dispose();
    }

    public boolean isAnimationFinished() { return animation.isAnimationFinished(animationTimer); }



}
