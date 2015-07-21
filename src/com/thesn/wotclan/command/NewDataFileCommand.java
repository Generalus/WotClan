package com.thesn.wotclan.command;


import com.thesn.wotclan.ConsoleHelper;
import com.thesn.wotclan.Const;
import com.thesn.wotclan.download.Downloader;
import com.thesn.wotclan.data.Clan;
import com.thesn.wotclan.exception.InterruptOperationException;
import com.thesn.wotclan.exception.JSONException;
import com.thesn.wotclan.exception.PAPIError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *  Команда: Подключиться к PAPI Wargaming и сгенерировать новый файл с данными на текущий день.
 */

class NewDataFileCommand implements Command {
    private static ResourceBundle res = ResourceBundle.getBundle(Const.RESOURCE_PATH + ".command_ru");

    public void execute() throws InterruptOperationException {
        try {
            String date = new SimpleDateFormat("dd-MM-yy").format(new Date());
            Path path = Paths.get(Const.ARCHIVE_PATH + "/" + date + ".txt");
            if (Files.exists(path)) {
                ConsoleHelper.writeMessage(res.getString("download.again"));
                if (!ConsoleHelper.readString().equalsIgnoreCase(res.getString("yes")))
                    return;
            }
            ConsoleHelper.writeMessage(res.getString("downloading"));
            Clan clan = new Clan();
            clan.setInfo(Downloader.loadClanDataFromServer());
            clan.setPlayers(Downloader.loadPlayersFromServer());
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, clan);
            Files.write(path, writer.toString().getBytes());
            ConsoleHelper.writeMessage("\n" + res.getString("download.success"));
        }
        catch(ParseException e){
            ConsoleHelper.writeMessage(res.getString("parse.exception"));
        }
        catch(IOException e){
            ConsoleHelper.writeMessage(res.getString("io.exception") + ": " + e.getMessage());
        }
        catch(PAPIError e){
            ConsoleHelper.writeMessage("\n" + res.getString("papi.error") + ": " + e.getMessage());
        }
        catch(JSONException e){
            ConsoleHelper.writeMessage(res.getString("json.exception"));
        }
    }

}
