package com.dlong.rep.dlsidebar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DLTextDialog {

    private Dialog select_dialog;
    private RelativeLayout RL;
    private TextView TXT;

    public void dismissD(){
        if (select_dialog != null && select_dialog.isShowing()){
            select_dialog.dismiss();
        }
    }

    public void showD(String str) {
        if (TXT != null) TXT.setText(str);
        if (select_dialog != null && !select_dialog.isShowing()){
            select_dialog.show();
        }
    }

    public DLTextDialog(Context context, int weight, int hight, int textColor, float textSize, Drawable bg){
        select_dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dl_dialog, null);
        RL = (RelativeLayout) view.findViewById(R.id.rl);
        TXT = (TextView) view.findViewById(R.id.txt);

        RL.setBackground(bg);
        TXT.setTextColor(textColor);
        TXT.setTextSize(textSize);

        select_dialog.getWindow().setGravity(Gravity.BOTTOM);
        select_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = select_dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = weight;   //设置宽度充满屏幕
        lp.height = hight;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
        select_dialog.setContentView(view);
    }
}
