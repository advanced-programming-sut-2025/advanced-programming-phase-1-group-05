package org.example.Client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.Common.Product;
import org.example.Server.models.Animal;
import org.example.Server.models.AnimalAnimations;

import java.util.List;

public class AnimalActor extends Actor {
    //private TextureRegion currentFrame;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion idleFrame;
    private AnimalAnimations animations;
    private float stateTime = 0f;
    private final Animal animal;
    private boolean isBeingPetted = false;
    private float pettingTimer = 0f;
    private final float PETTING_DURATION = 5f;
    private boolean showHeart = false;
    Texture heartTexture = GameAssetManager.getInstance().getOrLoadTexture("ui/heart.png");

    public AnimalActor(Animal animal) {
        this.animal = animal;
        System.out.println(animal.getX() + ", " + animal.getY());
        setPosition(animal.getX(), animal.getY());
        setSize(64, 64);
        animations = GameAssetManager.getInstance().animalAnimations.get(animal.getType());
        idleFrame = GameAssetManager.getInstance().getIdle(animal.getType());
        animal.setState(Animal.State.IDLE);
    }

    public void setState(Animal.State state) {
        animal.setState(state);
        this.stateTime = 0;
        if (state == Animal.State.IDLE) {
            currentAnimation = null; // no animation
        } else if (state == Animal.State.EATING) {
            currentAnimation = animations.eat;
        } else if (state == Animal.State.WALK_UP) {
            currentAnimation = animations.walk_up;
        } else if (state == Animal.State.WALK_RIGHT) {
            currentAnimation = animations.walk_right;
        } else if (state == Animal.State.WALK_DOWN) {
            currentAnimation = animations.walk_down;
        }
        else if (state == Animal.State.WALK_LEFT) {
            currentAnimation = animations.walk_left;
        }
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        if (currentAnimation != null) {
            stateTime += delta;
        }
        if (isBeingPetted) {
            pettingTimer -= delta;
            if (pettingTimer <= 0) {
                isBeingPetted = false;
                showHeart = false;
                currentAnimation = null;
                animal.setState(Animal.State.IDLE);
            }
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (animal.getState() == Animal.State.IDLE) {
            batch.draw(idleFrame, getX(), getY(), getWidth(), getHeight());
        } else if (currentAnimation != null) {
            TextureRegion frame = currentAnimation.getKeyFrame(stateTime, true);
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        }
        if (showHeart) {
            batch.draw(heartTexture,
                getX() + (getWidth() - heartTexture.getWidth()) / 2f,
                getY() + getHeight() + 5);
        }
        List<Product> products = animal.getUnCollectedProducts();
        if (!products.isEmpty()) {
            for (Product product : products) {
                Texture texture = product.getTexture().getTexture();
                batch.draw(texture, getX() + getWidth() + texture.getWidth(), getY() + texture.getHeight());
            }
        }
    }
    public Animal getAnimal() {
        return animal;
    }

    public void pet() {
        isBeingPetted = true;
        pettingTimer = PETTING_DURATION;
        showHeart = true;
        currentAnimation = animations.pet;
    }
}
