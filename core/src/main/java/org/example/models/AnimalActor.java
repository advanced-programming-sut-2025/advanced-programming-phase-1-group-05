package org.example.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimalActor extends Actor {
    //private TextureRegion currentFrame;

    private final Animal animal;

    public AnimalActor(Animal animal) {
        this.animal = animal;
//        Texture npcTexture = new Texture("NPCs/" + npc.getName().toLowerCase() + "/walkdown1.png");
//
//        setSize(npcTexture.getWidth() * 4f, npcTexture.getHeight() * 4f);
//        currentFrame = new TextureRegion(npcTexture);
        setPosition(animal.getX(), animal.getY());
    }

    public Animal getAnimal() {
        return animal;
    }
}
