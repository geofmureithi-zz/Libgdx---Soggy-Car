package com.traffic.spilot.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.traffic.spilot.TrafficGame;
import com.traffic.spilot.entities.Crystal;
import com.traffic.spilot.entities.HUD;
import com.traffic.spilot.entities.Player;
import com.traffic.spilot.handlers.B2DVars;
import com.traffic.spilot.handlers.BoundedCamera;
import com.traffic.spilot.handlers.Content;
import com.traffic.spilot.handlers.GameStateManager;
import com.traffic.spilot.handlers.TGContactListener;

import java.util.Iterator;

public class Play_5 extends GameState {

    private boolean debug = false;

    public static World world;
    // private Box2DDebugRenderer b2dRenderer;
    private BoundedCamera b2dCam;

    private TGContactListener cl;

    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmRenderer;

    private Content res;

    private Player player;
    private boolean tutorial;


    private HUD hud;

    private Array<Crystal> crystals;
    private Timer spawnCrystalTimer;


    public Play_5(GameStateManager gsm) {
        super(gsm);

        if(TrafficGame.musicToggle) {
            res = new Content();
            res.loadMusic("music/game.mp3");
            res.getMusic("game").setLooping(true);
            res.getMusic("game").setVolume(0.5f);
            res.getMusic("game").play();
        }

        if(TrafficGame.adsEnabled) {
            Timer loadAdAfter = new Timer();
            loadAdAfter.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    if(trafficgame.gameInterface.isAdEmpty()) {
                        trafficgame.gameInterface.reloadAd();
                        trafficgame.gameInterface.showAd();
                    }
                }
            },2);
        }

        // set up the box2d world and contact listener
        world = new World(new Vector2(0, 0f), true);
        cl = new TGContactListener();
        world.setContactListener(cl);
        Gdx.input.setInputProcessor(null);

        //b2dRenderer = new Box2DDebugRenderer();

        // create player
        createPlayer();

        tutorial = true;
        Timer turnoffTutorial = new Timer();
        turnoffTutorial.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                tutorial = false;
            }
        }, 4);

        // create walls
        createWalls();
        cam.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);

        crystals = new Array<Crystal>();
        createCrystals();
        spawnCrystalTimer = new Timer();
        spawnCrystalTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                createCrystals();
            }
        }, 3, 6, 999);

        hud = new HUD(player, trafficgame.getSkin());

//      set up box2d cam
        b2dCam = new BoundedCamera();
        b2dCam.setToOrtho(false, TrafficGame.G_WIDTH / B2DVars.PPM, TrafficGame.G_HEIGHT / B2DVars.PPM);
        b2dCam.setBounds(0, (tileMapWidth * tileSize) / B2DVars.PPM, 0, (tileMapHeight * tileSize) / B2DVars.PPM);

        Gdx.graphics.setContinuousRendering(true);
    }

    private void playerLeft() {
        player.getBody().applyForceToCenter(-15,0,true);
    }
    private void playerRight() {
        player.getBody().applyForceToCenter(15, 0, true);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2) {
                playerLeft();
            } else {
                playerRight();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(TrafficGame.STEP, 1, 1);

        // update timer
        hud.playTime += dt;

        // check for collected crystals
        Array<Body> bodies = cl.getBodies();
        for(int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            crystals.removeValue((Crystal) b.getUserData(), true);
            world.destroyBody(bodies.get(i));
            player.collectCrystal();
            if(TrafficGame.musicToggle) {
                TrafficGame.res.getSound("crystal").play(0.1f);
            }
        }
        bodies.clear();

        player.update(dt);

        // player won
        if(player.getBody().getPosition().y * B2DVars.PPM > tileMapHeight * tileSize - 500) {
            Gameover.finalScoreCrystals =  player.getNumCrystals();
            Gameover.finalTime = (int) Math.ceil(hud.playTime);
            trafficgame.getNormalPrefs().putBoolean("Level6", true);
            trafficgame.getNormalPrefs().flush();
            Gameover.nextLevel = 6;
            gsm.setState(GameStateManager.GAMEOVER);
        }

        // player failed
        if(cl.isPlayerDead()) {
            Gameover.finalScoreCrystals =  player.getNumCrystals();
            Gameover.finalTime = (int) Math.ceil(hud.playTime);
            Gameover.nextLevel = 5;
            gsm.setState(GameStateManager.GAMEOVER);
        }

        // update crystals
        for(int i = 0; i < crystals.size; i++) {
            crystals.get(i).update(dt);
        }


        Iterator<Crystal> iter = crystals.iterator();
        while(iter.hasNext()) {
            Crystal crystal = iter.next();
            if(crystal.getPosition().y < player.getPosition().y - 4) {
                iter.remove();
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(226 / 255f, 189 / 255f, 157 / 255f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // camera follow player
        cam.setPosition(400, player.getPosition().y * B2DVars.PPM + TrafficGame.G_HEIGHT / 4);
        cam.update();

        // draw tilemap
        tmRenderer.setView(cam);
        int [] backgroundLayers = {0, 1, 2, 3};
        int [] topLayers = { 4 };
        tmRenderer.render(backgroundLayers);

        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        tmRenderer.render(topLayers);

        // draw crystals
        for(int i = 0; i < crystals.size; i++) {
            crystals.get(i).render(sb);
        }

        sb.setProjectionMatrix(hudCam.combined);
        hud.render();

        sb.begin();
        sb.draw(trafficgame.myTextures().findRegion("gradientBG"), 0, 0, TrafficGame.G_WIDTH, TrafficGame.G_HEIGHT / 2);

        if (tutorial) {
            sb.draw(trafficgame.manager.get("images/tutorial.png", Texture.class), 0, Gdx.graphics.getHeight() / 2, TrafficGame.G_WIDTH, 409);
        }
        sb.end();


        if(debug) {
            // adding 0.5f to the x fixed it
            b2dCam.setPosition(player.getPosition().x + 0.4f, player.getPosition().y + (TrafficGame.G_HEIGHT/ 4) / B2DVars.PPM); // ERROR: ITS NOT FULLY SYNCED WITH RENDER but it works.
            b2dCam.update();
            //b2dRenderer.render(world, b2dCam.combined);
        }
    }

    @Override
    public void resize(int w, int h) {

    }

    @Override
    public void dispose() {
        if(TrafficGame.musicToggle) {
            res.getMusic("game").dispose();
        }
        crystals.clear(); // clearing fixed the error problematic frame w/ error "AL lib: (EE) alc_cleanup: 1 device not closed"
        spawnCrystalTimer.stop();
        tmRenderer.dispose();
        tileMap.dispose();
        hud.hudAtlas.dispose();
        hud.stage.dispose();
        hud.hudTimer.stop();
        world.dispose();
        world = null;
    }

    private void createPlayer() {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(TrafficGame.G_WIDTH / 2 / B2DVars.PPM, TrafficGame.G_HEIGHT / 4 / B2DVars.PPM);
        bdef.fixedRotation = true;

        bdef.linearVelocity.set(0f, trafficgame.getSpeed());

        // create body from bodydef
        Body body = world.createBody(bdef);

        // create box shape for player collision box
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(20 / B2DVars.PPM, 30 / B2DVars.PPM);

        // create fixturedef for player collision box
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1;
        fdef.friction = 0;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_WALL_BLOCK | B2DVars.BIT_CRYSTAL | B2DVars.BIT_SPIKE | B2DVars.BIT_CARS_BLOCK;

        // create player collision box fixture
        body.createFixture(fdef);
        shape.dispose();

        // create new player
        player = new Player(body, trafficgame.getCharID());
        body.setUserData(player);

        // final tweaks, manually set the player body mass to 1 kg
        MassData md = body.getMassData();
        md.mass = 1;
        body.setMassData(md);

        // i need a ratio of 0.005
        // so at 1kg, i need 200 N jump force
    }

    /**
     * Sets up the tile map collidable tiles.
     * Reads in tile map layers and sets up box2d bodies.
     */
    private void createWalls() {
        // load tile map and map renderer
        try {
            tileMap = new TmxMapLoader().load("maps/enhanced_5.tmx");
        }
        catch(Exception e) {
            System.out.println("Cannot find file: maps/enhanced.tmx");
            Gdx.app.exit();
        }

        tileMapWidth = tileMap.getProperties().get("width", Integer.class);
        tileMapHeight = tileMap.getProperties().get("height", Integer.class);
        tileSize = tileMap.getProperties().get("tileheight", Integer.class);

        tmRenderer = new OrthogonalTiledMapRenderer(tileMap, 1f);

        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("road");
        createBlocks(layer, B2DVars.BIT_ROAD_BLOCK);
        layer = (TiledMapTileLayer) tileMap.getLayers().get("wall");
        createBlocks(layer, B2DVars.BIT_WALL_BLOCK);
        layer = (TiledMapTileLayer) tileMap.getLayers().get("cars");
        createCars(layer, B2DVars.BIT_CARS_BLOCK);
    }

    private void createCars(TiledMapTileLayer layer, short bits) {
        float ts = layer.getTileWidth();

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col=0; col < layer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                // check that there is a cell
                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * ts / B2DVars.PPM, (row + 0.5f) * ts / B2DVars.PPM);
                // body.createFixture(cfdef).setUserData("crystal");

                //bdef.linearVelocity.set(0f,-0.6f);

                // box for cars
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(20 / B2DVars.PPM, 35 / B2DVars.PPM);

                // create fixture
                FixtureDef carsFx = new FixtureDef();

                carsFx.shape = shape;
                carsFx.filter.categoryBits = bits;
                carsFx.filter.maskBits = B2DVars.BIT_PLAYER;

                world.createBody(bdef).createFixture(carsFx).setUserData("cars");
                shape.dispose();
            }
        }

    }

    private void createBlocks(TiledMapTileLayer layer, short bits) {

        // tile size
        float ts = layer.getTileWidth();

        // go through all cells in layer
        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * ts / B2DVars.PPM, (row + 0.5f) * ts / B2DVars.PPM);
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
                v[1] = new Vector2(-ts / 2 / B2DVars.PPM, ts / 2 / B2DVars.PPM);
                v[2] = new Vector2(ts / 2 / B2DVars.PPM, ts / 2 / B2DVars.PPM);
                v[3] = new Vector2(ts / 2 / B2DVars.PPM, -ts / 2 / B2DVars.PPM);
                cs.createChain(v);
                FixtureDef fd = new FixtureDef();
                fd.friction = 0;

                fd.shape = cs;
                fd.filter.categoryBits = bits;
                fd.filter.maskBits = B2DVars.BIT_PLAYER;
                world.createBody(bdef).createFixture(fd);
                cs.dispose();
            }
        }
    }

    /**
     * Set up box2d bodies for crystals in tile map "crystals" layer
     */
    private void createCrystals() {
        BodyDef cdef = new BodyDef();
        cdef.type = BodyDef.BodyType.StaticBody;
        float x = MathUtils.random(2.4f, 6.3f); // map width - starts at 2.4 - ends at 6.3
        float y = (cam.position.y / B2DVars.PPM) + 7;
        cdef.position.set(x, y);

        Body body = world.createBody(cdef);
        FixtureDef cfdef = new FixtureDef();

        CircleShape cshape = new CircleShape();
        cshape.setRadius(20 / B2DVars.PPM);
        cshape.setPosition(new Vector2(-20 / B2DVars.PPM,0)); // Vector2 was originally 8 but I changed it to 1 & nothing changed.
        cfdef.shape = cshape;
        cfdef.isSensor = true;
        cfdef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
        cfdef.filter.maskBits = B2DVars.BIT_PLAYER;

        body.createFixture(cfdef).setUserData("crystal");
        Crystal c = new Crystal(body);
        body.setUserData(c);
        crystals.add(c);
        cshape.dispose();
    }
}