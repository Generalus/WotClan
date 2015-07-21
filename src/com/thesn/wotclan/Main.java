package com.thesn.wotclan;

import com.thesn.wotclan.command.CommandExecutor;
import com.thesn.wotclan.exception.InterruptOperationException;
import java.util.ResourceBundle;

/**
 *  <p>Программа WotClan служит для ведения статистики конкретного клана World of Tanks.<br>
 *  Ссылка на страницу клана: http://ru.wargaming.net/clans/140251/ <br>
 *  Ссылка на API: http://ru.wargaming.net/developers/api_explorer/wot/ </p>
 *
 *  <p><b>ФУНКЦИОНАЛ:</b><br>
 *  Генерирует HTML-файл со статистикой, которая показывает как уровень игры клана в целом, так и его игроков
 *  по отдельности. Для этого необходимо выбрать два текстовых файла с данными формата JSON, которые были сгенерированы
 *  ранее этой же программой с использованием публичного API Wargaming (PAPI). После чего программа сравнивает их и
 *  демонстрирует, что произошло за выбранный промежуток времени.<br>
 *  Также программа умеет подключаться к страничке ВКонтакте клана и смотреть, кто вступил в группу, а кто нет, а также,
 *  считать количество рефералов для каждого игрока.</p>
 *
 *
 *  <b>ИСПОЛЬЗОВАНО:</b><ul>
 *  <li>Паттерн "Command"</li>
 *  <li>Парсинг JSON-данных (JSONSimple) из стороннего API</li>
 *  <li>Сериализация и десериализация в этом же формате с помощью Jackson</li>
 *  <li>Чтение и генерация HTML (JSoup)</li>
 *  <li>Почти все текстовые данные хранятся в properties файлах</li></ul>
 *
 *  <p><b>ДАННЫЙ КЛАСС:</b><br>
 *  После запуска доступны команды просмотра всех файлов-срезов с детальной информацией (они находятся в папке archive),
 *  создания нового среза с использованием PAPI + сохранения его в архив, генерирования и показа статистики,
 *  а также выхода из программы ;)</p>
 *
 *  @author TheSN (Никита Соколов)
 */

public class Main {

    private static ResourceBundle res = ResourceBundle.getBundle(Const.RESOURCE_PATH + ".command_ru");

    public static void main(String[] args){

        try {
            Operation operation;
            do {
                ConsoleHelper.writeMessage("\n" + res.getString("choose.operation") + " \n" +
                        " - " + res.getString("list.command") + ": 1;\n" +
                        " - " + res.getString("new.command") + ": 2;\n" +
                        " - " + res.getString("print.command") + ": 3;\n" +
                        " - " + res.getString("statistics.command") + ": 4;\n" +
                        " - " + res.getString("exit.command") + ": 5.\n");
                operation = ConsoleHelper.askOperation();
                CommandExecutor.execute(operation);
            }
            while (operation != Operation.EXIT);
        } catch(InterruptOperationException e){
            ConsoleHelper.printExitMessage();
        }

    }
}