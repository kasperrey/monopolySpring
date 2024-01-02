package be.kasperreynders.monopoly.spel;

import be.kasperreynders.monopoly.fons.FonsKaart;
import be.kasperreynders.monopoly.fons.GaNaarFons;
import be.kasperreynders.monopoly.fons.GevangenisFons;
import be.kasperreynders.monopoly.fons.KrijgBetaalGeldFons;
import be.kasperreynders.monopoly.kans.GaNaarKans;
import be.kasperreynders.monopoly.kans.GevangenisKans;
import be.kasperreynders.monopoly.kans.KansKaart;
import be.kasperreynders.monopoly.kans.KrijgBetaalGeldKans;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class LoaderJson {
    private static ArrayList<JSONObject> leesBestand(String file) throws IOException {
        ArrayList<JSONObject> jsonObjecten = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(Files.readString(Path.of(file)));
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjecten.add(jsonArray.getJSONObject(i));
        }
        return jsonObjecten;
    }

    public static ArrayList<Kaart> leesKaarten(String map) throws IOException {
        ArrayList<Kaart> kaarten = new ArrayList<>();
        for (JSONObject jsonObject: leesBestand(map+"straten.json")) {
            int[] huures = {jsonObject.getInt("huur"), jsonObject.getInt("huurBouw1"), jsonObject.getInt("huurBouw2"),
                    jsonObject.getInt("huurBouw3"), jsonObject.getInt("huurBouw4"), jsonObject.getInt("huurBouw5")};
            kaarten.add(new Kaart(jsonObject.getInt("prijs"), jsonObject.getInt("huur"), jsonObject.getInt("positie"),
                    Optional.empty(), huures, jsonObject.getString("kleur"), 0, jsonObject.getInt("bouwPrijs")));
        }
        return kaarten;
    }

    public static ArrayList<TreinKaart> leesTreinKaarten(String map) throws IOException {
        ArrayList<TreinKaart> treinKaarten = new ArrayList<>();
        for (JSONObject jsonObject: leesBestand(map+"treinen.json")) {
            int[] huures = {jsonObject.getInt("rent1"), jsonObject.getInt("rent2"),
                    jsonObject.getInt("rent3"), jsonObject.getInt("rent4")};
            treinKaarten.add(new TreinKaart(jsonObject.getInt("prijs"), jsonObject.getInt("rent1"),
                    jsonObject.getInt("positie"), Optional.empty(), huures));
        }
        return treinKaarten;
    }

    public static ArrayList<SpecialeKaart> leesSpecialeKaarten(String map) throws IOException {
        ArrayList<SpecialeKaart> specialeKaarten = new ArrayList<>();
        for (JSONObject jsonObject: leesBestand(map+"speciaal.json")) {
            int[] malers = {jsonObject.getInt("maal1"), jsonObject.getInt("maal2")};
            specialeKaarten.add(new SpecialeKaart(jsonObject.getInt("positie"), jsonObject.getInt("prijs"),
                    jsonObject.getInt("maal1"), Optional.empty(), malers));
        }
        return specialeKaarten;
    }

    public static ArrayList<Tax> leesTax(String map) throws IOException {
        ArrayList<Tax> taxen = new ArrayList<>();
        for (JSONObject jsonObject: leesBestand(map+"tax.json")) {
            taxen.add(new Tax(jsonObject.getInt("positie"), jsonObject.getInt("prijs")));
        }
        return taxen;
    }

    public static ArrayList<KansKaart> leesKans(String map, Bord bord) throws IOException {
        ArrayList<KansKaart> kansKaarten = new ArrayList<>();
        HashMap<String, JSONArray> hashMap = getKeysOfObject(map+"kans.json");
        hashMap.keySet().forEach(s -> {
            switch (s) {
                case "ga naar" -> {
                    for (int i = 0; i < hashMap.get(s).length(); i++) {
                        JSONObject jsonObject = hashMap.get(s).getJSONObject(i);
                        kansKaarten.add(new GaNaarKans(convertJsonArrayToList(jsonObject.getJSONArray("plekken")), jsonObject.getString("type"),
                                bord, new Dobbelsteen(), jsonObject.getInt("terug"), jsonObject.getString("text")));
                    }
                }
                case "gevangenis" -> {
                    for (int i = 0; i < hashMap.get(s).length(); i++) {
                        JSONObject jsonObject = hashMap.get(s).getJSONObject(i);
                        kansKaarten.add(new GevangenisKans(jsonObject.getString("type"), jsonObject.getString("text")));
                    }
                }
                case "geld" -> {
                    for (int i = 0; i < hashMap.get(s).length(); i++) {
                        JSONObject jsonObject = hashMap.get(s).getJSONObject(i);
                        kansKaarten.add(new KrijgBetaalGeldKans(jsonObject.getString("type"), jsonObject.getInt("geld"), bord, jsonObject.getString("text")));
                    }
                }
            }
        });
        return kansKaarten;
    }

    public static ArrayList<FonsKaart> leesFons(String map, Bord bord) throws IOException {
        ArrayList<FonsKaart> fonsKaarten = new ArrayList<>();
        HashMap<String, JSONArray> hashMap = getKeysOfObject(map+"fons.json");
        hashMap.keySet().forEach(s -> {
            switch (s) {
                case "ga naar" -> {
                    for (int i = 0; i < hashMap.get(s).length(); i++) {
                        JSONObject jsonObject = hashMap.get(s).getJSONObject(i);
                        fonsKaarten.add(new GaNaarFons(jsonObject.getInt("pos"), jsonObject.getString("text")));
                    }
                }
                case "gevangenis" -> {
                    for (int i = 0; i < hashMap.get(s).length(); i++) {
                        JSONObject jsonObject = hashMap.get(s).getJSONObject(i);
                        fonsKaarten.add(new GevangenisFons(jsonObject.getString("type"), jsonObject.getString("text")));
                    }
                }
                case "geld" -> {
                    for (int i = 0; i < hashMap.get(s).length(); i++) {
                        JSONObject jsonObject = hashMap.get(s).getJSONObject(i);
                        fonsKaarten.add(new KrijgBetaalGeldFons(jsonObject.getString("type"), jsonObject.getInt("geld"), bord, jsonObject.getString("text")));
                    }
                }
            }
        });
        return fonsKaarten;
    }

    private static ArrayList<Integer> convertJsonArrayToList(JSONArray jsonArray) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            arrayList.add(jsonArray.getInt(i));
        }
        return arrayList;
    }

    private static HashMap<String, JSONArray> getKeysOfObject(String file) throws IOException {
        HashMap<String, JSONArray> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject(Files.readString(Path.of(file)));
        for (int j = 0; j < jsonObject.names().length(); j++) {
            String name = jsonObject.names().getString(j);
            map.put(name, jsonObject.getJSONArray(name));
        }
        return map;
    }

    public static ArrayList<Integer> getKansPos(String map) throws IOException {
        ArrayList<Integer> list = new ArrayList<>();
        for (JSONObject jsonObject: leesBestand(map+"kans fons.json")) {
            if (jsonObject.getString("type").equals("Chance")) {
                list.add(jsonObject.getInt("positie"));
            }
        }
        return list;
    }

    public static ArrayList<Integer> getFonsPos(String map) throws IOException {
        ArrayList<Integer> list = new ArrayList<>();
        for (JSONObject jsonObject: leesBestand(map+"kans fons.json")) {
            if (jsonObject.getString("type").equals("Chest")) {
                list.add(jsonObject.getInt("positie"));
            }
        }
        return list;
    }
}
