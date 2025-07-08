package org.example.models;

public class Mission {
    public enum Status {
        LOCKED,
        NOT_STARTED,
        COMPLETED,
        DONE_BY_OTHER
    }
    String title;

    private String requiredItem;
    private int requiredAmount;

    private String rewardItem;
    private int rewardAmount;
    private Status status = Status.NOT_STARTED;
    private String playerUsername = "";

    public Mission(String title, String itemRequired, int amountRequired, String rewardItem, int rewardAmount) {
        this.title = title;
        requiredItem = itemRequired;
        requiredAmount = amountRequired;
        this.rewardItem = rewardItem;
        this.rewardAmount = rewardAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void completed(String username) {
        playerUsername = username;
    }

    public Status getStatus() {
        return status;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

}
