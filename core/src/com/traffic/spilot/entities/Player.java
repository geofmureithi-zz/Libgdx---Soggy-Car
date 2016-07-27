package com.traffic.spilot.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.traffic.spilot.TrafficGame;

public class Player extends B2DSprite {

    private int numCrystals;
    private StringBuilder carTextureName;

    private TextureRegion[] sprites;

    public int updateScore;

    public Texture tex;

    public Player(Body body, int characterID) {
        super(body);

        carTextureName = new StringBuilder();

        if (characterID == 1) {
            carTextureName.append("images/car1_anim.png");
        }

        if (characterID == 2) {
            carTextureName.append("images/car2_anim.png");
        }
        if (characterID == 3) {
            carTextureName.append("images/car3_anim.png");
        }
        if (characterID == 4) {
            carTextureName.append("images/car4_anim.png");
        }
        if (characterID == 5) {
            carTextureName.append("images/car5_anim.png");
        }

        tex = TrafficGame.manager.get(carTextureName.substring(0));
        sprites = new TextureRegion[4];
        for(int i = 0; i < sprites.length; i++) {
            sprites[i] = new TextureRegion(tex, i * 80, 0, 80, 80);
        }

        animation.setFrames(sprites, 1 / 12f);

        width = sprites[0].getRegionWidth();
        height = sprites[0].getRegionHeight();

        // removing the string
        carTextureName.delete(0, carTextureName.length());
    }

    public void collectCrystal() {
        if (updateScore == 0) {
            numCrystals++;
        }
        updateScore = 1;
    }

    public int getNumCrystals() { return numCrystals; }
}
