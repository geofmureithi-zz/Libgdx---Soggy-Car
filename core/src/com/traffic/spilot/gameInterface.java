package com.traffic.spilot;

public interface gameInterface {
    void openConnection();
    void closeConnection();
    void applyOwnership(int car_id);
    void buyItem(String SKU_ID);
    void ShowDialog();

    //ads
    boolean adsEnabled();
    void reloadAd();
    void showAd();
    void hideAd();
    boolean isAdEmpty();
    void rewardAd();

    void loadStats();
    int getTotalCrystals(); // this should edit the static array (maybe make it all from trafficgame.?)
    void editBalance(int newValue);

    void setLastPlayed(int cha_id);
    int getLastPlayed();


    void HideDialog();
}

