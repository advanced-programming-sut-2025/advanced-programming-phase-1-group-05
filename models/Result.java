package models;

public class Result {
    boolean success;
    String message;
    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
        printMessage(message);
    }

    public void printMessage(String message) {
        if(!message.isEmpty()) System.out.println(message);
    }

}
