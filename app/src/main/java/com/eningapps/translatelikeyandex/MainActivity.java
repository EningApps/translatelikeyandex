package com.eningapps.translatelikeyandex;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //This key for using yandex.translate API , used for forming url in method translateWords(String textToBeTranslated)
    private static final String API_KEY = "trnsl.1.1.20171010T183713Z.837761618869bd3c.9efaea44e85419e9b1b20f2eaf0135c3af0b3286";



    EditText editText;
    TextView rezTextView;
    Button translateButton;
    String trasnlatedText="hardcode";

    //Find views from activity_main and set button's onClickListener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText) findViewById(R.id.editText);
        rezTextView=(TextView) findViewById(R.id.textRez);
        translateButton=(Button) findViewById(R.id.translateButton);
        translateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        YandexAPITraslater translater=new YandexAPITraslater();
        translater.execute();
        try {
            Thread.sleep(1000);//should wait response from Yandex API in YandexAPITraslater.doInBackground until varieble "trasnlatedText" change to result
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rezTextView.setText(trasnlatedText);//set result
    }

    //forming url and making "GET"-request to yandex API , than return response as Java.lang.String
    public static String translateWords(String textToBeTranslated)  {
        try {
            String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                    + API_KEY + "&lang=en-ru" + "&text=" + URLEncoder.encode(textToBeTranslated, "UTF-8");
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }catch (Exception e) {
            return "Шото у тебя проблемы, братан."+e.getMessage().toString();
        }
    }

    //unformattedText pattern - {"code":200,"lang":"en-ru","text":["HERE THE TEXT TO BE TRANSLATED"]}
    public static String parseResponse(String unformattedText){
        return unformattedText.substring(36,unformattedText.length()-3);
    }

    //to execute response to server we shoud use AsyncTask/Service
    //doInBackground(Void... voids) returns translated text as Java.lang.String
    class YandexAPITraslater extends AsyncTask<Void, Void, String>{
        String str=editText.getText().toString();
        @Override
        protected String doInBackground(Void... voids) {
            return  trasnlatedText=parseResponse(translateWords(str));
        }
    }

}