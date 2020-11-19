package com.example.musicplayer.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.fragment.MusicFullPageFragment;
import com.example.musicplayer.model.Music;

public class MusicFullPageActivity extends AppCompatActivity implements MusicFullPageFragment.MusicObject {

    public static final String EXTRA_ID_MUSIC_FULL_PAGER_ACTIVITY = "extraIdMusicFullPagerActivity";
    public static final String EXTRA_ID_FULL_PAGE_ACTIVITY = "extraIdFullPageActivity";
    public static final String EXTRA_CURRENT_PAGE_FULL_PAGE_ACTIVITY = "extraCurrentPageFullPageActivity";
    private Music mMusic;
    private static MusicPagerActivity mContext;

    public void setmMusic(Music mMusic) {
        this.mMusic = mMusic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_full_page);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_music_full_page);
        if (fragment == null) {
            fragmentManager.beginTransaction().add(R.id.activity_music_full_page, MusicFullPageFragment.newInstance((Long) getIntent().getSerializableExtra(EXTRA_ID_FULL_PAGE_ACTIVITY), (int) getIntent().getSerializableExtra(EXTRA_CURRENT_PAGE_FULL_PAGE_ACTIVITY))).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mContext.setSheetBottom(mMusic);

//        Intent intent = new Intent();
//        intent.putExtra(EXTRA_ID_MUSIC_FULL_PAGER_ACTIVITY, mMusic.getmID());
//        setResult(RESULT_OK, intent);
    }

    public static Intent newIntent(Context context, Long mID, int currentPage) {

        mContext = (MusicPagerActivity) context;

        Intent intent = new Intent(context, MusicFullPageActivity.class);

        intent.putExtra(EXTRA_ID_FULL_PAGE_ACTIVITY, mID);
        intent.putExtra(EXTRA_CURRENT_PAGE_FULL_PAGE_ACTIVITY, currentPage);

        return intent;
    }

    @Override
    public void getMusic(Music music) {
        setmMusic(music);
    }
}
