package com.demo.kawatask.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.request.RequestOptions;
import com.demo.kawatask.R;
import com.demo.kawatask.ui.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    private static RequestOptions requestOptions;
    public static ProgressDialog proDialog;
    public static RequestOptions imageDefault() {
        requestOptions = new RequestOptions();

        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);
        return requestOptions;
    }

    public static void showLoader(Context context) {
        if (proDialog == null || !proDialog.isShowing()) {
            proDialog = new ProgressDialog(context);
            proDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            proDialog.show();
            View view = LayoutInflater.from(context).inflate(R.layout.loader, null);
            proDialog.setContentView(view);

        }
    }

    public static void hideLoader() {
        if (proDialog != null && proDialog.isShowing()) {
            proDialog.dismiss();
        }
    }

    public static AlertDialog buildAlertDialog(Context context, String title, String message, String posBtnText, String negativeBtnText, boolean isCancelable, final DialogInterface.OnClickListener buttonsListener) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setCancelable(isCancelable);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, (posBtnText), buttonsListener);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, (negativeBtnText), buttonsListener);
        return dialog;
    }

    public static String capitalizeFirstCharOfEachWord(String capString) {
        try {
            StringBuffer capBuffer = new StringBuffer();
            Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
            while (capMatcher.find()) {
                capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
            }

            return capMatcher.appendTail(capBuffer).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
