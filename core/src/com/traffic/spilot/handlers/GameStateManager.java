package com.traffic.spilot.handlers;

import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.states.GameState;
import com.traffic.spilot.states.Gameover;
import com.traffic.spilot.states.Levels;
import com.traffic.spilot.states.Loading;
import com.traffic.spilot.states.Menu;
import com.traffic.spilot.states.Play_2;
import com.traffic.spilot.states.Play_3;
import com.traffic.spilot.states.Play_4;
import com.traffic.spilot.states.Play_5;
import com.traffic.spilot.states.Play;
import com.traffic.spilot.states.Play_6;
import com.traffic.spilot.states.Store;

import java.util.Stack;

public class GameStateManager {

    private TrafficGame trafficgame;

    private Stack<GameState> gameStates;

    public static final int LOADING = 555;
    public static final int LEVELS = 231325;
    public static final int PLAY_2 = 22888;
    public static final int PLAY_3 = 33888;
    public static final int PLAY_4 = 44888;
    public static final int PLAY_5 = 55888;
    public static final int PLAY_6 = 66888;
    public static final int MENU = 111;
    public static final int PLAY = 222;
    public static final int GAMEOVER = 333;
    public static final int STORE = 444;


    public GameStateManager(TrafficGame trafficgame) {
        this.trafficgame = trafficgame;
        gameStates = new Stack<GameState>();
        pushState(LOADING); // first state to show
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    public TrafficGame trafficgame() { return trafficgame; }

    private GameState getState(int state) {
        if(state == MENU) return new Menu(this);
        if(state == PLAY) return new Play(this);
        if(state == GAMEOVER) return new Gameover(this);
        if(state == STORE) return new Store(this);
        if(state == LOADING) return new Loading(this);
        if(state == LEVELS) return new Levels(this);
        if(state == PLAY_2) return new Play_2(this);
        if(state == PLAY_3) return new Play_3(this);
        if(state == PLAY_4) return new Play_4(this);
        if(state == PLAY_5) return new Play_5(this);
        if(state == PLAY_6) return new Play_6(this);
        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }


}