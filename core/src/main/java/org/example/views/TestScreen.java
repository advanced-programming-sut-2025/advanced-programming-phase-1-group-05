package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.GameManager;
import org.example.controllers.GameMenuController;
import org.example.models.*;
import org.example.models.Enums.AnimalType;
import org.example.models.Enums.Season;

import java.util.ArrayList;

public class TestScreen implements Screen {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private SpriteBatch batch;
    Stage stage = new Stage();

    public TestScreen() {
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        NpcActor actor = new NpcActor(MyGame.getNPCByName("Leah"));

        showNpcDialogue(actor);
    }

    @Override
    public void render(float delta) {
        stateTime += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void showNpcDialogue(NpcActor npcActor) {
        Texture dialogueBoxTexture = GameAssetManager.getInstance().getOrLoadTexture("NPCs/dialogueTemplate.png");
        dialogueBoxTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        NinePatch ninePatch = new NinePatch(dialogueBoxTexture, 16, 16, 16, 16);
        NinePatchDrawable dialogueBackground = new NinePatchDrawable(ninePatch);

        Skin skin = GameAssetManager.getSkin();
        String message = npcActor.getMessage();
        if (message == null) return;
        Table dialogueTable = new Table();
        dialogueTable.setFillParent(true);
        dialogueTable.pad(20);
        dialogueTable.setBackground(dialogueBackground);
        stage.addActor(dialogueTable);
        Label dialogueText = new Label(message, skin);
        dialogueText.setWrap(true);
        dialogueText.setFontScale(2f);
        dialogueText.setColor(86f / 225f, 22f / 225f, 12f / 225f, 1);
        dialogueText.setWidth(8000);

        String NpcName = npcActor.getNpc().getName();
        Texture tex = GameAssetManager.getInstance().getOrLoadTexture("NPCs/" + NpcName + "/avatar.png");
        TextureRegionDrawable avatarDrawable = new TextureRegionDrawable(new TextureRegion(tex));
        Image npcAvatar = new Image(avatarDrawable);


        Label npcNameLabel = new Label(NpcName, skin);
        npcNameLabel.setFontScale(2f);
        npcNameLabel.setColor(86f / 225f, 22f / 225f, 12f / 225f, 1);
        VerticalGroup rightGroup = new VerticalGroup();
        rightGroup.space(100); // space between avatar and name label
        npcAvatar.setScaling(Scaling.stretch); // or Scaling.fit if you want aspect ratio

        Container<Image> avatarContainer = new Container<>(npcAvatar);
        avatarContainer.size(400, 400); // adjust to desired size
        avatarContainer.fill(); // make it fill the container

        rightGroup.addActor(avatarContainer);
        rightGroup.addActor(npcNameLabel);
        rightGroup.center();


        dialogueTable.add(dialogueText).expand().left().width(1500).padLeft(200);
        dialogueTable.add(rightGroup).padTop(770).padRight(100).width(1000).height(1000);
    }
}
