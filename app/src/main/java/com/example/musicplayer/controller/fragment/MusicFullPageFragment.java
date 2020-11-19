package com.example.musicplayer.controller.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.MediaPlayerTasks;
import com.example.musicplayer.model.Music;
import com.example.musicplayer.prefs.MusicPrefences;
import com.example.musicplayer.repository.Repository;
import com.example.musicplayer.utils.PhotoUtils;
import com.example.musicplayer.utils.SeekbarSetting;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class MusicFullPageFragment extends Fragment implements MediaPlayerTasks.PrevNextInterface {

    public static final String ARG_UUID_FULL_PAGE = "argUuidFullPage";
    public static final String ARG_CURRENT_PAGE_FULL_PAGE = "argCurrentPageFullPage";
    public static final String TAG = "tag";
    private android.media.MediaPlayer mMediaPlayer;
    private ImageView mImage;
    private LinearLayout mLinearLayout;
    private ImageButton mPausePlayButton, mPrevButton, mNextButton, mShuffleButton, mRepeatAllButton;
    private SeekBar mSeekBar;
    private TextView mProgressSeekBar, mDurationSeekBar, mMusicTitle;
    private Handler mHandler;
    private int currentPage;
    private Music mMusic;
    private boolean mShuffleFlag, mRepeatAllFlag, mRepeatOneFlag;
    private String mRepeat = "no repeat";
    private MusicObject mMusicObject;
    private Runnable mRunnable;
    private MediaPlayerTasks mMediaPlayerTasks;

    public static MusicFullPageFragment newInstance(Long mID, int currentPage) {
        
        Bundle args = new Bundle();

        args.putSerializable(ARG_UUID_FULL_PAGE, mID);
        args.putSerializable(ARG_CURRENT_PAGE_FULL_PAGE, currentPage);

        MusicFullPageFragment fragment = new MusicFullPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMusic = Repository.getInstance(getActivity()).getMusic((Long) getArguments().getSerializable(ARG_UUID_FULL_PAGE));
        currentPage = (int) getArguments().getSerializable(ARG_CURRENT_PAGE_FULL_PAGE);
        mMediaPlayerTasks = MediaPlayerTasks.getTestMediaPlayer(getActivity());
        mMediaPlayer = MediaPlayerTasks.getMediaPlayer();
        mHandler = new Handler();
        mMusicObject.getMusic(mMusic);
        mMediaPlayerTasks.setPrevNextInterface(this);
        mMediaPlayerTasks.setMusic(mMusic);
        mRepeatOneFlag = MusicPrefences.getRepeatOne(getActivity());
        mShuffleFlag = MusicPrefences.getShuffle(getActivity());
    }

    public void setmMusic(Music mMusic) {
        this.mMusic = mMusic;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_page, container, false);

        initUI(view);

        setUI();

        initRunnable();

        initListener();

        updateProgress();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setUI() {
        try {
            if (mMusic.getmImage() != null){
                mImage.setImageBitmap(PhotoUtils.getScaledBitmap(mMusic.getmImage(), getActivity()));
                mImage.setBackground(new BitmapDrawable(getResources(), PhotoUtils.getScaledBitmap(mMusic.getmImage(), getActivity())));

//            Picasso.with(getActivity()).load(Uri.parse("file://" + mMusic.getmImage())).transform(new BlurTransformation(getActivity().getApplicationContext(), 20, 10)).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    Log.d("tagMusic","helloooo");
//                    mLinearLayout.setBackground(new BitmapDrawable(bitmap));
//                }
//
//                @Override
//                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            });
//        } else {
//            mImage.setImageResource(R.drawable.background_image);
////            mLinearLayout.setBackgroundColor(Color.BLACK);
            }
        }catch (Exception e){

        }
        try {
            mMusicTitle.setText(mMusic.getmTitle());
        }catch (Exception e){

        }

        setFlag();
        mMediaPlayerTasks.setUIPlayPause(mPausePlayButton);
    }

    private void initListener() {

        mPausePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayerTasks.playPause(mPausePlayButton);
            }
        });

//        mPrevButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMediaPlayerTasks.prevMusic(mMusic.getmID(), currentPage);
//            }
//        });

//        mNextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mMediaPlayerTasks.nextMusic(mMusic.getmID(), currentPage);
//            }
//        });

        mRepeatAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mRepeat){
                    case "no repeat":{
                        mRepeat = "repeat one";
                        mRepeatAllButton.setBackgroundResource(R.drawable.ic_repeat_one);
                        mRepeatOneFlag = true;
                        mRepeatAllFlag = false;
                        break;
                    }case "repeat one":{
                        mRepeat = "repeat all";
                        mRepeatAllButton.setBackgroundResource(R.drawable.ic_yellow_repeat_all);
                        mRepeatAllFlag = true;
                        mRepeatOneFlag = false;
                        break;
                    }case "repeat all":{
                        mRepeat = "no repeat";
                        mRepeatAllButton.setBackgroundResource(R.drawable.ic_white_repeat_all);
                        mRepeatAllFlag = false;
                        mRepeatOneFlag = false;
                        break;
                    }
                }
                MusicPrefences.setRepeatOne(getActivity().getApplicationContext(), mRepeatOneFlag);
                MusicPrefences.setRepeatAll(getActivity().getApplicationContext(), mRepeatAllFlag);
//                mMediaPlayerTasks.setRepeatOneFlag(mRepeatOneFlag);
//                mMediaPlayerTasks.setRepeatAllFlag(mRepeatAllFlag);
            }
        });

        mShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mShuffleFlag){
                    mShuffleButton.setBackgroundResource(R.drawable.ic_white_shuffle);
                } else {
                    mShuffleButton.setBackgroundResource(R.drawable.ic_yellow_shuffle);
                }
                mShuffleFlag = !mShuffleFlag;
                mMediaPlayerTasks.setShuffleFlag(mShuffleFlag);
                MusicPrefences.setShuffle(getActivity().getApplicationContext(), mShuffleFlag);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mRunnable);

                int currentDuration = SeekbarSetting.progressToTime(seekBar.getProgress(), mMediaPlayer.getDuration());
                mMediaPlayer.seekTo(currentDuration);

                updateProgress();
            }
        });
    }

    private void initUI(View view) {
        mImage = view.findViewById(R.id.image_view_full_page);
        mPausePlayButton = view.findViewById(R.id.pause_play);
        mPrevButton = view.findViewById(R.id.prev_button);
        mNextButton = view.findViewById(R.id.next_button);
        mShuffleButton = view.findViewById(R.id.shuffle_button);
        mSeekBar = view.findViewById(R.id.schema_seek_bar);
        mProgressSeekBar = view.findViewById(R.id.progress_seek_bar_text_view);
        mDurationSeekBar = view.findViewById(R.id.duration_seek_bar_text_view);
        mMusicTitle = view.findViewById(R.id.name_music_full_page);
        mLinearLayout = view.findViewById(R.id.container_full_page_fragment);
        mRepeatAllButton = view.findViewById(R.id.repeat_all_button);
    }

    private void initRunnable(){
        mRunnable = new Runnable() {
            @Override
            public void run() {
                long totalDuration = mMediaPlayer.getDuration();
                long currentDuration = mMediaPlayer.getCurrentPosition();

                mProgressSeekBar.setText(SeekbarSetting.milli_to_time(currentDuration));
                mDurationSeekBar.setText(SeekbarSetting.milli_to_time(totalDuration));

                int progress = SeekbarSetting.getProgressPercentage(currentDuration, totalDuration);
                mSeekBar.setProgress(progress);

                mHandler.postDelayed(this, 100);
            }
        };
    }

    private void setFlag(){
        if (mShuffleFlag){
            mShuffleButton.setBackgroundResource(R.drawable.ic_yellow_shuffle);
        }if (mRepeatOneFlag){
            mRepeatAllButton.setBackgroundResource(R.drawable.ic_repeat_one);
        }else if (mRepeatAllFlag){
            mRepeatAllButton.setBackgroundResource(R.drawable.ic_yellow_repeat_all);
        }
    }

    private void updateProgress(){
        mHandler.postDelayed(mRunnable, 100);
    }

    @Override
    public void uiForPrevNext() {
        setUI();
        mMusicObject.getMusic(mMusic);
    }

    @Override
    public void setMusicForPrevNext(Music music) {
        setmMusic(music);
    }

    public interface MusicObject{
        void getMusic(Music music);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MusicObject){
            mMusicObject = (MusicObject) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mMusicObject = null;
    }
}
