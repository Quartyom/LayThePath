package com.quartyom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;
import com.quartyom.game_elements.DrawingQueue;
import com.quartyom.game_elements.FontHolder;
import com.quartyom.game_elements.Locale;
import com.quartyom.game_elements.QuGame;
import com.quartyom.game_elements.Vibrator;
import com.quartyom.game_elements.SoundHolder;
import com.quartyom.screens.Editor.EditorScreen;
import com.quartyom.screens.Level.LevelScreen;
import com.quartyom.screens.Menu.MenuTab;
import com.quartyom.screens.Zen.ZenScreen;

public class MakeTheWay extends QuGame implements InputProcessor {
	public SpriteBatch batch;
	public FontHolder fontHolder;
	public GlyphLayout glyphLayout;
	public SoundHolder soundHolder;
	//public TextureHolder textureHolder, buttonTextureHolder;
	public TextureAtlas field_atlas, buttons_atlas, sliders_atlas;
	public Json json;
	public Locale locale;

	public UserData userData;
	public Vibrator vibrator;

	// размеры экрана
	public float WIDTH, HALF_WIDTH, HEIGHT, HALF_HEIGHT;

	public DrawingQueue drawingQueue;

	public boolean is_back_button_pressed;

	public MakeTheWay(){}

	public void create() {
		batch = new SpriteBatch();
		glyphLayout = new GlyphLayout();
		soundHolder = new SoundHolder();
		//textureHolder = new TextureHolder("textures/", ".png");
		field_atlas = new TextureAtlas("textures/field.atlas");
		buttons_atlas = new TextureAtlas("textures/buttons.atlas");
		sliders_atlas = new TextureAtlas("textures/sliders.atlas");
		//buttonTextureHolder = new TextureHolder();

		json = new Json();

		load_user_data();

		locale = new Locale(this);
		locale.set(userData.locale);

		fontHolder = new FontHolder("fonts/OpenSans-Light.ttf", userData.locale);

		vibrator = new Vibrator(userData);

		drawingQueue = new DrawingQueue(this);

		this.add_default("menu", new MenuTab(this));
		//this.add("hex", new HexTextScreen(this));
		this.add("level", new LevelScreen(this));
		this.add("zen", new ZenScreen(this));
		this.add("editor", new EditorScreen(this));
		this.setScreen("menu");

		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		Gdx.input.setInputProcessor(this);
	}

	public void set_locale(String language){
		userData.locale = language;
		locale.set(language);
		save_user_data();

		if (fontHolder != null){fontHolder.dispose();}
		fontHolder = new FontHolder("fonts/OpenSans-Light.ttf", language);

		this.add_default("menu", new MenuTab(this));
		//this.add("hex", new HexTextScreen(this));
		this.add("level", new LevelScreen(this));
		this.add("zen", new ZenScreen(this));
		this.add("editor", new EditorScreen(this));
		this.setScreen("menu");
	}

	public void save_user_data(){
		Gdx.files.local("user_data.json").writeString(json.prettyPrint(userData), false);
	}

	public void load_user_data(){
		String a = new String();
		try {
			a = Gdx.files.local("user_data.json").readString();
		}
		catch (Exception exception){
			a = Gdx.files.internal("default_user_data.json").readString();
		}
		finally {
			userData = json.fromJson(UserData.class, a);
			userData.launches++;
			save_user_data();
		}
	}

	public void render() {
		super.render(); // important!
		drawingQueue.draw();

		if (is_back_button_pressed){
			setScreen(null);
		}
	}

	public void dispose() {
		super.dispose(); // important!

		batch.dispose();
		fontHolder.dispose();
		soundHolder.dispose();

		field_atlas.dispose();
		buttons_atlas.dispose();
		sliders_atlas.dispose();
	}

	public int HOW_MANY_BUTTONS = 6;
	public float padding = 0.8f;

	public float down_margin, button_actual_size_x, button_actual_size_y, upper_button_center_x = 0, upper_button_center_y;
	public float upper_button_corner_x, upper_button_corner_y, button_w, button_h;

	@Override
	public void resize (int width, int height) {
		WIDTH = width;
		HALF_WIDTH = WIDTH / 2;
		HEIGHT = height;
		HALF_HEIGHT = HEIGHT / 2;

		batch.getProjectionMatrix().setToOrtho2D(-HALF_WIDTH, -HALF_HEIGHT, WIDTH, HEIGHT);

		down_margin = HEIGHT / HOW_MANY_BUTTONS;

		button_actual_size_x = HALF_WIDTH * padding;
		button_actual_size_y = down_margin / 2 * padding;

		//upper_button_center_x = 0;
		upper_button_center_y = HALF_HEIGHT - down_margin / 2;

		upper_button_corner_x = upper_button_center_x - button_actual_size_x;
		upper_button_corner_y = upper_button_center_y - button_actual_size_y;

		button_w = button_actual_size_x * 2;
		button_h = button_actual_size_y * 2;

		super.resize(width, height);
	}

	@Override
	public boolean keyDown(int i) {
		if (i == Input.Keys.BACK){
			is_back_button_pressed = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int i) {
		return false;
	}

	@Override
	public boolean keyTyped(char c) {
		return false;
	}

	@Override
	public boolean touchDown(int i, int i1, int i2, int i3) {
		return false;
	}

	@Override
	public boolean touchUp(int i, int i1, int i2, int i3) {
		return false;
	}

	@Override
	public boolean touchDragged(int i, int i1, int i2) {
		return false;
	}

	@Override
	public boolean mouseMoved(int i, int i1) {
		return false;
	}

	@Override
	public boolean scrolled(float v, float v1) {
		return false;
	}

}
