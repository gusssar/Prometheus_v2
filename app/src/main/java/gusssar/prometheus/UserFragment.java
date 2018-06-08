package gusssar.prometheus;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import java.io.InputStream;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserFragment extends Fragment {
    TextView LogContView;
    TextView HumContView;
    String LogContText = null;

    //json
    public static String LOG_TAG = "my_log";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        LogContView = (TextView) view.findViewById(R.id.LogContView);
        HumContView = (TextView) view.findViewById(R.id.HumContView);
        Button OnPostBtn = (Button) view.findViewById(R.id.OnPostBtn);

        //json
        new ProgressTask().execute();

        //OnPostBtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        if (LogContText == null) {
        //            LogContView.setText("Загрузка...");
        //            new UserFragment.ProgressTask().execute("https://api.exmo.com/v1/currency/");
        //        }
        //    }
        //});
        return view;
    }

    private class ProgressTask extends AsyncTask<Void, Void, String> {

        //json
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        //@Override
        //protected String doInBackground(String... path) {
        //    String content;
        //    try{
        //        content = getContent(path[0]);
        //    }
        //    catch (IOException ex){
        //        content = ex.getMessage();
        //    }
        //    return content;
        //}

        //json
        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                //URL url = new URL("http://androiddocs.ru/api/friends.json");
                URL url = new URL("https://api.exmo.com/v1/currency/");

                urlConnection = (HttpURLConnection) url.openConnection();
                //setRequestMethod GET или POST не понятно
                urlConnection.setRequestMethod("GET");
                //urlConnection.setRequestMethod("POST");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        //@Override
        //protected void onPostExecute(String content) {
        //    LogContText=content;
        //    LogContView.setText(content);
        //    Toast.makeText(getActivity(), "Данные загружены", Toast.LENGTH_SHORT).show();
        //}

        //json

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, "Весь текст: " + strJson);

            JSONObject dataJsonObj = null;
            String secondName = "";

            try {
                //проба
               JSONArray data_JsonObj = new JSONArray(strJson);
               String arrayString = data_JsonObj.toString();
               Log.d(LOG_TAG, "В строке массив: " + arrayString);
               for (int j=1; j < data_JsonObj.length()+1; j++){
                   Log.d(LOG_TAG, "В цикле: " + j);
                   //тут закончил
                   //JSONObject name = data_JsonObj.getJSONObject(j);
                   //String nameStr = name.toString();
                   //Log.d(LOG_TAG, "Наименование: " + nameStr);
               }
               // JSONArray test = dataJsonObj.getJSONArray("");
               // Log.d(LOG_TAG, "Великий тест: " + test);

               dataJsonObj = new JSONObject(strJson);
               JSONArray friends = dataJsonObj.getJSONArray("friends");

                // 1. достаем инфо о втором друге - индекс 1
                JSONObject secondFriend = friends.getJSONObject(1);
                secondName = secondFriend.getString("name");
                Log.d(LOG_TAG, "Второе имя: " + secondName);

                // 2. перебираем и выводим контакты каждого друга
                for (int i = 0; i < friends.length(); i++) {
                    JSONObject friend = friends.getJSONObject(i);

                    JSONObject contacts = friend.getJSONObject("contacts");

                    String phone = contacts.getString("mobile");
                    String email = contacts.getString("email");
                    String skype = contacts.getString("skype");

                    Log.d(LOG_TAG, "phone: " + phone);
                    Log.d(LOG_TAG, "email: " + email);
                    Log.d(LOG_TAG, "skype: " + skype);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


       // private String getContent(String path) throws IOException {
       //     BufferedReader reader=null;
       //     try {
       //         URL url=new URL(path);
       //         HttpsURLConnection c=(HttpsURLConnection)url.openConnection();
       //         c.setRequestMethod("POST");
       //         c.setReadTimeout(10000);
       //         c.connect();
       //         reader= new BufferedReader(new InputStreamReader(c.getInputStream()));
       //         StringBuilder buf=new StringBuilder();
       //         String line=null;
       //         while ((line=reader.readLine()) != null) {
       //             buf.append(line + "\n");
       //         }
       //         return(buf.toString());
       //     }
       //     finally {
       //         if (reader != null) {
       //             reader.close();
       //         }
       //     }
        }
    }
}
