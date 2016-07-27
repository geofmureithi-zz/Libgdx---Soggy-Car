package com.traffic.spilot.desktop;


import com.badlogic.gdx.Gdx;
import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.gameInterface;
import com.traffic.spilot.states.Menu;
import com.traffic.spilot.states.Store;

import static com.traffic.spilot.states.Menu.ownershipArr;

public class DesktopInterface implements gameInterface {


    @Override
    public void loadStats() {
        if(TrafficGame.prefs.contains("played")){
            
            TrafficGame.prefs.putString("car_1", "1");
            TrafficGame.prefs.putString("car_2", "0");
            TrafficGame.prefs.putString("car_3", "0");
            TrafficGame.prefs.putString("car_4", "0");
            TrafficGame.prefs.putString("car_5", "0");
            TrafficGame.prefs.putBoolean("music", true);
            TrafficGame.prefs.putBoolean("tutorial", true);
            TrafficGame.prefs.putInteger("lastplayed", 0);
            TrafficGame.prefs.putInteger("played", 1);
            TrafficGame.prefs.flush();
            ownershipArr.add("1");
            ownershipArr.add("1");
            ownershipArr.add("0");
            ownershipArr.add("0");
            ownershipArr.add("0");
            ownershipArr.add("0");
        }

        if(!TrafficGame.prefs.contains("played")){
            ownershipArr.add("1"); // 0
            ownershipArr.add(TrafficGame.prefs.getString("car_1")); // 1
            ownershipArr.add(TrafficGame.prefs.getString("car_2")); // 2
            ownershipArr.add(TrafficGame.prefs.getString("car_3")); // 3
            ownershipArr.add(TrafficGame.prefs.getString("car_4")); // 4
            ownershipArr.add(TrafficGame.prefs.getString("car_5")); // 5
            TrafficGame.musicToggle = TrafficGame.prefs.getBoolean("music");
        }
    }

    @Override
    public void buyItem(String SKU_CAR) {
        if(SKU_CAR.equals("crystals_10k")) {
            Gdx.app.log("traffic10", "Buying item: " + SKU_CAR);

            int newTotalCoins = TrafficGame.prefs.getInteger("totalCrystals") + 10000;
            TrafficGame.prefs.putInteger("totalCrystals", newTotalCoins);
            TrafficGame.prefs.flush();

            Store.updateScore = true;
        }
        if(SKU_CAR.equals("remove_ads")) {
            Gdx.app.log("traffic10", "purchased remove_ads!");
        }
    }

    @Override
    public void ShowDialog() {
        Gdx.app.log("traffic10", "Dialog: you don't afford it!");
    }

    @Override
    public boolean adsEnabled() {
        return false;
    }

    @Override
    public void reloadAd() {
        Gdx.app.log("traffic10", "reloaded ad.");
    }

    @Override
    public void showAd() {
        Gdx.app.log("traffic10", "shown ad.");
    }

    @Override
    public void hideAd() {
        Gdx.app.log("traffic10", "initlized ad.");
    }

    @Override
    public boolean isAdEmpty() {
        return false;
    }

    @Override
    public void rewardAd() {
        Gdx.app.log("traffic10", "initlized rewarded ad.");
        int newTotalCoins = TrafficGame.prefs.getInteger("totalCrystals") + 500;
        TrafficGame.prefs.putInteger("totalCrystals", newTotalCoins);
        TrafficGame.prefs.flush();
        Menu.updateScore = true;
    }

    @Override
    public int getTotalCrystals() {
        return TrafficGame.prefs.getInteger("totalCrystals");

    }

    @Override
    public void editBalance(int newValue) {
        TrafficGame.prefs.putInteger("totalCrystals", newValue);
        TrafficGame.prefs.flush();
    }

    @Override
    public void setLastPlayed(int cha_id) {
        TrafficGame.prefs.putInteger("lastplayed", cha_id);
        TrafficGame.prefs.flush();
    }

    @Override
    public int getLastPlayed() {
        return TrafficGame.prefs.getInteger("lastplayed");
    }

    @Override
    public void HideDialog() {

    }

    @Override
    public void openConnection() {
        Gdx.app.log("Traffic10", "Opened connection!");
    }

    @Override
    public void closeConnection() {
        Gdx.app.log("Traffic10", "Closed connection!");
    }

    @Override
    public void applyOwnership(int car_id) {
        switch (car_id) {
            case 2:  TrafficGame.prefs.putString("car_2", "1");
                TrafficGame.prefs.flush();
                break;
            case 3:  TrafficGame.prefs.putString("car_3", "1");
                TrafficGame.prefs.flush();
                break;
            case 4:  TrafficGame.prefs.putString("car_4", "1");
                TrafficGame.prefs.flush();
                break;
            case 5:  TrafficGame.prefs.putString("car_5", "1");
                TrafficGame.prefs.flush();
                break;
        }
        Gdx.app.log("Traffic10", "Error applying ownership.");
    }
}
