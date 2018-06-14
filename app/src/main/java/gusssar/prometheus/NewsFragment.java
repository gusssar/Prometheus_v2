package gusssar.prometheus;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsFragment extends Fragment {
    final String LOG_TAG = "myLogs";
    ProgressBar proBar;
    public ArrayList<Product> coinArray = new ArrayList<>();
    //private ProgressDialog mDialog;
    private final int mTotalTime = 70;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "NewsFrag onCreate");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int counter = 0;
        View view=inflater.inflate(R.layout.fragment_news, container, false);
        //ProgressBar proBar = (ProgressBar) view.findViewById(R.id.prog_bar);
        //while(counter <= mTotalTime){
        //    try {
        //        Thread.sleep(500);
        //        counter ++;
        //        proBar.setProgress(counter);
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
//
        //}
        return view;
    }

    private class ProgressTask extends AsyncTask<Void, Integer, ArrayList> {

        ProgressBar proBar = (ProgressBar)getView().findViewById(R.id.prog_bar);
       // ProgressBar prbr = getResources().getP
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
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

            try {
                //String[] pairListForLinkTest = getResources().getStringArray(R.array.pairListForLink);
                //coinArray.add(new Product(pairListForLink[1], price_sell, price_buy));
                for (int p=0; p <= 2; p++) {
                    Thread.sleep(500);
                    p ++;
                    proBar.setProgress(p);
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
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            try {
                proBar.setProgress(values[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



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
            Toast.makeText(context, "новости", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }




  // private void postProgress(int progress) {
  //     //String strProgress = String.valueOf(progress) + " %";
  //     proBar.setProgress(progress);
  //      }

}
