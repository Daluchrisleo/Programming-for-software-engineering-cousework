package com.boostphysioclinic.presentation;

import com.boostphysioclinic.util.Result;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class BasicConsoleView implements ConsoleView {
    private Scanner sc = new Scanner(System.in);

    @Override
    public void showMessage(String message, MessageType messageType) {
        switch (messageType) {
            case INFO -> System.out.println("[INFO]: " + message);
            case WARNING -> System.out.println("[WARNING]: " + message);
            case ERROR -> System.out.println("[ERROR]: " + message);
        }
    }

    @Override
    public <T> T promptInput(String message, Function<String, Result<T, String>> validator) {
        while (true) {
            String input = prompt(message);
            var result = validator.apply(input);
            if (result.isSuccess()) {
                System.out.println("success");
                return result.getData();
            }

            showMessage(result.getError(), MessageType.ERROR);
            return promptInput(message, validator);
        }
    }

    private void showMessage(String message) {
        System.out.println(message);
    }

    private String prompt(String prompt) {
        System.out.println(prompt + ": ");
        return sc.nextLine();
    }



    @Override
    public int showMenu(List<String> menuItems, String title, boolean includeExit) {
        if (title != null && !title.isEmpty()) showMessage(title);

        for (int i = 0; i < menuItems.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, menuItems.get(i));
        }

        return promptInput("Select option (1-" + menuItems.size() + ")", input -> {
            try {
                int index = Integer.parseInt(input);
                if (index >= 1 && index <= menuItems.size()) {
                    return Result.success(index - 1);
                } else {
                    return Result.error("Invalid selection. Please pick a number from the list.");
                }
            } catch (NumberFormatException e) {
                return Result.error("Please enter a valid number.");
            }
        });
    }
}
