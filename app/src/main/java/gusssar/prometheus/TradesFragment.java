package gusssar.prometheus;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class TradesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String LOG_TAG = "myLogs";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TradesFragment() {
    }

    public static TradesFragment newInstance(String param1, String param2) {
        TradesFragment fragment = new TradesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "TradesFrag onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            }
          }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_trades, container, false);
        ListView listView = (ListView)view.findViewById(R.id.list_trades);
        String[] pairList = getResources().getStringArray(R.array.pairlist);
        String[] pairLink = getResources().getStringArray(R.array.pairlink);
            //ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_2,pairList);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item,pairList);
            listView.setAdapter(adapter);
        return view;
    }

//   private class CatAdapter extends ArrayAdapter<Cat> {

//       public CatAdapter(Context context) {
//           super(context, android.R.layout.simple_list_item_2, cats);
//       }

//       @Override
//       public View getView(int position, View convertView, ViewGroup parent) {
//           Cat cat = getItem(position);

//           if (convertView == null) {
//               convertView = LayoutInflater.from(getContext())
//                       .inflate(android.R.layout.simple_list_item_2, null);
//           }
//           ((TextView) convertView.findViewById(android.R.id.text1))
//                   .setText(cat.name);
//           ((TextView) convertView.findViewById(android.R.id.text2))
//                   .setText(cat.gender);
//           return convertView;
//       }
//   }




    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "торги", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

