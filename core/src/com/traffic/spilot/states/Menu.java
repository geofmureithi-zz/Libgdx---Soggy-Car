package com.traffic.spilot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.handlers.GameStateManager;

import java.util.ArrayList;

public class Menu extends GameState {
    private Table table;

    private int characterID;
    private Array<TextureRegion> characterSelection;
    private ImageButton characterImage;
    private Label carName;

    public static ArrayList<String> ownershipArr;

    private Button startButton;
    private Timer updateSelectionTimer;
    private Image tablePurchaseCar2;
    private Image tablePurchaseCar3;
    private Image tablePurchaseCar4;
    private Image tablePurchaseCar5;
    private static boolean purchasedCar2;
    private static boolean purchasedCar3;
    private static boolean purchasedCar4;
    private static boolean purchasedCar5;

    private Timer updateScoreTimer;
    public static boolean updateScore;

    private Label totalScore;

    private Stage stage;

    public Menu(final GameStateManager gsm) {
        super(gsm);

        characterSelection = new Array<TextureRegion>();
        ownershipArr = new ArrayList<String>();

        ownershipArr.clear();
        //Gdx.app.log("traffic10", " before " + ownershipArr);
                trafficgame.gameInterface.loadStats();
                TrafficGame.adsEnabled = trafficgame.gameInterface.adsEnabled();
        //Gdx.app.log("traffic10", " after " + ownershipArr);

        //Gdx.app.log("traffic10", " total coins: " + trafficgame.gameInterface.getTotalCrystals());

        purchasedCar2 = false;
        purchasedCar3 = false;
        purchasedCar4 = false;
        purchasedCar5 = false;
        updateSelectionTimer = new Timer();
        updateSelectionTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if(purchasedCar2){
                    table.getCells().get(9).setActor(startButton);
                    purchasedCar2 = false;
                    totalScore.setText(Integer.toString(trafficgame.getTotalBalance()));
                }
                if(purchasedCar3){
                    table.getCells().get(9).setActor(startButton);
                    purchasedCar3 = false;
                    totalScore.setText(Integer.toString(trafficgame.getTotalBalance()));
                }
                if(purchasedCar4){
                    table.getCells().get(9).setActor(startButton);
                    purchasedCar4 = false;
                    totalScore.setText(Integer.toString(trafficgame.getTotalBalance()));
                }
                if(purchasedCar5){
                    table.getCells().get(9).setActor(startButton);
                    purchasedCar5 = false;
                    totalScore.setText(Integer.toString(trafficgame.getTotalBalance()));
                }
            }
        }, 2, 2, 500);
        updateSelectionTimer.start();

        updateScore = false;
        updateScoreTimer = new Timer();
        updateScoreTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if(updateScore) {
                    totalScore.setText(Integer.toString(trafficgame.getTotalBalance()));
                }
            }
        }, 2, 2, 500);
        updateScoreTimer.start();

        characterID = 1;
        trafficgame.setCharID(1);
        trafficgame.setSpeed(1.5f);


        // Initiate Stage
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        cam.setToOrtho(false, TrafficGame.G_WIDTH, TrafficGame.G_HEIGHT);

        // -------------- //
        // character selection related
        Texture characters = trafficgame.manager.get("images/car1_anim.png");
        TextureRegion carOne = new TextureRegion(characters, 0, 0,80,80);
        characterSelection.add(carOne);

        Texture characters2 = trafficgame.manager.get("images/car2_anim.png");
        TextureRegion carTwo = new TextureRegion(characters2, 0, 0, 80,80);
        characterSelection.add(carTwo);

        Texture characters3 = trafficgame.manager.get("images/car3_anim.png");
        TextureRegion carThree = new TextureRegion(characters3, 0, 0, 80,80);
        characterSelection.add(carThree);


        Texture characters4 = trafficgame.manager.get("images/car4_anim.png");
        TextureRegion carFour = new TextureRegion(characters4, 0, 0, 80,80);
        characterSelection.add(carFour);


        Texture characters5 = trafficgame.manager.get("images/car5_anim.png");
        TextureRegion carFive = new TextureRegion(characters5, 0, 0, 80,80);
        characterSelection.add(carFive);

        ImageButton.ImageButtonStyle characterImageStyle = new ImageButton.ImageButtonStyle();
        characterImageStyle.imageUp = new TextureRegionDrawable(new TextureRegion(characterSelection.get(0))); // place the first item in characterSelection in the placeholder

        characterImage = new ImageButton(characterImageStyle);

        carName = new Label("cookie", trafficgame.getSkin());  // gets edited when shuffling

        Image leftButton = new Image(trafficgame.myTextures().findRegion("leftButton"));
        leftButton.setScaling(Scaling.none);
        startButton = new Button(trafficgame.getSkin());
        Image rightButton = new Image(trafficgame.myTextures().findRegion("rightButton"));
        rightButton.setScaling(Scaling.none);
        // UI related
        Image crystalIcon = new Image(trafficgame.myTextures().findRegion("mainCrystal"));
        crystalIcon.setScaling(Scaling.none);
        Image logo = new Image(trafficgame.myTextures().findRegion("logo"));
        logo.setScaling(Scaling.fit);
        Image plus = new Image(trafficgame.myTextures().findRegion("plus"));
        plus.setScaling(Scaling.none);
        plus.setAlign(Align.left);

        final ImageButton.ImageButtonStyle musicIconStyle = new ImageButton.ImageButtonStyle();

        if(TrafficGame.musicToggle) {
            musicIconStyle.imageUp = new TextureRegionDrawable(new TextureRegion(trafficgame.myTextures().findRegion("sound")));
        } else {
            musicIconStyle.imageUp = new TextureRegionDrawable(new TextureRegion(trafficgame.myTextures().findRegion("soundDisabled")));
        }
        ImageButton musicToggleImage = new ImageButton(musicIconStyle);

        totalScore = new Label(Integer.toString(trafficgame.getTotalBalance()), trafficgame.getSkin(), "menu-text");
        /* TL;DR
        crystalIcon | totalScore | plus         ------              musicToggleImage
                                                 logo
        leftButton                           characterImage            rightButton
                                               startButton
        // -------------- */ //

        table = new Table();
        table.setWidth(stage.getWidth());
        table.setFillParent(true);
        table.top();
        table.left();
        table.padTop(10);

        table.add(crystalIcon).height(75).width(60).padLeft(15f);
        table.add(totalScore).height(60).padTop(10);
        table.add(plus).height(75).width(60).padLeft(10).expandX().left();
        table.add(musicToggleImage).height(75).width(100).right();

        table.row();

        table.add(logo).align(Align.center).colspan(4);

        table.row();

        table.add(leftButton).expandX().right().fill();
            table.getChildren().get(5).setVisible(false);
        table.add(characterImage).padLeft(20).colspan(2).width(90);
        table.add(rightButton).expandX().left().fill();

        table.row();

        table.add(carName).colspan(4);

        table.row();

        table.add(startButton).colspan(4).padTop(50);

        stage.addActor(table);

        tablePurchaseCar2 = new Image(trafficgame.myTextures().findRegion("500"));
        tablePurchaseCar3 = new Image(trafficgame.myTextures().findRegion("500"));
        tablePurchaseCar4 = new Image(trafficgame.myTextures().findRegion("1500"));
        tablePurchaseCar5 = new Image(trafficgame.myTextures().findRegion("2000"));
        checkLastPlayed();

        tablePurchaseCar2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(trafficgame.gameInterface.getTotalCrystals() > 500) {
                    trafficgame.gameInterface.editBalance(trafficgame.gameInterface.getTotalCrystals() - 500);
                    trafficgame.gameInterface.applyOwnership(2);
                    ownershipArr.set(2, "1");


                    Menu.purchasedCar2 = true;
                    updateScore = true;
                } else {
                    trafficgame.gameInterface.ShowDialog();
                }
            }
        });

        tablePurchaseCar3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(trafficgame.gameInterface.getTotalCrystals() > 500) {
                    trafficgame.gameInterface.applyOwnership(3);
                    trafficgame.gameInterface.editBalance(trafficgame.gameInterface.getTotalCrystals() - 500);

                    ownershipArr.set(3, "1");
                    Menu.purchasedCar3 = true;
                    updateScore = true;
                } else {
                    trafficgame.gameInterface.ShowDialog();
                }
            }
        });

        tablePurchaseCar4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(trafficgame.gameInterface.getTotalCrystals() > 1500) {
                    trafficgame.gameInterface.applyOwnership(4);
                    trafficgame.gameInterface.editBalance(trafficgame.gameInterface.getTotalCrystals() - 1500);

                    ownershipArr.set(4, "1");
                    Menu.purchasedCar4 = true;
                   updateScore = true;
                } else {
                    trafficgame.gameInterface.ShowDialog();
                }
            }
        });

        tablePurchaseCar5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(trafficgame.gameInterface.getTotalCrystals() > 2000) {
                    trafficgame.gameInterface.applyOwnership(5);
                    trafficgame.gameInterface.editBalance(trafficgame.gameInterface.getTotalCrystals() - 2000);

                    ownershipArr.set(5, "1");
                    Menu.purchasedCar5 = true;
                    updateScore = true;
                } else {
                    trafficgame.gameInterface.ShowDialog();
                }
            }
        });

        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                trafficgame.gameInterface.setLastPlayed(characterID);
                if(TrafficGame.musicToggle){ TrafficGame.res.getMusic("main").pause(); }
                gsm.setState(GameStateManager.LEVELS);

            }
        });
        leftButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                moveLeft();
            }
        });
        rightButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                moveRight(); }
        });

        table.getChildren().get(0).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               //gsm.setState(GameStateManager.STORE);
                trafficgame.gameInterface.rewardAd();
            }
        });
        table.getChildren().get(1).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gsm.setState(GameStateManager.STORE);
                trafficgame.gameInterface.rewardAd();
            }
        });
        table.getChildren().get(2).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //gsm.setState(GameStateManager.STORE);
                trafficgame.gameInterface.rewardAd();
            }
        });
        table.getChildren().get(3).addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               if (trafficgame.getNormalPrefs().getBoolean("music")){
                    ((TextureRegionDrawable) musicIconStyle.imageUp).setRegion(trafficgame.myTextures().findRegion("soundDisabled"));
                    TrafficGame.musicToggle = false;
                    trafficgame.getNormalPrefs().putBoolean("music", false);
                    trafficgame.getNormalPrefs().flush();
                    TrafficGame.res.getMusic("main").dispose();
                } else {
                   ((TextureRegionDrawable) musicIconStyle.imageUp).setRegion(trafficgame.myTextures().findRegion("sound"));
                    TrafficGame.musicToggle = true;
                    trafficgame.getNormalPrefs().putBoolean("music", true);
                    trafficgame.getNormalPrefs().flush();

                    TrafficGame.res.loadMusic("music/main.mp3");
                    TrafficGame.res.getMusic("main").play();
                }
            }
        });

        Gdx.graphics.setContinuousRendering(false);

        if(TrafficGame.musicToggle) {
            TrafficGame.res.getMusic("main").play();
        }
    }

    private void checkLastPlayed() {
            characterID = trafficgame.gameInterface.getLastPlayed();
            switch (characterID) {
                case 0:
                    characterID = 1;
                    trafficgame.setCharID(1);
                    trafficgame.setSpeed(1.5f);
                    ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(0));
                    carName.setText("Cookie");
                    break;
                case 1:
                    characterID = 1;
                    trafficgame.setCharID(1);
                    trafficgame.setSpeed(1.5f);
                    ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(0));
                    carName.setText("Cookie");
                    break;
                case 2:
                    characterID = 2;
                    trafficgame.setCharID(2);
                    trafficgame.setSpeed(1.5f);
                    ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(1));
                    carName.setText("Smoking hot");
                    table.getChildren().get(0).setVisible(true);
                    table.getChildren().get(2).setVisible(true);
                    break;
                case 3:
                    characterID = 3;
                    trafficgame.setCharID(3);
                    trafficgame.setSpeed(1.5f);
                    ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(2));
                    carName.setText("Ice Baby, Ice");
                    table.getChildren().get(0).setVisible(true);
                    table.getChildren().get(2).setVisible(true);
                    break;
                case 4:
                    characterID = 4;
                    trafficgame.setCharID(4);
                    trafficgame.setSpeed(1.5f);
                    ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(3));
                    carName.setText("Iron Fist");
                    table.getChildren().get(0).setVisible(true);
                    table.getChildren().get(2).setVisible(true);
                    break;
                case 5:
                    characterID = 5;
                    trafficgame.setCharID(5);
                    trafficgame.setSpeed(1.5f);
                    ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(4));
                    carName.setText("Sizzlin' Hot");
                    table.getChildren().get(0).setVisible(true);
                    table.getChildren().get(2).setVisible(false);
                    break;
            }
    }

    private void moveRight(){
        switch (characterID) {
            case 1:
                characterID = 2;
                trafficgame.setCharID(2);
                trafficgame.setSpeed(1.5f);
                ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(1));
                carName.setText("Dinosaur");
                table.getChildren().get(5).setVisible(true);
                table.getCells().get(9).setActor(startButton);
                if (ownershipArr.get(2).matches("0")){
                    table.getCells().get(9).setActor(tablePurchaseCar2);
                }
                break;
            case 2:
                characterID = 3;
                trafficgame.setCharID(3);
                trafficgame.setSpeed(1.5f);
                ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(2));
                carName.setText("Ice Baby, Ice");
                table.getCells().get(9).setActor(startButton);
                if (ownershipArr.get(3).equals("0")){
                    table.getCells().get(9).setActor(tablePurchaseCar3);
                }
                break;
            case 3:
                characterID = 4;
                trafficgame.setCharID(4);
                trafficgame.setSpeed(1.5f);
                ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(3));
                carName.setText("Iron Fist");
                table.getCells().get(9).setActor(startButton);
                if (ownershipArr.get(4).equals("0")){
                    table.getCells().get(9).setActor(tablePurchaseCar4);
                }
                break;
            case 4:
                characterID = 5;
                trafficgame.setCharID(5);
                trafficgame.setSpeed(1.5f);
                ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(4));
                carName.setText("Sizzlin' Hot");
                table.getChildren().get(7).setVisible(false);
                table.getChildren().get(5).setVisible(true);
                table.getCells().get(9).setActor(startButton);
                if (ownershipArr.get(5).equals("0")){
                    table.getCells().get(9).setActor(tablePurchaseCar5);
                }
                break;
            case 5: break;
        }
    }
    private void moveLeft(){
        switch (characterID) {
            case 1: break;
            case 2: characterID = 1;
                trafficgame.setCharID(1);
                trafficgame.setSpeed(1.5f);
                ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(0));
                carName.setText("Cookie");
                table.getChildren().get(7).setVisible(true);
                table.getChildren().get(5).setVisible(false);
                table.getCells().get(9).setActor(startButton);
                break;
            case 3:
                characterID = 2;
                trafficgame.setCharID(2);
                trafficgame.setSpeed(1.5f);
                ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(1));
                carName.setText("Dinosaur");
                table.getCells().get(9).setActor(startButton);
                if (ownershipArr.get(2).equals("0")){
                    table.getCells().get(9).setActor(tablePurchaseCar3);
                }
                break;
            case 4:
                characterID = 3;
                trafficgame.setCharID(3);
                trafficgame.setSpeed(1.5f);
                ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(2));
                carName.setText("Ice Baby, Ice");
                table.getCells().get(9).setActor(startButton);
                if (ownershipArr.get(3).equals("0")){
                    table.getCells().get(9).setActor(tablePurchaseCar3);
                }
                break;
            case 5:
                characterID = 4;
                trafficgame.setCharID(4);
                trafficgame.setSpeed(1.5f);
                ((TextureRegionDrawable) characterImage.getStyle().imageUp).setRegion(characterSelection.get(3));
                carName.setText("Iron Fist");
                table.getChildren().get(7).setVisible(true);
                table.getChildren().get(5).setVisible(true);
                table.getCells().get(9).setActor(startButton);
                if (ownershipArr.get(4).equals("0")){
                    table.getCells().get(9).setActor(tablePurchaseCar4);
                }
                break;
        }
    }

    @Override
    public void handleInput() { }

    @Override
    public void update(float dt) { handleInput(); }

    @Override
    public void render() {
        Gdx.gl.glClearColor(251 / 255f, 238 / 255f, 227 / 255f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        sb.setProjectionMatrix(cam.combined);
        sb.begin();
            sb.draw(trafficgame.myTextures().findRegion("background"), 0, 0, TrafficGame.G_WIDTH, TrafficGame.G_HEIGHT);
        sb.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        updateScoreTimer.stop();
        updateSelectionTimer.stop();
        table.clear();
        stage.dispose();
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }
    }


