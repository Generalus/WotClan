package com.thesn.wotclan.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Объект, содержащий информацию о ВСЕМ клане, сериализуется и десериализуется (txt-файлы в archive)
 */

@JsonAutoDetect
public class Clan {
    @JsonDeserialize(as=HashSet.class)
    private Set<Player> players;
    private String name;
    private String description;

    public Clan(){}

    public void setInfo(List<String> list){
        name = list.get(0);
        description = list.get(1);
    }

    public void setPlayers(Set<Player> players)
    {
        this.players = players;
    }

    public Set<Player> getPlayers()
    {
        return players;
    }

    public String getDescription()
    {
        return description;
    }



    public String getName()
    {
        return name;
    }


    public Player getPlayerById(int id){
        for(Player player: players)
            if (player.getId() == id) return player;
        return null;
    }

    @Override
    public String toString()
    {
        String result = "Название: " + name +
                "\nОписание: " + description +
                "\nСписок пользователей: \n";
        for(Player player: players){
            result = result + player.toString() + "\n\n";
        }
        return result;
    }
}
