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
    private final float homeX;
    private final float homeY;
    private final float MAX_DISTANCE = 320f;

    private float speed = 50f; // units per second
    private float targetX;
    private float targetY;
    private final float MOVE_INTERVAL = 2f; // seconds between choosing a new target
    private float moveTimer = 0f;
    public AnimalActor(Animal animal) {
        this.animal = animal;
        System.out.println(animal.getX() + ", " + animal.getY());
        setPosition(animal.getX(), animal.getY());
        setSize(64, 64);
        animations = GameAssetManager.getInstance().animalAnimations.get(animal.getType());
        idleFrame = GameAssetManager.getInstance().getIdle(animal.getType());
        animal.setState(Animal.State.IDLE);
        homeX = animal.getX(); homeY = animal.getY();
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
//        if (animal.getState() == Animal.State.EATING)
//            return;
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
           //return;
        }
//        moveTimer -= delta;
//        if (moveTimer <= 0) {
//            pickRandomTarget();
//            moveTimer = MOVE_INTERVAL;
//        }

//        float dx = targetX - getX();
//        float dy = targetY - getY();
//        float distance = (float) Math.sqrt(dx*dx + dy*dy);
//
//        if (distance > 1f) { // avoid jitter
//            float moveX = dx / distance * speed * delta;
//            float moveY = dy / distance * speed * delta;
//
//            setPosition(getX() + moveX, getY() + moveY);
//
//            // Set walking animation based on direction
//            if (Math.abs(dx) > Math.abs(dy)) {
//                if (dx > 0) {
//                    setState(Animal.State.WALK_RIGHT);
////                    currentAnimation = animations.walk_right;
//                }
//                else {
//                    setState(Animal.State.WALK_LEFT);
////                    currentAnimation = animations.walk_left;
//                }
//            } else {
//                if (dy > 0) {
//                    setState(Animal.State.WALK_UP);
////                    currentAnimation = animations.walk_up;
//                }
//                else {
//                    setState(Animal.State.WALK_DOWN);
////                    currentAnimation = animations.walk_down;
//                }
//            }
//        } else {
//            setState(Animal.State.IDLE);
//        }
//
//        // Clamp inside home area
//        clampToHomeArea();
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
        animal.setState(Animal.State.EATING);
    }

    private void pickRandomTarget() {
        double angle = Math.random() * Math.PI * 2; // random direction
        float distance = (float) (Math.random() * MAX_DISTANCE);
        targetX = homeX + (float) Math.cos(angle) * distance;
        targetY = homeY + (float) Math.sin(angle) * distance;
    }

    private void clampToHomeArea() {
        float dx = getX() - homeX;
        float dy = getY() - homeY;
        float distance = (float) Math.sqrt(dx*dx + dy*dy);

        if (distance > MAX_DISTANCE) {
            float scale = MAX_DISTANCE / distance;
            float clampedX = homeX + dx * scale;
            float clampedY = homeY + dy * scale;
            setPosition(clampedX, clampedY);
        }
    }

}
