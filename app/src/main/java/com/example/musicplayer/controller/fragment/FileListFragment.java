package com.example.musicplayer.controller.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.activity.FileListActivity;
import com.example.musicplayer.model.Album;
import com.example.musicplayer.model.Artist;
import com.example.musicplayer.model.Music;
import com.example.musicplayer.repository.Repository;
import com.example.musicplayer.utils.PhotoUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileListFragment extends Fragment {

    public static final String ARG_POSITION_FILE_LIST = "argPositionFileList";
    public static final int REQUEST_CODE_FILE_LIST_FRAGMENT = 0;
    private RecyclerView mRecyclerView;
    private FileAdapter mFileAdapter;
    private Repository mRepository;
    private List mFileList;
    private int mCurrentPage;

    public static FileListFragment newInstance(int position) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_POSITION_FILE_LIST, position);

        FileListFragment fragment = new FileListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentPage = (int) getArguments().getSerializable(ARG_POSITION_FILE_LIST);
        mRepository = Repository.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);

        initUI(view);

        initAdapterRecyclerView();

        return view;
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.file_recycler_view);

        if (mCurrentPage == 1) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else if(mCurrentPage == 2){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void initAdapterRecyclerView() {
        mFileList = mRepository.getTypeList(mCurrentPage);
        mFileAdapter = new FileAdapter(mFileList);
        mRecyclerView.setAdapter(mFileAdapter);
    }

    private class FileHolder extends RecyclerView.ViewHolder {

        private Album mAlbum;
        private Artist mArtist;
        private TextView mArtistOrAlbum;
        private TextView mCount;
        private ImageView mImage;

        public FileHolder(@NonNull View itemView) {
            super(itemView);
            mArtistOrAlbum = itemView.findViewById(R.id.music_title);
            mCount = itemView.findViewById(R.id.music_artist);
            mImage = itemView.findViewById(R.id.music_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCurrentPage == 1) {
                        startActivityForResult(FileListActivity.newIntent(getActivity(), mCurrentPage, mAlbum.getID()), REQUEST_CODE_FILE_LIST_FRAGMENT);
                    }
                    else if (mCurrentPage == 2){
                        startActivityForResult(FileListActivity.newIntent(getActivity(), mCurrentPage, mArtist.getID()), REQUEST_CODE_FILE_LIST_FRAGMENT);
                    }
                }
            });
        }

        public void bind(Object object){

            if (mCurrentPage == 1){
                mAlbum = (Album) object;
                mArtistOrAlbum.setText(mAlbum.getAlbum());
                mImage.setImageBitmap(PhotoUtils.getScaledBitmap(mAlbum.getImage(), getActivity()));
                mCount.setText(String.valueOf(mRepository.getAlbumIDList(mAlbum.getID()).size()));
            }
            if(mCurrentPage == 2) {
                mArtist = (Artist) object;
                mArtistOrAlbum.setText(mArtist.getArtist());
                mCount.setText(String.valueOf(mRepository.getArtistIDList(mArtist.getID()).size()));

                for (Music music : Repository.getInstance(getActivity()).getArtistIDList(mArtist.getID())) {
                    if (music.getmImage() != null) {
                        mImage.setImageBitmap(PhotoUtils.getScaledBitmap(music.getmImage(), getActivity()));
                        return;
                    }
                }
            }
        }
    }

    private class FileAdapter extends RecyclerView.Adapter<FileHolder>{

        private List mFileList;

        public FileAdapter(List mFileList) {
            this.mFileList = mFileList;
        }

        @NonNull
        @Override
        public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            if (mCurrentPage == 1) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.album_list_item, parent, false);

            } else if (mCurrentPage == 2){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.music_list_item, parent, false);
            }
            return new FileHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FileHolder holder, int position) {
            holder.bind(mFileList.get(position));
        }

        @Override
        public int getItemCount() {
            return mFileList.size();
        }
    }
}
