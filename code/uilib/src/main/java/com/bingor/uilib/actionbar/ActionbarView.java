package com.bingor.uilib.actionbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bingor.uilib.R;


/***
 * 替换actionbar 1.可通过配置设置标题或者setTitle设置标题 2.setListenEvent(ListenEvent)来添加监听事件
 *
 * @date 2015年10月14日
 */
public class ActionbarView extends LinearLayout {

    private View rootView;
    private TextView txtTitle;
    private TextView txtLeft;
    private TextView txtRight;

    private ImageView btnBack;
    private ImageView btnRight;
    private View ViewLeft;
    private LinearLayout ViewRight;

    private Context context;
    private OnListenEvent onListenEvent;

    public OnListenEvent getOnListenEvent() {
        return onListenEvent;
    }

    public void setOnListenEvent(OnListenEvent onListenEvent) {
        this.onListenEvent = onListenEvent;
    }

    /***
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setTitle(int id) {
        txtTitle.setText(id);
    }

    public String getTitle() {
        return txtTitle.getText().toString();
    }

    /***
     * 设置左边文字
     *
     * @param txt
     */
    public void setTxtLeft(String txt) {
        txtLeft.setText(txt);
        txtLeft.setVisibility(View.VISIBLE);
    }

    /***
     * 设置右边文字
     *
     * @param txt
     */
    public void setTxtRight(String txt) {
        txtRight.setText(txt);
        txtRight.setVisibility(View.VISIBLE);
    }

    /**
     * 获取中间TextView
     *
     * @return
     */
    public TextView getCenterTitle() {
        return txtTitle;
    }

    /***
     * 获取左边图标按钮
     *
     * @return
     */
    public ImageView getBtnLeft() {
        return btnBack;
    }

    /***
     * 获取右边图标按钮
     *
     * @return
     */
    public ImageView getBtnRight() {
        return btnRight;
    }

    /***
     * 获取右侧所有控件
     *
     * @return
     */
    public LinearLayout getViewRight() {
        return ViewRight;
    }

    public void setViewRightEnable(boolean enable) {
        if (Build.VERSION.SDK_INT >= 11) {
            if (enable) {
                ViewRight.setEnabled(true);
                ViewRight.setAlpha(1.0f);
            } else {
                ViewRight.setEnabled(false);
                ViewRight.setAlpha(0.5f);
            }
        }
    }

    /***
     * 右侧添加新控件
     */
    public void addViewRight(View child) {
        ViewRight.addView(child, 0);
    }

    /**
     * 根据给进来的颜色值设置文本颜色
     *
     * @param color
     */
    public void setTextColor(@ColorInt int color) {
        txtTitle.setTextColor(color);
        txtLeft.setTextColor(color);
        txtRight.setTextColor(color);
    }

    public ActionbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_actionbar, this);
        txtTitle = (TextView) rootView.findViewById(R.id.txt_title);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_icon);
        btnRight = (ImageView) rootView.findViewById(R.id.btn_icon_right);
        ViewLeft = rootView.findViewById(R.id.action_left);
        ViewRight = (LinearLayout) rootView.findViewById(R.id.action_right);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ActionbarView);
        // 左图标
        if (typedArray.getBoolean(R.styleable.ActionbarView_showIconLeft, true)) {
            btnBack.setVisibility(View.VISIBLE);
        } else {
            btnBack.setVisibility(View.GONE);
        }
        // 左图标
        int bid = typedArray.getResourceId(R.styleable.ActionbarView_btnBackIcon, 0);
        if (bid != 0) {
            btnBack.setImageResource(bid);
        }

        // 右图标
        int rid = typedArray.getResourceId(R.styleable.ActionbarView_rightIcon, 0);
        if (rid != 0) {
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setImageResource(rid);
        } else {
            btnRight.setVisibility(View.GONE);
        }

        // 标题
        String title = typedArray.getString(R.styleable.ActionbarView_txtTitle);
        txtTitle.setText(title);
        // 左文字
        txtLeft = (TextView) rootView.findViewById(R.id.txt_back);
        String txtl = typedArray.getString(R.styleable.ActionbarView_txtLeft);
        if (txtl == null || txtl.equals("")) {
            txtLeft.setVisibility(View.GONE);
        } else {
            txtLeft.setText(txtl);
            txtLeft.setVisibility(View.VISIBLE);
        }
        // 右文字
        txtRight = (TextView) rootView.findViewById(R.id.txt_right);

        String txtr = typedArray.getString(R.styleable.ActionbarView_txtRight);
        if (txtr == null || txtr.equals("")) {
            txtRight.setVisibility(View.GONE);
        } else {
            txtRight.setText(txtr);
            txtRight.setVisibility(View.VISIBLE);
        }

        ViewRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (onListenEvent != null
                        && (txtRight.getVisibility() == View.VISIBLE || btnRight.getVisibility() == View.VISIBLE))
                    onListenEvent.onRight();
            }
        });

        ViewLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (onListenEvent != null)
                    onListenEvent.onBack();
            }
        });

        setBackgroundColor(context.getResources().getColor(R.color.main_color));
        for (int i = 0, size = attrs.getAttributeCount(); i < size; i++) {
            String name = attrs.getAttributeName(i);
            String value = attrs.getAttributeValue(i);
//            Log.i("ActionbarView", "#onCreate attr[" + i + "] name = " + name + " and value = " + value);
            if ("background".equals(name)) {
                if (value.startsWith("@")) {
                    setBackgroundResource(attrs.getAttributeResourceValue(i, 0));
                } else if (value.startsWith("#")) {
                    setBackgroundColor(attrs.getAttributeIntValue(i, 0));
                }
                break;
            }

        }

        String value = attrs.getAttributeValue("android", "background");

//        Log.i("ActionbarView", "#onCreate background = " + value);

//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.App);
//        if (ta.getBoolean(R.styleable.App_fitSystem, false)) {
//            ScreenUtil.setStatuBarPadding(context, (LinearLayout) rootView.findViewById(R.id.ll_main));
//        }
//        ta.recycle();

    }

    /***
     * 监听事件
     *
     * @author 姜永健
     * @date 2015年10月14日
     */
    public interface OnListenEvent {
        /***
         * 左边按钮监听
         */
        void onBack();

        /***
         * 右边按钮监听
         */
        void onRight();

    }

}
