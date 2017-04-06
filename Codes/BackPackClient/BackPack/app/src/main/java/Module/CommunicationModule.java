/*
 *Create by:ZhangYunpeng
 *Date:2017/04/05
 *Modify by:
 *Date:
 *Modify by:
 *Date:
 */
package Module;
import android.os.AsyncTask;

import static com.example.peng.backpack.MainActivity.bt;

public class CommunicationModule {
    public int Send(byte[] data) {
        try {
            byte[] to_send = new byte[1];
            to_send[0] = '1';
            bt.send(to_send, false);
            return 1;
        }catch(Exception e){
            return 0;
        }
    }
}

