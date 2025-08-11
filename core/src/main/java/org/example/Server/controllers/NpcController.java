package org.example.Server.controllers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.example.Client.GameAssetManager;
import org.example.Client.GameScreen;
import org.example.Client.NpcActor;
import org.example.Common.Enums.Direction;
import org.example.Server.models.NPC;
import org.example.Server.models.ServerNPC;
import org.example.Server.models.Store;

import java.util.ArrayList;
import java.util.List;

public class NpcController {
    public ServerNPC npc;
    public Direction direction;
    public float moveTimer = 0f;
    public Vector2 directionVector = new Vector2();

    public NpcController(ServerNPC npc) {
        this.npc = npc;
    }

    public ServerNPC getNpc () {
        return npc;
    }

    public void update(float delta) {
        System.out.println("Updating NPC " + npc.name + " moveTimer: " + moveTimer);
        if (!npc.walking) return;
        moveTimer -= delta;
        Vector2 target = new Vector2(npc.storeX, npc.storeY);
        int hour = GameManager.getGameClock().hour;
        float biasStrength = 0f;
        if (hour >= npc.startHour - 2 && hour < npc.startHour - 1) {
            biasStrength = 0.5f;
        } else if (hour >= npc.startHour - 1 && hour < npc.endHour) {
            biasStrength = 1f;
        }

        if (moveTimer <= 0) {
            Vector2 randomDir = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));

            Vector2 toTarget = target.cpy().sub(npc.x, npc.y).nor();

            directionVector = randomDir.scl(1f - biasStrength).add(toTarget.scl(biasStrength)).nor();

            moveTimer = MathUtils.random(1f, 3f);
        }

        float newX = npc.x + directionVector.x * 30 * delta;
        float newY = npc.y + directionVector.y * 30 * delta;

        if (newX < 100 || newX + 60 > 8500 || newY < 100 || newY + 104 > 8525) {
            return; // skip movement this frame
        }
        Rectangle nextBounds = new Rectangle(newX, newY, 60, 104);

        if (!overlapsAnyFarm(nextBounds)) {
            if (Math.abs(directionVector.x) > Math.abs(directionVector.y)) {
                npc.direction = directionVector.x > 0 ? Direction.RIGHT : Direction.LEFT;
            } else {
                npc.direction = directionVector.y > 0 ? Direction.UP : Direction.DOWN;
            }
            npc.x = newX;
            npc.y = newY;
        }



    }


    private boolean overlapsAnyFarm(Rectangle bounds) {
        List<Rectangle> farms = new ArrayList<>();
        int TILE_SIZE = 64;
        farms.add(new Rectangle(10f * TILE_SIZE, 10f * TILE_SIZE, 50f * TILE_SIZE, 50f * TILE_SIZE));
        farms.add(new Rectangle(80 * TILE_SIZE, 10 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE));
        farms.add(new Rectangle(10 * TILE_SIZE, 80 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE));
        farms.add(new Rectangle(80 * TILE_SIZE, 80 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE));
        for (Rectangle farm : farms) {
            if (farm.overlaps(bounds))
                return true;
        }
        return false;
    }
}
