package gusssar.prometheus;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import java.lang.annotation.Repeatable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    public String setUrl = "";
    public ArrayList<TradeFullDataBase> tradeFullArray = new ArrayList<>();
    TradesDbManager tradesDbManager;
    //private Handler handler = new Handler();
    //проба с таймером
    //private Timer timerExecutor = new Timer();
    //private TimerTask doAsynchronousTaskExecutor;

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
    protected void onCreate(Bundle savedInstanceState)  {
    //public void onCreate(Bundle savedInstanceState) {
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
        tradesDbManager = new TradesDbManager(this);
        //new Timer().schedule(new RepeatTimerTask(),0,10000);

                new TradesTask().execute();
                //RepeatTimer();
    }

    /**проба повтора с задержкой*/
//    public void RepeatTimer() {
//        final Handler handler = new Handler();
//        doAsynchronousTaskExecutor = new TimerTask() {
//            //@Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        try {
                            //@Override
                            private  class TradesTask extends AsyncTask<Void, Integer, ArrayList> {
                            //class TradesTask extends AsyncTask<Void, Integer, ArrayList> {

                                HttpURLConnection urlConnection = null;
                                BufferedReader reader = null;
                                String resultJson = "";
                                ContentValues cv_trades = new ContentValues();

                                @Override
                                protected void onPreExecute() {
                                    //setRetainInstance(true);
                                    super.onPreExecute();
                                    try {
                                        // TradesDbManager tradesDbManager = new TradesDbManager();
                                        //        String waitStr = getResources().getString(R.string.waitStr);
                                        //        waitArray.add(new Product(waitStr,null,null));
                                        //        tradeListAdapter =  new TradeListAdapter(getActivity(),waitArray);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                                //для заполнения полного массива
                                @Override
                                protected ArrayList<TradeFullDataBase> doInBackground(Void... params) {

                                    String pairLink = getResources().getString(R.string.pairLink);
                                    String[] pairListForLink = getResources().getStringArray(R.array.pairListForLink);

                                    SQLiteDatabase db_trades = tradesDbManager.getWritableDatabase();
                                    //предварительная очистка базы!
                                    for (int y = 0; y <= 52; y++) {
                                        db_trades.delete(pairListForLink[y], null, null);
                                    }
                                    //ContentValues cv_trades = new ContentValues();
                                    try {
                                        //for (int p=0; p <= 52; p++) {
                                        for (int p = 0; p <= 2; p++) {
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


                                            for (int i = 0; i < 20; i++) {
                                                //for (int i = 0; i <= 2; i++) {
                                                JSONObject lineTrades = fullTradesArr.getJSONObject(i);
                                                Integer idTr = lineTrades.getInt("trade_id");
                                                String typeTr = lineTrades.getString("type");
                                                Double priceTr = lineTrades.getDouble("price");
                                                Double quantityTr = lineTrades.getDouble("quantity");
                                                Double amountTr = lineTrades.getDouble("amount");
                                                Integer dateTr = lineTrades.getInt("date");

                                                //tradeFullArray.add(
                                                //        new TradeFullDataBase(
                                                //                idTr,
                                                //                typeTr,
                                                //                priceTr,
                                                //                quantityTr,
                                                //                amountTr,
                                                //                dataTr
                                                //        ));
                                                cv_trades.put("TABLE_TRADE_ID", idTr);
                                                cv_trades.put("TABLE_TYPE", typeTr);
                                                cv_trades.put("TABLE_PRICE", priceTr);
                                                cv_trades.put("TABLE_QUANTITY", quantityTr);
                                                cv_trades.put("TABLE_AMOUNT", amountTr);
                                                cv_trades.put("TABLE_DATE", dateTr);
                                                Log.d(LOG_TAG, "cv_trades =" + cv_trades);
                                                db_trades.insert(pairListForLink[p], null, cv_trades);
                                                //if (typeTr.equals("buy")) {
                                                //    price_buy = lineTrades.getString("price");
                                                //    break;
                                                //}
                                            }
                                            //for (int j = 0; j < 20; j++) {
                                            //    JSONObject lineTrades = fullTradesArr.getJSONObject(j);
                                            //    String typeTr = lineTrades.getString("type");
//
                                            //    if (typeTr.equals("sell")) {
                                            //        price_sell = lineTrades.getString("price");
                                            //        break;
                                            //    }
                                            //}
                                            //coinArray.add(new TradeFullDataBase(pairListForLink[p], price_sell, price_buy));

                                            publishProgress(p);
                                            Log.d(LOG_TAG, "--------ALEXEEV----------MAIN-------------ALEXEEV-----");

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //return coinArray;
                                    tradesDbManager.close();
                                    return tradeFullArray;
                                }

                                @Override
                                protected void onPostExecute(ArrayList ALLJson) {
                                    super.onPostExecute(ALLJson);
                                    try {
                                        //Log.d(LOG_TAG, "Весь текст: " + ALLJson);
                                        //ListView listView = (ListView)getView().findViewById(R.id.list_trades);
                                        //tradeListAdapter = new TradeListAdapter(getActivity(), ALLJson);
                                        //listView.setAdapter(tradeListAdapter);
                                    } catch (Exception e) {
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
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        };
//        timerExecutor.schedule(doAsynchronousTaskExecutor, 0, 4000);
//    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }


/**
public class RepeatTimerTask extends TimerTask {
    private Runnable runnable = new Runnable() {
        public void run() {
            new TradesTask().execute();
        }
    };

    public void run() {
        handler.post(runnable);
    }
}
*/
}
class TradeFullDataBase {

    Integer TABLE_TRADE_ID;
    String  TABLE_TYPE;
    Double   TABLE_PRICE;
    Double   TABLE_QUANTITY;
    Double   TABLE_AMOUNT;
    Integer  TABLE_DATE;

    TradeFullDataBase (Integer _describe1,
                       String _describe2,
                       Double _describe3,
                       Double _describe4,
                       Double _describe5,
                       Integer _describe6) {

        TABLE_TRADE_ID  = _describe1;
        TABLE_TYPE      = _describe2;
        TABLE_PRICE     = _describe3;
        TABLE_QUANTITY  = _describe4;
        TABLE_AMOUNT    = _describe5;
        TABLE_DATE      = _describe6;

    }
}