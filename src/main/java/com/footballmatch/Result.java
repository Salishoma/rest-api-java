package main.java.com.footballmatch;

//import com
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Result {

    /*
     * The function calculates the total number of matches drawn
     * Complete the 'getNumDraws' function below.
     *
     *
     * The function is expected to return an INTEGER.
     * The function accepts INTEGER year as parameter.
     */

    public static int getNumDraws(int year) {
        int count = 0;
        for (int i = 0; i <= 10; i++) {
            count += numDraws(year, i);
        }

        return count;
    }

    private static int numDraws(int year, int numGoals) {
        String response = null;
        try {
            response = getData(year, 1, numGoals);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        if (response == null) {
            return 0;
        }

        JsonObject res = new Gson().fromJson(response, JsonObject.class);
        JsonObject asJsonObject = res.getAsJsonObject();
        return asJsonObject.get("total").getAsInt();
    }

    private static String getData(int year, int page, int numGoals) throws Exception {
        StringBuilder builder = new StringBuilder();
        URL url = new URL("https://jsonmock.hackerrank.com/api/football_matches?year="
                + year + "&team1goals=" + numGoals + "&team2goals=" + numGoals + "&page=" + page);

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

    public static void main(String[] args) throws Exception {
        System.out.println(Result.getNumDraws(2011));
    }

}