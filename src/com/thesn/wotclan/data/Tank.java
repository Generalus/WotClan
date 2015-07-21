package com.thesn.wotclan.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

/**
 *  Объект, содержащий информацию о танке, принадлежащем какому-то конкретному игроку
 */

@JsonAutoDetect
public class Tank {
    private int battles;
    private int wins;
    private int averageDamage;
    private int marksOnGun;
    @JsonDeserialize(as=HashMap.class)
    private Map<String, Integer> medals;

    public Tank(){
        battles = 0;
        wins = 0;
        averageDamage = 0;
        medals = new HashMap<>();
    }

    public int getBattles()
    {
        return battles;
    }

    public void setBattles(int battles)
    {
        this.battles = battles;
    }

    public int getWins()
    {
        return wins;
    }

    public void setWins(int wins)
    {
        this.wins = wins;
    }

    public int getAverageDamage()
    {
        return averageDamage;
    }

    public void setAverageDamage(int averageDamage)
    {
        this.averageDamage = averageDamage;
    }

    public Map<String, Integer> getMedals()
    {
        return medals;
    }

    public void setMedals(Map<String, Integer> medals)
    {
        this.medals = medals;
    }

    public int getMarksOnGun()
    {
        return marksOnGun;
    }

    public void setMarksOnGun(int marksOnGun)
    {
        this.marksOnGun = marksOnGun;
    }

    @Override
    public String toString() {
        return "Танк{" +
                "Бои=" + battles +
                ", Победы=" + wins +
                ", Средний дамаг=" + averageDamage +
                ", Отметок на пушке=" + marksOnGun +
                ", Медали=" + medals +
                '}';
    }
}
