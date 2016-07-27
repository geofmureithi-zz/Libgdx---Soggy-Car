package com.traffic.spilot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.handlers.GameStateManager;

public class Gameover extends GameState {

    private Stage stage;

    public static int finalScoreCrystals;
    public static int finalTime;
    public static int nextLevel;

    public Gameover(GameStateManager gsm) {
        super(gsm);

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.top();

        Label yourScore = new Label("YOUR SCORE", trafficgame.getSkin(), "title-text");
        Label crystalPoints = new Label(Integer.toString(finalScoreCrystals) + "+", trafficgame.getSkin());
        Label timePlayed = new Label(Integer.toString(finalTime / 2) + "+", trafficgame.getSkin());

        int calculateFinal = finalScoreCrystals + (finalTime / 2); // calculate final score
        int oldCoins = trafficgame.getTotalBalance();
        trafficgame.gameInterface.editBalance(oldCoins + calculateFinal);

        Label finalScore = new Label(Integer.toString(calculateFinal), trafficgame.getSkin()); // display final score

        Image crystalIcon = new Image(trafficgame.myTextures().findRegion("hudCrystal"));
        crystalIcon.setScaling(Scaling.none);

        Image stopwatch = new Image(trafficgame.myTextures().findRegion("stopwatch"));
        stopwatch.setScaling(Scaling.none);

        Button startButton = new Button(trafficgame.getSkin());

        Image homeButton = new Image(trafficgame.myTextures().findRegion("homeButton"));
        homeButton.setScaling(Scaling.none);
        homeButton.setAlign(Align.center);
        Image shareButton = new Image(trafficgame.myTextures().findRegion("shareButton"));
        shareButton.setScaling(Scaling.none);

        // add the score + buttons to the table created above
        table.add(yourScore).padTop(50).colspan(3);
        table.row();
        table.add(finalScore).colspan(3);

        // centered table
        Table summaryTable = new Table();
        summaryTable.setFillParent(true);

        summaryTable.add(crystalIcon).left().padTop(100).padBottom(10).padRight(50);
        summaryTable.add(crystalPoints).padTop(100).padBottom(10).colspan(2).right();

        summaryTable.row();

        summaryTable.add(stopwatch).left().padRight(50);
        summaryTable.add(timePlayed).colspan(2).right();

        summaryTable.row();

        summaryTable.add(startButton).padTop(50).colspan(3);

        summaryTable.row();

        summaryTable.add(homeButton).padTop(10).fillX().colspan(3);
        //table.add(shareButton).right().bottom().colspan(1);

        // create button listeners
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TrafficGame.res.getMusic("main").pause();
                rePlay();
            }
        });

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goMenu();
            }
        });

        // add the table to the Stage
        stage.addActor(table);
        stage.addActor(summaryTable);
        cam.setToOrtho(false, TrafficGame.G_WIDTH, TrafficGame.G_HEIGHT);

        Gdx.graphics.setContinuousRendering(false);

        if(TrafficGame.musicToggle) {
            TrafficGame.res.getMusic("main").setLooping(true);
            TrafficGame.res.getMusic("main").play();
        }

        // -- Ads -- //
        if (TrafficGame.adsEnabled) {

            if(trafficgame.gameInterface.isAdEmpty()) {
                trafficgame.gameInterface.reloadAd();
                trafficgame.gameInterface.showAd();
            } else {
                trafficgame.gameInterface.showAd();
            }

        }
    }

    private void rePlay() {
        trafficgame.gameInterface.hideAd();
        switch(nextLevel){
            case 1:
                gsm.setState(GameStateManager.PLAY);
                break;
            case 2:
                gsm.setState(GameStateManager.PLAY_2);
                break;
            case 3:
                gsm.setState(GameStateManager.PLAY_3);
                break;
            case 4:
                gsm.setState(GameStateManager.PLAY_4);
                break;
            case 5:
                gsm.setState(GameStateManager.PLAY_5);
                break;
            case 6:
                gsm.setState(GameStateManager.PLAY_6);
                break;
        }
    }

    private void goMenu() {
        trafficgame.gameInterface.hideAd();
        gsm.setState(GameStateManager.MENU); }


    @Override
    public void handleInput() { }

    @Override
    public void update(float dt) { }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f,0f,0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(trafficgame.myTextures().findRegion("background"), 0, 0, TrafficGame.G_WIDTH, TrafficGame.G_HEIGHT);
        sb.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
