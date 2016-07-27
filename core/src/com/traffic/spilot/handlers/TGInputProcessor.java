package com.traffic.spilot.handlers;

import com.badlogic.gdx.InputAdapter;

public class TGInputProcessor extends InputAdapter {

    public boolean mouseMoved(int x, int y) {
        TGInput.x = x;
        TGInput.y = y;
        return true;
    }

    public boolean touchDragged(int x, int y, int pointer) {
        TGInput.x = x;
        TGInput.y = y;
        TGInput.down = true;
        return true;
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        TGInput.x = x;
        TGInput.y = y;
        TGInput.down = true;
        return true;
    }

    public boolean touchUp(int x, int y, int pointer, int button) {
        TGInput.x = x;
        TGInput.y = y;
        TGInput.down = false;
        return true;
    }
}
