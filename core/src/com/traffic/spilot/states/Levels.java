package com.traffic.spilot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.handlers.GameStateManager;

public class Levels extends GameState {
    private TextureRegion backgroundImage;
    private Table levelstable;
    private Stage stage;

    private TextButton level1Text;
    private TextButton level2Text;
    private TextButton level3Text;
    private TextButton level4Text;
    private TextButton level5Text;
    private TextButton level6Text;

    public Levels (GameStateManager gsm) {
        super(gsm);
        //cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        backgroundImage = new TextureRegion(trafficgame.myTextures().findRegion("background"));

        levelstable = new Table();
        levelstable.setFillParent(true);


        level1Text = new TextButton("1", trafficgame.getSkin(), "select-button");

        if(trafficgame.getNormalPrefs().getBoolean("Level2")) {
            level2Text = new TextButton("2", trafficgame.getSkin(), "select-button");
        } else {
            level2Text = new TextButton("--", trafficgame.getSkin(), "select-button-disabled");
        }

        if(trafficgame.getNormalPrefs().getBoolean("Level3")) {
            level3Text = new TextButton("3", trafficgame.getSkin(), "select-button");
        } else {
            level3Text = new TextButton("--", trafficgame.getSkin(), "select-button-disabled");

        }
        if(trafficgame.getNormalPrefs().getBoolean("Level4")) {
            level4Text = new TextButton("4", trafficgame.getSkin(), "select-button");
        } else {
            level4Text = new TextButton("--", trafficgame.getSkin(), "select-button-disabled");
        }
        if(trafficgame.getNormalPrefs().getBoolean("Level5")) {
            level5Text = new TextButton("5", trafficgame.getSkin(), "select-button");
        } else {
            level5Text = new TextButton("--", trafficgame.getSkin(), "select-button-disabled");
        }
        if(trafficgame.getNormalPrefs().getBoolean("Level6")) {
            level6Text = new TextButton("6", trafficgame.getSkin(), "select-button");
        } else {
            level6Text = new TextButton("--", trafficgame.getSkin(), "select-button-disabled");
        }

        levelstable.add(level1Text).padBottom(20).width(80).height(100).fillX();
        levelstable.add(level2Text).width(80).height(100).padLeft(35).padBottom(20).fillX();
        levelstable.add(level3Text).width(80).height(100).padLeft(35).padBottom(20).fillX();
        levelstable.row();
        levelstable.add(level4Text).width(80).height(100).fillX();
        levelstable.add(level5Text).padLeft(35).width(80).height(100).fillX();
        levelstable.add(level6Text).padLeft(35).width(80).height(100).fillX();

        levelstable.getChildren().get(0).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goLevel1();
            }
        });

        if(trafficgame.getNormalPrefs().getBoolean("Level2")){
            levelstable.getChildren().get(1).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    goLevel2();
                }
            });
        }
        if(trafficgame.getNormalPrefs().getBoolean("Level3")){
            levelstable.getChildren().get(2).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    goLevel3();
                }
            });
        }
        if(trafficgame.getNormalPrefs().getBoolean("Level4")){
            levelstable.getChildren().get(3).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    goLevel4();
                }
            });
        }
        if(trafficgame.getNormalPrefs().getBoolean("Level5")){
            levelstable.getChildren().get(4).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    goLevel5();
                }
            });
        }
        if(trafficgame.getNormalPrefs().getBoolean("Level5")){
            levelstable.getChildren().get(4).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    goLevel5();
                }
            });
        }

        stage.addActor(levelstable);

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

    private void goLevel1() {
        gsm.setState(GameStateManager.PLAY);
    }
    private void goLevel2() {
        gsm.setState(GameStateManager.PLAY_2);
    }
    private void goLevel3() {
        gsm.setState(GameStateManager.PLAY_3);
    }
    private void goLevel4() {
        gsm.setState(GameStateManager.PLAY_4);
    }
    private void goLevel5() {
        gsm.setState(GameStateManager.PLAY_5);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(255 / 255f, 255 / 255f, 255 / 255f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
            sb.draw(backgroundImage, 0, 0, TrafficGame.G_WIDTH, TrafficGame.G_HEIGHT);
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
