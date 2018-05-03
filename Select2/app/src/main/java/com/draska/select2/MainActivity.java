package com.draska.select2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.draska.select2.api.IWmsService;
import com.draska.select2.api.WmsService;

import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView search_TextView;
    CardView search_Card;
    EditText search_Keyword;
    ListView search_Result;
    Button search_ButtonClear;

    Timer mTimer;
    IWmsService mService;

    boolean mToggleSearch = true;
    String mLastKeyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = WmsService.createService(IWmsService.class, getApplicationContext());

        search_TextView = findViewById(R.id.search_TextView);
        search_Card = findViewById(R.id.search_Card);
        search_Keyword = findViewById(R.id.search_Keyword);
        search_Result = findViewById(R.id.search_Result);
        search_ButtonClear = findViewById(R.id.search_ButtonClear);

        search_TextView.setOnClickListener(this);
        search_ButtonClear.setOnClickListener(this);
        search_Keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mTimer != null) mTimer.cancel();
            }

            @Override
            public void afterTextChanged(final Editable editable) {

                final String keyword = search_Keyword.getText().toString();
                mLastKeyword = keyword;

                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(!keyword.equals("")) {
                            Log.d("afterTextChanged", "Do Search");
                            remoteSearch(editable.toString());
                        } else {
                            setListDataByRespose("");
                        }
                    }
                }, 800);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_TextView:
                onSearch_TextViewClick();
                break;
            case R.id.search_ButtonClear:
                onSearch_ButtonClear();
                break;
        }
    }

    private void onSearch_ButtonClear() {
        search_TextView.setText("");
    }

    private void onSearch_TextViewClick() {
        toggleSearch();
    }

    private void toggleSearch() {
        if(mToggleSearch) {
            search_Card.setVisibility(View.VISIBLE);
            search_Keyword.setText(mLastKeyword);
            search_Keyword.requestFocus();
            showKeyboard(true);
        } else {
            search_Card.setVisibility(View.GONE);
            showKeyboard(false);
        }
        mToggleSearch = mToggleSearch ? false : true;
    }

    private void showKeyboard(boolean bool) {
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        if (bool)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
        else
            imm.hideSoftInputFromWindow(search_Keyword.getWindowToken(), 0);
    }

    private void remoteSearch(String keyword) {
        try {
            mService.getSources("source_search", keyword)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            setListDataByRespose(response.body());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("HANCA", "Failure");
                        }
                    });
        } catch (Exception e) {
            Log.d("HANCA", e.getMessage());
        }
    }

    private void setListDataByRespose(String response) {
        ArrayAdapter<String> adapter;
        final String[] lists;

        if (!response.equals("")) {
            try {

                JSONArray json = new JSONArray(response);
                if (json.length() > 0) {
                    lists = new String[json.length()];
                    for (int i = 0; i < json.length(); i++) {
                        lists[i] = json.getJSONObject(i).getString("SOURCE");
                    }

                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item, lists);
                    search_Result.setAdapter(adapter);
                    search_Result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String text = lists[i];
                            search_TextView.setText(text);

                            toggleSearch();
                        }
                    });
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    search_Result.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item, new String[]{}));
                }
            });

        }


    }
}
