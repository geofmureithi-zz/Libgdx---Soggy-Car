package com.traffic.spilot;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.chartboost.sdk.Libraries.CBLogging;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.securepreferences.SecurePreferences;
import com.traffic.spilot.states.Menu;
import com.traffic.spilot.states.Store;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.ChartboostDelegate;

import static com.traffic.spilot.states.Menu.ownershipArr;

public class AndroidLauncher extends AndroidApplication implements gameInterface, BillingProcessor.IBillingHandler {

    private Handler dialogHandler;
    private Dialog noCrystalDialog;
    private boolean readyToPurchase;
    private AndroidLauncher instance;

    private static final String AD_UNIT_ID = "1234"; // this is your admob advertisment code
    private AdView adView;
    private RelativeLayout.LayoutParams adParams;
    private boolean adLoaded;

    private RelativeLayout layout;
    private final static String base64EncodedPublicKey = "1234"; // found in your google play console account

    private BillingProcessor bp;
    private SecurePreferences androidPrefs;
    private Location lastKnownLocation;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        View gameView = initializeForView(new TrafficGame(this), config);

        instance = this;

        androidPrefs = new SecurePreferences(this, "12345", "soggy_game.xml");
        InitDialog();  // for menu when user tries to purchase car w/o enough crystals

        // creating the layout
        layout = new RelativeLayout(instance);
                // ad view
                adView = new AdView(instance);
                adView.setAdUnitId(AD_UNIT_ID);
                adView.setAdSize(AdSize.SMART_BANNER);
                adLoaded = false;

                // params
                adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            // adding views to the layout

            layout.addView(gameView);
            layout.addView(adView, adParams);

            setContentView(layout);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
           lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (SecurityException e) {
            Log.d("Traffic10", "don't have permission to get the location.");
        }

        // creating things related to chartboost reward ad
        String appId = "12345"; // these two values found in your chartboost account
        String appSig = "12345"; // 
        Chartboost.startWithAppId(this, appId, appSig);
        Chartboost.onCreate(this);
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_MAIN_MENU);
        Chartboost.setActivityCallbacks(true);
        Chartboost.setLoggingLevel(CBLogging.Level.ALL);
        ChartboostDelegate chartboostDelegate = new ChartboostDelegate() {
            @Override
            public void didCloseRewardedVideo(String location) {
                super.didCloseRewardedVideo(location);
                // Log.d("traffic10", "closed video");
            }

            @Override
            public void didCompleteRewardedVideo(String location, int reward) {
                super.didCompleteRewardedVideo(location, reward);
                int newTotalCoins = androidPrefs.getInt("totalCrystals", 0) + reward;
                androidPrefs.edit().putInt("totalCrystals", newTotalCoins).apply();
                Menu.updateScore = true;
            }
        };

        Chartboost.setDelegate(chartboostDelegate);
    }

    public void reloadAd() {
        // Log.d("traffic10", "notification:  ad initialized");
        runOnUiThread(new Runnable() {
            public void run() {
                adView.loadAd(new AdRequest.Builder().setLocation(lastKnownLocation).build());
                adLoaded = true;
            }});
    }

    public void rewardAd() {
        if (Chartboost.hasRewardedVideo(CBLocation.LOCATION_MAIN_MENU)) {
            Chartboost.showRewardedVideo(CBLocation.LOCATION_MAIN_MENU);
            // Log.d("traffic10", "rewardAd launched");
        } else {
            // Log.d("traffic10", "rewardAd is not ready");
            Chartboost.cacheRewardedVideo(CBLocation.LOCATION_MAIN_MENU);
        }
    }

    public void showAd() {
        Log.d("traffic10", "notification:  ad shown");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adView.setVisibility(View.VISIBLE);
                adView.invalidate();
                adView.requestLayout();
            }
        });
    }

    public void hideAd() {
        Log.d("traffic10", "notification:  ad hide");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adView.setVisibility(View.GONE);
            }
        });

    }

    public boolean isAdEmpty() {
        if(adLoaded) {
            return false;
        } else {
            return true;
        }
    }

    public void openConnection() {
        if(!BillingProcessor.isIabServiceAvailable(this)) {
            Gdx.app.log("Traffic10", "In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
            return;
        }
        bp = new BillingProcessor(instance, base64EncodedPublicKey, this);
    }
    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if(productId.equals("crystals_10k")) {
            Log.d("traffic10", "Consuming " + productId + " // Transaction: " + details);
            int newTotalCoins = androidPrefs.getInt("totalCrystals", 0) + 10000;
            androidPrefs.edit().putInt("totalCrystals", newTotalCoins).apply();
            Store.updateScore = true;
            androidPrefs.edit().putBoolean("adsEnabled", false).apply();
            TrafficGame.adsEnabled = false;
            bp.consumePurchase(productId);
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d("traffic20", "onPurchaseHistoryRestored");
        for(String sku : bp.listOwnedProducts())
            Log.d("traffic20", "Owned Managed Product: " + sku);
        for(String sku : bp.listOwnedSubscriptions())
            Log.d("traffic20", "Owned Subscription: " + sku);
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d("traffic10", "onBillingError: user cancelled");
    }

    @Override
    public void onBillingInitialized() {
        Log.d("traffic10", "notification:  onBillingInitialized");
        readyToPurchase = true;
    }
    public void closeConnection() {
        if (bp != null)
            bp.release();
    }

    @Override
    public void applyOwnership(int car_id) {
        switch (car_id) {
            case 2: androidPrefs.edit().putString("car_2", "1").apply();
                break;
            case 3: androidPrefs.edit().putString("car_3", "1").apply();
                break;
            case 4: androidPrefs.edit().putString("car_4", "1").apply();
                break;
            case 5: androidPrefs.edit().putString("car_5", "1").apply();
                break;
        }
        Log.d("traffic10", "Error applying ownership.");
    }

    @Override
	public void buyItem(String SKU_ID) {
        Log.d("traffic10", "Starting purchase: " + SKU_ID);
        if (!readyToPurchase) {
            // Log.d("traffic10", "traffic10: buyItem();: Billing not initialized.");
            return;
        }
        bp.purchase(instance, SKU_ID);
    }

    private void InitDialog(){
        dialogHandler=new Handler();
        noCrystalDialog = new Dialog(this);
        noCrystalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noCrystalDialog.setContentView(R.layout.dialog);
        noCrystalDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    private final Runnable showDialogRunnable=new Runnable(){
        @Override
        public void run() {
            if(noCrystalDialog!=null && !noCrystalDialog.isShowing())
                noCrystalDialog.show();
        }

    };
    @Override
    public void ShowDialog() {
        dialogHandler.post(showDialogRunnable);
    }

    @Override
    public void loadStats() {
        if(androidPrefs.getAll().isEmpty()){
            androidPrefs.edit().putInt("totalCrystals", 0).apply();
            androidPrefs.edit().putString("car_1", "1").apply();
            androidPrefs.edit().putString("car_2", "0").apply();
            androidPrefs.edit().putString("car_3", "0").apply();
            androidPrefs.edit().putString("car_4", "0").apply();
            androidPrefs.edit().putString("car_5", "0").apply();
            androidPrefs.edit().putBoolean("music", true).apply();
            androidPrefs.edit().putBoolean("tutorial", true).apply();
            androidPrefs.edit().putInt("lastplayed", 0).apply();
            androidPrefs.edit().putBoolean("adsEnabled", true).apply();
            ownershipArr.add("1");
            ownershipArr.add("1");
            ownershipArr.add("0");
            ownershipArr.add("0");
            ownershipArr.add("0");
            ownershipArr.add("0");
        }

        if(!androidPrefs.getAll().isEmpty()){
            ownershipArr.add("1"); // 0
            ownershipArr.add(androidPrefs.getString("car_1", "1")); // 1
            ownershipArr.add(androidPrefs.getString("car_2", "0")); // 2
            ownershipArr.add(androidPrefs.getString("car_3", "0")); // 3
            ownershipArr.add(androidPrefs.getString("car_4", "0")); // 4
            ownershipArr.add(androidPrefs.getString("car_5", "0")); // 5
        }
    }
    @Override
    public boolean adsEnabled() {
        return androidPrefs.getBoolean("adsEnabled", true);
    }
    @Override
    public int getTotalCrystals() {
        return androidPrefs.getInt("totalCrystals", 0);
    }

    @Override
    public void setLastPlayed(int cha_id) {
        androidPrefs.edit().putInt("lastplayed", cha_id).apply();
    }

    @Override
    public int getLastPlayed() {
        return androidPrefs.getInt("lastplayed", 0);
    }

    @Override
    public void editBalance(int newValue) {
        androidPrefs.edit().putInt("totalCrystals", newValue).apply();
    }

    private final Runnable hideDialogRunnable=new Runnable(){
        @Override
        public void run() {
            if(noCrystalDialog!=null && !noCrystalDialog.isShowing())
                noCrystalDialog.dismiss();
        }

    };
    @Override
    public void HideDialog() {
        dialogHandler.post(hideDialogRunnable);
    }

    @Override
    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
        Chartboost.onDestroy(this);
    }

    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close it.
        if (Chartboost.onBackPressed())
            return;
        else
            super.onBackPressed();
    }
}