package com.boostphysioclinic.presentation;

import com.boostphysioclinic.util.Result;

import java.util.List;
import java.util.function.Function;

public interface ConsoleView {
    enum MessageType{
        INFO, WARNING, ERROR
    }

    void showMessage(String message, MessageType messageType);

    <T> T promptInput(String message, Function<String, Result<T, String>> validator);

    int showMenu(List<String> menuItems, String title, boolean includeExit);


}
