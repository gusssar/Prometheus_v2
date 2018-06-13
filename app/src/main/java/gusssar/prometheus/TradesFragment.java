package gusssar.prometheus;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class TradesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ArrayList<Product> coinArray = new ArrayList<>();
    public ArrayList<Product> waitArray = new ArrayList<>();
    TradeListAdapter tradeListAdapter;

    public static String LOG_TAG = "my_log";
    public String setUrl = "";
    protected ProgressBar proBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trades, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_trades);
        ProgressBar proBar = (ProgressBar) view.findViewById(R.id.prog_bar);
        //proBar.setProgress(0);
        proBar.setMax(52);
        //String pairLink = getResources().getString(R.string.pairLink);
        //String[] pairListForLink = getResources().getStringArray(R.array.pairListForLink);
        //String setUrl = "";
             //Log.d(LOG_TAG, "onCreateView  setUrl =" + setUrl);
             //Log.d(LOG_TAG, "onCreateView  line =" + line);

            new ProgressTask().execute();

        listView.setAdapter(tradeListAdapter);
        return view;
    }

    private class ProgressTask extends AsyncTask<Void, Integer, ArrayList> {
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
                for (int k=0; k<=10; k++){
                    Thread.sleep(100);
                    proBar.setProgress(k);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        @Override
        protected ArrayList<Product> doInBackground(Void... params) {

                String pairLink = getResources().getString(R.string.pairLink);
                String[] pairListForLink = getResources().getStringArray(R.array.pairListForLink);

                try {
                    //for (int p=0; p < 53; p++) {
                    for (int p=0; p <= 52; p++) {
                        setUrl = pairLink + pairListForLink[p];
                        URL url = new URL(setUrl);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();
                            InputStream inputStream = urlConnection.getInputStream();
                            StringBuffer buffer = new StringBuffer();
                                reader = new BufferedReader(new InputStreamReader(inputStream));
                                    String line_string;
                                        while ((line_string = reader.readLine()) != null) {
                                            buffer.append(line_string);
                                        }
                        resultJson = buffer.toString();
                        Log.d(LOG_TAG, "doInBackground  resultJson =" + resultJson);

                        String price_buy = "";
                        String price_sell = "";

                        JSONObject dataJsonObj = new JSONObject(resultJson);
                        JSONArray fullTradesArr = dataJsonObj.getJSONArray(pairListForLink[p]);

                        for (int i = 0; i < 35; i++) {
                            JSONObject lineTrades = fullTradesArr.getJSONObject(i);
                            String typeTr = lineTrades.getString("type");

                            if (typeTr.equals("buy")) {
                                price_buy = lineTrades.getString("price");
                                break;
                            }
                        }
                        for (int j = 0; j < 35; j++) {
                            JSONObject lineTrades = fullTradesArr.getJSONObject(j);
                            String typeTr = lineTrades.getString("type");

                            if (typeTr.equals("sell")) {
                                price_sell = lineTrades.getString("price");
                                break;
                            }
                        }
                        coinArray.add(new Product(pairListForLink[p], price_sell, price_buy));
                        publishProgress(p);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            return coinArray;
        }

        @Override
        protected void onPostExecute(ArrayList ALLJson) {
            super.onPostExecute(ALLJson);
            Log.d(LOG_TAG, "Весь текст: " + ALLJson);

            ListView listView = (ListView)getView().findViewById(R.id.list_trades);
            tradeListAdapter = new TradeListAdapter(getActivity(), ALLJson);
            listView.setAdapter(tradeListAdapter);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                proBar.setProgress(values[0]);
                Log.d(LOG_TAG, "бляяяяяя ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "торги", Toast.LENGTH_SHORT).show();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
    class Product {
        String name;
        String name2;
        String name3;

        Product(String _describe, String _describe2, String _describe3) {
            name = _describe;
            name2 = _describe2;
            name3 = _describe3;
        }
    }

/**
    {"BTC_USD":
            [{
                "trade_id":62013038,
                "type":"sell",
                "quantity":"0.00104142",
                "price":"7615",
                "amount":"7.9304133",
                "date":1528444187
            }]
    }
*/

