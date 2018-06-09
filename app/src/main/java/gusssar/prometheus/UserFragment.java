package gusssar.prometheus;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class UserFragment extends Fragment {

    public ArrayList<Product> coinArray = new ArrayList<>();
    public ArrayList<Product> waitArray = new ArrayList<>();
    TradeListAdapter tradeListAdapter;

    public static String LOG_TAG = "my_log";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_user);

        new ProgressTask().execute();
        listView.setAdapter(tradeListAdapter);

        return view;
    }

    private class ProgressTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                String waitStr = getResources().getString(R.string.waitStr);
                waitArray.add(new Product(waitStr,null,null));
                tradeListAdapter =  new TradeListAdapter(getActivity(),waitArray);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("https://api.exmo.com/v1/currency/");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
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

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            Log.d(LOG_TAG, "Весь текст: " + strJson);

            try {
               JSONArray data_JsonObj = new JSONArray(strJson);
               String arrayString = data_JsonObj.toString();
               Log.d(LOG_TAG, "В строке массив: " + arrayString);

               for (int j=0; j < data_JsonObj.length(); j++){
                   Log.d(LOG_TAG, "В цикле: " + j);
                   String object = data_JsonObj.getString(j);
                   Log.d(LOG_TAG, "имя: " + object);

                   coinArray.add(new Product(object,null,null));
                   ListView listView = (ListView)getView().findViewById(R.id.list_user);
                   tradeListAdapter = new TradeListAdapter(getActivity(), coinArray);
                   listView.setAdapter(tradeListAdapter);
               }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
