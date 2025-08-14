package org.example.Client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.example.Server.Packets.NpcDialogueRequest;
import org.example.Server.controllers.GameManager;
import org.example.Common.Enums.Direction;
import org.example.Server.models.MyGame;
import org.example.Server.models.NPC;

import java.util.Random;

public class NpcActor extends Actor {
    private final Image exclamationImage;
    public TextureRegion currentFrame;
    private final NPC npc;
    private boolean dialogueReady = false;
    private final int dialogueTime;

    public NpcActor (NPC npc) {
        this.npc = npc;
        Texture npcTexture = new Texture("NPCs/" + npc.getName().toLowerCase() + "/walkdown1.png");

        setSize(npcTexture.getWidth() * 4f, npcTexture.getHeight() * 4f);
        currentFrame = new TextureRegion(npcTexture);
        setPosition(npc.getX(), npc.getY());
        Random random = new Random();
        dialogueTime = random.nextInt(10, 18);

        Texture tex= GameAssetManager.getInstance().getOrLoadTexture("ui/exclamation.png");
        exclamationImage = new Image(new TextureRegion(tex));
        exclamationImage.setVisible(false);
        exclamationImage.setSize(tex.getWidth() * 3.5f, tex.getHeight() *3.5f);
        exclamationImage.addAction(Actions.forever(
            Actions.sequence(
                Actions.moveBy(0, 5, 0.5f),
                Actions.moveBy(0, -5, 0.5f)
            )
        ));
    }

    @Override
    public void act(float delta) {
        if (GameManager.getGameClock().hour >= dialogueTime && !npc.dialogueForToday) {
            dialogueReady = true;
            exclamationImage.setVisible(true);
        }
        super.act(delta);
        exclamationImage.act(delta);

        Animation<TextureRegion> anim = GameAssetManager.getInstance().getNPCWalkingAnimation(npc, npc.direction);
        currentFrame = anim.getKeyFrame(npc.stateTime);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        if (dialogueReady) {
            exclamationImage.setPosition(
                getX() + (getWidth() - exclamationImage.getWidth()) / 2f,
                getY() + getHeight() + 5
            );
            exclamationImage.draw(batch, parentAlpha);

        }

    }

    public NPC getNpc() {
        return npc;
    }

    public void setWalking(boolean walking) {
        npc.walking = walking;
    }

    public boolean isDialogueReady() {
        return dialogueReady;
    }

    public void getMessage() {
        String message = null;
         if (dialogueReady) {
//             message = DialogueManager.(npc.getName(), MyGame.getCurrentWeather().name());
             NpcDialogueRequest request = new NpcDialogueRequest();
             request.NpcName = npc.getName();
             request.username = MyGame.getCurrentPlayer().getUsername();
             request.weather = MyGame.getCurrentWeather().name();
             request.hour = GameManager.getCurrentHour();
             GameClient.client.sendTCP(request);
             npc.dialogueForToday = true;
         }
         dialogueReady = false;

    }

}
