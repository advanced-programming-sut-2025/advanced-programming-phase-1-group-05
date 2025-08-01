package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import org.example.controllers.ChatController;
import org.example.models.ChatMessage;
import java.util.List;

public class ChatView {
    private final Stage stage;
    private final Skin skin;
    private final ChatController controller;

    private Window chatWindow;
    private SelectBox<String> targetSelect;
    private Table messageTable;
    private ScrollPane scrollPane;
    private TextField inputField;

    public ChatView(Stage stage, Skin skin, ChatController controller, List<String> playerNames) {
        this.stage = stage;
        this.skin = skin;
        this.controller = controller;

        chatWindow = new Window("Chat", skin);
        float width = Gdx.graphics.getWidth() * 0.4f;
        float height = Gdx.graphics.getHeight() * 0.4f;
        chatWindow.setSize(width, height);
        chatWindow.setPosition((Gdx.graphics.getWidth() - width) / 2f, (Gdx.graphics.getHeight() - height) / 2f);
        chatWindow.setVisible(false);
        chatWindow.setColor(1, 1, 1, 0.9f);

        targetSelect = new SelectBox<>(skin);
        targetSelect.setItems(playerNames.toArray(new String[0]));
        chatWindow.add(new Label("ارسال به:", skin)).pad(5);
        chatWindow.add(targetSelect).growX().pad(5).row();

        messageTable = new Table();
        messageTable.top().left();
        scrollPane = new ScrollPane(messageTable, skin);
        chatWindow.add(scrollPane).colspan(2).grow().pad(5).row();

        inputField = new TextField("", skin);
        inputField.setMessageText("پیام...");
        TextButton sendButton = new TextButton("ارسال", skin);

        chatWindow.add(inputField).growX().pad(5);
        chatWindow.add(sendButton).pad(5);

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendMessage();
            }
        });

        inputField.setTextFieldListener((field, c) -> {
            if (c == '\r' || c == '\n') sendMessage();
        });

        targetSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onTargetChanged(targetSelect.getSelected());
            }
        });
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            controller.onSendMessage(targetSelect.getSelected(), text);
            inputField.setText("");
        }
    }

    public void refreshMessages(List<ChatMessage> messages) {
        messageTable.clear();
        for (ChatMessage msg : messages) {
            Label label = new Label(msg.getSenderName() + ": " + msg.getText(), skin);
            label.setAlignment(Align.left);
            messageTable.add(label).left().row();
        }
        scrollPane.layout();
        scrollPane.setScrollPercentY(1f);
    }

    public boolean isVisible() {
        return chatWindow.hasParent();
    }

    public void setVisible(boolean visible) {
        if (visible) {
            if (!chatWindow.hasParent()) {
                stage.addActor(chatWindow);
            }
            chatWindow.setVisible(true);
            stage.setKeyboardFocus(inputField);
            System.out.println("Chat window added and visible");
        } else {
            chatWindow.remove();
            stage.unfocus(inputField);
            System.out.println("Chat window removed");
        }
    }

    public Skin getSkin() {
        return skin;
    }

    public String getTarget() {
        return targetSelect.getSelected();
    }
}
