package gusssar.prometheus;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class TradesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ArrayList<Product> products = new ArrayList<>();
    TradeListAdapter tradeListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trades, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_trades);
        fillData();
        tradeListAdapter = new TradeListAdapter(getActivity(), products);
        listView.setAdapter(tradeListAdapter);
        return view;
    }

    //заполнение массива
    void fillData() {
        String[] pairList = getResources().getStringArray(R.array.pairlist);
        String[] pairLink = getResources().getStringArray(R.array.pairlink);
        for (int i = 1; i <= 20; i++) {
            products.add(new Product(pairList[i], pairList[i], pairList[i]));
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

