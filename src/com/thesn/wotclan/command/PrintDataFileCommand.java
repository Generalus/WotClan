package com.thesn.wotclan.command;


import com.thesn.wotclan.ConsoleHelper;
import com.thesn.wotclan.Const;
import com.thesn.wotclan.download.Downloader;
import com.thesn.wotclan.data.Clan;
import com.thesn.wotclan.exception.InterruptOperationException;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 *  Команда: Вывести на экран информацию из заданного файла в более-менее читаемом виде
 */

class PrintDataFileCommand implements Command {
    private static ResourceBundle res = ResourceBundle.getBundle(Const.RESOURCE_PATH + ".command_ru");

    public void execute() throws InterruptOperationException {
        try {
            String name = ConsoleHelper.readFileName();
            Clan clan = Downloader.getClanObjectFromFile(name);
            ConsoleHelper.writeMessage(clan.toString());
        }
        catch(IOException e){
            ConsoleHelper.writeMessage(res.getString("io.exception") + ": " + e.getMessage());
        }
    }
}
