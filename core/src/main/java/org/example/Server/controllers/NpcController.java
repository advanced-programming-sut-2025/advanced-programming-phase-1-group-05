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
import org.example.Server.models.Store;

public class NpcController {
    NpcActor npc;
    Store store;

    public NpcController(NpcActor npc, Store store) {
        this.npc = npc;
        this.store = store;
    }

    public NpcActor getNpc () {
        return npc;
    }

    public void update(float delta) {
        if (!npc.walking) return;
        npc.moveTimer -= delta;
        Vector2 target = new Vector2(store.getX() + store.getWidth()/2, store.getY());
        int hour = GameManager.getGameClock().hour;
        float biasStrength = 0f;
        if (hour >= store.getOpeningTime() - 2 && hour < store.getOpeningTime() - 1) {
            biasStrength = 0.5f;
        } else if (hour >= store.getOpeningTime() - 1) {
            biasStrength = 1f;
        }

        if (npc.moveTimer <= 0) {
            Vector2 randomDir = new Vector2(MathUtils.random(-1f, 1f), MathUtils.random(-1f, 1f));

            Vector2 toTarget = target.cpy().sub(npc.getX(), npc.getY()).nor();

            npc.directionVector = randomDir.scl(1f - biasStrength).add(toTarget.scl(biasStrength)).nor();

            npc.moveTimer = MathUtils.random(1f, 3f);
        }

        float newX = npc.getX() + npc.directionVector.x * npc.speed * delta;
        float newY = npc.getY() + npc.directionVector.y * npc.speed * delta;

        if (newX < 100 || newX + npc.getWidth() > 8500 || newY < 100 || newY + npc.getHeight() > 8525) {
            return; // skip movement this frame
        }
        Rectangle nextBounds = new Rectangle(newX, newY, npc.getWidth(), npc.getHeight());

        if (!overlapsAnyFarm(nextBounds)) {
            npc.setPosition(newX, newY);
            npc.getNpc().setPosition(newX, newY);

            if (Math.abs(npc.directionVector.x) > Math.abs(npc.directionVector.y)) {
                npc.direction = npc.directionVector.x > 0 ? Direction.RIGHT : Direction.LEFT;
            } else {
                npc.direction = npc.directionVector.y > 0 ? Direction.UP : Direction.DOWN;
            }

        }

        npc.stateTime += delta;
        Animation<TextureRegion> anim = GameAssetManager.getInstance().getNPCWalkingAnimation(npc.getNpc(), npc.direction);
        npc.currentFrame = anim.getKeyFrame(npc.stateTime, true);
    }


    private boolean overlapsAnyFarm(Rectangle bounds) {
        for (Rectangle farm : GameScreen.farms) {
            if (bounds.overlaps(farm)) {
                return true;
            }
        }
        return false;
    }
}
