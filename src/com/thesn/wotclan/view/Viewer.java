package com.thesn.wotclan.view;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


import com.thesn.wotclan.ConsoleHelper;
import com.thesn.wotclan.Const;
import com.thesn.wotclan.download.Downloader;
import com.thesn.wotclan.data.Clan;
import com.thesn.wotclan.data.Player;
import com.thesn.wotclan.data.Tank;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *  Класс генерирует HTML-файл со статистикой и сохраняет его в своем каталоге (results.html)
 */

public class Viewer {

    private static ResourceBundle res = ResourceBundle.getBundle(Const.RESOURCE_PATH + ".view_ru");

    private static Document vkPage;
    private static Document vkRef;


    public static void saveStatistics(Clan oldClan, Clan newClan, String oldDate, String newDate) throws IOException{
        updateFile(getUpdatedFileContent(getRating(oldClan, newClan), oldDate, newDate));
        ConsoleHelper.writeMessage(res.getString("successful.operation"));
    }


    public static String getUpdatedFileContent(List<PlayerStatistics> list, String oldDate, String newDate) throws IOException{

        Document doc = getDocument();
        ConsoleHelper.writeMessage(res.getString("saving"));

        doc.getElementsByClass("tableName").first()
                .text(String.format(res.getString("clan.statistics.format"), oldDate.replace("-", "."), newDate.replace("-", ".")));

        Element template = doc.getElementsByClass("pattern").first();
        Element pattern = template.clone();
        doc.select("tr[class=green]").remove();
        doc.select("tr[class=yellow]").remove();
        doc.select("tr[class=red]").remove();
        doc.select("tr[class=purple]").remove();
        pattern.removeClass("pattern");
        int counter = 1;
        for(PlayerStatistics stat: list){
            Element newElem = pattern.clone();

            newElem.children().get(0)
                    .text(counter++ + "");

            newElem.children().get(1)
                    .text(stat.getName())
                    .attr("title", String.format(res.getString("user.info.format"),
                            stat.getRating() > 0 ? res.getString("good.player") : (stat.getRating() == 0) ? res.getString("not.active.player") : res.getString("bad.player"),
                            stat.getRole(),
                            stat.hasVkAccount ? color("green", res.getString("yes")) : color("red", res.getString("no")),
                            stat.getReferrals() > 0 ? stat.getReferrals() + "" : res.getString("no"),
                            new SimpleDateFormat("dd.MM.yy").format(stat.getLastBattleTime())));


            newElem.children().get(2)
                    .text(stat.getAllKv1Battles() + stat.getAllT67Battles() + "")
                    .attr("title", String.format(res.getString("tank.format.2"),
                            stat.getAllKv1Battles() + " " + getStars(stat.getKv1Marks()),
                            stat.getAllT67Battles() + " " + getStars(stat.getT67Marks())));


            String kv1 = ""; String t67 = ""; String notice = "";

            if (stat.getKv1Battles() > 0)
                kv1 = String.format(res.getString("tank.format"),
                        "http://wiki.gcdn.co/images/0/08/Ussr-KV1.png",
                        stat.getKv1Battles(),
                        Math.round(stat.getKv1Wins() * 1. / stat.getKv1Battles() * 100),
                        stat.getKv1Battles() > 5 ? stat.getKv1AverageDamage() + "" : "—");

            if (stat.getT67Battles() > 0)
                t67 = String.format(res.getString("tank.format"),
                        "http://wiki.gcdn.co/images/c/c2/Usa-T67.png",
                        stat.getT67Battles(),
                        Math.round(stat.getT67Wins() * 1. / stat.getT67Battles() * 100),
                        stat.getT67Battles() > 5 ? stat.getT67AverageDamage() + "" : "—");

            if (stat.getT67Battles() == 0 && stat.getKv1Battles() == 0 && stat.getRating() < 0)
                notice = color("red", res.getString("player.warn"));

            newElem.children().get(3)
                    .text(((stat.getBattles() != 0) ? (Math.round((stat.getKv1Battles() + stat.getT67Battles() + 0.) / stat.getBattles() * 100)) : 0) + "%")
                    .attr("title", String.format(res.getString("tank.format.3"), stat.getBattles(), kv1, t67, notice));

            newElem.children().get(4)
                    .text(stat.getSkirmishBattles() + "");
            if (stat.getSkirmishBattles() > 0)
                newElem.children().get(4)
                        .attr("title", String.format(res.getString("skirmish.info.format"),
                                Math.round(stat.getSkirmishWins() * 100. / stat.getSkirmishBattles()),
                                Math.round(stat.getSkirmishSurvive() * 100. / stat.getSkirmishBattles())));


            int achievementsCount = stat.getMedalsCount() + (stat.getKv1NewMarks() > 0 ? 1 : 0) + (stat.getT67NewMarks() > 0 ? 1 : 0);
            newElem.children().get(5)
                    .text(achievementsCount + "");
            if (achievementsCount > 0)
                newElem.children().get(5)
                        .attr("title", stat.getMarksString() + stat.getMedalsString());

            newElem.children().get(6)
                    .text(stat.getRating() + "");


            if (stat.getRating() > 0) newElem.addClass("green"); else
                if (stat.getRating() == 0) newElem.addClass("yellow"); else
                    newElem.addClass("red");
            newElem.getElementsByAttribute("title").addClass("help");

            template.before(newElem.outerHtml());
        }

        Elements green = doc.select("tr[class=green]");
        if(green.size() > 10)
            for(int i = 0; i < 3; i++)
                green.get(i).removeClass("green").addClass("purple");


        // составляем общую статистику
        int kv1Battles = 0;
        int t67Battles = 0;
        int allBattles = 0;
        int kv1Wins = 0;
        int t67Wins = 0;
        int totalDamage = 0;

        for(PlayerStatistics stat: list){
            kv1Battles += stat.getKv1Battles();
            t67Battles += stat.getT67Battles();
            allBattles += stat.getBattles();
            kv1Wins += stat.getKv1Wins();
            t67Wins += stat.getT67Wins();
            totalDamage += stat.getKv1AverageDamage() * stat.getKv1Battles()
                    + stat.getT67AverageDamage() * stat.getT67Battles();
        }

        doc.getElementById("commonstat")
                .html(String.format(res.getString("common.statistics.format"),
                        allBattles,
                        (int)Math.round(kv1Battles * 100. / allBattles),
                        (int)Math.round(t67Battles * 100. / allBattles),
                        (int)Math.round((kv1Wins + t67Wins) * 100. / (kv1Battles + t67Battles)),
                        (int)Math.round(totalDamage * 1. / (kv1Battles + t67Battles))));


        return doc.html();
    }


    public static void updateFile(String content) throws IOException{

        BufferedWriter writer = new BufferedWriter(new FileWriter(Const.HTML_PATH));
        writer.write(content);
        writer.close();

    }

    protected static Document getDocument() throws IOException
    {
        return Jsoup.parse(new File(Const.HTML_PATH), "UTF-8");
    }

    public static List<PlayerStatistics> getRating(Clan oldClan, Clan newClan) throws IOException{
        ConsoleHelper.writeMessage(res.getString("creating.rating"));
        List<PlayerStatistics> list = new ArrayList<>();
        for(Player oldPlayer: oldClan.getPlayers()){
            Player newPlayer = newClan.getPlayerById(oldPlayer.getId());
            if(newPlayer != null){  // если игрок все еще в клане
                String oldName = !oldPlayer.getName().equals(newPlayer.getName()) ? "(" + oldPlayer.getName() + ")" : ""; // если игрок сменил никнейм
                PlayerStatistics stat = new PlayerStatistics(newPlayer.getName() + oldName);
                stat.setBattles(newPlayer.getBattles() - oldPlayer.getBattles());
                stat.setRole(newPlayer.getRole());
                stat.setLastBattleTime(newPlayer.getLastBattleTime());
                stat.setMedals(new HashMap<String, Integer>());

                Map<String, Integer> map;
                Map<String, Integer> map2;

                Tank newKv = newPlayer.getKV1();
                Tank oldKv = oldPlayer.getKV1();
                if (newKv != null)
                {
                    if (oldKv == null) {
                        oldPlayer.setKV1(new Tank());
                        oldKv = oldPlayer.getKV1();
                    }
                    stat.setAllKv1Battles(newKv.getBattles());
                    stat.setKv1Battles(newKv.getBattles() - oldKv.getBattles());
                    stat.setKv1Wins(newKv.getWins() - oldKv.getWins());

                    int averageDamage = (stat.getKv1Battles() == 0) ? 0 :
                            (newKv.getAverageDamage()*newKv.getBattles() - oldKv.getAverageDamage()*oldKv.getBattles())
                            / stat.getKv1Battles();
                    stat.setKv1AverageDamage(averageDamage);


                    stat.setKv1Marks(newKv.getMarksOnGun());
                    if(newKv.getMarksOnGun() > oldKv.getMarksOnGun())
                        stat.setKv1NewMarks(newKv.getMarksOnGun());
                    else stat.setKv1NewMarks(0);


                    map = oldKv.getMedals();
                    map2 = newKv.getMedals();

                    for(Map.Entry<String, Integer> pair: map2.entrySet()){
                        if(map.containsKey(pair.getKey())){
                            stat.getMedals().put(pair.getKey(), pair.getValue() - map.get(pair.getKey()));
                        } else {
                            stat.getMedals().put(pair.getKey(), pair.getValue());
                        }
                    }
                }

                if (newPlayer.getT67() != null)
                {
                    if (oldPlayer.getT67() == null) oldPlayer.setT67(new Tank());
                    stat.setAllT67Battles(newPlayer.getT67().getBattles());
                    stat.setT67Battles(newPlayer.getT67().getBattles() - oldPlayer.getT67().getBattles());
                    stat.setT67Wins(newPlayer.getT67().getWins() - oldPlayer.getT67().getWins());
                    if (stat.getT67Battles() != 0) {
                        stat.setT67AverageDamage((newPlayer.getT67().getAverageDamage() * newPlayer.getT67().getBattles()
                                    - oldPlayer.getT67().getAverageDamage() * oldPlayer.getT67().getBattles())
                                    / stat.getT67Battles()
                        );
                    } else {
                        stat.setT67AverageDamage(0);
                    }

                    stat.setT67Marks(newPlayer.getT67().getMarksOnGun());
                    if(newPlayer.getT67().getMarksOnGun() > oldPlayer.getT67().getMarksOnGun())
                        stat.setT67NewMarks(newPlayer.getT67().getMarksOnGun());
                    else stat.setT67NewMarks(0);

                    map = oldPlayer.getT67().getMedals();
                    map2 = newPlayer.getT67().getMedals();
                    for(Map.Entry<String, Integer> pair: map2.entrySet()){
                        if(map.containsKey(pair.getKey())){
                            stat.getMedals().put(pair.getKey(), (stat.getMedals().containsKey(pair.getKey()) ? stat.getMedals().get(pair.getKey()) : 0) + pair.getValue() - map.get(pair.getKey()));
                        } else {
                            stat.getMedals().put(pair.getKey(), (stat.getMedals().containsKey(pair.getKey()) ? stat.getMedals().get(pair.getKey()) : 0) + pair.getValue());
                        }
                    }
                }

                stat.setSkirmishBattles(newPlayer.getSkirmish().getBattles() - oldPlayer.getSkirmish().getBattles());
                stat.setSkirmishWins(newPlayer.getSkirmish().getWins() - oldPlayer.getSkirmish().getWins());
                stat.setSkirmishSurvive(newPlayer.getSkirmish().getSurvive() - oldPlayer.getSkirmish().getSurvive());


                if (vkPage == null)
                    vkPage = Downloader.getVkPage();
                if (vkPage.outerHtml().contains(newPlayer.getName()))
                    stat.hasVkAccount = true;

                if (vkRef == null)
                    vkRef = Downloader.getVkRef();
                if (vkRef.outerHtml().contains(newPlayer.getName()))
                    stat.setReferrals(vkRef.outerHtml().split(newPlayer.getName()).length - 1);

                stat.calculateRating();
                list.add(stat);
            }
        }
        Collections.sort(list);
        ConsoleHelper.writeMessage(res.getString("rating.ready"));
        return list;
    }

    private static String getStars(int count){
        String s = "";
        for(int i = 0; i < count; i++){
            s = s + res.getString("star.format");
        }
        if (count == 3) s = s + " " + res.getString("pro");
        return s;
    }

    private static String color(String colorName, String text){
        return String.format(res.getString("font.color.format"), colorName, text);
    }

}
