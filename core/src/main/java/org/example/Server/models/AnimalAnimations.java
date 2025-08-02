package org.example.Server.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimalAnimations {
    //Animation<TextureRegion> idle;
    public Animation<TextureRegion> walk_down;
    public Animation<TextureRegion> walk_right;
    public Animation<TextureRegion> walk_left;
    public Animation<TextureRegion> walk_up;
    public Animation<TextureRegion> eat;
    public Animation<TextureRegion> pet;

    public AnimalAnimations(Animation<TextureRegion> walkDown,Animation<TextureRegion> walkRight,
                            Animation<TextureRegion> walkLeft,Animation<TextureRegion> walkUp
                            ,Animation<TextureRegion> beingPet, Animation<TextureRegion> eat) {
        walk_down = walkDown;
        walk_right = walkRight;
        walk_left = walkLeft;
        walk_up = walkUp;
        this.eat = eat;
        pet = beingPet;
    }
}
