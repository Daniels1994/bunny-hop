package com.hyb0025.bunnyhop.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.hyb0025.bunnyhop.MyTestGame;
import com.hyb0025.bunnyhop.sprites.Cloud;
import com.hyb0025.bunnyhop.sprites.Explosion;
import com.hyb0025.bunnyhop.sprites.Minion;
import com.hyb0025.bunnyhop.sprites.Tube;
import com.hyb0025.bunnyhop.helpers.Size;
import com.hyb0025.bunnyhop.sprites.Bunny;
import com.hyb0025.bunnyhop.sprites.Coin;


public class PlayState extends State
{
    // Finals
    private static final int TUBE_SPACING = 125;
    private static final int TUBE_COUNT = 4;
    // Objects
    private Bunny bunny;
    private Array<Tube> tubes;
    private Array<Coin> coins;

    private int tubeCounter;

    // Textures
    private Texture bg;
    private Texture bgfg;
    private Texture ground;

    private Texture retryButton;
    private Vector2 retryButtonPos;
    private Size retryButtonSize;

    // Logic
    private Vector2 groundPos1, groundPos2, bgfgPos1, bgfgPos2;
    private Boolean isPlayerDead;
    private int score;
    private int highscore;

    private Array<Minion> minions;
    private Array<Explosion> explosions;
    private Array<Cloud> clouds;

    private Sound gameOverSound;
    private Sound gameOverSound2;

    private BitmapFont scoreText;
    private BitmapFont highscoreText;
    private GlyphLayout scoreTextLayout;
    private GlyphLayout highscoreTextLayout;
    private SpriteBatch hudBatch;

    protected PlayState(GameStateManager gsm)
    {
        super(gsm);
        cam.setToOrtho(false, MyTestGame.WIDTH / 2, MyTestGame.HEIGHT / 2);

        bunny = new Bunny(50, 200);
        tubes = new Array<Tube>();
        coins = new Array<Coin>();
        minions = new Array<Minion>();
        explosions = new Array<Explosion>();

        // Textury
        String bgPath = "bg_dark.png";

        bg = new Texture(bgPath);

        bgfg = new Texture("town.png");
        ground = new Texture("ground.png");

        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("glass.mp3"));
        gameOverSound2 = Gdx.audio.newSound(Gdx.files.internal("glass2.mp3"));


        retryButton = new Texture("retrybtn.png");
        retryButtonSize = new Size(50, 50);

        tubeCounter = 0;

        // vytvareni mraku
        clouds = new Array<Cloud>();
        float randomY = (float) Math.floor(Math.random() * (cam.viewportHeight - 50) + 50);
        clouds.add(new Cloud( cam.viewportWidth, randomY));
        randomY = (float) Math.floor(Math.random() * (cam.viewportHeight - 50) + 50);
        clouds.add(new Cloud( cam.viewportWidth + 70, randomY));
        randomY = (float) Math.floor(Math.random() * (cam.viewportHeight - 50) + 50);
        clouds.add(new Cloud( cam.viewportWidth + 140, randomY));
        randomY = (float) Math.floor(Math.random() * (cam.viewportHeight - 50) + 50);
        clouds.add(new Cloud( cam.viewportWidth + 210, randomY));
        randomY = (float) Math.floor(Math.random() * (cam.viewportHeight - 50) + 50);
        clouds.add(new Cloud( cam.viewportWidth + 280, randomY));


        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, MyTestGame.GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), MyTestGame.GROUND_Y_OFFSET);

        bgfgPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, 0);
        bgfgPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + cam.viewportWidth, 0);

        isPlayerDead = false;
        score = 0;
        highscore = Gdx.app.getPreferences("gamePreferences").getInteger("highScore");

        // fonty uziti
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("FiraSans-Regular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 24;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = 2;


        scoreText = fontGenerator.generateFont(fontParameter);
        highscoreText = fontGenerator.generateFont(fontParameter);

        scoreTextLayout = new GlyphLayout(scoreText, "Score: " + score);
        highscoreTextLayout = new GlyphLayout(highscoreText, "Highscore: " + highscore);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);





        hudBatch = new SpriteBatch();

        for (int i = 1; i <= TUBE_COUNT; i++)
        {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH) + 175));
        }
    }

    @Override
    protected void handleInput()
    {
        if (Gdx.input.justTouched())
        {
            if (!isPlayerDead)
            {
                Vector3 tp = new Vector3();
                cam.unproject(tp.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                score++;

                Boolean interacted = false;
                for (Coin coin: coins)
                {
                    Rectangle bounds = new Rectangle(coin.getPosition().x, coin.getPosition().y, coin.getTexture().getWidth(), coin.getTexture().getHeight());
                    if (bounds.contains(tp.x, tp.y))
                    {
                        explosions.add(new Explosion(coin.getPosition().x, coin.getPosition().y));
                        coin.explode();
                        coins.removeIndex(coins.indexOf(coin, true));
                        score++;
                        interacted = true;
                        coin.playPickupSound();

                    }
                }

                for (Minion minion: minions)
                {
                    if (minion.getBounds().contains(tp.x, tp.y))
                    {
                        explosions.add(new Explosion(minion.getPosition().x, minion.getPosition().y));
                        coins.add(new Coin(minion.getPosition().x, minion.getPosition().y, minion.getPosition().y > cam.viewportHeight / 2 ));
                        minion.die();

                        minions.removeIndex(minions.indexOf(minion, true));
                        interacted = true;

                    }
                }

                if (!interacted)
                {
                    bunny.jump();
                }
            }
            else
            {
                Vector3 tp = new Vector3();
                cam.unproject(tp.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                Rectangle bounds = new Rectangle(retryButtonPos.x, retryButtonPos.y, retryButtonSize.getWidth(), retryButtonSize.getHeight());

                if (bounds.contains(tp.x, tp.y))
                {
                    gsm.set(new PlayState(gsm));
                }
            }

        }
    }

    @Override
    public void update(float dt)
    {
        handleInput();

        if (!isPlayerDead)
        {
            updateBackgroundMovement();
            bunny.update(dt, cam);
            cam.position.x = bunny.getPosition().x + 80;

            bgfgPos1.add(50 * dt, 0);
            bgfgPos2.add(50 * dt, 0);

            for (int i = 0; i < tubes.size; i++)
            {
                Tube tube = tubes.get(i);
                double camPos = cam.position.x - (cam.viewportWidth / 2);
                double tubePos = tube.getPosTopTube().x + tube.getTopTube().getWidth();

                if (camPos > tubePos)
                {
                    tubeCounter++;
                    repositionTube(tube);
                    handleSpawnMinion(tubeCounter);
                    handleSpawnCoin(tubeCounter);
                    bunny.increaseMovement();
                }

                if (tube.collides(bunny.getBounds()))
                {
                    handleDeath();
                }
            }

            for (Minion minion: minions)
            {
                minion.update(dt, bunny);
                if (minion.collides(bunny.getBounds()))
                {
                    handleDeath();
                }
            }

            for (Coin coin: coins)
            {
                coin.update(bunny);
            }

            for (Explosion explosion: explosions)
            {
                explosion.update(dt);
                if (explosion.isAnimationFinished())
                {
                    explosions.removeIndex(explosions.indexOf(explosion, true));
                }
            }

            for (Cloud cloud: clouds)
            {
                cloud.update(dt);
                double camPos = cam.position.x - (cam.viewportWidth / 2);
                double cloudPos = cloud.getPosition().x + cloud.getSize().getWidth();

                if (camPos > cloudPos)
                {
                    float randomY = (float) Math.floor(Math.random() * (cam.viewportHeight - 50) + 50);
                    cloud.setPosition(cam.position.x + cam.viewportWidth + 20, randomY);
                }
            }

            if (bunny.getPosition().y <= ground.getHeight() + MyTestGame.GROUND_Y_OFFSET)
            {
                handleDeath();
            }
        }

        cam.update();
    }

    private void repositionTube(Tube tube)
    {
        tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
    }

    private void handleSpawnMinion(int tubeCounter)
    {
        int number = (int) Math.floor(Math.random() * (cam.viewportHeight - 50) + 50);

        if (tubeCounter < 8 && tubeCounter % 2 == 0)
        {
            minions.add(new Minion(cam.position.x + cam.viewportWidth + 30, number));
        }
        else if (4 <= tubeCounter && tubeCounter < 12 && tubeCounter % 2 == 0)
        {
            minions.add(new Minion(cam.position.x + cam.viewportWidth + 30, number));
            minions.add(new Minion(cam.position.x + cam.viewportWidth + 30, number + 50));
        }
        else if (8 <= tubeCounter && tubeCounter < 12)
        {
            minions.add(new Minion(cam.position.x + cam.viewportWidth + 30, number));
        }
        else if (8 <= tubeCounter && tubeCounter < 12 && tubeCounter % 2 == 0)
        {
            minions.add(new Minion(cam.position.x + cam.viewportWidth + 30, number));
            minions.add(new Minion(cam.position.x + cam.viewportWidth + 30, number + 50));
        }
        else
        {
            int numOfMinions =  (int) Math.round(Math.floor(tubeCounter / 4));
            for (int i = 0; i < numOfMinions; i++)
            {
                minions.add(new Minion(cam.position.x + cam.viewportWidth + 30, number + (i * 70)));
            }
        }
    }

    private void handleSpawnCoin(int tubeCounter)
    {

        if (tubeCounter  % 2 == 1)
        {
            boolean isSinking = Math.floor(Math.random() * 2) == 0;
            coins.add(new Coin(cam.position.x + cam.viewportWidth + 30, cam.viewportHeight / 2, isSinking));
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        renderGameplay(sb);
        if (isPlayerDead)
        {
            renderGameover(sb);
        }
    }

    public void renderGameplay(SpriteBatch sb)
    {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), - MyTestGame.GROUND_Y_OFFSET / 2);

        for (Cloud cloud: clouds)
        {
            sb.draw(cloud.getTexture(), cloud.getPosition().x, cloud.getPosition().y, 80, 40);
        }

        sb.draw(bgfg, bgfgPos1.x, 0, cam.viewportWidth, cam.viewportHeight / 2);
        sb.draw(bgfg, bgfgPos2.x, 0, cam.viewportWidth, cam.viewportHeight / 2);



        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        for (Explosion explosion: explosions)
        {
            sb.draw(explosion.getCurrentFrame(), explosion.getPosition().x, explosion.getPosition().y );
        }

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        for (Coin coin: coins)
        {
            //sb.draw(coin.getTexture(), coin.getPosition().x, coin.getPosition().y);
        }

        for (Minion minion: minions)
        {
            sb.draw(minion.getTexture(), minion.getPosition().x, minion.getPosition().y, minion.getWidth(), minion.getHeight());
        }

        sb.draw(bunny.getTexture(), bunny.getPosition().x, bunny.getPosition().y, bunny.getTextureWidth(), bunny.getTextureHeight());
        sb.end();

        if (!isPlayerDead)
        {
            hudBatch.setProjectionMatrix(cam.projection);
            hudBatch.begin();
            scoreText.draw(hudBatch,  String.valueOf(score), -cam.viewportWidth/2 + 10, cam.viewportHeight / 2 - 10);
            hudBatch.end();
        }
    }

    public void renderGameover(SpriteBatch sb)
    {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.projection);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 0.5f);
        shapeRenderer.rect( - cam.viewportWidth / 2, -cam.viewportHeight / 2, cam.viewportWidth, cam.viewportHeight);
        shapeRenderer.end();


        sb.begin();

        scoreText.draw(sb, scoreTextLayout, cam.position.x - scoreTextLayout.width / 2, cam.viewportHeight / 10 * 8);
        highscoreText.draw(sb, highscoreTextLayout, cam.position.x - highscoreTextLayout.width / 2, cam.viewportHeight / 10 * 7);
        sb.draw(retryButton, retryButtonPos.x, retryButtonPos.y, retryButtonSize.getWidth(), retryButtonSize.getHeight());
        sb.end();
    }

    @Override
    public void dispose()
    {

        ground.dispose();
        bg.dispose();
        bgfg.dispose();
        bunny.dispose();
        scoreText.dispose();
        retryButton.dispose();
        for (Tube tube: tubes)
        {
            tube.dispose();
        }
        for (Explosion explosion: explosions)
        {
            explosion.dispose();
        }
        for (Coin coin: coins)
        {
            coin.dispose();
        }
        for (Minion  minion: minions)
        {
            minion.dispose();
        }
        System.out.println("Play State Disposed");
    }

    private void handleDeath()
    {
        int flippedCoin = (int) Math.floor(Math.random()*2);
        if (flippedCoin == 0)
        {
            gameOverSound.play(.3f);
        }
        else
        {
            gameOverSound2.play(.3f);
        }

        isPlayerDead = true;
        scoreTextLayout = new GlyphLayout(scoreText, "Score: " + score);
        retryButtonPos = new Vector2(cam.position.x - retryButtonSize.getWidth() / 2, cam.viewportHeight / 10 * 4);
        Preferences preferences = Gdx.app.getPreferences("gamePreferences");
        if (score > highscore)
        {
            preferences.putInteger("highScore", score);
            preferences.flush();
        }
    }

    private void updateBackgroundMovement()
    {
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth())
        {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth())
        {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
        if (cam.position.x - (cam.viewportWidth / 2) > bgfgPos1.x + cam.viewportWidth)
        {
            bgfgPos1.add(cam.viewportWidth * 2, 0);
        }
        if (cam.position.x - (cam.viewportWidth / 2) > bgfgPos2.x + cam.viewportWidth)
        {
            bgfgPos2.add(cam.viewportWidth * 2, 0);
        }
    }
}
