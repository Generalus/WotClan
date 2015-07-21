package com.thesn.wotclan.exception;

/**
 *  Выбрасывается, если на сервере Wargaming возникла непредвиденная ошибка
 */
public class PAPIError extends Exception {
    public PAPIError(String message){
        super(message);
    }
}
