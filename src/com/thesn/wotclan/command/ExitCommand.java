package com.thesn.wotclan.command;


import com.thesn.wotclan.ConsoleHelper;
import com.thesn.wotclan.Const;
import com.thesn.wotclan.exception.InterruptOperationException;

import java.util.ResourceBundle;

/**
 *  Команда: Выход из программы
 */

class ExitCommand implements Command {
    private static ResourceBundle res = ResourceBundle.getBundle(Const.RESOURCE_PATH + ".command_ru");

    public void execute() throws InterruptOperationException {
        ConsoleHelper.writeMessage(res.getString("good.luck"));
    }
}
