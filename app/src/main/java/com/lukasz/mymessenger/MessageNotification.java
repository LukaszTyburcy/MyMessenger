package com.lukasz.mymessenger;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Lukasz on 2018-03-02.
 */

public class MessageNotification extends AsyncTask <String, Void, Void> {


    @Override
    protected Void doInBackground(String... strings) {
        try {
            String jsonResponse;
            String senderName;

            senderName = strings[0];

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic N2FiNGQyNWEtZTA1MS00YTljLWFhYjMtMGZhMzdmNjRiY2Zm");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"acfc064a-421a-494c-8f2a-004478ad4d17\","
                    +   "\"include_player_ids\": [\"25e2adc6-2000-477d-878c-02c2520bb5cf\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"en\": \"Nowa wiadomość od " + senderName
                    + "\"}}";

            Log.e("Nottification", "Weszlo");

            //System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);
            OutputStream outputStream = con.getOutputStream();
            Log.e("Nottification", "Weszlo4");
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            String StringResponse = Integer.toString(httpResponse);
            Log.e("httpResponse: " ,StringResponse);


            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Log.e("Nottification", "HTTP OK");
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                Log.e("Nottification", "HTTP ERROR");
                scanner.close();
            }

            Log.e("jsonResponse:\n" , jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}
