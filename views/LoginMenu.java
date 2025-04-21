package views;


import controllers.MenuController;
import models.Result;

public class LoginMenu implements views.AppMenu {
    private final MenuController menuController;

    public LoginMenu(MenuController menuController) {
        this.menuController = menuController;
    }

    @Override
    public void handleUserInput(String input) {
        System.out.println("\n=== Login Menu ===");
//        String input = scanner.nextLine().trim();

        if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        } else if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        } else if (input.equals("menu exit")) {
            Result result = menuController.exitMenu();
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid Command!");
        }
    }
    @Override
    public String getMenuName() {
        return "Login Menu";
    }
}