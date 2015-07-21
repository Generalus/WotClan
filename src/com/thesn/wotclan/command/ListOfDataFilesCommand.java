package com.thesn.wotclan.command;


import com.thesn.wotclan.ConsoleHelper;
import com.thesn.wotclan.Const;
import com.thesn.wotclan.exception.InterruptOperationException;

import java.io.File;
import java.util.ResourceBundle;

/**
 *  Команда: Вывести список файлов с данными из архива (как правило, одному дню соответствует один файл)
 */

class ListOfDataFilesCommand implements Command {
    private static ResourceBundle res = ResourceBundle.getBundle(Const.RESOURCE_PATH + ".command_ru");

    public void execute() throws InterruptOperationException {
        File folder = new File(Const.ARCHIVE_PATH);
        File[] listFiles = folder.listFiles();

        if (listFiles != null && listFiles.length != 0){
            ConsoleHelper.writeMessage(res.getString("list.files"));
            for (File file: listFiles)
                System.out.println(file.getName());
        } else
            ConsoleHelper.writeMessage(res.getString("files.not.found"));
    }
}
