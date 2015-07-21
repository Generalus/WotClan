package com.thesn.wotclan.command;


import com.thesn.wotclan.ConsoleHelper;
import com.thesn.wotclan.Const;
import com.thesn.wotclan.download.Downloader;
import com.thesn.wotclan.data.Clan;
import com.thesn.wotclan.exception.InterruptOperationException;
import com.thesn.wotclan.view.Viewer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 *  Команда: Сгенерировать HTML-файл со статистикой по двум заданным файлам с данными
 */

class StatisticsCommand implements Command {
    private static ResourceBundle res = ResourceBundle.getBundle(Const.RESOURCE_PATH + ".command_ru");

    public void execute() throws InterruptOperationException {
        try {
            ConsoleHelper.writeMessage(res.getString("choose.files"));
            String name1 = ConsoleHelper.readFileName();
            String name2 = ConsoleHelper.readFileName();
            String s1[] = name1.split("-");
            String s2[] = name2.split("-");
            int compare = s2[2].compareTo(s1[2])*10000 + s2[1].compareTo(s1[1])*100 + s2[0].compareTo(s1[0]);
            if(compare == 0) {
                ConsoleHelper.writeMessage(res.getString("equal.files"));
                return;
            } else if (compare < 0){
                String temp = name1;
                name1 = name2;
                name2 = temp;
            }
            Clan clan1 = Downloader.getClanObjectFromFile(name1);
            Clan clan2 = Downloader.getClanObjectFromFile(name2);

            Viewer.saveStatistics(clan1, clan2, name1, name2);
            Desktop.getDesktop().open(new File(Const.HTML_PATH));
        }
        catch(IOException e){
            ConsoleHelper.writeMessage(res.getString("io.exception") + ": " + e.getMessage());
        }
    }
}
