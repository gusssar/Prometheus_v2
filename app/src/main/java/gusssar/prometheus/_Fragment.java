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
import org.json.JSONObject;
import java.util.ArrayList;

public class _Fragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment__, container, false);
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
                URL url = new URL("https://api.exmo.com/v1/trades/?pair=BTC_USD");

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
                    JSONObject dataJsonObj = new JSONObject(strJson);
                    JSONArray friends = dataJsonObj.getJSONArray("BTC_USD");

                    for (int j=0; j < 20; j++) {
                        JSONObject lastTrans = friends.getJSONObject(j);
                        String typeTr = lastTrans.getString("type");
                        Log.d(LOG_TAG, "Тип транзакции: " + typeTr);
                        String priceTr = lastTrans.getString("price");
                        Log.d(LOG_TAG, "Тип транзакции: " + priceTr);
                        coinArray.add(new Product("BTC_USD",typeTr,priceTr));
                    }
                    ListView listView = (ListView)getView().findViewById(R.id.list_user);
                    tradeListAdapter = new TradeListAdapter(getActivity(), coinArray);
                    listView.setAdapter(tradeListAdapter);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
/**
{"BTC_USD":[
        {"trade_id":62105171,
            "type":"buy",
            "quantity":"0.01349674",
            "price":"7608.487081",
            "amount":"102.68977192",
            "date":1528552877
        }
 */