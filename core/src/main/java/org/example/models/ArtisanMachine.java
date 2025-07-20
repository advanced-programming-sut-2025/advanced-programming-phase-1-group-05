package org.example.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.models.Enums.ArtisanType;

import java.util.List;

public class ArtisanMachine extends Actor {
    private TextureRegion textureNormal;
    private TextureRegion textureReady;
    ArtisanType type;
    private boolean ready = false, working = false;
    private float processingTime;
    private float elapsedTime;
    private ArtisanProduct product;

    public ArtisanMachine(ArtisanType type) {
        this.type = type;
        this.textureNormal = new TextureRegion(new Texture("ArtisanMachines/" + type.name().toLowerCase() + ".png"));
        this.textureReady = new TextureRegion(new Texture("ArtisanMachines/" + type.name().toLowerCase() + "_ready.png"));
        // TODO set the position
        setSize(textureNormal.getRegionWidth(), textureNormal.getRegionHeight());
    }

    public Result insertItem(List<String> items) {
        type.useArtisan(items, this);
        if (product == null)
            return Result.error("invalid items!");

        this.processingTime = product.getProcessingTime();
        this.elapsedTime = 0;
        this.ready = false;
        working = true;


        return new Result(true, product.getName() + " will be ready in " + processingTime + " hours!");
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (product == null || ready) return;
        elapsedTime += delta;
        if (elapsedTime >= processingTime) {
            ready = true;
        }
    }

    public ArtisanProduct getProduct() {
        if (!ready) return null;
        ArtisanProduct artisanProduct = product;
        reset();
        return artisanProduct;
    }

    private void reset() {
        product = null;
        elapsedTime = 0;
        processingTime = 0;
        ready = false;
        working = false;
    }

    public void setProduct(ArtisanProduct product) {
        this.product = product;
    }

    public boolean isReady() {
        return ready;
    }

    public void finish() {
        ready = true;
        elapsedTime = processingTime;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion texture = ready ? textureReady : textureNormal;

        batch.draw(
            texture,
            getX(), getY(),
            getWidth(), getHeight()
        );
    }

    public boolean isWorking() {
        return working;
    }
}
