package com.thesn.wotclan;

import java.util.HashMap;
import java.util.Map;

/**
 *  В данном классе хранятся все константы:
 *  <ul>
 *  <li>ссылки на разделы API</li>
 *  <li>ID клана, танков</li>
 *  <li>пути для различных ресурсных файлов проекта</li>
 *  <li>дефолтный браузер</li>
 *  <li>список поддерживаемых медалей игры</li>
 *  </ul>
 *  Конструктор приватный.
 */

public class Const {

    public final static String APPLICATION_ID = "demo";  // заменить на свой ключ
    public final static String CLAN_ID = "140251";
    public final static String CLAN_URL = String.format("http://api.worldoftanks.ru/wgn/clans/info/?application_id=%s&clan_id=%s", APPLICATION_ID, CLAN_ID);
    public final static String ACCOUNT_URL_FORMAT = "http://api.worldoftanks.ru/wot/account/info/?application_id=%s&account_id=%s";
    public final static String KV1_ID = "11777";
    public final static String T67_ID = "10529";
    public final static String TANK_URL_FORMAT = "https://api.worldoftanks.ru/wot/tanks/stats/?application_id=%s&account_id=%s&tank_id=%s";
    public final static String TANK_URL_FORMAT_2 = "https://api.worldoftanks.ru/wot/tanks/stats/?application_id=%s&fields=all.battles&account_id=%s";
    public final static String ACHIEVEMENTS_URL_FORMAT = "https://api.worldoftanks.ru/wot/tanks/achievements/?application_id=%s&account_id=%s&tank_id=%s";

    public final static String PROGRAM_NAME = Const.class.getPackage().getName();
    public final static String ROOT_PATH = "./src/" + PROGRAM_NAME.replace('.', '/');
    public final static String HTML_PATH = ROOT_PATH + "/view/result.html";
    public final static String ARCHIVE_PATH = ROOT_PATH + "/archive";
    public final static String RESOURCE_PATH = PROGRAM_NAME + ".resources";

    public final static String VK_PAGE = "http://vk.com/topic-61870147_32354436";
    public final static String VK_REF = "http://vk.com/topic-61870147_32495291";

    public final static String BROWSER_NAME = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36";


    public final static Map<String, String> medals;

    static {
        medals = new HashMap<>();
        medals.put("Герои Рассейняя", "heroesOfRassenay");
        medals.put("Колобанов", "medalKolobanov");
        medals.put("Пул", "medalLafayettePool");
        medals.put("Рэдли-Уолтерс", "medalRadleyWalters");
        medals.put("Де Ланглад", "medalDeLanglade");
        medals.put("Камикадзе", "kamikaze");
        medals.put("Халонен", "medalHalonen");
        medals.put("Братья по оружию", "medalBrothersInArms");
        medals.put("Решающий вклад", "medalCrucialContribution");
    }

    private Const(){}

}
