package com.traffic.spilot.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class TGContactListener implements ContactListener {

    private int numFootContacts;
    private Array<Body> bodiesToRemove;
    private boolean playerDead;

    public TGContactListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }

    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
        }
        if(fa.getUserData() != null && fa.getUserData().equals("crystal")) {
            bodiesToRemove.add(fa.getBody());
        }
        if(fb.getUserData() != null && fb.getUserData().equals("crystal")) {
            bodiesToRemove.add(fb.getBody());
        }

        if(fa.getUserData() != null && fa.getUserData().equals("cars")) {
            System.out.println("car FA: " +  fa.getUserData());
            playerDead = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("cars")) {
            playerDead = true;
        }

        if(fa.getUserData() != null && fa.getUserData().equals("wall")) {
            playerDead = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("wall")) {
            playerDead = true;
        }

    }

    public void endContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;

        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }

    }

    public boolean playerCanJump() { return numFootContacts > 0; }
    public Array<Body> getBodies() { return bodiesToRemove; }
    public boolean isPlayerDead() { return playerDead; }

    public void preSolve(Contact c, Manifold m) {}
    public void postSolve(Contact c, ContactImpulse ci) {}
}