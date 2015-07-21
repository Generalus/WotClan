package com.thesn.wotclan.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 *  Объект, содержащий информацию о количестве и качестве вылазок конкретного игрока
 */

@JsonAutoDetect
public class Skirmish {
    private int battles;
    private int wins;
    private int survive;

    public Skirmish() {}

    public int getBattles() {
        return battles;
    }

    public void setBattles(int battles) {
        this.battles = battles;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getSurvive() {
        return survive;
    }

    public void setSurvive(int survive) {
        this.survive = survive;
    }

    @Override
    public String toString() {
        return "Вылазки{" +
                "Боев=" + battles +
                ", Побед=" + wins +
                ", Выживаний=" + survive +
                '}';
    }
}
