package com.hyb0025.bunnyhop.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Coin
{
    private Vector3 position;
    private Texture texture;
    private Boolean isSinking;
    private Sound pickupSound;

    public Coin(float posX, float posY, boolean newIsSinking)
    {
        position = new Vector3(posX, posY, 0);
        texture = new Texture("coin_silver.png");
        isSinking = newIsSinking;
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));
    }

    public Vector3 getPosition() { return position; }

    public Texture getTexture() { return texture; }

    public void update(Bunny bird)
    {
        if (isSinking)
        {
            position.y -= .5;
        }
        else
        {
            position.y += .5;
        }
    }

    public void explode()
    {
        dispose();
    }

    public void dispose()
    {
        texture.dispose();
    }

    public void playPickupSound()
    {
        pickupSound.play(0.15f);
    }

}
