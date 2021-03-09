package com.example.projectnavernews;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.Html.fromHtml;

public class MainActivity extends AppCompatActivity {

    private TextView tv_rank, tv_title, tv_content, tv_link;
    private EditText et_search;
    private Button btn_search;
    String keyword;
    String[][] result_data = new String[20][3];
    int rank = 1;

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

        tv_rank = (TextView)findViewById(R.id.tv_rank);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_content = (TextView)findViewById(R.id.tv_content);
        tv_link = (TextView)findViewById(R.id.tv_link);

        btn_search = (Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                et_search = (EditText)findViewById(R.id.et_search);
                keyword = et_search.getText().toString();

//                Log.e("쓰레드들어가기전",keyword);


                // 네트워크 연결은 Thread 생성 필요
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

//                        Log.e("쓰레드들어온후","여기까지오는지체크");

                        try {
                            if(getNaverNewsJson(keyword)) {
//                                flag = 1;
//                                Log.e("네이버API가져온후","여기까지오는지체크");
                                runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void run() {
                                        Log.e("result_data 길이 ", String.valueOf(result_data.length));
                                        for(int i=0;i<result_data.length;i++) {

                                            ResultData resultData = new ResultData(String.valueOf(rank), result_data[i][0], result_data[i][1], result_data[i][2]);
                                            arrayList.add(resultData);
                                            mainAdapter.notifyDataSetChanged();
                                            rank++;

                                            // 이건 여기서 세팅하는게 x
//                                            tv_title.setText(result_data[i][0]+"");
//                                            tv_title.setClickable(true);
//                                            tv_title.setMovementMethod(LinkMovementMethod.getInstance());
//                                            String linktext = "<a href='"+result_data[i][2]+"'>"+tv_title.getText()+"</a>";
//                                            tv_title.setText(Html.fromHtml(linktext, Html.FROM_HTML_MODE_COMPACT));
//                                            tv_content.setText(result_data[i][1]);
//                                            tv_link.setText(result_data[i][2]);

                                        }
                                    }
                                });
                            } else { }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

//                Log.e("에러나기전","여기까지오는지체크");

//                Log.e("tv_title값",tv_title.getText().toString());
//                Log.e("tv_content값",tv_content.getText().toString());


//                if(flag == 1) {
////                ResultData resultData = new ResultData(tv_title.getText().toString(),tv_content.getText().toString(),tv_link.getText().toString());
//                    Log.e("result_data.length 값", String.valueOf(result_data.length));
//                    for (int i=0;i<result_data.length;i++) {
//                        ResultData resultData = new ResultData(result_data[i][0], result_data[i][1], result_data[i][2]);
//                        arrayList.add(resultData);
//                        mainAdapter.notifyDataSetChanged();
//                    }
//                }

            }
        });

    }

    public boolean getNaverNewsJson(String keyword) { // json 형태
        String clientID = "AINAdj8QhDDClfeZPneY";
        String clientSecret = "DM8CS94a1s";
        int display = 20; // 보여지는 검색결과의 수
        String sort = "date"; // 날짜순으로 정렬
        StringBuilder sb;

        Log.e("네이버API함수 안 ", keyword);
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

//            Log.e("가져온 API 데이터", data);


            String[] array;
            array = data.split("\"");
//            StringBuffer result = new StringBuffer();

//            Log.e("array 길이", String.valueOf(array.length));
//            Log.e("array[15]", array[15]);

            int k=0;
            for(int i=0;i<array.length;i++) {
                if (array[i].equals("title")) {
//                    Log.e("i값", String.valueOf(i));
                    result_data[k][0] = array[i+2].replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
//                    Log.e("title 값", array[i+2]);
                }
                if (array[i].equals("link")) {
                    result_data[k][2] = array[i+2].replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
//                    Log.e("link 값", array[i+2]);
                }
                if (array[i].equals("description")) {
                    result_data[k][1] = array[i+2].replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
//                    Log.e("description 값", array[i+2]);
                    k++;
                }

            }
//            Log.e("k", String.valueOf(k));
//            Log.e("array 길이", String.valueOf(result_data.length));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}