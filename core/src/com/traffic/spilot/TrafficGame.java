package com.traffic.spilot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.traffic.spilot.entities.TrafficSelection;
import com.traffic.spilot.handlers.BoundedCamera;
import com.traffic.spilot.handlers.Content;
import com.traffic.spilot.handlers.GameStateManager;
import com.traffic.spilot.handlers.TGInput;
import com.traffic.spilot.handlers.TGInputProcessor;
import com.traffic.spilot.states.Menu;
import com.traffic.spilot.states.Play;

public class TrafficGame extends ApplicationAdapter {

	public static final String G_TITLE = "Soggy Car - Version 13";
	public static final int G_WIDTH = 720;
	public static final int G_HEIGHT = 1280;
	public static final float STEP = 1 / 60f;

	public final gameInterface gameInterface;

	private SpriteBatch sb;
	private TrafficSelection trafficSelection;
	private BoundedCamera cam;
	private OrthographicCamera hudCam;

	private GameStateManager gsm;
	public static Content res;

	private Skin skin;
	private TextureAtlas resourceAtlas;

	public static AssetManager manager;

    public static boolean musicToggle;

    public static Preferences prefs;

	public static boolean adsEnabled;

	public TrafficGame(gameInterface gameInterface) {
		this.gameInterface = gameInterface;
	}

	@Override
	public void create() {
		Gdx.input.setInputProcessor(new TGInputProcessor());

		// load selection
		trafficSelection = new TrafficSelection();

        // load basic prefs
        prefs = Gdx.app.getPreferences("GameData");

        if (prefs.get().isEmpty()) {
			prefs.putBoolean("played", false);
			prefs.putBoolean("music", true);
			prefs.putBoolean("Level1", true);
			prefs.putBoolean("Level2", false);
			prefs.putBoolean("Level3", false);
			prefs.putBoolean("Level4", false);
			prefs.putBoolean("Level5", false);
			prefs.putBoolean("Level6", false);
			musicToggle = true;
			prefs.flush();
		}
        musicToggle = prefs.getBoolean("music");

		res = new Content();
        res.loadSound("sfx/crystal.wav");
        res.loadMusic("music/main.mp3");
		res.getMusic("main").setLooping(true);

		ResolutionFileResolver.Resolution _320x480 = new ResolutionFileResolver.Resolution(320, 480, "320x480");
		ResolutionFileResolver.Resolution _480x800 = new ResolutionFileResolver.Resolution(480, 800, "480x800");
		ResolutionFileResolver.Resolution _720x1280 = new ResolutionFileResolver.Resolution(700, 1000, "720x1280");
		ResolutionFileResolver resolver = new ResolutionFileResolver(new InternalFileHandleResolver(), _320x480, _480x800, _720x1280);


		manager = new AssetManager();
			manager.setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
            manager.load("buttons.atlas", TextureAtlas.class);
			manager.load("za7mat_assets.pack", TextureAtlas.class);
			manager.load("za7mat_assets_ar.pack", TextureAtlas.class);
            manager.load("music/main.mp3", Music.class);
            manager.load("images/car1_anim.png", Texture.class);
            manager.load("images/car2_anim.png", Texture.class);
            manager.load("images/car3_anim.png", Texture.class);
            manager.load("images/car4_anim.png", Texture.class);
            manager.load("images/car5_anim.png", Texture.class);
            manager.load("images/crystal.png", Texture.class);
            manager.load("images/tutorial.png", Texture.class);
		manager.finishLoading();


		if(java.util.Locale.getDefault().getLanguage().equals("ar")) {
			resourceAtlas = manager.get("za7mat_assets_ar.pack", TextureAtlas.class);
			manager.unload("za7mat_assets.pack");
		} else {
			resourceAtlas = manager.get("za7mat_assets.pack", TextureAtlas.class);
			manager.unload("za7mat_assets_ar.pack");
		}

		TextureAtlas atlas = manager.get("buttons.atlas", TextureAtlas.class);
		skin = new Skin(Gdx.files.internal("skin.json"), atlas);

		cam = new BoundedCamera();
		cam.setToOrtho(false, G_WIDTH, G_HEIGHT);

		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, G_WIDTH, G_HEIGHT);

		sb = new SpriteBatch();

		gsm = new GameStateManager(this);
	}

	@Override
	public void render() {
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
		TGInput.update();
	}

	@Override
	public void pause() {
		Gdx.graphics.setContinuousRendering(false);
		gameInterface.closeConnection();
	}
	@Override
	public void dispose() {
        gameInterface.closeConnection();
        manager.dispose();
		resourceAtlas.dispose();
		skin.dispose();
		res.removeAll();
		Gdx.app.exit();
	}

	public void resize(int w, int h) {
	}

	public void resume() {
		Gdx.graphics.setContinuousRendering(true);
		Gdx.graphics.requestRendering();
		if(Play.world == null) {
			System.out.println("world is null");
		}
		if(Play.world != null) {
			System.out.println("world not null");
		}
		gsm.setState(GameStateManager.MENU);
	}

	public SpriteBatch getSb() {return sb; }
	public BoundedCamera getCam() {
		return cam;
	}
	public OrthographicCamera getHUDCamera() { return hudCam; }

	public void setCharID(int x) {
		trafficSelection.setCharacterID(x);
	}
	public void setSpeed(float x) {
		trafficSelection.setSpeed(x);
	}
	public int getCharID() {
		return trafficSelection.getCharacterID();
	}
	public float getSpeed() {
		return trafficSelection.getSpeed();
	}

	public Skin getSkin() {
		return skin;
	}

	public TextureAtlas myTextures() {
		return resourceAtlas;
	}

    public int getTotalBalance() { return gameInterface.getTotalCrystals(); }

    public Preferences getNormalPrefs() {
        return prefs;
    }
}