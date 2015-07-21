package com.thesn.wotclan.view;

import java.util.Date;
import java.util.Map;


/**
 *  Класс содержит статистику конкретного игрока за некоторый период времени.
 *  Данные берутся из ДВУХ файлов из папки archive.
 */


public class PlayerStatistics implements Comparable<PlayerStatistics> {

    private String name; // имя игрока (прибавляется старое, если игрок менял его)
    private String role; // клановая должность
    private Date lastBattleTime; // последний раз заходил в игру
    private int battles; // общее количество боев

    private int allKv1Battles;
    private int allT67Battles;

    private int kv1Battles;
    private int kv1Wins;
    private int kv1AverageDamage;
    private int kv1Marks;
    private int kv1NewMarks;

    private int t67Battles;
    private int t67Wins;
    private int t67AverageDamage;
    private int t67Marks;
    private int t67NewMarks;

    private int skirmishBattles;
    private int skirmishWins;
    private int skirmishSurvive;

    private int referrals; // количество приглашенных игроков


    private Map<String, Integer> medals; // список медалей <Название, Количество>

    boolean hasVkAccount = false;

    private int rating; // итоговый рейтинг

    public int getRating()
    {
        return rating;
    }


    /**
     *  Вычисление значения рейтинга, на основе которого производится сортировка итоговой таблицы
     *  в HTML-файле со статистикой
     */
    public void calculateRating()
    {
        if (battles > 10){
            if (kv1Battles + t67Battles > 0)
            {
                double commonRating = ((allKv1Battles + allT67Battles) < 5000 ? (allKv1Battles + allT67Battles) : 5000) * (kv1Marks + t67Marks + 1) / 1600.
                        + ((kv1Battles + t67Battles + 0.)/ battles * 100 - 40) + getMedalsCount() * 15 + (kv1NewMarks + t67NewMarks) * 15 + (hasVkAccount ? 10 : 0)
                        + ((skirmishBattles != 0) ? Math.round(skirmishWins * skirmishSurvive / skirmishBattles) : 0) + referrals * 30;
                double kv1Rating = 0;
                double t67Rating = 0;
                if (kv1Battles > 5) kv1Rating = (kv1Wins + 0.) / kv1Battles * 100 - 57 + ((kv1AverageDamage - 600.)/20);
                if (t67Battles > 5) t67Rating = (t67Wins + 0.) / t67Battles * 100 - 57 + ((t67AverageDamage - 650.)/20);
                if (kv1Rating < -25) kv1Rating = -25;
                if (t67Rating < -30) t67Rating = -30;
                if (kv1Rating > 20) kv1Rating = 20;
                if (t67Rating > 25) t67Rating = 25;
                rating = (int)Math.round(commonRating + (kv1Rating > t67Rating ? kv1Rating : t67Rating));
                if (rating == 0 || rating == -1) rating = 1;
            } else rating = -100; // игрок, пренебрегающий Т67 и КВ-1.
        } else rating = 0; // неактивный игрок

    }

    /**
     * При сравнении сперва учитывается рассчитанный рейтинг, а потом дата последнего боя (для отсева неактивных игроков)
     * @param stat другой объект этого класса
     * @return результат сравнения
     */
    public int compareTo(PlayerStatistics stat){
        int rating = (stat.rating - this.rating) * 100;
        int time = -1;
        if (stat.getLastBattleTime().after(this.getLastBattleTime())) time = 1;

        return rating + time;
    }

    public PlayerStatistics(String name)
    {
        this.name = name;
        battles = 0;

        allKv1Battles = 0;
        allT67Battles = 0;

        kv1Battles = 0;
        kv1Wins = 0;
        kv1AverageDamage = 0;

        t67Battles = 0;
        t67Wins = 0;
        t67AverageDamage = 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public int getBattles()
    {
        return battles;
    }

    public void setBattles(int battles)
    {
        this.battles = battles;
    }

    public Date getLastBattleTime()
    {
        return lastBattleTime;
    }

    public void setLastBattleTime(Date lastBattleTime)
    {
        this.lastBattleTime = lastBattleTime;
    }

    public int getKv1Battles()
    {
        return kv1Battles;
    }

    public void setKv1Battles(int kv1Battles)
    {
        this.kv1Battles = kv1Battles;
    }

    public int getKv1Wins()
    {
        return kv1Wins;
    }

    public void setKv1Wins(int kv1Wins)
    {
        this.kv1Wins = kv1Wins;
    }

    public int getKv1AverageDamage()
    {
        return kv1AverageDamage;
    }

    public void setKv1AverageDamage(int kv1AverageDamage)
    {
        this.kv1AverageDamage = kv1AverageDamage;
    }

    public int getT67Battles()
    {
        return t67Battles;
    }

    public void setT67Battles(int t67Battles)
    {
        this.t67Battles = t67Battles;
    }

    public int getT67Wins()
    {
        return t67Wins;
    }

    public void setT67Wins(int t67Wins)
    {
        this.t67Wins = t67Wins;
    }

    public int getT67AverageDamage()
    {
        return t67AverageDamage;
    }

    public void setT67AverageDamage(int t67AverageDamage)
    {
        this.t67AverageDamage = t67AverageDamage;
    }

    public Map<String, Integer> getMedals()
    {
        return medals;
    }

    public void setMedals(Map<String, Integer> medals)
    {
        this.medals = medals;
    }

    public int getAllKv1Battles()
    {
        return allKv1Battles;
    }

    public void setAllKv1Battles(int allKv1Battles)
    {
        this.allKv1Battles = allKv1Battles;
    }

    public int getAllT67Battles()
    {
        return allT67Battles;
    }

    public void setAllT67Battles(int allT67Battles)
    {
        this.allT67Battles = allT67Battles;
    }

    public int getKv1Marks()
    {
        return kv1Marks;
    }

    public void setKv1Marks(int kv1Marks)
    {
        this.kv1Marks = kv1Marks;
    }

    public int getT67Marks()
    {
        return t67Marks;
    }

    public void setT67Marks(int t67Marks)
    {
        this.t67Marks = t67Marks;
    }

    public int getKv1NewMarks()
    {
        return kv1NewMarks;
    }

    public void setKv1NewMarks(int kv1NewMarks)
    {
        this.kv1NewMarks = kv1NewMarks;
    }

    public int getT67NewMarks()
    {
        return t67NewMarks;
    }

    public void setT67NewMarks(int t67NewMarks)
    {
        this.t67NewMarks = t67NewMarks;
    }

    public int getSkirmishBattles() {
        return skirmishBattles;
    }

    public void setSkirmishBattles(int skirmishBattles) {
        this.skirmishBattles = skirmishBattles;
    }

    public int getSkirmishWins() {
        return skirmishWins;
    }

    public void setSkirmishWins(int skirmishWins) {
        this.skirmishWins = skirmishWins;
    }

    public int getSkirmishSurvive() {
        return skirmishSurvive;
    }

    public void setSkirmishSurvive(int skirmishSurvive) {
        this.skirmishSurvive = skirmishSurvive;
    }

    public int getReferrals() {
        return referrals;
    }

    public void setReferrals(int referrals) {
        this.referrals = referrals;
    }

    public int getMedalsCount(){
        int count = 0;
        for(Map.Entry<String, Integer> pair: medals.entrySet()){
            count += pair.getValue();
        }
        return count;
    }

    public String getMedalsString(){
        String result = "";
        for(Map.Entry<String, Integer> pair: medals.entrySet()){
            if (pair.getValue() > 0) result = result + " - " + pair.getKey() + ": " + pair.getValue() + "<br />";
        }
        return result;
    }

    public String getMarksString(){
        String result = "";
        String arr[] = {"", "получена <font color='green'><b>одна</b></font> отметка",
                "получено <font color='green'><b>две</b></font> отметки",
                "получено <font color='purple'><b>ТРИ</b></font> отметки, вот это да"};

        if (kv1NewMarks > 0)
            result = result + " - На КВ-1 " + arr[kv1NewMarks] + "!<br />";

        if (t67NewMarks > 0)
            result = result + " - На T67 " + arr[t67NewMarks] + "!<br />";

        return result;
    }
}
