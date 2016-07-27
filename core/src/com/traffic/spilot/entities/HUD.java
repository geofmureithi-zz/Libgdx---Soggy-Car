package com.traffic.spilot.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;

public class HUD {
    public Stage stage;
    private Player player;

    private Label crystalScoreText;
    private Label timeText;

    public int playTime; // only seconds

    public TextureAtlas hudAtlas;

    public Timer hudTimer;
    private boolean updateTimer;

    public HUD(Player player, Skin skin) {

        stage = new Stage();

        playTime = 0;

        hudAtlas = new TextureAtlas("images/hudPack.pack");
        Image crystal = new Image(hudAtlas.findRegion("hudCrystal"));
        crystal.setScaling(Scaling.none);
        Image stopWatch = new Image(hudAtlas.findRegion("stopwatch"));
        stopWatch.setScaling(Scaling.none);

        Table tableHUD = new Table();
        tableHUD.setDebug(false);
        tableHUD.setWidth(Gdx.graphics.getWidth());
        tableHUD.align(Align.left);
        tableHUD.setPosition(20, Gdx.graphics.getHeight() - 50);

        crystalScoreText = new Label(Integer.toString(player.getNumCrystals()), skin, "default");
        timeText = new Label("0 z", skin, "default");

        tableHUD.add(crystal).padRight(10f);
        tableHUD.add(crystalScoreText).right().padTop(5);
        tableHUD.add().expandX();
        tableHUD.add(timeText).padRight(10f);
        tableHUD.add(stopWatch).padRight(40f);

        stage.addActor(tableHUD);

        this.player = player;

        hudTimer = new Timer();
        hudTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                playTime++;
                updateTimer = true;
            }
        }, 0, 1, 999999);

        hudTimer.start();
    }

    public void render() {

        if (player.updateScore == 1) {
            crystalScoreText.setText(Integer.toString(player.getNumCrystals()));
            player.updateScore = 0;
        }

        if (updateTimer) {
            timeText.setText(playTime + " s");
            updateTimer = false;
        }

        stage.act();
        stage.draw();
    }
}
/* calculation & references
    player.getNumCrystals()  /  player.getTotalCrystals()
    sb.draw(crystal, 10, Gdx.graphics.getHeight() - 50, 12, 12); || font.draw(sb, sBuilderCrystals, 30, Gdx.graphics.getHeight() - 35);

    Double.toString((int) Math.ceil(playTime)
    font.draw(sb, sBuilderTime, 30, Gdx.graphics.getHeight() - 60);

    //            sBuilderCrystals.append(player.getNumCrystals());
//            sBuilderCrystals.append(seperator);
//            sBuilderCrystals.append(Integer.toString(player.getTotalCrystals()));

//            font.draw(sb, sBuilderCrystals, 30, Gdx.graphics.getHeight() - 35); normal font creation

//
//            sBuilderTime.append(Double.toString((int) Math.ceil(playTime)));
//            sBuilderTime.append("s");

//           font.draw(sb, sBuilderTime, 30, Gdx.graphics.getHeight() - 60);   normal font creation

//            cacheScore.draw(sb);
//            cacheTime.draw(sb);
//            font.draw(sb, sBuilderTime, 30, Gdx.graphics.getHeight() - 60);
//            font.draw(sb, sBuilderCrystals, 30, Gdx.graphics.getHeight() - 35);
//            cacheScore.setText(sBuilderCrystals, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
 */
