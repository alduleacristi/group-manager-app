package team.groupmanager.org.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import team.groupmanager.org.exceptions.GroupManagerClientException;

/**
 * Created by Cristi on 4/18/2015.
 */
public class ShowMessageUtil {
    private Handler handler;
    private Context ctx;

    public ShowMessageUtil(Handler handler,Context ctx){
        this.handler = handler;
        this.ctx = ctx;
    }

    public void showToast(final String msg,final int duration) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(ctx.getApplicationContext(), msg, duration);
                toast.show();
            }
        });
    }

        public void showToast(final GroupManagerClientException exc,final int duration){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(ctx.getApplicationContext(), exc.getMessage(), duration);
                    toast.show();
                }
            });
        }
 }
