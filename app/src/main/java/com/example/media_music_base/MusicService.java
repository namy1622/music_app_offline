package com.example.media_music_base;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener
{
    private MediaPlayer player; // đối tượng mediaPlayer để phát nhạc
    private ArrayList<Song> songs; // danh sách các bài hát
    private int songPos; // vịtri bài hát trong danhsach
    private String songTitle = ""; // Tiêu đề bài hát
    private static final int NOTIFY_ID = 1; // ID cho thông báo
    private boolean shuffle = false; // biến ktra tình trạng nhẫu nhiên
    private Random rand; // đối tượng random hỗ trọ phát ngẫu nhiên
    private final IBinder musicBind = new MusicBinder(); // đối tượng IBinder để liên kếtvowis Service

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);

    }

    // Hàm lập danh sách bài hát trong dichvu MusicService
    public void setList(ArrayList<Song> theSongs){
        songs = theSongs;  // gán danh sách bài hát được truyền đến từ tham số vào songs
    }

    // đây là lớp bên trong (inner class) gọi là MusicBinder, mở rộng từ lớp Binder.
    // Lớp này sd để kết nối or liên kết dịch vụ MusicService với thành phần khcs của ưd
    public class MusicBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }


    //là phthuc ghi đè từ gia diên MediaPlayer.OnCompletionListener
    // sd để xử lý sk  khi một phát lại âm nhạc hoàn tất
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition() > 0){
            mp.reset();
            playNext();
        }
    }

    // pthuc được gọi khi có lỗi xảy ra
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset(); // thiết lập về trạng thái chưa khởi tạo

        return false;  // cho biết pthuc ko xử lý được hết lỗi
                    // và cho phép tiếp tục xử lý lỗi khác
    }

    // được gọi khi trình phát nhạc ss phát sau khi hoàn tất chuẩn bị
    @SuppressLint("ForegroundServiceType")
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        //Intent notificationIntent: tạo Intent để mở MainActivity khi ng dùng nhấn vòa thông báo
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 // FLAG_ACTIVITY_CLEAR_TOP để đảm bảo rằng nếu MainActivity đã được mở,
                // nó sẽ không được mở lại mà chỉ đưa lên đầu stack.

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification.Builder nBuilder = new Notification.Builder(this);
        nBuilder.setContentIntent(pendingIntent)
                .setTicker(songTitle)
                .setSmallIcon(R.drawable.play)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);

        Notification notifi = nBuilder.getNotification();

        startForeground(NOTIFY_ID, notifi);
    }

    public boolean onUnbind(Intent intent){
        player.stop(); // dừng phát nhạc
        player.release();// giải phóng tài nguyên của mediaPlayer

        return false; // trả về false để o tái liên kết tự động
    }

    public void playSong(){
        if (player == null) {
            player = new MediaPlayer();
        }
        else if (player != null) {
            player.reset();
        }

        //gọi pthuc reset trên đối tượng player
                        // đối tượng này là một instance của trình phát nhạc, thiết lập về trạng thái chưa khởi tạo
                        // giải phóng tài nguyên, chuẩn bị cho 1 bài hát mới

        Song playSong = songs.get(songPos); // lấy đối tượng Song từ ds songs tại vịtri hiện tại được chỉ bởi songPos

        songTitle = playSong.getTitle(); //lấy tiêu đề bài hát bằng cách gọi phtuc getTitle trên đối tượng playSong
                                            // và gán cho biến songTitle

        Long currentSong = playSong.getId(); // lấy Id của bài hát hiện tại, gán nó cho biến currentSong

        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong);
            // tạo đối tượng Uri đại diện cho URI nội dung bài hát
            // bằng cách thêm ID currentSong vào URI cơ bản MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            // URI này sd để truy cập dl của bài hát trong bộ nhớ ngoài

        try{
            player.setDataSource(getApplicationContext(), trackUri);
                // thiết lập nguồn dl cho trình phát nhạc đến trackUri đã chỉ định
                // nó sd context của ứng dụng getApplicationContext() và URI của bài hát
        }
        catch(Exception e){ // xử lý ngoại lệ xảy ra khi thiết lập nguồn dl
            Log.e("MUSIC SERVICE","Error starting data source", e);
                // ghi lại thông báo lỗi với nhãn MUSIC SERVICE...
        }

        player.prepareAsync(); // chuẩn bị trình phát nhạc để phát lại 1 cách bất đồng bộ




    }

    // pthuc này thiết lập vitri bài hát hiện tại songPos thành songIndex được cung cấp
    public void setSong(int songIndex){
        songPos = songIndex;
    }

    // trả về vitri phát hiện tại của bài hát theo mili giây = cách gọi pthuc getCurrentPosition trên đtuong player
    // điều này để theo dõi bài hát phát đến đâu trong quá trình phát lại
    public int getSongPos(){
        return player.getCurrentPosition();
    }

    // trả về thời lượng của bài hát = cách gọi pthuc getDuration trên đt player( dạng mili giây)
    public int getDur(){
        return player.getDuration();
    }

    // cho biết liệu trình bài hát có đang phát bài hát không
    public boolean isPlaying(){
        return player.isPlaying();
    }

    // tạm dừng phát lại
    public void pausePlayer(){
        player.pause();
    }

    // chuyển vitri phát lại đến vitri cụ thể chỉ định bơi pos
    public void seek(int pos){
        player.seekTo(pos);
    }

    // pthuc để bắt đầu or tiếp tục phát
    public void go(){
        player.start();
    }

    public void playPrev(){
        songPos--;
        if(songPos <0) // nếu nhỏ hơn 0
            songPos = songs.size() -1;  // thì chuyển đến chỉ số của bài hát cuối tỏng ds

        playSong();
    }

    public void playNext(){
        if(shuffle){ // nếu chế độ ngẫu nhiên shuffle được bật
            int newSongPos = songPos;
            while(newSongPos == songPos){
                newSongPos = rand.nextInt(songs.size());
            }
            songPos = newSongPos;
        }
        else{ // phát ngẫu nhiên không bật
            songPos++;
            if(songPos >= songs.size()) // nếu chỉ số lớn hơn số bài hát -> quay về bài hát đầu
                songPos = 0;
        }

        playSong();
    }

    @Override
    public void onDestroy(){
        stopForeground(true);
    }

    // pthuc thay đổi chế độ ngẫu nhiên
    public void setShuffle(){
        if(shuffle)
            shuffle = false;
        else
            shuffle = true;
    }
}
