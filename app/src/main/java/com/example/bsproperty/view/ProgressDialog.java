package com.example.bsproperty.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.example.bsproperty.R;

/**
 * Created by yezi on 2018/1/27.
 */

public class ProgressDialog extends Dialog {

    private TextView tv_msg;

    public ProgressDialog(Context context) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.dialog_progress);

        tv_msg = (TextView) findViewById(R.id.tv_msg);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setMsg(String msg) {
        tv_msg.setText(msg);
    }

    public void show(String msg) {
        tv_msg.setText(msg);
        show();
    }
}
