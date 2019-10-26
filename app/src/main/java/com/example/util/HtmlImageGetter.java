package com.example.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.zzhoujay.richtext.ext.Base64;

/**
 * Author:Mr.Duan
 * Date:2019/10/24
 * Describe:
 */
public class HtmlImageGetter implements Html.ImageGetter {

    private TextView mTextView;
    private Activity mActivity;
    private float firstRatio = 2.0f;
    private float nextRatio = 1.0f;
    private int mMax = 3;
    boolean isFirst = true;

    public HtmlImageGetter(TextView textView, Activity activity) {
        this.mTextView = textView;
        this.mActivity = activity;
        firstRatio = 2.0f;
    }

    public HtmlImageGetter(TextView textView, Activity activity, int size) {
        this.mTextView = textView;
        this.mActivity = activity;
        this.nextRatio = (float) size / (float) 16;
    }

    @Override
    public Drawable getDrawable(final String source) {

            byte[] byteIcon = Base64.decode(source);
            if (byteIcon != null && byteIcon.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteIcon, 0, byteIcon.length);
                Drawable drawable = new BitmapDrawable(mActivity.getResources(), bitmap);
                drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * 1.7f), (int) (drawable.getIntrinsicHeight() * 1.7f));

                return drawable;
            } else {
                return null;
            }
    }

    private static final class URLDrawable extends BitmapDrawable {
        private Drawable drawable;

        @SuppressWarnings("deprecation")
        public URLDrawable() {
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null)
                drawable.draw(canvas);
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }
}
