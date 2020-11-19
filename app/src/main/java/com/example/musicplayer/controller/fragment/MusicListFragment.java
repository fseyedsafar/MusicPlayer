package com.example.musicplayer.controller.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.MediaPlayerTasks;
import com.example.musicplayer.model.Music;
import com.example.musicplayer.repository.Repository;
import com.example.musicplayer.service.MusicService;
import com.example.musicplayer.utils.PhotoUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment{

    public static final String ARG_CURRENT_PAGE_MUSIC_LIST = "argCurrentPageMusicList";
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private RecyclerView mListRecyclerView;
    private FrameLayout mItemLayout;
    private LinearLayout mLinearLayout;
    private MusicAdapter mAdapter;
    private Intent mPlayIntent;
    private UISheetBottom mUISheetBottom;
    private List<Music> mMusicList = new ArrayList<>();
    private int mCurrentPage;
    private Long id;
    private Music mMusic;
    private FrameLayout mProgressBar;

    public static MusicListFragment newInstance(int currentPage) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_CURRENT_PAGE_MUSIC_LIST, currentPage);

        MusicListFragment fragment = new MusicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setUISheetBottom(UISheetBottom UISheetBottom) {
        mUISheetBottom = UISheetBottom;
    }

    public void setInterface(Fragment fragment){
        mUISheetBottom = (UISheetBottom) fragment;
    }

    public Music getmMusic() {
        return mMusic;
    }

    public void setmMusic(Music mMusic) {
        this.mMusic = mMusic;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentPage = (int) getArguments().getSerializable(ARG_CURRENT_PAGE_MUSIC_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        initUI(view);

        initAdapterRecyclerView();

        return view;
    }

    private void initUI(View view) {
        mListRecyclerView = view.findViewById(R.id.list_recycler_view);
        mListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLinearLayout = view.findViewById(R.id.linear_layout);
        mProgressBar = view.findViewById(R.id.progress_bar);
    }

    private void initAdapterRecyclerView() {

//        runTimePermisson();
        mMusicList = Repository.getInstance(getActivity()).getMusicList(mCurrentPage, id);

        if (mMusicList.size() > 0){
            mProgressBar.setVisibility(View.GONE);
        }

        mAdapter = new MusicAdapter(mMusicList);
        mListRecyclerView.setAdapter(mAdapter);
    }

//    public void runTimePermisson(){
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                // Explain to the user why we need to read the contacts
//            }
//
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//
//            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
//            // app-defined int constant that should be quite unique
//
//            return;
//        }
//    }

    public interface UISheetBottom{
        void setSheetBottom(Music music);
    }

    public class MusicHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mArtist;
        private ImageView mImage;
        private int mPosition;
        private Music mMusic;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.music_title);
            mArtist = itemView.findViewById(R.id.music_artist);
            mImage = itemView.findViewById(R.id.music_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mAdapter.notifyDataSetChanged();

                    MediaPlayerTasks.getTestMediaPlayer(getActivity()).setMusicPosition(mPosition);
                    MediaPlayerTasks.getTestMediaPlayer(getActivity()).setMusicList(mMusicList);
                    MediaPlayerTasks.getTestMediaPlayer(getActivity()).playMusic();

                    mUISheetBottom.setSheetBottom(mMusic);
                    setmMusic(mMusic);
                }
            });
        }

        public void bind(Music music, int position){
            mMusic = music;
            mPosition = position;
            mTitle.setText(music.getmTitle());
            mArtist.setText(music.getmArtist());

            if (mMusic.getmImage() != null) {
                Picasso.with(getActivity()).load(mMusic.getmImage()).into(mImage);
//                Picasso.get()
//                        .load(mMusic.getmImage())
//                        .into(mImage);
//                mImage.setImageBitmap(PhotoUtils.getScaledBitmap(mMusic.getmImage(), getActivity()));
            }
        }
    }

    private class MusicAdapter extends RecyclerView.Adapter<MusicHolder>{

        private List<Music> mMusicList;

        public MusicAdapter(List<Music> mMusicList) {
            this.mMusicList = mMusicList;
        }

        @NonNull
        @Override
        public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.music_list_item, parent, false);
            return new MusicHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
            holder.bind(mMusicList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mMusicList.size();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mPlayIntent == null){
            mPlayIntent = new Intent(getActivity(), MusicService.class);
            try {
                getActivity().bindService(mPlayIntent, MediaPlayerTasks.getTestMediaPlayer(getActivity()).musicConnetion, Context.BIND_AUTO_CREATE);
            }catch (Exception e){

            }
            getActivity().startService(mPlayIntent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof UISheetBottom)
            mUISheetBottom = (UISheetBottom) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

        mUISheetBottom = null;
    }
}
