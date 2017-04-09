package com.sleepingbear.english;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    private static final int MY_PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        System.out.println("=============================================== App Start ======================================================================");
        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        //DB가 새로 생성이 되었으면 이전 데이타를 DB에 넣고 Flag를 N 처리함
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if ( "Y".equals(prefs.getString("db_new", "N")) ) {
            DicUtils.dicLog("backup data import");

            //DicUtils.readInfoFromFile(this, db);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("db_new", "N");
            editor.commit();
        };

        checkPermission();

        ((Button) findViewById(R.id.my_b_foreign_dic)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_han_dic)).setOnClickListener(this);

        ((Button) findViewById(R.id.my_b_news)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_news_word)).setOnClickListener(this);

        ((Button) findViewById(R.id.my_b_conversation_study)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_pattern)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_conv_search)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_conv_note)).setOnClickListener(this);

        ((Button) findViewById(R.id.my_b_naver_conv)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_category)).setOnClickListener(this);

        ((Button) findViewById(R.id.my_b_my_conv)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_voc)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_voc_study)).setOnClickListener(this);

        ((Button) findViewById(R.id.my_b_share)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_patch)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_help)).setOnClickListener(this);
        ((Button) findViewById(R.id.my_b_setting)).setOnClickListener(this);

        AdView av = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        av.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        DicUtils.dicLog("onClick");
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.my_b_foreign_dic:
                bundle.putString("KIND", CommConstants.dictionaryKind_f);

                Intent englishIntent = new Intent(getApplication(), DictionaryActivity.class);
                englishIntent.putExtras(bundle);
                startActivity(englishIntent);

                break;
            case R.id.my_b_han_dic:
                bundle.putString("KIND", CommConstants.dictionaryKind_h);

                Intent hanIntent = new Intent(getApplication(), DictionaryActivity.class);
                hanIntent.putExtras(bundle);
                startActivity(hanIntent);

                break;
            case R.id.my_b_news:
                Intent newsIntent = new Intent(getApplication(), NewsActivity.class);
                newsIntent.putExtras(bundle);
                startActivity(newsIntent);

                break;
            case R.id.my_b_news_word:
                Intent newClickWordIntent = new Intent(getApplication(), NewsClickWordActivity.class);
                newClickWordIntent.putExtras(bundle);
                startActivity(newClickWordIntent);

                break;
            case R.id.my_b_conversation_study:
                startActivity(new Intent(getApplication(), ConversationStudyActivity.class));

                break;
            case R.id.my_b_pattern:
                startActivity(new Intent(getApplication(), PatternActivity.class));
                break;
            case R.id.my_b_conv_search:
                startActivity(new Intent(getApplication(), ConversationActivity.class));
                break;
            case R.id.my_b_conv_note:
                startActivity(new Intent(getApplication(), ConversationNoteActivity.class));
                break;
            case R.id.my_b_naver_conv:
                break;
            case R.id.my_b_category:
                break;
            case R.id.my_b_my_conv:
                break;
            case R.id.my_b_voc:
                break;
            case R.id.my_b_voc_study:
                startActivity(new Intent(getApplication(), StudyActivity.class));

                break;
            case R.id.my_b_share:
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_SUBJECT, "최고의 영어학습 어플");
                msg.putExtra(Intent.EXTRA_TEXT, "영어.. 참 어렵죠? '최고의 영어학습' 어플을 사용해 보세요. https://play.google.com/store/apps/details?id=com.sleepingbear.ennewsvoc ");
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "어플 공유"));

                break;
            case R.id.my_b_patch :
                startActivity(new Intent(getApplication(), PatchActivity.class));
                break;
            case R.id.my_b_help :
                Intent helpIntent = new Intent(getApplication(), HelpActivity.class);
                helpIntent.putExtras(bundle);
                startActivity(helpIntent);
                break;
            case R.id.my_b_setting:
                startActivity(new Intent(getApplication(), SettingsActivity.class));

                break;
        }
    }

    public boolean checkPermission() {
        Log.d(CommConstants.tag, "checkPermission");
        boolean isCheck = false;
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ) {
            Log.d(CommConstants.tag, "권한 없음");
            if ( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ) {
                Toast.makeText(this, "(중요)파일로 내보내기, 가져오기를 하기 위해서 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
            Log.d(CommConstants.tag, "2222");
        } else {
            Log.d(CommConstants.tag, "권한 있음");
            isCheck = true;
        }

        return isCheck;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(CommConstants.tag, "권한 허가");
                } else {
                    Log.d(CommConstants.tag, "권한 거부");
                    Toast.makeText(this, "파일 권한이 없기 때문에 파일 내보내기, 가져오기를 할 수 없습니다.\n만일 권한 팝업이 안열리면 '다시 묻지 않기'를 선택하셨기 때문입니다.\n어플을 지우고 다시 설치하셔야 합니다.", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }
}
