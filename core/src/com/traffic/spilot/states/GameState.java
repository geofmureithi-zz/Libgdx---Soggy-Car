package com.traffic.spilot.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.handlers.BoundedCamera;
import com.traffic.spilot.handlers.GameStateManager;

public abstract class GameState {
    protected GameStateManager gsm;
    protected TrafficGame trafficgame;

    protected SpriteBatch sb;
    protected BoundedCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        trafficgame = gsm.trafficgame();
        sb = trafficgame.getSb();
        cam = trafficgame.getCam();
        hudCam = trafficgame.getHUDCamera();
    }

    public abstract void handleInput();

    public abstract void update(float dt);
    public abstract void render();
    public abstract void resize(int w, int h);
    public abstract void dispose();
}

