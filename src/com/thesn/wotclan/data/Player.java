package com.thesn.wotclan.data;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Date;

/**
 *  Объект, содержащий информацию о конкретном игроке клана
 */

@JsonAutoDetect
public class Player {

    private String name;
    private int id;
    private String role;
    private Date lastBattleTime;
    private int battles;
    private Tank KV1;
    private Tank T67;
    private Skirmish skirmish;

    public Player(String name, int id)
    {
        this.name = name;
        this.id = id;
    }

    public Player() {}

    public Skirmish getSkirmish() {
        return skirmish;
    }

    public void setSkirmish(Skirmish skirmish) {
        this.skirmish = skirmish;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getBattles()
    {
        return battles;
    }

    public void setBattles(int battles)
    {
        this.battles = battles;
    }

    public Tank getKV1()
    {
        return KV1;
    }

    public void setKV1(Tank KV1)
    {
        this.KV1 = KV1;
    }

    public Tank getT67()
    {
        return T67;
    }

    public void setT67(Tank t67)
    {
        T67 = t67;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public Date getLastBattleTime()
    {
        return lastBattleTime;
    }

    public void setLastBattleTime(Date lastBattleTime)
    {
        this.lastBattleTime = lastBattleTime;
    }

    @Override
    public String toString()
    {
        return name + " {" +
                  "id=" + id +
                ", Звание='" + role + '\'' +
                ", Заходил последний раз=" + lastBattleTime +
                ", Боев на аккаунте=" + battles +
                ", КВ-1=" + KV1 +
                ", T67=" + T67 +
                ", " + skirmish +
                "}";
    }
}
