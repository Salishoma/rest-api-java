package main.java.com.footballmatch;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class Result2 {

    /*
     * The function calculates the total number of goals scored by a given team
     * Complete the 'getTotalGoals' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. STRING team
     *  2. INTEGER year
     */

    private static final String MATCH_URL = "https://jsonmock.hackerrank.com/api/football_matches";

    public static int getTotalGoals(String team, int year) throws UnsupportedEncodingException {
        return numGoals(team, year, 1, 0, "team1") + numGoals(team, year, 1, 0, "team2");
    }

    private static int numGoals(String team, int year, int page, int count, String teamType) throws UnsupportedEncodingException {
        String url = String.format(MATCH_URL + "?year=%d&%s=%s&page=%s", year, teamType, URLEncoder.encode(team, "UTF-8"), page);
        String response = null;
        try {
            response = getData(url);
        } catch(Exception ex) {
            System.out.println("Error while fetching page");
        }
        if (response == null) {
            return count;
        }

        JsonObject res = new Gson().fromJson(response, JsonObject.class);
        JsonArray data = res.getAsJsonArray("data");
        if (data.size() == 0) {
            return count;
        }

        for (JsonElement e : data) {
            count += e.getAsJsonObject().get(teamType+"goals").getAsInt();
        }

        return numGoals(team, year, page + 1, count, teamType);

    }

    private static String getData(String teamUrl) throws Exception {
        StringBuilder builder = new StringBuilder();
        URL url = new URL(teamUrl);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.addRequestProperty("Content-Type", "application/json");
        int status = urlConnection.getResponseCode();
        if (status < 200 || status >= 300) {
            throw new IOException("Error in reading data with status: " + status);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String response;
        while((response = reader.readLine()) != null) {
            builder.append(response);
        }

        return builder.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(getTotalGoals("Barcelona", 2011));
//        System.out.println(getTotalGoals("AS Roma", 2012));
    }
}
