package gusssar.prometheus;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    public String setUrl = "";
    //TradesDbManager tradesDbManager = new TradesDbManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction =fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_trades:
                    transaction.replace(R.id.main_frag, new TradesFragment()).commit();
                    //textView.setText(R.string.title_trades);
                    Log.d(LOG_TAG, "MainActivity trades");
                    return true;
                case R.id.navigation_news:
                    transaction.replace(R.id.main_frag, new NewsFragment()).commit();
                    //textView.setText(R.string.title_news);
                    Log.d(LOG_TAG, "MainActivity news");
                    return true;
                case R.id.navigation_:
                    transaction.replace(R.id.main_frag, new _Fragment()).commit();
                    //textView.setText(R.string.title_);
                    Log.d(LOG_TAG, "MainActivity ___");
                    return true;
                case R.id.navigation_user:
                    transaction.replace(R.id.main_frag, new UserFragment()).commit();
                    //textView.setText(R.string.title_user);
                    Log.d(LOG_TAG, "MainActivity user");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "MainActivity onCreate");
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.main_frag, new TradesFragment()).commit();
        }

        new TradesTask().execute();
    }

    private class TradesTask extends AsyncTask<Void, Integer, ArrayList> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";


        @Override
        protected void onPreExecute() {
            //setRetainInstance(true);
            super.onPreExecute();
            try {
                TradesDbManager tradesDbManager = new TradesDbManager();
        //        String waitStr = getResources().getString(R.string.waitStr);
        //        waitArray.add(new Product(waitStr,null,null));
        //        tradeListAdapter =  new TradeListAdapter(getActivity(),waitArray);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected ArrayList<Product> doInBackground(Void... params) {

            String pairLink = getResources().getString(R.string.pairLink);
            String[] pairListForLink = getResources().getStringArray(R.array.pairListForLink);

            try {
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

                    //String price_buy = "";
                    //String price_sell = "";

                    JSONObject dataJsonObj = new JSONObject(resultJson);
                    JSONArray fullTradesArr = dataJsonObj.getJSONArray(pairListForLink[p]);

                 //   for (int i = 0; i < 20; i++) {
                 //       JSONObject lineTrades = fullTradesArr.getJSONObject(i);
                 //       String typeTr = lineTrades.getString("type");
//
                 //       if (typeTr.equals("buy")) {
                 //           price_buy = lineTrades.getString("price");
                 //           break;
                 //       }
                 //   }
                 //   for (int j = 0; j < 20; j++) {
                 //       JSONObject lineTrades = fullTradesArr.getJSONObject(j);
                 //       String typeTr = lineTrades.getString("type");
//
                 //       if (typeTr.equals("sell")) {
                 //           price_sell = lineTrades.getString("price");
                 //           break;
                 //       }
                 //   }
                 //   coinArray.add(new Product(pairListForLink[p], price_sell, price_buy));
                    publishProgress(p);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //return coinArray;
            return;
        }

        @Override
        protected void onPostExecute(ArrayList ALLJson) {
            super.onPostExecute(ALLJson);
            try {
                //Log.d(LOG_TAG, "Весь текст: " + ALLJson);
                //ListView listView = (ListView)getView().findViewById(R.id.list_trades);
                //tradeListAdapter = new TradeListAdapter(getActivity(), ALLJson);
                //listView.setAdapter(tradeListAdapter);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //ProgressBar proBarTest = (ProgressBar)getView().findViewById(R.id.prog_bar);
            super.onProgressUpdate(values);
            try {
             //   proBarTest.setProgress(values[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

}