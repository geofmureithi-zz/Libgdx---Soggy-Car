package com.traffic.spilot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.handlers.GameStateManager;

/**
 * Created on 19/6/2016.
 *  Sketch idea:
 *      Items are taken from a database ? or just displayed here.
 *      Database includes a player's $ and purchased items.
 *      If a player buys an item, the db gets updated and he gets the item.
 */

public class Store extends GameState {
    private Stage stage;

    private Timer updateScoreTimer;
    public static boolean updateScore;
    private Label scoreNumber;

    public Store(GameStateManager gsm) {
        super(gsm);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        updateScore = false;
        trafficgame.gameInterface.openConnection();

        // <! -- header table
        Table tableScore = new Table();
        tableScore.setWidth(stage.getWidth());
        tableScore.setFillParent(true);
        tableScore.top().left();

        Label storePageTitle = new Label("STORE", trafficgame.getSkin(), "brown-text");
        storePageTitle.setAlignment(Align.center);
        tableScore.add(storePageTitle).colspan(3).fillX().padBottom(30).padTop(30);

        tableScore.row();

        Image crystalIcon = new Image(trafficgame.myTextures().findRegion("mainCrystal"));
        crystalIcon.setScaling(Scaling.none);
        scoreNumber = new Label(Integer.toString(trafficgame.getTotalBalance()), trafficgame.getSkin(), "menu-text");
        tableScore.add(crystalIcon).height(75).width(60).padLeft(15f); // changing the w/h will lead to the picture being scaled inside the cell (Scaling.fit)
        tableScore.add(scoreNumber).height(60).width(30).padTop(10).left().expandX();
        final ImageButton.ImageButtonStyle musicIconStyle = new ImageButton.ImageButtonStyle();
        if(TrafficGame.musicToggle) {
            musicIconStyle.imageUp = new TextureRegionDrawable(new TextureRegion(trafficgame.myTextures().findRegion("sound")));
        } else {
            musicIconStyle.imageUp = new TextureRegionDrawable(new TextureRegion(trafficgame.myTextures().findRegion("soundDisabled")));
        }
        ImageButton musicToggleImage = new ImageButton(musicIconStyle);
        tableScore.add(musicToggleImage).height(75).width(100).right();

        updateScoreTimer = new Timer();
        updateScoreTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if(updateScore) {
                    scoreNumber.setText(Integer.toString(trafficgame.getTotalBalance()));
                }
            }
        }, 2, 2, 500);
        updateScoreTimer.start();
        // --> header table

        // <! --
        Table tableBG = new Table();
        tableBG.setWidth(stage.getWidth());
        tableBG.setFillParent(true);
        Image tableBackground = new Image(trafficgame.myTextures().findRegion("tableBG"));
        tableBackground.setScaling(Scaling.stretch);
        tableBG.add(tableBackground).width(500).height(100);
        // -->

        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.setFillParent(true);

        table.row();

        // <--- 10k crystals
        Image bundleCrystals = new Image(trafficgame.myTextures().findRegion("mainCrystal"));
        table.add(bundleCrystals);

        Label crystalPrice = new Label("10000", trafficgame.getSkin());
        table.add(crystalPrice).padLeft(10).padRight(50);

        Label crystalBuy10000 = new Label("$0.99 BUY", trafficgame.getSkin(), "brown-text");
        crystalBuy10000.setAlignment(Align.right);
        table.add(crystalBuy10000).height(80).right();
        // --->

//        table.row();

        // <--- remove ads
//        Image noAdsImage = new Image(trafficgame.myTextures().findRegion("noAds"));
//        table.add(noAdsImage);
//
//        Label noAdsText = new Label("remove ads", trafficgame.getSkin());
//        table.add(noAdsText).padLeft(10);
//
//        Label noAdsBuy = new Label("$2 BUY", trafficgame.getSkin(), "brown-text");
//        noAdsBuy.setAlignment(Align.right);
//        table.add(noAdsBuy).height(80).width(300).right();
//        Label adsWillBeRemoved = new Label("ads will be removed when you complete a purchase ;)", trafficgame.getSkin(), "brown-text");
//        adsWillBeRemoved.setWrap(true);
//        table.add(adsWillBeRemoved).padLeft(10).width(300);
        // --->

        // <! -- bottom
        Table navButtons = new Table();
        navButtons.setWidth(stage.getWidth());
        navButtons.setFillParent(true);
        navButtons.bottom();
        Image homeButton = new Image(trafficgame.myTextures().findRegion("homeButton"));
        homeButton.setScaling(Scaling.none);
        navButtons.add(homeButton).left().padBottom(50).width(200).height(200);


        stage.addActor(navButtons);
        stage.addActor(tableBG);
        stage.addActor(tableScore);
        stage.addActor(table);

        tableScore.getChildren().get(3).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (trafficgame.getNormalPrefs().getBoolean("music")){
                    ((TextureRegionDrawable) musicIconStyle.imageUp).setRegion(trafficgame.myTextures().findRegion("soundDisabled"));
                    TrafficGame.musicToggle = false;
                    trafficgame.getNormalPrefs().putBoolean("music", false);
                    trafficgame.getNormalPrefs().flush();
                    TrafficGame.res.getMusic("main").dispose();
                } else {
                    ((TextureRegionDrawable) musicIconStyle.imageUp).setRegion(trafficgame.myTextures().findRegion("sound"));
                    TrafficGame.musicToggle = true;
                    trafficgame.getNormalPrefs().putBoolean("music", true);
                    trafficgame.getNormalPrefs().flush();

                    TrafficGame.res.loadMusic("music/main.mp3");
                    TrafficGame.res.getMusic("main").setLooping(true);
                    TrafficGame.res.getMusic("main").play();
                }
            }
        });

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goHome();

            }
        });

        crystalBuy10000.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                trafficgame.gameInterface.buyItem("crystals_10k");
            }
        });

//        noAdsBuy.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                trafficgame.gameInterface.buyItem("remove_ads");
//            }
//        });

        // -- Ads -- //
        if (TrafficGame.adsEnabled) {

            if(trafficgame.gameInterface.isAdEmpty()) {
                trafficgame.gameInterface.reloadAd();
                trafficgame.gameInterface.showAd();;
            } else {
                trafficgame.gameInterface.showAd();
            }
        }
    }

    private void goHome() {
        if(TrafficGame.adsEnabled) {
            trafficgame.gameInterface.hideAd();
        }
        gsm.setState(GameStateManager.MENU);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(251 / 255f, 238 / 255f, 227 / 255f, 0f);
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
        trafficgame.gameInterface.closeConnection();
        stage.dispose();
        updateScoreTimer.stop();
    }
}
