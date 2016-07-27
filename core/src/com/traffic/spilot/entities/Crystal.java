package com.traffic.spilot.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.traffic.spilot.TrafficGame;

public class Crystal extends B2DSprite {

    public Crystal(Body body) {
        super(body);

        Texture tex =  TrafficGame.manager.get("images/crystal.png");
        TextureRegion[] sprites = TextureRegion.split(tex, 80, 80)[0];
        animation.setFrames(sprites, 1 / 8f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();
    }

}
