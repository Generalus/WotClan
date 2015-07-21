package com.thesn.wotclan;


import com.thesn.wotclan.exception.InterruptOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 *  <b>Класс-помощник для вывода информации в консоль</b>
 *  Используется по всей программе вместо System.out.println
 */


public class ConsoleHelper {

    private static ResourceBundle res = ResourceBundle.getBundle(Const.RESOURCE_PATH + ".command_ru");
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private ConsoleHelper(){}

    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static void writeSmallMessage(String message){
        System.out.print(message);
    }

    public static String readString() throws InterruptOperationException
    {
        String s = "";
        try {
            s = reader.readLine();
        } catch(IOException e){
            writeMessage(res.getString("io.exception"));
        }
        if (s.equalsIgnoreCase("exit"))
            throw new InterruptOperationException();
        return s;
    }

    public static Operation askOperation() throws InterruptOperationException{
        while(true){
            try{
                return Operation.getAllowableOperationByOrdinal(Integer.parseInt(readString()));
            } catch(NumberFormatException e){
                writeMessage(res.getString("not.number"));
            }
            catch(IllegalArgumentException e){
                writeMessage(res.getString("incorrect.operation"));
            }
        }
    }

    public static void printExitMessage() {
        writeMessage(res.getString("good.luck"));
    }

    public static String readFileName() throws InterruptOperationException
    {
        String name;
        Path path;
        while(true) {
            writeMessage(res.getString("write.filename"));
            name = readString();
            path = Paths.get(Const.ARCHIVE_PATH + "/" + name + ".txt");
            if (Files.exists(path)) break;
            writeMessage(res.getString("incorrect.file"));
        }
        return name;
    }
}
