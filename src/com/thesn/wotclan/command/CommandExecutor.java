package com.thesn.wotclan.command;

import com.thesn.wotclan.Operation;
import com.thesn.wotclan.exception.InterruptOperationException;

import java.util.HashMap;
import java.util.Map;

/**
 *  Единый объект для выполнения команд, доступный из всей программы.
 *  Вне пакета доступ к командам, минуя этот executor, невозможен.
 */

public class CommandExecutor {
    private static Map<Operation, Command> map = new HashMap<>();

    static {
        map.put(Operation.NEW_DATA_FILE, new NewDataFileCommand());
        map.put(Operation.PRINT_DATA_FILE, new PrintDataFileCommand());
        map.put(Operation.LIST_OF_DATA_FILES, new ListOfDataFilesCommand());
        map.put(Operation.STATISTICS, new StatisticsCommand());
        map.put(Operation.EXIT, new ExitCommand());
    }

    private CommandExecutor(){}

    public static void execute(Operation operation) throws InterruptOperationException {
        map.get(operation).execute();
    }

}
