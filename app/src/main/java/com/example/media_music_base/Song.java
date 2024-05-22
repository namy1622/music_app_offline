package com.example.media_music_base;


// lớp đại diện cho bài hát
public class Song {
    private long id; // mã bài hát
    private String title; // tên bài hát
    private String artist; // tên nghệ sĩ


    // khởi tạo đối tượng Song
    public Song(long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }


    // Getter và Setter : phương thức truy xuất + thay đổi giá trị thuộc tính
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
