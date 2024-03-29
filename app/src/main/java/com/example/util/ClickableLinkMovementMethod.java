package com.example.util;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

import com.zzhoujay.richtext.spans.LongClickableSpan;

/**
 * Author:Mr.Duan
 * Date:2019/10/24
 * Describe:
 */
public class ClickableLinkMovementMethod extends LinkMovementMethod {
    private static ClickableLinkMovementMethod sInstance;
    public static  ClickableLinkMovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new ClickableLinkMovementMethod();
        }
        return sInstance;
    }
    private static final int MIN_INTERVAL = 500;

    private long lastTime;
//    int x1;
//    int x2;
//    int y1;
//    int y2;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            LongClickableSpan[] link = buffer.getSpans(off, off, LongClickableSpan.class);

            if (link.length != 0) {
                long currTime = System.currentTimeMillis();
                LongClickableSpan l = link[0];
                int ls = buffer.getSpanStart(l);
                int le = buffer.getSpanEnd(l);
                // 判断点击的点是否在Image范围内
                StickerSpan[] is = buffer.getSpans(ls, le, StickerSpan.class);
                if (is.length > 0) {
                    if (!is[0].clicked(x)) {
                        Selection.removeSelection(buffer);
                        return false;
                    }
                } else if (off < layout.getOffsetToLeftOf(ls) || off > layout.getOffsetToLeftOf(le + 1)) {
                    // 判断点击位置是否在链接范围内
                    Selection.removeSelection(buffer);
                    return false;
                }
                if (action == MotionEvent.ACTION_UP) {
                    // 如果按下时间超过５００毫秒，触发长按事件
//                    if (currTime - lastTime > MIN_INTERVAL) {
//                        if (!l.onLongClick(widget)) {
//                            // onLongClick返回false代表事件未处理，交由onClick处理
//                            l.onClick(widget);
//                        }
//                    } else {
                        l.onClick(widget);
//                    }
//                    LogUtils.i("ClickableLinkMovementMethod-action",link[0].toString());
                } else {
                    Selection.setSelection(buffer,
                            ls, le);
                }
                lastTime = currTime;
                return true;
            } else {
                Selection.removeSelection(buffer);
                return false;
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }
}
