package org.example.controllers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.models.ChatMessage;
import org.example.models.ChatModel;
import org.example.views.ChatView;
import org.example.models.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private final ChatModel model;
    private final ChatView view;
    private final Player currentPlayer;
    private final Stage stage;

    public ChatController(Stage stage, Skin skin, List<Player> players, Player currentPlayer) {
        this.stage = stage;
        this.currentPlayer = currentPlayer;
        this.model = new ChatModel();

        List<String> names = new ArrayList<>();
        names.add("همه (عمومی)");
        for (Player p : players) {
            if (!p.equals(currentPlayer)) names.add(p.getUsername());
        }

        this.view = new ChatView(stage, skin, this, names);
    }

    public void onTargetChanged(String target) {
        if (target.equals("همه (عمومی)")) {
            view.refreshMessages(model.getPublicMessages());
        } else {
            view.refreshMessages(model.getPrivateMessages(currentPlayer.getUsername(), target));
        }
    }

    public void onSendMessage(String target, String text) {
        if (target.equals("همه (عمومی)")) {
            model.addPublicMessage(currentPlayer.getUsername(), text);
            view.refreshMessages(model.getPublicMessages());
            // TODO: ارسال به سرور
        } else {
            model.addPrivateMessage(currentPlayer.getUsername(), target, text);
            view.refreshMessages(model.getPrivateMessages(currentPlayer.getUsername(), target));
            // TODO: ارسال به سرور
        }
    }

    public void receivePublicMessage(String sender, String text) {
        model.addPublicMessage(sender, text);

        // اگر منشن شدی
        if (text.contains("@" + currentPlayer.getUsername()) && !view.isVisible()) {
            Dialog dialog = new Dialog("منشن شدی!", view.getSkin());
            dialog.text(sender + ": " + text);
            dialog.button("باشه");
            dialog.show(stage);
        }

        if (view.isVisible() && view.getTarget().equals("همه (عمومی)")) {
            view.refreshMessages(model.getPublicMessages());
        }
    }

    public void receivePrivateMessage(String sender, String text) {
        model.addPrivateMessage(sender, currentPlayer.getUsername(), text);

        if (!view.isVisible() || !view.getTarget().equals(sender)) {
            Dialog dialog = new Dialog("پیام خصوصی", view.getSkin());
            dialog.text("پیام جدید از " + sender);
            dialog.button("باشه");
            dialog.show(stage);
        }

        if (view.isVisible() && view.getTarget().equals(sender)) {
            view.refreshMessages(model.getPrivateMessages(currentPlayer.getUsername(), sender));
        }
    }

    public void toggleChatWindow() {
        if (view != null) {
            boolean current = view.isVisible();
            view.setVisible(!current);
            System.out.println("Chat window toggled: " + !current);
        } else {
            System.out.println("view is null!");
        }
    }
}
