package gusssar.prometheus;
/**
 * Этот класс создан в поытке вынести задачу периодического заполнения БД в отдельнй класс
 * */
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//private  class SetInBaseDataAsyncTask extends AsyncTask<Void, Integer, ArrayList> {
public  class SetInBaseDataAsyncTask extends AsyncTask<Void, Integer, ArrayList> {
    //class TradesTask extends AsyncTask<Void, Integer, ArrayList> {



    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String resultJson = "";
    ContentValues cv_trades = new ContentValues();
    final String LOG_TAG = "myLogs";
    TradesDbManager tradesDbManager;
    public ArrayList<TradeFullDataBase> tradeFullArray = new ArrayList<>();

    private static String pairLink;
    private static String[] pairListForLink;
    private   SetInBaseDataAsyncTask (Context context) {
            pairLink = context.getResources().getString(R.string.pairLink);
            pairListForLink = context.getResources().getStringArray(R.array.pairListForLink);
        //return null;
    }

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

        //String pairLink = context.getResources().getString(R.string.pairLink);
        //String[] pairListForLink = getResources().getStringArray(R.array.pairListForLink);

        SQLiteDatabase db_trades = tradesDbManager.getWritableDatabase();
        /**предварительная очистка базы!*/
        for (int y = 0; y <= 52; y++) {
            db_trades.delete(pairListForLink[y], null, null);
        }
        //ContentValues cv_trades = new ContentValues();
        try {
            //for (int p=0; p <= 52; p++) {
            for (int p = 0; p <= 2; p++) {
                String setUrl = pairLink + pairListForLink[p];
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
