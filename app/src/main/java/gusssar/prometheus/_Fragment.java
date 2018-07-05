package gusssar.prometheus;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.app.Activity;

public class _Fragment extends Fragment {

    public ArrayList<Product> coinArray = new ArrayList<>();
    public ArrayList<Product> waitArray = new ArrayList<>();
    TradeListAdapter tradeListAdapter;
    TradesDbManager tradesDbManager;

    public interface onSomeEventListener {
        public void someEvent(String s);
    }
    SwipeRefreshLayout mSwipeRefreshLayout;
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

        listView.setAdapter(tradeListAdapter);

        int count_list = 0;
        SQLiteDatabase db_trades = tradesDbManager.getWritableDatabase();
        String[] pairListForLink = getResources().getStringArray(R.array.pairListForLink);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.blue_swipe, R.color.green_swipe,
                R.color.orange_swipe, R.color.red_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    //someEventListener.someEvent("Test text from SwipeRefresh");
                    //new ProgressTask().execute();

                    Cursor c = db_trades.query(pairListForLink[0],null,null,null,null,null,null,null);
                    if (c.moveToLast()){
                        //if (c.moveToPosition(20)){
                        //получаем индексы
                        int table_trade_id  = c.getColumnIndex("TABLE_TRADE_ID");
                        int table_type      = c.getColumnIndex("TABLE_TYPE");
                        int table_price     = c.getColumnIndex("TABLE_PRICE");
                        int table_quantity  = c.getColumnIndex("TABLE_QUANTITY");
                        int table_amount    = c.getColumnIndex("TABLE_AMOUNT");
                        int table_date      = c.getColumnIndex("TABLE_DATE");

                        do {
                            count_list++;
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
                            if (count_list <= 15)
                            {
                                tradeFullArray.add(new Product(
                                        TABLE_TRADE_ID,
                                        TABLE_TYPE,
                                        //TABLE_PRICE
                                        TABLE_QUANTITY
                                ));
                            }  else return tradeFullArray;
                        } while (c.moveToPrevious());
                        //} while (c.moveToPosition(2));
                    } else
                        Log.d(LOG_TAG, "0 rows");
                    c.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
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