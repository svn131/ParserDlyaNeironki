package bot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


public class ExecutorClass {

    ObrabotkaSsylok obrabotkaSsylok;
    Map<Integer, double[]> mapaCefov;

//    Set<Dop> setDopov = new HashSet<>();//доп логика кефы и прочие - на данный момент устранение багга с четвертями 3 например а временем 4-20


    public ExecutorClass(ObrabotkaSsylok obrabotkaSsylok, Map<Integer, double[]> mapaCefov) {
        this.obrabotkaSsylok = obrabotkaSsylok;
        this.mapaCefov = mapaCefov;
    }

    public List<List<String>> processJson(JSONObject jsonObject) {

        List<List<String>> allValues = new ArrayList<>();

        JSONArray valueList = jsonObject.optJSONArray("Value");
        if (valueList != null && valueList.length() > 0) {
            for (int j = 0; j < valueList.length(); j++) {
                JSONObject gameObj = valueList.getJSONObject(j); // получаем из джейсона валью - там был лист джейсонов вот получаем с него каждый раз по одному джейсону с игрой
                List<String> values = new ArrayList<>();


                String o1 = gameObj.optString("O1", "0");
                String o2 = gameObj.optString("O2", "0");
                values.add(o1);
                values.add(o2);

                JSONObject scObject = gameObj.optJSONObject("SC");


                if (scObject != null) {
                    JSONArray psList = scObject.optJSONArray("PS");
                    if (psList != null) {
                        for (int i = 0; i < psList.length(); i++) {
                            JSONObject psObject = psList.getJSONObject(i);
                            String nf = psObject.optString("NF", "");
                            int s1 = psObject.optInt("S1", 0);
                            int s2 = psObject.optInt("S2", 0);


                            if ("1-я Четверть".equals(nf)) {
                                values.add(String.valueOf(s1));
                                values.add(String.valueOf(s2));
                                break;
                            }
                        }


                    }

                    String ts = scObject.optString("TS", "0");
                    values.add(ts);

                    JSONObject fsObject = scObject.optJSONObject("FS");
                    if (fsObject != null) {
                        int s1 = fsObject.optInt("S1", 0);
                        int s2 = fsObject.optInt("S2", 0);
                        values.add(String.valueOf(s1));
                        values.add(String.valueOf(s2));
                    }


                    System.out.println(gameObj.toString());


                    int serialKey = extractSerialKeyGame(gameObj);
                    values.add(String.valueOf(serialKey));

                    System.out.println("V=================================================");

                    extractEArray2(gameObj,values);
                    System.out.println("/|======================================================");


                }


                if (scObject.getString("CPS").equals("1-я Четверть") && values.get(6) != null) { // а что будет если другие параметры нулл ?
                    allValues.add(values);
                    System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");

                }
                Integer ssilka = gameObj.getInt("I");
                obrabotkaSsylok.podgotovkaUrl(ssilka);
            }
        }

        return allValues;
    }


    public void extractEArray2(JSONObject json, List<String> values) {
        int seriinikIgry = json.getJSONArray("O2IS").optInt(0, 0);
        System.out.println("111112");
        try {
            JSONArray sgArray = json.getJSONArray("SG");
            System.out.println("111113");
            for (int i = 0; i < sgArray.length(); i++) {
                JSONObject sgObject = sgArray.getJSONObject(i);
                System.out.println("111114");
                JSONArray eArray = sgObject.getJSONArray("E");
                System.out.println("Массив E: " + eArray);
                System.out.println("111115");
                for (int j = 0; j < eArray.length(); j++) {
                    JSONObject eObject = eArray.getJSONObject(j);

                    System.out.println("111116");
                    double c = eObject.getDouble("C");
                    int g = eObject.getInt("G");
                    int t = eObject.getInt("T");
                    double p = eObject.optDouble("P", 0.0);
                    String chetvertAreya =sgObject.getString("PN"); //////доработка

                    System.out.println(chetvertAreya + "111117");

                    // Обработка элемента массива E
//                    if (p > 30.0 && p < 100.0 && t == 9 && g == 17) { //@todo тонкая настройка бота


                    if(chetvertAreya.equals("1-я Четверть")){
                        System.out.println("piiiiiiiiiiiiiiiiii");
//                    if(chetvertAreya.equals("2-я Четверть")){
                        System.out.println("Найден подходящий элемент:");

                        System.out.println("C: " + c);
                        System.out.println("G: " + g);
                        System.out.println("T: " + t);
                        System.out.println("P: " + p);

                            values.add(String.valueOf(p));

                    }
                }
            }
        } catch (JSONException e) {
            System.out.println("В метода extractEArray2 не оказалось чегото из SG , E ,C , G, T, P");
        }
    }


    public int extractSerialKeyGame(JSONObject json) {
        int seriinikIgry = json.getJSONArray("O2IS").optInt(0, 0); // @todo проверить чтобы не было исключений
        return seriinikIgry;
    }


}
