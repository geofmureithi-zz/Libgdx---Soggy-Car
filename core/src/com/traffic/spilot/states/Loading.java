package com.traffic.spilot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.handlers.GameStateManager;

public class Loading extends GameState {

    private Timer timer;
    private Image loadingLogo;
    private TextureRegion backgroundImage;
    private Stage stage;

    public Loading(final GameStateManager gsm) {
        super(gsm);

        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                gsm.setState(GameStateManager.MENU);
            }
        }, 1);

        loadingLogo = new Image(trafficgame.myTextures().findRegion("logo"));
        backgroundImage = new TextureRegion(trafficgame.myTextures().findRegion("background"));

        Gdx.graphics.setContinuousRendering(false);

        stage = new Stage();
        Table loadingTable = new Table();
        loadingTable.setFillParent(true);
        loadingTable.setWidth(stage.getWidth());

        loadingLogo.setScaling(Scaling.fit);
        loadingTable.add(loadingLogo);
        stage.addActor(loadingTable);
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
        sb.begin();
            sb.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            //sb.draw(loadingLogo, (Gdx.graphics.getWidth() / 2) - (loadingLogo.getRegionWidth() / 2), (Gdx.graphics.getHeight() / 2) - (loadingLogo.getRegionHeight() / 2));
        sb.end();
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
        cam.setToOrtho(false, TrafficGame.G_WIDTH * w / (float)h, TrafficGame.G_HEIGHT); // +++
        sb.setProjectionMatrix(cam.combined); // +++
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
