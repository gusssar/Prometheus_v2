package gusssar.prometheus;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.view.View.OnClickListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

import android.app.Activity;

public class UserFragment extends Fragment {
    /**Связка Fragment vs Activity*/
    public interface onSomeEventListener {
        public void someEvent(String s);
    }

    onSomeEventListener someEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }
    /***/

    //public ArrayList<TradeFullDataBase> tradeFullArray = new ArrayList<>();
    public ArrayList<Product> tradeFullArray = new ArrayList<>();
    public ArrayList<Product> waitArray = new ArrayList<>();
    TradeListAdapter tradeListAdapter;
    TradesDbManager tradesDbManager;

    public static String LOG_TAG = "my_log";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //tradesDbManager = new TradesDbManager(this);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_user);
            tradesDbManager = new TradesDbManager(getActivity());
            //final SQLiteDatabase db_trades = tradesDbManager.getReadableDatabase();
                new ProgressTask().execute();
        listView.setAdapter(tradeListAdapter);

            Button textButton = (Button) view.findViewById(R.id.btn_user);
                textButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**
                         * Здесь необходимо прописать обработчик нажатия для обновления БД*/
                        //Toast.makeText(getActivity(),"нажата кнопка",Toast.LENGTH_SHORT).show();
                        someEventListener.someEvent("Test text to User Fragment");
                         /** */
                    }
                });

        return view;
    }

    private class ProgressTask extends AsyncTask<Void, Integer, ArrayList> {
        //HttpURLConnection urlConnection = null;
        //BufferedReader reader = null;
        //String resultJson = "";

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
        //protected ArrayList<TradeFullDataBase> doInBackground(Void... params) {
        protected ArrayList<Product> doInBackground(Void... params) {
            SQLiteDatabase db_trades = tradesDbManager.getWritableDatabase();
            String[] pairListForLink = getResources().getStringArray(R.array.pairListForLink);
            try {
                Cursor c = db_trades.query(pairListForLink[0],null,null,null,null,null,null,null);
                    if (c.moveToLast()){
                        //получаем индексы
                        int table_trade_id  = c.getColumnIndex("TABLE_TRADE_ID");
                        int table_type      = c.getColumnIndex("TABLE_TYPE");
                        int table_price     = c.getColumnIndex("TABLE_PRICE");
                        int table_quantity  = c.getColumnIndex("TABLE_QUANTITY");
                        int table_amount    = c.getColumnIndex("TABLE_AMOUNT");
                        int table_date      = c.getColumnIndex("TABLE_DATE");

                        do {
                            // получаем значения по номерам столбцов и пишем все в лог
                            Log.d(LOG_TAG,
                                    "User_FR_table_trade_id = "     + c.getInt(table_trade_id) +
                                            ", table_type = "    + c.getString(table_type) +
                                            ", table_quantity = "    + c.getString(table_quantity) +
                                            ", table_price = "    + c.getString(table_price));
                            // переход на следующую строку
                            // а если следующей нет (текущая - последняя), то false - выходим из цикла

                                    String   TABLE_TRADE_ID = c.getString(table_trade_id);
                                    String   TABLE_TYPE = c.getString(table_type);
                                    String   TABLE_PRICE = c.getString(table_price);
                                    //Double   TABLE_QUANTITY = c.getDouble(table_quantity);
                                            String   TABLE_QUANTITY = c.getString(table_quantity);
                                    Double   TABLE_AMOUNT = c.getDouble(table_amount);
                                    Integer  TABLE_DATE = c.getInt(table_date);

//                            tradeFullArray.add(new TradeFullDataBase(
//                                    TABLE_TRADE_ID,
//                                    TABLE_TYPE,
//                                    TABLE_PRICE,
//                                    TABLE_QUANTITY,
//                                    TABLE_AMOUNT,
//                                    TABLE_DATE
//                            ));
                                tradeFullArray.add(new Product(
                                        TABLE_TRADE_ID,
                                        TABLE_TYPE,
                                        //TABLE_PRICE
                                        TABLE_QUANTITY
                                ));
                        } while (c.moveToPrevious());
                    } else
                        Log.d(LOG_TAG, "0 rows");
                c.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            db_trades.close();
            return tradeFullArray;
        }

        @Override
        protected void onPostExecute(ArrayList FromBackArray) {
            super.onPostExecute(FromBackArray);
            Log.d(LOG_TAG, "Весь текст: " + FromBackArray);

            try {
            //   JSONArray data_JsonObj = new JSONArray(strJson);
            //   String arrayString = data_JsonObj.toString();
            //   Log.d(LOG_TAG, "В строке массив: " + arrayString);
//
            //   for (int j=0; j < data_JsonObj.length(); j++){
            //       Log.d(LOG_TAG, "В цикле: " + j);
            //       String object = data_JsonObj.getString(j);
            //       Log.d(LOG_TAG, "имя: " + object);
//
            //       coinArray.add(new Product(object,null,null));
                   ListView listView = (ListView)getView().findViewById(R.id.list_user);
                   tradeListAdapter = new TradeListAdapter(getActivity(), FromBackArray);
                   listView.setAdapter(tradeListAdapter);
            //   }
//
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ShortTrades {

    Integer name1;
    String name2;
    Double name3;

    ShortTrades(Integer _describe, String _describe2, Double _describe3)
    {
        name1 = _describe;
        name2 = _describe2;
        name3 = _describe3;
    }
}
