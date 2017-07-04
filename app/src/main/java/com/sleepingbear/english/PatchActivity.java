package com.sleepingbear.english;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PatchActivity extends AppCompatActivity {
    private int fontSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = (ActionBar) getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        StringBuffer patch = new StringBuffer();

        patch.append("* 영어 공부에 도움이 될만한 기능을 모아서 어플을 개발하게 되었습니다." + CommConstants.sqlCR);
        patch.append("사용하시다가 문제점이나 개선사항이 있으면 메일을 보내주세요." + CommConstants.sqlCR);
        patch.append("개발에 참고하겠습니다." + CommConstants.sqlCR);
        patch.append("영어 공부에 많은 도움이 되었으면 합니다." + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);
        patch.append("" + CommConstants.sqlCR);

        patch.append("* 패치 내역" + CommConstants.sqlCR);
        patch.append("- 영문 소설 기능 수정 : 페이지 단위로 보도록 수정, 무료 영문소설 사이트 추가" + CommConstants.sqlCR);
        patch.append("- 영문 소설 보는 기능 추가" + CommConstants.sqlCR);
        patch.append("- 사전에서 한글 검색시 단어체크 여부에 상관없이 한글 검색이 되도록 수정" + CommConstants.sqlCR);
        patch.append("- 영한사전, 한영 사전을 한 화면으로 통합" + CommConstants.sqlCR);
        patch.append("- Daum 단어장에 동기화 안되는 문제점 수정" + CommConstants.sqlCR);
        patch.append("- 오늘의 단어 기능 추가" + CommConstants.sqlCR);
        patch.append("- 숙어 모음 및 예제 보기 기능 추가" + CommConstants.sqlCR);
        patch.append("- 2017.05.01 : 영어 학습 어플 통합 개발" + CommConstants.sqlCR);

        ((TextView) this.findViewById(R.id.my_c_patch_tv1)).setText(patch.toString());

        fontSize = Integer.parseInt( DicUtils.getPreferencesValue( this, CommConstants.preferences_font ) );
        ((TextView) this.findViewById(R.id.my_c_patch_tv1)).setTextSize(fontSize);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}