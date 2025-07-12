package org.example.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.example.models.Enums.ArtisanType;

import java.util.List;

public class ArtisanMachine {
    private TextureRegion textureNormal;
    private TextureRegion textureReady;
    private final Vector2 position = new Vector2();
    ArtisanType type;
    private boolean ready = false;
    private float processingTime;
    private float elapsedTime;
    private ArtisanProduct product;

    public ArtisanMachine(ArtisanType type) {
        this.type = type;
        this.textureNormal = new TextureRegion(new Texture("ArtisanMachines/" + type.name().toLowerCase() + ".png"));
        this.textureReady = new TextureRegion(new Texture("ArtisanMachines/" + type.name().toLowerCase() + "_ready.png"));
        // TODO set the position
    }

    public boolean insertItem(List<String> items) {
        type.useArtisan(items, this);
        if (product == null) ;// error
        else{
            this.processingTime = product.getProcessingTime();
            this.elapsedTime = 0;
            this.ready = false;
        };  // success message


        return true;
    }

    public void update(float v) {
        if (product == null || ready) return;
        elapsedTime += v;
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
    }

    public void setProduct(ArtisanProduct product) {
        this.product = product;
    }

    public boolean isReady() {
        return ready;
    }

    public void render(SpriteBatch batch) {
        TextureRegion texture;
        if (ready) texture = textureReady;
        else  texture = textureNormal;

        batch.draw(texture, position.x, position.y);
    }
}
