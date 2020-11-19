package com.example.musicplayer.controller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.fragment.MusicListFragment;
import com.example.musicplayer.controller.fragment.SheetBottomFragment;

public class FileListActivity extends AppCompatActivity {

    public static final String EXTRA_CURRENT_PAGE_FILE_LIST_ACTIVITY = "extraCurrentPageFileListActivity";
    public static final String EXTRA_ID_FILE_LIST_ACTIVITY = "extraIdFileListActivity";
    public static final String EXTRA_MUSIC_ID_FILE_LIST_ACTIVITY = "extraMusicIdFileListActivity";
    private MusicListFragment mMusicListFragment;
    private SheetBottomFragment mSheetBottomFragment;
    private int mCurrentPage;
    private Long mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        mCurrentPage = (int) getIntent().getSerializableExtra(EXTRA_CURRENT_PAGE_FILE_LIST_ACTIVITY);
        mMusicListFragment = MusicListFragment.newInstance(mCurrentPage);
        mSheetBottomFragment = SheetBottomFragment.newInstance(mCurrentPage);
        mID = (Long) getIntent().getSerializableExtra(EXTRA_ID_FILE_LIST_ACTIVITY);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container_file_activity);
        if (fragment == null) {
            fragmentManager.beginTransaction().add(R.id.container_file_activity, mMusicListFragment).commit();
        }

        Fragment fragment1 = fragmentManager.findFragmentById(R.id.container_bottom_sheet_file_activity);
        if (fragment1 == null) {
            fragmentManager.beginTransaction().add(R.id.container_bottom_sheet_file_activity, mSheetBottomFragment).commit();
        }

        mMusicListFragment.setId(mID);

        mMusicListFragment.setUISheetBottom(mSheetBottomFragment);
    }

    public static Intent newIntent(Context context, int currentPage, Long id){
        Intent intent = new Intent(context, FileListActivity.class);

        intent.putExtra(EXTRA_CURRENT_PAGE_FILE_LIST_ACTIVITY, currentPage);
        intent.putExtra(EXTRA_ID_FILE_LIST_ACTIVITY, id);

        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        Intent intent = new Intent();
//        intent.putExtra(EXTRA_MUSIC_ID_FILE_LIST_ACTIVITY, mMusicListFragment.getmMusic().getmID());
//        setResult(RESULT_OK, intent);
    }
}
