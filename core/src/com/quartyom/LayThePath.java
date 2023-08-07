package com.quartyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;
import com.quartyom.game_elements.DrawingQueue;
import com.quartyom.game_elements.FontHolder;
import com.quartyom.game_elements.Locale;
import com.quartyom.game_elements.QuGame;
import com.quartyom.game_elements.SoundHolder;
import com.quartyom.game_elements.Vibrator;
import com.quartyom.screens.ColorsTest.ColorsScreen;
import com.quartyom.screens.EasterColorsGame.EasterScreen;
import com.quartyom.screens.Editor.EditorScreen;
import com.quartyom.screens.Level.LevelScreen;
import com.quartyom.screens.Menu.MenuTab;
import com.quartyom.screens.Zen.ZenScreen;

import java.util.Random;

public class LayThePath extends QuGame {
    public SpriteBatch batch;
    public FontHolder fontHolder;
    public GlyphLayout glyphLayout;
    public SoundHolder soundHolder;
    public TextureAtlas fieldAtlas, buttonsAtlas, slidersAtlas;
    public Json json;
    public Locale locale;
    public Random random;

    public UserData userData;
    public Vibrator vibrator;

    public DrawingQueue drawingQueue;

    public boolean isBackButtonPressed;
    public boolean isFirstLaunch;
    private boolean isRequestedToSaveUserData;

    public void create() {
        batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();
        soundHolder = new SoundHolder();
        fieldAtlas = new TextureAtlas("textures/field.atlas");
        buttonsAtlas = new TextureAtlas("textures/buttons.atlas");
        slidersAtlas = new TextureAtlas("textures/sliders.atlas");

        json = new Json();
        loadUserData();
        locale = new Locale(this);
        locale.set(userData.locale);
        fontHolder = new FontHolder(this, "fonts/OpenSans-Light.ttf");

        random = new Random();

        vibrator = new Vibrator(this);
        drawingQueue = new DrawingQueue();

        addScreens();
        this.setScreen(isFirstLaunch ? "menu_locale" : "menu");

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setInputProcessor(this);
    }

    public void changeLocale(String language) {
        locale.set(language);
        fontHolder.updateLocale();

        super.dispose();    // чтобы корректно выгрузить прошлые Screen
        addScreens();
    }

    private void addScreens(){
        this.addDefault("menu", new MenuTab(this));
        this.add("level", new LevelScreen(this));
        this.add("zen", new ZenScreen(this));
        this.add("editor", new EditorScreen(this));
        this.add("colors_level", new ColorsScreen(this));
        this.add("easter_colors", new EasterScreen(this));
    }

    public void saveUserData() {
        isRequestedToSaveUserData = true;
    }

    public void saveUserDataImmediately() {
        isRequestedToSaveUserData = false;
        Gdx.files.local("user_data.json").writeString(json.prettyPrint(userData), false);
    }

    public void loadUserData() {
        String a = "";
        try {
            a = Gdx.files.local("user_data.json").readString();
        } catch (Exception exception) {
            isFirstLaunch = true;
            a = Gdx.files.internal("default_user_data.json").readString();
        } finally {
            userData = json.fromJson(UserData.class, a);
            userData.launches++;
            saveUserData();
        }
    }

    @Override
    public void render() {
        batch.begin();
        super.render(); // important!
        drawingQueue.draw();
        batch.end();

        if (isRequestedToSaveUserData) {
            saveUserDataImmediately();
        }

        if (isBackButtonPressed) {
            setScreen(null);    // установит Screen по умолчанию
        }
    }

    @Override
    public void dispose() {
        super.dispose(); // important!

        batch.dispose();
        fontHolder.dispose();
        soundHolder.dispose();

        fieldAtlas.dispose();
        buttonsAtlas.dispose();
        slidersAtlas.dispose();
    }

    public int HOW_MANY_BUTTONS = 6;
    public float padding = 0.8f;

    public float downMargin, buttonActualSizeX, buttonActualSizeY, upperButtonCenterX = 0, upperButtonCenterY;
    public float upperButtonCornerX, upperButtonCornerY, buttonW, buttonH;

    @Override
    public void resize(int width, int height) {
        WIDTH = width;
        HALF_WIDTH = WIDTH / 2;
        HEIGHT = height;
        HALF_HEIGHT = HEIGHT / 2;

        batch.getProjectionMatrix().setToOrtho2D(-HALF_WIDTH, -HALF_HEIGHT, WIDTH, HEIGHT);

        downMargin = HEIGHT / HOW_MANY_BUTTONS;

        buttonActualSizeX = HALF_WIDTH * padding;
        buttonActualSizeY = downMargin / 2 * padding;

        upperButtonCenterY = HALF_HEIGHT - downMargin / 2;

        upperButtonCornerX = upperButtonCenterX - buttonActualSizeX;
        upperButtonCornerY = upperButtonCenterY - buttonActualSizeY;

        buttonW = buttonActualSizeX * 2;
        buttonH = buttonActualSizeY * 2;

        super.resize(width, height);
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.BACK) {
            isBackButtonPressed = true;
            return true;
        }
        return false;
    }

}
