package com.thesn.wotclan;

/**
 *  <b>Список поддерживаемых операций паттерна "Command:</b>
 *  <ul>
 *  <li>Вывести список файлов с данными из архива (как правило, на одному дню соответствует один файл)</li>
 *  <li>Подключиться к PAPI Wargaming и сгенерировать новый файл с данными на текущий день.</li>
 *  <li>Вывести на экран информацию из заданного файла в более-менее читаемом виде</li>
 *  <li>Сгенерировать HTML-файл со статистикой по двум заданным файлам с данными</li>
 *  <li>Выход из программы</li>
 *  </ul>
 */

public enum Operation {

    LIST_OF_DATA_FILES, NEW_DATA_FILE, PRINT_DATA_FILE, STATISTICS,  EXIT;

    /**
     * Выбор операции по ее порядковому номеру
     * @param i номер операции, выбранной пользователем в терминале
     * @return операция, которая попадет в CommandExecutor
     * @throws IllegalArgumentException если пользователь задал неверный номер операции
     */

    public static Operation getAllowableOperationByOrdinal(Integer i) throws IllegalArgumentException{
        switch(i){
            case 1: return LIST_OF_DATA_FILES;
            case 2: return NEW_DATA_FILE;
            case 3: return PRINT_DATA_FILE;
            case 4: return STATISTICS;
            case 5: return EXIT;
            default: throw new IllegalArgumentException();
        }
    }

}
