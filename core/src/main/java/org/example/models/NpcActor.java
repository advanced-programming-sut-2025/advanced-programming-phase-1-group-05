package org.example.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.models.Enums.Direction;
import org.example.views.GameScreen;

public class NpcActor extends Actor {
    private Direction direction = Direction.DOWN;
    private float stateTime = 0f;
    private TextureRegion currentFrame;
    float moveTimer = 0f;
    float speed = 30f;
    Vector2 directionVector = new Vector2();
    private NPC npc;
    public NpcActor (NPC npc) {
        this.npc = npc;
        Texture npcTexture = new Texture("NPCs/" + npc.getName().toLowerCase() + "/walkdown1.png");
        //playerSprite.setSize(playerTexture.getWidth()*4f, playerTexture.getHeight()*4f);
        setSize(npcTexture.getWidth() * 2f, npcTexture.getHeight() * 2f);
        currentFrame = new TextureRegion(npcTexture);
        setPosition(npc.getX(), npc.getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        moveTimer -= delta;

        if (moveTimer <= 0) {
            directionVector.set(MathUtils.random(-1, 1), MathUtils.random(-1, 1)).nor();

            moveTimer = MathUtils.random(1f, 3f);
        }


        float newX = getX() + directionVector.x * speed * delta;
        float newY = getY() + directionVector.y * speed * delta;

        if (newX < -23 || newX + getWidth() > 8900 || newY < 7 || newY + getHeight() > 8925) {
            return; // skip movement this frame
        }
        Rectangle nextBounds = new Rectangle(newX, newY, getWidth(), getHeight());

        if (!overlapsAnyFarm(nextBounds)) {
            setPosition(newX, newY);
            npc.setPosition(newX, newY);
            if (Math.abs(directionVector.x) > Math.abs(directionVector.y)) {
                direction = directionVector.x > 0 ? Direction.RIGHT : Direction.LEFT;
            } else if (directionVector.len2() > 0.001f) {
                direction = directionVector.y > 0 ? Direction.UP : Direction.DOWN;
            }
        }

        stateTime += delta;
        Animation<TextureRegion> anim = GameAssetManager.getInstance().getNPCWalkingAnimation(npc, direction);
        currentFrame = anim.getKeyFrame(stateTime, true);


        if (currentFrame == null) {
            System.out.println("Animation frame is null for NPC: " + npc.getName());
        }
    }

    private boolean overlapsAnyFarm(Rectangle bounds) {
        for (Rectangle farm : GameScreen.farms) {
            if (bounds.overlaps(farm)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }
}
