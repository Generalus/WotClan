package com.thesn.wotclan.command;


import com.thesn.wotclan.exception.InterruptOperationException;


interface Command {
    void execute() throws InterruptOperationException;
}
