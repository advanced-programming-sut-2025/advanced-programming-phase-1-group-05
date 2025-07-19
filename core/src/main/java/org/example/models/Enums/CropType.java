package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Item;

import java.util.List;

public enum CropType implements Material, Item {
    BlueJazz("Blue Jazz", "Jazz Seeds", "1-2-2-2", 7, true, 0, 50, true, 45, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Crops/Blue_Jazz_Stage_5.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];

        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Blue_Jazz_Stage_" +(i + 1) + ".png"));
            }
        }

        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },
    Carrot("Carrot", "Carrot Seeds", "1-1-1", 3, true, 0, 35, true, 75, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Crops/Carrot.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[4];

        {
            for (int i = 0; i < 4; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Carrot_Stage_" + (i + 1)+ ".png"));
            }
        }

        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },
    Cauliflower("Cauliflower", "Cauliflower Seeds", "1-2-4-4-1", 12, true, 0, 175, true, 75, List.of(Season.SPRING), true,"Stardew_Valley_Images-main/Farming/Cauliflower.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[4];

        {
            for (int i = 0; i < 4; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Cauliflower_Stage_" + (i + 1)+ ".png"));
            }
        }

        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },
    CoffeeBean("Coffee Bean", "Coffee Bean", "1-2-2-3-2", 12, false, 2, 15, false, 0, List.of(Season.SPRING, Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Coffee_Stage_7.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Coffee_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Garlic("Garlic", "Garlic Seeds", "1-1-1-1", 4, true, 0, 60, true, 20, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Garlic_Stage_5.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Garlic_Stage_" + (i + 1)+ ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    GreenBean("Green Bean", "Bean Starter", "1-1-3-4", 10, false, 3, 40, true, 25, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Green_Bean_Stage_8.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[8];
        {
            for (int i = 0; i < 8; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Green_Bean_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Kale("Kale", "Kale Seeds", "1-2-2-1", 6, true, 0, 110, true, 50, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Kale_Stage_5.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Kale_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Parsnip("Parsnip", "Parsnip Seeds", "1-1-1-1", 4, true, 0, 35, true, 25, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Parsnip.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Parsnip_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Potato("Potato", "Potato Seeds", "1-1-1-2-1", 6, true, 0, 80, true, 25, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Potato.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Potato_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Rhubarb("Rhubarb", "Rhubarb Seeds", "2-2-2-3-4", 13, true, 0, 220, false, 0, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Rhubarb.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Rhubarb_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Strawberry("Strawberry", "Strawberry Seeds", "1-1-2-2-2", 8, false, 4, 120, true, 50, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Strawberry.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Strawberry_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Tulip("Tulip", "Tulip Bulb", "1-1-2-2", 6, true, 0, 30, true, 45, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Tulip.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Tulip_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    UnmilledRice("Unmilled Rice", "Rice Shoot", "1-2-2-3", 8, true, 0, 30, true, 3, List.of(Season.SPRING), false, "Stardew_Valley_Images-main/Crops/Unmilled_Rice.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Unmilled_Rice_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Blueberry("Blueberry", "Blueberry Seeds", "1-3-3-4-2", 13, false, 4, 50, true, 25, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Blueberry.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Blueberry_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Corn("Corn", "Corn Seeds", "2-3-3-3-3", 14, false, 4, 50, true, 25, List.of(Season.SUMMER, Season.FALL), false, "Stardew_Valley_Images-main/Crops/Corn.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Corn_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Hops("Hops", "Hops Starter", "1-1-2-3-4", 11, false, 1, 25, true, 45, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Hops.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Hops_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    HotPepper("Hot Pepper", "Pepper Seeds", "1-1-1-1-1", 5, false, 3, 40, true, 13, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Hot_Pepper.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Hot_Pepper_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Melon("Melon", "Melon Seeds", "1-2-3-3-3", 12, true, 0, 250, true, 113, List.of(Season.SUMMER), true, "Stardew_Valley_Images-main/Crops/Melon.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Melon_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Poppy("Poppy", "Poppy Seeds", "1-2-2-2", 7, true, 0, 140, true, 45, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Poppy.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Poppy_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Radish("Radish", "Radish Seeds", "2-1-2-1", 6, true, 0, 90, true, 45, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Radish.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Radish_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    RedCabbage("Red Cabbage", "Red Cabbage Seeds", "2-1-2-2-2", 9, true, 0, 260, true, 75, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Red_Cabbage.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Red_Cabbage_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    StarFruit("StarFruit", "Startfruit Seeds", "2-3-2-3-3", 13, true, 0, 750, true, 125, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Starfruit.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Starfruit_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    SummerSpangle("Summer Spangle", "Spangle Seeds", "1-2-3-1", 8, true, 0, 90, true, 45, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Summer_Spangle.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Summer_Spangle_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    SummerSquash("Summer Squash", "Summer Squash Seeds", "1-1-1-2-1", 6, false, 3, 45, true, 63, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Summer_Squash.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Summer_Squash_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Sunflower("Sunflower", "Sunflower Seeds", "1-2-3-2", 8, true, 0, 80, true, 45, List.of(Season.SUMMER, Season.FALL), false, "Stardew_Valley_Images-main/Crops/Sunflower.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Sunflower_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Tomato("Tomato", "Tomato Seeds", "2-2-2-2-3", 11, false, 4, 60, true, 20, List.of(Season.SUMMER), false, "Stardew_Valley_Images-main/Crops/Tomato.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Tomato_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Wheat("Wheat", "Wheat Seeds", "1-1-1-1", 4, true, 0, 25, false, 0, List.of(Season.SUMMER, Season.FALL), false, "Stardew_Valley_Images-main/Crops/Wheat.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Wheat_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Amaranth("Amaranth", "Amaranth Seeds", "1-2-2-2", 7, true, 0, 150, true, 50, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Amaranth.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Amaranth_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Artichoke("Artichoke", "Artichoke Seeds", "2-2-1-2-1", 8, true, 0, 160, true, 30, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Artichoke.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Artichoke_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Beet("Beet", "Beet Seeds", "1-1-2-2", 6, true, 0, 100, true, 30, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Beet.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Beet_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    BokChoy("Bok Choy", "Bok Choy Seeds", "1-1-1-1", 4, true, 0, 80, true, 25, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Bok_Choy.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Bok_Choy_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Broccoli("Broccoli", "Broccoli Seeds", "2-2-2-2", 8, false, 4, 70, true, 63, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Broccoli.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Broccoli_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Cranberries("Cranberries", "Cranberry Seeds", "1-2-1-1-2", 7, false, 5, 75, true, 38, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Cranberries.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Cranberry_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Eggplant("Eggplant", "Eggplant Seeds", "1-1-1-1", 5, false, 5, 60, true, 20, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Eggplant.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Eggplant_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    FairyRose("Fairy Rose", "Fairy Seeds", "1-4-4-3", 12, true, 0, 290, true, 45, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Fairy_Rose.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Fairy_Rose_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Grape("Grape", "Grape Starter", "1-1-2-3-3", 10, false, 3, 80, true, 38, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Grape.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Grape_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Pumpkin("Pumpkin", "Pumpkin Seeds", "1-2-3-4-2", 13, true, 0, 320, false, 0, List.of(Season.FALL), true, "Stardew_Valley_Images-main/Crops/Pumpkin.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Pumpkin_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    Yam("Yam", "Yam Seeds", "1-3-3-3", 10, true, 0, 160, true, 45, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Yam.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[5];
        {
            for (int i = 0; i < 5; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Yam_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    SweetGemBerry("Sweet Gem Berry", "Rare Seed", "2-4-6-6-6", 24, true, 0, 3000, false, 0, List.of(Season.FALL), false, "Stardew_Valley_Images-main/Crops/Sweet_Gem_Berry.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Sweet_Gem_Berry_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    PowderMelon("Powdermelon", "Powdermelon Seeds", "1-2-1-2-1", 7, true, 0, 60, true, 63, List.of(Season.WINTER), true, "Stardew_Valley_Images-main/Crops/Powdermelon.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[6];
        {
            for (int i = 0; i < 6; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Powdermelon_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    },

    AncientFruit("Ancient Fruit", "Ancient Seeds", "2-7-7-7-5", 28, false, 7, 550, false, 0, List.of(Season.SPRING, Season.SUMMER, Season.FALL), false, "Stardew_Valley_Images-main/Crops/Ancient_Fruit.png") {
        private final TextureRegion[] stageTextures = new TextureRegion[7];
        {
            for (int i = 0; i < 7; i++) {
                stageTextures[i] = new TextureRegion(new Texture("Stardew_Valley_Images-main/Crops/Ancient_Fruit_Stage_" + (i + 1) + ".png"));
            }
        }
        @Override
        TextureRegion getStageTexture(int stage) {
            return stageTextures[Math.min(stage, stageTextures.length - 1)];
        }
    };

    private final String name;
    private final String source;
    private final String stages;
    private final int totalHarvestTime;
    private final boolean oneTime;
    private final int regrowthTime;
    private final int price;
    private final boolean isEdible;
    private final int energy;
    private final List<Season> seasons;
    private final boolean canBecomeGiant;
    private final String texturePath;
    private final TextureRegion textureRegion;
    abstract TextureRegion getStageTexture(int stage);

    CropType(String name, String source, String stages, int totalHarvestTime, boolean oneTime,
             int regrowthTime, int price, boolean isEdible, int energy, List<Season> seasons, boolean canBeGiant, String texturePath) {
        this.name = name;
        this.source = source;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.price = price;
        this.isEdible = isEdible;
        this.energy = energy;
        this.seasons = seasons;
        this.canBecomeGiant = canBeGiant;
        this.texturePath = texturePath;
        this.textureRegion = new TextureRegion(new Texture(texturePath));

    }
    @Override
    public String getName() {
        return name;
    }
    public String getSource() {
        return source;
    }
    public String getStages() {
        return stages;
    }
    public int getTotalHarvestTime() {
        return totalHarvestTime;
    }
    public boolean isOneTime() {
        return oneTime;
    }
    public int getRegrowthTime() {
        return regrowthTime;
    }
    public int getPrice() {
        return price;
    }
    public boolean isEdible() {
        return isEdible;
    }
    public int getEnergy() {
        return energy;
    }
    public List<Season> getSeasons() {
        return seasons;
    }
    public boolean canBecomeGiant() {
        return canBecomeGiant;
    }

    public static CropType getSeedType(String seed){
        for(CropType cropType : CropType.values()){
            if(cropType.source.contains(seed)){
                return cropType;
            }
        }
        return null;
    }
    public static CropType fromString(String name){
        for (CropType cropType : CropType.values()) {
            if (cropType.getName().equals(name)) {
                return cropType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name :").append(name).append("\n").append("Source :").
                append(source).append("\n").append("Stages :").append(stages).
                append("\n").append("Total Harvest Time :").append(totalHarvestTime).
                append("\n").append("One Time :").append(oneTime).append("\n").
                append("Regrowth Time :").append(regrowthTime).append("\n").
                append("Base Sell Price :").append(price).append("\n").append("Is Edible :").
                append(isEdible).append("\n").append("Base Energy :").append(energy).append("\n").
                append("Season :");
        for(Season season : seasons){
            builder.append(season).append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("\n" + "Can Become Giant :").append(canBecomeGiant);
        return builder.toString();
    }
    public TextureRegion getTexture() {
        return textureRegion;
    }
}
