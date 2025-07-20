package org.example.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class MessageBanner extends Table {
    private final Label messageLabel;

    public MessageBanner(Skin skin) {
        setBackground(skin.newDrawable("white", Color.DARK_GRAY));
        messageLabel = new Label("", skin);
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);
        add(messageLabel).width(400).pad(10);
        setVisible(false);
    }

    public void showMessage(String text, Color color, float duration) {
        messageLabel.setText(text);
        messageLabel.setColor(color);
        setVisible(true);
        addAction(Actions.sequence(
            Actions.alpha(1f),
            Actions.delay(duration),
            Actions.fadeOut(0.5f),
            Actions.visible(false)
        ));
    }
}
