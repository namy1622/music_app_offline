package com.example.media_music_base;

import android.content.Context;
import android.widget.MediaController;


// Lớp tùy chỉnh trong Android kế thừa từ lớp MediaController
// được sd điều khiển phát nhạc trong ưd Android
public class MusicController extends MediaController {
    public MusicController(Context context) { // Constructor gọi constructor của lớp cha
        // và truyền context vào

        super(context); //
    }
}

//-------------------------------
/*--- MediaController ---
    Là lớp cung cấp bởi Android để tạo giao diện Điều Khiển Phương Tiện
    cho phép người dùng : PHÁT, TẠM DỪNG, TUA NHANH, TUA LÙI  các tệp media
    - MediaController thường được sd với mediaPlayer hoặc VideoView

    --------------------------------------------
    ----- MusicController --------
    là lơpps kế thừa MediaCtroller, có thể được sd để phát nhạc trong ud,
    cụ thể nó cung cấp giao diện cho người dùng để tương tác với quá trình phát nhạc


*/