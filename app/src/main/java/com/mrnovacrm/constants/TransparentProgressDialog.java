package com.mrnovacrm.constants;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.mrnovacrm.R;

public class TransparentProgressDialog extends Dialog {
	
	public TransparentProgressDialog(Context context) {
		super(context, R.style.TransparentProgressDialog);
        	WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        	wlmp.gravity = Gravity.CENTER;
        	getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		setContentView(R.layout.progbar);
	}
	@Override
	public void show() {
		super.show();
	}
}