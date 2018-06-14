package gusssar.prometheus;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    //private TextView textView;
    final String LOG_TAG = "myLogs";

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
    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

}