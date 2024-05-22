//package com.example.media_music_base;
//
//
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.content.ComponentName;
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.provider.MediaStore;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.MediaController;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//
//public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl{
//
//    private ArrayList<Song> songList;
//    private ListView songView;
//    private MusicService musicService;
//    private Intent playIntent;
//    private boolean musicBound = false;
//    private MusicController controller;
//    private boolean paused = false;
//    private boolean playbackPaused = false;
//
//    @Override
//    protected  void onStart(){
//        super.onStart();
//        if(playIntent == null){
//            playIntent = new Intent(this, MusicService.class);
//            bindService(playIntent, musicConnection, BIND_AUTO_CREATE);
//            startService(playIntent);
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//        } else {
//            getSongList();
//        }
//
//
//
//        //
//        songView = (ListView) findViewById(R.id.song_list);
//        //
//        songList = new ArrayList<Song>();
//        //
//        getSongList();
//        //
//        Collections.sort(songList, new Comparator<Song>() {
//            @Override
//            public int compare(Song o1, Song o2) {
//                return o1.getTitle().compareTo(o2.getTitle());
//            }
//        });
//        //
//        SongAdapter songAdapter = new SongAdapter(this, songList);
//        songView.setAdapter(songAdapter);
//        setController();
//    }
//
//    //
//    public void getSongList(){
//        //
//        ContentResolver musicResolver = getContentResolver();
//        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
//        //
//        if(musicCursor != null && musicCursor.moveToFirst()){
//            //
//            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
//            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
//            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
//
//            do{
//                Long thisId = musicCursor.getLong(idColumn);
//                String thisTitle = musicCursor.getString(titleColumn);
//                String thisArtist = musicCursor.getString(artistColumn);
//                songList.add(new Song(thisId, thisTitle, thisArtist));
//            }while(musicCursor.moveToNext());
//        }
//    }
//
//    //
//    private ServiceConnection musicConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
//            musicService = binder.getService();
//            musicService.setList(songList);
//            musicBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            musicBound = false;
//        }
//    };
//
//    @Override
//    public void start() {
////        super.onStart();
////        if(playIntent == null){
////            playIntent = new Intent(this, MusicService.class);
////            bindService(playIntent, musicConnection, BIND_AUTO_CREATE);
////        }
//        musicService.go();
//    }
//
//    @Override
//    public void pause() {
//        playbackPaused = true;
//        musicService.pausePlayer();
//    }
//
//    @Override
//    public int getDuration() {
//        if(musicService != null && musicBound && musicService.isPlaying()){
//            return musicService.getDur();
//        }
//        return 0;
//    }
//
//    @Override
//    public int getCurrentPosition() {
//        return 0;
//    }
//
//    @Override
//    public void seekTo(int pos) {
//        musicService.seek(pos);
//    }
//
//    @Override
//    public boolean isPlaying() {
//        if(musicService != null && musicBound){
//            return musicService.isPlaying();
//        }
//        else {
//            return false;
//        }
//    }
//
//    @Override
//    public int getBufferPercentage() {
//        return 0;
//    }
//
//    @Override
//    public boolean canPause() {
//        return true;
//    }
//
//    @Override
//    public boolean canSeekBackward() {
//        return true;
//    }
//
//    @Override
//    public boolean canSeekForward() {
//        return true;
//    }
//
//    @Override
//    public int getAudioSessionId() {
//        return 0;
//    }
//
//    //---------------------
//    @Override
//    protected  void onStop(){
//        controller.hide();
//        super.onStop();
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        if(paused){
//            setController();
//            paused = false;
//        }
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        paused = true;
//    }
//
//
//    public void playPrev(){
//        musicService.playPrev();
//        if(playbackPaused){
//            setController();
//            playbackPaused = false;
//        }
//        controller.show(0);
//    }
//    public void playNext(){
//        musicService.playNext();
//        if(playbackPaused){
//            setController();
//            playbackPaused = false;
//        }
//        controller.show(0);
//    }
//
//    //
//    private  void setController(){
//        controller = new MusicController(this);
//        controller.setPrevNextListeners(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playNext();
//            }
//        }, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playPrev();
//            }
//        });
//        controller.setMediaPlayer(this);
//        controller.setAnchorView(findViewById(R.id.song_list));
//        controller.setEnabled(true);
//
//    }
//
//    @Override
//    protected void onDestroy(){
//        stopService(playIntent);
//        musicService = null;
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        if(item.getItemId() == R.id.action_shuffle){
//            musicService.setShuffle();
//        } else if (item.getItemId() == R.id.action_end) {
//            stopService(playIntent);
//            musicService = null;
//            System.exit(0);
//        }
//
////        switch(item.getItemId()){
////
////            case R.id.action_shuffle:
////                musicService.setShuffle();
////                break;
////            case R.id.action_end:
////                stopService(playIntent);
////                musicService = null;
////                System.exit(0);
////                break;
////        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_main, menu);
//        return true;
//
//        // return super.onCreateOptionsMenu(menu);
//
//    }
//
//    public void songPicked(View v){
//        musicService.setSong(Integer.parseInt(v.getTag().toString()));
//        musicService.playSong();
//
//        if(playbackPaused){
//            setController();
//            playbackPaused = false;
//        }
//        controller.show(0);
//    }
//
//    //
////    private ServiceConnection musicConnection = new ServiceConnection() {
////        @Override
////        public void onServiceConnected(ComponentName name, IBinder service) {
////            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
////            musicService = binder.getService();
////            musicService.setList(songList);
////            musicBound = true;
////        }
////
////        @Override
////        public void onServiceDisconnected(ComponentName name) {
////            musicBound = false;
////        }
////    };
////
//
//}

package com.example.media_music_base;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {
    // MediaController.MediaPlayerControl: cung cấp chức nặng điều khiển phát nhạc

    private ArrayList<Song> songList; // lưu trữ ds bài hát
    private ListView songView; // hiển thị danh sách
    private MusicService musicService; // dịch vụ nhạc
    private Intent playIntent; // intent để kết nối với các dich vụ
    private boolean musicBound = false; // trạng thái kết nối dịch vụ
    private MusicController controller; // bộ điều khiển phát nhạc
    private boolean paused = false; // trạng thái phát nhạc
    private boolean playbackPaused = false; // trạng thái phát nhạc

    // khởi động dv nhạc nếu nó chưa được khởi động,
    // và hiển thị bộ điểu khiển nhạc nếu dv đã được kết nối
    @Override
    protected void onStart() {
        super.onStart(); // gọi pt onStart của lớp cha để đảm bảo hành động nào của lớp cha cần ,
                            // trong pt này cũng thực hiện
        if (playIntent == null) {
            // tạo Intent mới để bắt đầu MusicService
            playIntent = new Intent(this, MusicService.class);
            // kết nối với MusicService = cách sd playIntent và musicConnection
            // cờ BIND_AUTO_CREATE: đảm bảo dv khởi tạo nếu nps chưa chạy
            bindService(playIntent, musicConnection, BIND_AUTO_CREATE);

            startService(playIntent);// bd dv MusicService
        }

        if (musicService != null && musicBound) {
            setController(); // goi pt setController để tạo bộ điều khiển nhạc
            controller.show(0); // hiển thị bộ điều khiển nhạc ngay lập tức
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // thiết lập giao diện người dùng

        songView = findViewById(R.id.song_list);
        songList = new ArrayList<>();  // Khởi tạo ArrayList ở đây

        // kiểm tra ưd có quyền đọc bộ nhớ ngoài hay không
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // yc quyền đọc bộ nhớ noài NẾU CHƯA CÓ
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            // nếu đã có quyền, goij pt getSongList để lấy ds bài hát trong bộ nhớ máy
            getSongList();
        }

        // sx ds bài hát songList theo tiêu đề bài hát
        Collections.sort(songList, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                // định nghĩa hàm ss để sx bài hát theo tiêu đề( chữ cái)
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });

        // tạo adapter mới để qly dl và hiển thị ds bài hát
        SongAdapter songAdapter = new SongAdapter(this, songList);
        // gán adapter cho ListView(songView) để hiển thị ds bài hát
        songView.setAdapter(songAdapter);
        setController(); // gọi pt setController để thiết lập bộ điều khiển nhạc
    }

    // hàm lấy ds bài hát
    public void getSongList() {
        // tạo đối tượng ContentResolver để truy vấn nội dung từ bộ nhớ ngoài
        ContentResolver musicResolver = getContentResolver();
        // tạo ủi để truy cập vào dl âm thanh trong bộ nhớ ngoiaf
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // thực hiện truy vấn đến uri để lấy dl,
        // kết quả lưu trong musicCursor
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        // kiểm tra musicCorsor không null và di chuyển đến hàng đầu tiên
        if (musicCursor != null && musicCursor.moveToFirst()) {
            // lấy chỉ số cột tiêu đề bài hát
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            // lấy chỉ số của id bài hát
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            // lấy chỉ số cột nghệ sĩ
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {// lặp qua các hàng cảu Cursor, đọc dl từng bài hát và thêm vào songList

                Long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                //
                //tạo đối tượng Song mới và thêm vào ds bài hát songList
                songList.add(new Song(thisId, thisTitle, thisArtist));
            } while (musicCursor.moveToNext());
        }

        if (musicCursor != null) {
            musicCursor.close();  // Đảm bảo đóng Cursor để tránh lãng phí tài nguyên
        }
    }

    // đối tượng để qly kết nối với dichvu
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            musicService.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    // ghi đè pt start() để bắt đầu phát nhạc
    @Override
    public void start() {

        musicService.go(); // pt go() của musicService để bd phat nhạc
    }

    @Override
    public void pause() {
        playbackPaused = true; // đặt thành true để biểu thi phát nhạc đã tạm dừng
        musicService.pausePlayer(); // pt pausePlayer() để tạm dừng phát nhac
    }

    // ghi đè pt getDuration để lấy thời lượng bài hát hiện tại
    @Override
    public int getDuration() {
        // ktra musicService không null, đã kết nối, đang phát nhạc
        if (musicService != null && musicBound && musicService.isPlaying()) {
            return musicService.getDur(); // trả về thời lượng bài hát
        }
        return 0; // trả về 0 nếu ko tman đkien
    }

    // pt này chỉ ghi đè một pt từ một lớp cha hoặc 1 giao diện
    @Override
    public int getCurrentPosition() { // trả về vị trí iện tại bài hát đang phát(tính bằng miligiay)

        return 0; // trả về 0 là do chưa được triển khai đầy đủ
    }

    @Override
    public void seekTo(int pos) { // pt di chuyển vitri phát lại hiện tại đến vitri mới

        musicService.seek(pos); // seek: thực hiện việc di chuyển vịtri phát lại đến vitri pos được chỉ định
    }

    @Override

    // pt trả về true: nếu dichvu âm nhạc đang phát, ngược lại false
    public boolean isPlaying() {  //
        if (musicService != null && musicBound) { // ktra musicService khác null và đã được ketnoi
            return musicService.isPlaying();        // neeus đúng: trả về giatri pt isPlaying từ musicService
        }
        return false;  // nếu musicService là null OR chưa ketnoi --> trả về false
    }

    @Override
    // pt trả về phần trăm của âm thanh
    public int getBufferPercentage() {

        return 0;
    }

    @Override
    // pt trả về true để chỉ ra nhạc có thể tạm dừng
    public boolean canPause() {

        return true;
    }

    @Override
    // trả về true chỉ ra ng dùng có thể tua ngược lại nhạc
    public boolean canSeekBackward() {
        return true;
    }

    @Override

    // trả về true chỉ ra ng dùng có thể tua tiến (seek Forward) khi phát nhạc
    public boolean canSeekForward() {
        return true;
    }

    @Override
    //
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    // gọi khi Activity ko còn hiển thị cho ng dùng và đang được dừng lại
    protected void onStop() {
       controller.hide(); // ẩn bộ điều khiển nhac(MediaController) khi Activity dừng lại
        super.onStop(); // gọi onStop của lớp cha(Activity) để đảm bảo hành động dưng lại khác vẫn được thực hiện
    }

    @Override
    // được gọi ki Activity bắt đầu tương tác với ng dùng sau khi đã tạm dừng
    protected void onResume() {
        super.onResume(); // goi pt onResome của lớp cha để đảm bảo mọi hành động tiếp tục khác của Activity vẫn được thực hiện
        if (paused) { // ktra nếu Activity bị tạm dừng thì
            setController(); //  đặt lại bộ điêuf khiển nhạc
            paused = false; //  và cập nhật trạng thái false
        }

        if (musicService != null && musicBound) {
            setController();
            controller.show(0);
        }
    }

    @Override
    // gọi khi ko còn hiển thị cho ng dùng nhưng vẫn còn đàn chạy trong nền
    protected void onPause() {
        super.onPause(); // đảm bảo hành động tạm dừng nhưng hành động khác vẫn được thực hiện
        paused = true; // đặt trạng thái pause thành true để ghi nhận Activity đã bị tạm dừng
    }

 // gọi khi ng dùng muốn phát bài hát trước đó
    public void playPrev() {
        musicService.playPrev(); // gọi pt playPrev từ musicService để phát bài hát trước đó
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    public void playNext() {
        musicService.playNext();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }

    // thiết lập bộ điều khiển phát nhạc
    private void setController() {
        // khởi tạo đối tượng MusicController với ngữ cảnh hiện tại(thís)
        controller = new MusicController(this);

        // thiết lập các sk khi nhấn nút 'Previous' và 'Next' trên bộ điều khiển
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }

    @Override
    // được gọi khi activity bị hủy
    protected void onDestroy() {
        stopService(playIntent); // dừng dv nhạc (MusicService)
        musicService = null; // đặt đối tượng musicService  thành null
        super.onDestroy(); // dảm bảo mọi hành độn hủy khác của Activity vẫn được thực hiện
    }

    @Override
    // xử lý sk khi ng dùng chọn một mục trong menu
    public boolean onOptionsItemSelected(MenuItem item) {
                if(item.getItemId() == R.id.action_shuffle){ // ktra nếu mục menu được chọn là Shuffle
                    musicService.setShuffle();  // bật chế độ phát ngẫu nhiên
                } else if (item.getItemId() == R.id.action_end) {
                    stopService(playIntent); // dừng dv nhạc
                    musicService = null; // đặt đối tượng musicService thành null
                    System.exit(0);  // THOÁT KHỎI ỨNG DỤNG
                }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // TẠO MENU CỦA ACTIVITY KHI NÓ ĐƯỢC KHỞI TẠO
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();       // LẤY ĐỐI TƯỢNG MENUINFLATER ĐỂ CÓ TỂ TẠO MENU TỪ XML
        menuInflater.inflate(R.menu.menu_main, menu);    // TẠO MENU TỪ TỆP XML menu_main VÀ GÁN NÓ VÀO  menu
        return true;         // TRẢ VỀ true  ĐỂ HIỂN THỊ MENU
    }

    // PT ĐƯỢC GỌI KHI BÀI HÁT ĐƯỢC CHỌN TỪ DANH SÁCH
    public void songPicked(View v) {
//        musicService.setSong(Integer.parseInt(v.getTag().toString()));
//        musicService.playSong();
//
//        if (playbackPaused) {
//            setController();
//            playbackPaused = false;
//        }
//        controller.show(0);

        int songIndex = Integer.parseInt(v.getTag().toString());
        musicService.setSong(songIndex);
        musicService.playSong(); // Bắt đầu phát bài hát ngay lập tức

        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        if (controller != null) {
            controller.show(0); // Hiển thị MediaController khi bắt đầu phát bài hát
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Quyền đã được cấp
                    getSongList();
                } else {
                    // Quyền bị từ chối
                }
                return;
            }
        }
    }
}
