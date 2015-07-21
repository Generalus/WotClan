package com.thesn.wotclan.download;

import com.thesn.wotclan.ConsoleHelper;
import com.thesn.wotclan.Const;
import com.thesn.wotclan.data.Clan;
import com.thesn.wotclan.data.Player;
import com.thesn.wotclan.data.Skirmish;
import com.thesn.wotclan.data.Tank;
import com.thesn.wotclan.exception.JSONException;
import com.thesn.wotclan.exception.PAPIError;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 *  Класс для работы с PAPI Wargaming и со всеми сохраненными файлами.
 *  Цель его методов: загрузить файл в JSON формате с сервера PAPI, заполнить объект Clan его данными.
 *  А затем сериализовать этот объект в txt-файл формата JSON,
 *  который можно будет десериализовать потом в объект Clan с помощью Jackson.
 */

public class Downloader {

    /**
     * Десериализация заданного txt-файла JSON формата из папки archive с помощью Jackson
     * @param name имя файла
     * @return десериализованный объект Clan
     * @throws IOException
     */
    public static Clan getClanObjectFromFile(String name) throws IOException{
        Path path = Paths.get(Const.ARCHIVE_PATH + "/" + name + ".txt");
        if (Files.notExists(path)) throw new FileNotFoundException("Файл не существует!");
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String str = Files.readAllLines(path).get(0);
        StringReader reader = new StringReader(str);
        return mapper.readValue(reader, Clan.class);
    }


    /**
     * Получение информации с сервера WOT PAPI в JSON формате
     * @param urlStr URL сервера
     * @return адаптер JSONAdapter, в котором содержится JSON файл и инструменты для работы с ним
     * @throws ParseException
     * @throws IOException
     * @throws PAPIError
     * @throws JSONException
     */
    private static JSONAdapter getJSONAdapterFromPAPI(String urlStr) throws ParseException, IOException, PAPIError, JSONException
    {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();

        JSONParser parser = new JSONParser();

        JSONAdapter obj = new JSONAdapter(parser.parse(sb.toString()));
        if (!obj.getString("status").equals("ok"))
            throw new PAPIError(obj.fromObject("error").getString("message"));
        return obj.fromObject("data");
    }

    /**
     * Загрузка всех игроков с сервера WOT PAPI. Процесс загрузки отображается в консоли.
     * @return множество игроков
     * @throws ParseException
     * @throws IOException
     * @throws PAPIError
     * @throws JSONException
     */

    public static Set<Player> loadPlayersFromServer() throws ParseException, IOException, PAPIError, JSONException
    {
        JSONAdapter obj = getJSONAdapterFromPAPI(Const.CLAN_URL);
        JSONAdapter users = obj.fromObject(Const.CLAN_ID).fromObject("members");


        Set<Player> players = new HashSet<>();

        for(int i = 0; i < users.getArray().size(); i++){
            Player player = loadOnePlayerFromServer(users.fromArray(i));
            if (player != null)
                players.add(player);
            ConsoleHelper.writeSmallMessage("|"); // полоса загрузки
        }

        return players;
    }

    /**
     * Загрузка ОДНОГО КОНКРЕТНОГО игрока с сервера WOT PAPI
     * @param jo JSON-адаптер с информацией по этому игроку
     * @return объект Player
     * @throws ParseException
     * @throws IOException
     * @throws PAPIError
     * @throws JSONException
     */

    public static Player loadOnePlayerFromServer(JSONAdapter jo) throws ParseException, IOException, PAPIError, JSONException
    {
        Integer id = jo.getInt("account_id");

        String url = String.format(Const.ACCOUNT_URL_FORMAT, Const.APPLICATION_ID, id);
        JSONAdapter data = getJSONAdapterFromPAPI(url).fromObject(id);

        String name = data.getString("nickname");
        Date date = new Date(data.getLong("last_battle_time") * 1000);
        Player player = new Player(name, id);

        player.setLastBattleTime(date);

        // изучаем вылазки игрока

        JSONAdapter skrm = data.fromObject("statistics").fromObject("stronghold_skirmish");
        Skirmish skirmish = new Skirmish();
        skirmish.setBattles(skrm.getInt("battles"));
        skirmish.setWins(skrm.getInt("wins"));
        skirmish.setSurvive(skrm.getInt("survived_battles"));
        player.setSkirmish(skirmish);

        player.setRole(jo.getString("role_i18n"));

        player.setKV1(loadTankInfoFromServer(id, Const.KV1_ID));
        player.setT67(loadTankInfoFromServer(id, Const.T67_ID));


        // считаем количество боев на аккаунте, складывая значение с каждого танка
        url = String.format(Const.TANK_URL_FORMAT_2, Const.APPLICATION_ID, id);
        JSONAdapter array = getJSONAdapterFromPAPI(url).fromObject(id);
        int battles = 0;
        for(int i = 0; i < array.getArray().size(); i++)
            battles += array.fromArray(i).fromObject("all").getInt("battles");
        player.setBattles(battles);

        return player;
    }

    /**
     * Загрузка конкретного танка, принадлежащего конкретному игроку
     * @param playerID ID игрока клана
     * @param tankID ID загружаемого танка
     * @return объект Tank
     * @throws ParseException
     * @throws IOException
     * @throws PAPIError
     * @throws JSONException
     */

    public static Tank loadTankInfoFromServer(Integer playerID, String tankID) throws ParseException, IOException, PAPIError, JSONException
    {
        String url = String.format(Const.TANK_URL_FORMAT, Const.APPLICATION_ID, playerID, tankID);
        JSONAdapter array = getJSONAdapterFromPAPI(url).fromObject(playerID);
        if (array.getArray() == null) return null;
        JSONAdapter data = array.fromArray(0).fromObject("all");

        Tank tank = new Tank();

        int battles = data.getInt("battles");
        int wins = data.getInt("wins");
        int damage = data.getInt("damage_dealt") / battles;

        tank.setBattles(battles);
        tank.setWins(wins);
        tank.setAverageDamage(damage);

        url = String.format(Const.ACHIEVEMENTS_URL_FORMAT, Const.APPLICATION_ID, playerID, tankID);
        array = getJSONAdapterFromPAPI(url).fromObject(playerID);
        Map<String, Integer> realMedals = new HashMap<>();
        int marks = 0;

        if (array != null) {
            data = array.fromArray(0).fromObject("achievements");

            for (Map.Entry<String, String> entry: Const.medals.entrySet())
                if (data.getObject().containsKey(entry.getValue()))
                    realMedals.put(entry.getKey(), data.getInt(entry.getValue()));

            if (data.getObject().containsKey("marksOnGun"))
                marks = data.getInt("marksOnGun");
        }

        tank.setMedals(realMedals);
        tank.setMarksOnGun(marks);

        return tank;
    }

    /**
     * Загрузка информации о клане
     * @return Список некоторых данных о клане, их порядок важен
     * @throws ParseException
     * @throws IOException
     * @throws PAPIError
     * @throws JSONException
     */
    public static List<String> loadClanDataFromServer() throws ParseException, IOException, PAPIError, JSONException
    {
        JSONAdapter data = getJSONAdapterFromPAPI(Const.CLAN_URL).fromObject(Const.CLAN_ID);

        List<String> result = new ArrayList<>();
        result.add(data.getString("name"));
        result.add(data.getString("description_html"));
        return result;
    }

    /**
     * Загрузка темы из группы клана ВК, на которой игроки отмечались. Метод используется классом View.
     * @return HTML-страница для Jsoup
     * @throws IOException
     */

    public static Document getVkPage() throws IOException{
        return Jsoup
                .connect(Const.VK_PAGE)
                .userAgent(Const.BROWSER_NAME)
                .referrer("none")
                .get();
    }

    /**
     * Загрузка темы из группы клана ВК, на которой производился реферальный учет. Метод используется классом View.
     * @return HTML-страница для Jsoup
     * @throws IOException
     */

    public static Document getVkRef() throws IOException{
        return Jsoup
                .connect(Const.VK_REF)
                .userAgent(Const.BROWSER_NAME)
                .referrer("none")
                .get();
    }

    private Downloader(){}
}
