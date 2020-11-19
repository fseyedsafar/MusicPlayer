package com.example.musicplayer.controller.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.MediaPlayerTasks;
import com.example.musicplayer.controller.fragment.FileListFragment;
import com.example.musicplayer.controller.fragment.MusicListFragment;
import com.example.musicplayer.model.Music;
import com.example.musicplayer.repository.Repository;
import com.example.musicplayer.utils.PhotoUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

public class MusicPagerActivity extends AppCompatActivity implements MusicListFragment.UISheetBottom {

    public static final String EXTRA_ID_MUSIC_PAGER_ACTIVITY = "extraIdMusicPagerActivity";
    public static final String EXTRA_CURRENT_PAGE_MUSIC_PAGER_ACTIVITY = "extraCurrentPageMusicPagerActivity";
    public static final int REQUEST_CODE_MUSIC_PAGER_ACTIVITY = 0;
    public static final int REQUEST_CODE_READ_EXTERNAL_PERMISSION = 1;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private LinearLayout mLinearLayout, mMusicListItem;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Fragment fragment;
    private FragmentPagerAdapter mAdapter;
    private LinearLayout mBottomSheetCoordinatorLayout;
    private MusicListFragment mMusicListFragment;
    private SeekBar mSeekBarBottom;
    private ImageView mImageViewBottom;
    private ImageButton mPlayPauseBottom;
    private TextView mMusicTitleBottom, mMusicArtistBottom;
    private Music mMusic;
    private int mCurrentPage;
    private MediaPlayerTasks mMediaPlayerTasks;

    public void setmMusic(Music mMusic) {
        this.mMusic = mMusic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_pager);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_PERMISSION);

        initUI();

        mBottomSheetCoordinatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MusicFullPageActivity.newIntent(MusicPagerActivity.this, mMusic.getmID(), mCurrentPage));
//                Intent intent = new Intent(MusicPagerActivity.this, MusicFullPageFragment.class);
//
//                intent.putExtra(EXTRA_ID_MUSIC_PAGER_ACTIVITY, mMusic.getmID());
//                intent.putExtra(EXTRA_CURRENT_PAGE_MUSIC_PAGER_ACTIVITY, mCurrentPage);
//
//                startActivityForResult(intent, REQUEST_CODE_MUSIC_PAGER_ACTIVITY);
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:{
                        return "Music";
                    }
                    case 1:{
                        return "Album";
                    }
                    case 2:{
                        return "Artist";
                    }
                }
                return null;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:{
                        mMusicListFragment = MusicListFragment.newInstance(position);
                        return mMusicListFragment;
                    }
                    case 1:{
                        return FileListFragment.newInstance(position);
                    }
                    case 2:{
                        return FileListFragment.newInstance(position);
                    }
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                } else {

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MusicPagerActivity.class);
        return intent;
    }

    private void initUI() {
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mLinearLayout = findViewById(R.id.linear_bottom_sheet);
        mMusicListItem = findViewById(R.id.music_list_item);
        mImageViewBottom = findViewById(R.id.image_view_bottom);
//        mSeekBarBottom = findViewById(R.id.seek_bar_bottom);
        mPlayPauseBottom = findViewById(R.id.pause_play);
        mMusicTitleBottom = findViewById(R.id.music_name_bottom);
        mMusicArtistBottom = findViewById(R.id.music_artist_bottom);
        mBottomSheetCoordinatorLayout = findViewById(R.id.coordinator_bottom_sheet);


//        mBottomSheetBehavior = BottomSheetBehavior.from(mLinearLayout);
//        mBottomSheetBehavior.setSkipCollapsed(false);
//        mBottomSheetBehavior.setPeekHeight(mBottomSheetCoordinatorLayout.getHeight());
        mCurrentPage = mViewPager.getCurrentItem();
//        mMediaPlayerTasks = MediaPlayerTasks.getTestMediaPlayer(getApplicationContext());
    }

    @Override
    public void setSheetBottom(Music music) {

        mMusic = music;

//        mBottomSheetCoordinatorLayout.setVisibility(View.VISIBLE);
        mBottomSheetCoordinatorLayout.setVisibility(View.VISIBLE);

        mMusicTitleBottom.setText(music.getmTitle());
        mMusicArtistBottom.setText(music.getmArtist());
        mImageViewBottom.setImageBitmap(PhotoUtils.getScaledBitmap(music.getmImage(), this));

//        MediaPlayer.getInstance(MusicPagerActivity.this).createAndSendNoification(music);

        mPlayPauseBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerTasks.getTestMediaPlayer(MusicPagerActivity.this).playPause(mPlayPauseBottom);

            }
        });

        MediaPlayerTasks.getTestMediaPlayer(MusicPagerActivity.this).setUIPlayPause(mPlayPauseBottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || resultCode != RESULT_OK){
            return;
        } if(requestCode == FileListFragment.REQUEST_CODE_FILE_LIST_FRAGMENT && data != null){
            setSheetBottom(Repository.getInstance(MusicPagerActivity.this).getMusic(Long.valueOf(getIntent().getStringExtra(FileListActivity.EXTRA_MUSIC_ID_FILE_LIST_ACTIVITY))));
        }
    }

    public void setUIPlayPause(ImageButton button){
        if (mMediaPlayerTasks.isPlaying()) {
            button.setBackgroundResource(R.drawable.ic_pause_activity);

        }else {
            button.setBackgroundResource(R.drawable.ic_play_activity);
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (data == null || resultCode != RESULT_OK){
//            return;
//        } else if(requestCode == REQUEST_CODE_MUSIC_PAGER_ACTIVITY && data != null){
//            Long id = (Long) getIntent().getSerializableExtra(MusicFullPageActivity.EXTRA_ID_MUSIC_FULL_PAGER_ACTIVITY);
//            setmMusic(Repository.getInstance(getApplicationContext()).getMusic(id));
//        }
//    }
}
