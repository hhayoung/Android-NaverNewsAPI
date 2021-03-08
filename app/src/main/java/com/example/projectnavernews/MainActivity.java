package com.example.projectnavernews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tv_title, tv_content;
    String keyword, str;
    String[][] result_data;

    private ArrayList<ResultData> arrayList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.rv_result);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        mainAdapter = new MainAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        // 네트워크 연결은 Thread 생성 필요
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String[][] setResult = getNaverNewsJson(keyword);
                try {
                    Intent intent = getIntent();
                    keyword = intent.getStringExtra("keyword");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_title = (TextView)findViewById(R.id.tv_title);
                            tv_content = (TextView)findViewById(R.id.tv_content);

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        Button btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultData resultData = new ResultData(tv_title.getText().toString(),tv_content.getText().toString());
                arrayList.add(resultData);
                mainAdapter.notifyDataSetChanged();
            }
        });





    }

    public String[][] getNaverNewsJson(String keyword) { // json 형태
        String clientID = "AINAdj8QhDDClfeZPneY";
        String clientSecret = "DM8CS94a1s";
        int display = 20; // 보여지는 검색결과의 수
        String sort = "date"; // 날짜순으로 정렬
        StringBuilder sb;

        try {
            String text = URLEncoder.encode(keyword, "UTF-8");

            String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + text + "&display=" + display + "&sort=" + sort; // json 결과

            // Json 형태로 결과값을 받아옴.
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientID);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            sb = new StringBuilder();

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine + "\n");
            }
            br.close();
            con.disconnect();

            String data = sb.toString();

            String[] array;
            array = data.split("\"");
            StringBuffer result = new StringBuffer();

            for(int i=0;i<array.length;i++) {
                if (array[i].equals("title")) {
                    result_data[i][0] = array[i+2];
                }

                if (array[i].equals("link")) {
                    result_data[i][1] = array[i+2];
                }
                if (array[i].equals("description")) {
                    result_data[i][2] = array[i+2];
                }
            }
            return result_data;

        } catch (Exception e) {
            e.printStackTrace();
            return result_data;
        }
    }
}