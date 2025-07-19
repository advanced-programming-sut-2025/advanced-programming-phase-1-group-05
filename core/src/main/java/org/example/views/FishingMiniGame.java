package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.models.*;
import org.example.models.Enums.FishType;
import org.example.models.Enums.FishingPoleType;
import org.example.models.Enums.ItemLevel;
import org.example.models.Tool.FishingPole;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class FishingMiniGame implements Screen {
    private ProgressMeter progressMeter;
    private Texture backgroundTexture;
    private boolean fishCaught = false;
    private Stage stage;
    Skin skin = GameAssetManager.getSkin();
    private FishingPole pole;
    private  FishType fishType;
    public FishingMiniGame(GameScreen game, FishingPole pole, FishType type) {
        this.pole = pole;
        fishType = type;
    }
     @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("ui/FishingBG.png");
        progressMeter = new ProgressMeter();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);

        if (progressMeter.isComplete()&& !fishCaught) {
            fishCaught  = true;
            String fishName = fishType.getName();

            Dialog caughtDialog = new Dialog("Fish Caught!", skin);

            ItemLevel level = getLevel();

            Image fishImage = new Image(fishType.getTexture());
            caughtDialog.getContentTable().add(fishImage).pad(10).row();

            caughtDialog.text("You caught a " + level.toString() + " " + fishName + "!");
            caughtDialog.button("OK");
            caughtDialog.show(stage);
            Product product = new Product(fishName, fishType.getPrice(), -1, null, List.of(), Map.of(), "fish");
            product.setItemLevel(level);
            MyGame.getCurrentPlayer().getBackPack().addToInventory(product, 1);

            MyGame.getCurrentPlayer().getFishingSkill().increaseCapacity();

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    caughtDialog.hide();
                }
            }, 3);
        }



        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();

    }

    private ItemLevel getLevel() {
        Player player = MyGame.getCurrentPlayer();
        int fishingLevel = player.getFishingSkill().getLevel();
        Random rand = new Random();
        double weatherCoefficient = MyGame.getCurrentWeather().getFishingCoefficient();
        int numOfFish = Math.min((int) (rand.nextDouble() * weatherCoefficient * (fishingLevel + 2)), 6);
        int qualityScore = (int) ((rand.nextDouble() * (fishingLevel + 2) * pole.getLevel().getFishingCoefficient()) / (7 - weatherCoefficient));
        ItemLevel level;
        if (qualityScore <= 0.5) level = ItemLevel.Normal;
        else if (qualityScore <= 0.7) level = ItemLevel.Brass;
        else if (qualityScore <= 0.9) level = ItemLevel.Gold;
        else level = ItemLevel.Iridium;
        return level;
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
}
