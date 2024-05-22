package com.example.media_music_base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {
    // Song list và layout
    private ArrayList<Song> songs;  // danh sách bài hát
    private LayoutInflater songInf; // lấy dữ liệu, view để sử dọng nó

    //- LayoutInflater: lớp được sd để nạp (ìnlate) các tập tin giao diện người dùng
    //                  từ các tài nguyên xml thành đối tượng View
    //- songInf: biến để lưu trữ 1 thể hiện của lớp layoutInflater

    // Contructor
    public SongAdapter( Context c, ArrayList<Song> theSongs) {
        //this.songs = songs;
        this.songs = theSongs != null ? theSongs : new ArrayList<Song>();
        this.songInf = LayoutInflater.from(c); // Context: dungf để hiện thị danh sahcs bài hát
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ánh xạ đến layout mỗi bài hát
        LinearLayout songLayout = (LinearLayout) songInf.inflate(R.layout.song, parent, false);
        // - songlayout: biến khai báo để lưu trữ đối tượng LinearLayout,
        // mà sau đó chứa các thành phần gia diện người dùng được ìnlate từ 'song.xml'

        TextView songView = (TextView) songLayout.findViewById(R.id.song_title);
        TextView artistView = (TextView) songLayout.findViewById(R.id.song_artist);

        // lấy bài hát hiện
        Song currentSong = songs.get(position);

        //lấy tên tiêu đề và tác giả
        songView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getArtist());

        // cài đặt tag cho mỗi bài là vị trị trí ...
        songLayout.setTag(position);


        return songLayout;
    }
}
