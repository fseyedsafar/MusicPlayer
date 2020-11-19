package com.example.musicplayer.controller.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.MediaPlayerTasks;
import com.example.musicplayer.controller.activity.MusicFullPageActivity;
import com.example.musicplayer.model.Music;
import com.example.musicplayer.utils.PhotoUtils;

public class SheetBottomFragment extends Fragment implements MusicListFragment.UISheetBottom {

    public static final String ARG_CURRENT_PAGE_SHEET_BOTTOM_FRAGMENT = "argCurrentPageSheetBottomFragment";
    private LinearLayout mBottomSheetCoordinatorLayout;
    private MediaPlayerTasks mMediaPlayerTasks;
    private ImageView mImageViewBottom;
    private ImageButton mPlayPauseBottom;
    private TextView mMusicTitleBottom, mMusicArtistBottom;
    private Music mMusic;
    private int mCurrentPage;

    public static SheetBottomFragment newInstance(int currentPage) {

        Bundle args = new Bundle();

        args.putInt(ARG_CURRENT_PAGE_SHEET_BOTTOM_FRAGMENT, currentPage);

        SheetBottomFragment fragment = new SheetBottomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentPage = getArguments().getInt(ARG_CURRENT_PAGE_SHEET_BOTTOM_FRAGMENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sheet_bottom, container, false);

        initUI(view);

        mBottomSheetCoordinatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MusicFullPageActivity.newIntent(getActivity(), mMusic.getmID(), mCurrentPage));
            }
        });

        return view;
    }

    private void initUI(View view) {
        mPlayPauseBottom = view.findViewById(R.id.pause_play);
        mMusicTitleBottom = view.findViewById(R.id.music_name_bottom);
        mMusicArtistBottom = view.findViewById(R.id.music_artist_bottom);
        mImageViewBottom = view.findViewById(R.id.image_view_bottom);
        mBottomSheetCoordinatorLayout = view.findViewById(R.id.coordinator_bottom_sheet);
    }

    @Override
    public void setSheetBottom(Music music) {
        mMusic = music;

        mBottomSheetCoordinatorLayout.setVisibility(View.VISIBLE);

        mMusicTitleBottom.setText(music.getmTitle());
        mMusicArtistBottom.setText(music.getmArtist());
        mImageViewBottom.setImageBitmap(PhotoUtils.getScaledBitmap(music.getmImage(), getActivity()));

        mPlayPauseBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerTasks.getTestMediaPlayer(getActivity()).playPause(mPlayPauseBottom);
            }
        });
    }
}
