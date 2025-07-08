package org.example.models.Enums;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Tool.*;

public enum ItemLevel {
    Normal("Training", 5, 40, 0, 1){
        @Override
        public TextureRegion getToolTextureRegion(Tool tool) {
            if (tool instanceof Hoe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Hoe/Hoe.png"));
            } else if (tool instanceof Axe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Axe/Axe.png"));
            } else if (tool instanceof Pickaxe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Pickaxe/Pickaxe.png"));
            } else if (tool instanceof Scythe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Scythe.png"));
            } else if(tool instanceof WateringCan) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Watering_Can/Watering_Can.png"));
            }
            return null;
        }
    },
    Brass("Brass", 4, 55, 0.15, 1.25) {
        @Override
        public TextureRegion getToolTextureRegion(Tool tool) {
            if (tool instanceof Hoe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Hoe/Copper_Hoe.png"));
            } else if (tool instanceof Axe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Axe/Copper_Axe.png"));
            } else if (tool instanceof Pickaxe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Pickaxe/Copper_Pickaxe.png"));
            } else if (tool instanceof Scythe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Scythe.png"));
            } else if(tool instanceof WateringCan) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Watering_Can/Copper_Watering_Can.png"));
            }
            return null;
        }
    },
    Iron("Iron",3, 70,0.3, 1.25) {
        @Override
        public TextureRegion getToolTextureRegion(Tool tool) {
            if (tool instanceof Hoe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Hoe/Steel_Hoe.png"));
            } else if (tool instanceof Axe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Axe/Copper_Axe.png"));
            } else if (tool instanceof Pickaxe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Pickaxe/Steel_Pickaxe.png"));
            } else if (tool instanceof Scythe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Scythe.png"));
            } else if(tool instanceof WateringCan) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Watering_Can/Steel_Watering_Can.png"));
            }
            return null;
        }
    },
    Gold("Gold",2, 85,0.45, 1.5) {
        @Override
        public TextureRegion getToolTextureRegion(Tool tool) {
            if (tool instanceof Hoe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Hoe/Gold_Hoe.png"));
            } else if (tool instanceof Axe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Axe/Gold_Axe.png"));
            } else if (tool instanceof Pickaxe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Pickaxe/Gold_Pickaxe.png"));
            } else if (tool instanceof Scythe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Golden_Scythe.png"));
            } else if(tool instanceof WateringCan) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Watering_Can/Gold_Watering_Can.png"));
            }
            return null;
        }
    },
    Iridium("Iridium",1, 1000,0.6, 2) {
        @Override
        public TextureRegion getToolTextureRegion(Tool tool) {
            if (tool instanceof Hoe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Hoe/Iridium_Hoe.png"));
            } else if (tool instanceof Axe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Axe/Iridium_Axe.png"));
            } else if (tool instanceof Pickaxe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Pickaxe/Iridium_Pickaxe.png"));
            } else if (tool instanceof Scythe) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Tools/Iridium_Scythe.png"));
            } else if(tool instanceof WateringCan) {
                return new TextureRegion(new Texture("Stardew_Valley_Images-main/Watering_Can/Iridium_Watering_Can.png"));
            }
            return null;
        }
    };

    private final String name;
    private final int energyUsage;
    private final int wateringcanCapacity;
    private final double trashcanCoeff;
    private final double priceCoefficient;
    public abstract TextureRegion getToolTextureRegion(Tool tool);
    ItemLevel(String name, int energyUsage, int wateringcanCapacity, double trashcanCoeff, double priceCoefficient) {
        this.name = name;
        this.energyUsage = energyUsage;
        this.wateringcanCapacity = wateringcanCapacity;
        this.trashcanCoeff = trashcanCoeff;
        this.priceCoefficient = priceCoefficient;
    }
    public String getName() {
        return name;
    }
    public int getEnergyUsage() {
        return energyUsage;
    }
    public int getWateringcanCapacity() {
        return wateringcanCapacity;
    }
    public double getTrashcanCoeff() {
        return trashcanCoeff;
    }

    public double getPriceCoefficient() {
        return priceCoefficient;
    }

    public ItemLevel upgradeLevel() {
        switch (this) {
            case Normal : {
                return Brass;
            }
            case Brass :{
                return Iron;
            }
            case Iron : {
                return Gold;
            }
            case Gold : {
                return Iridium;
            }
            case Iridium : {
                return Iridium;
            }
        }
        return null;
    }

    public boolean isMaxLevel() {
        return this == ItemLevel.values()[ItemLevel.values().length - 1];
    }

}
