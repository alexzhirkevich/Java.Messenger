package com.alexz.messenger.app.ui.common;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.gridlayout.widget.GridLayout;

import com.alexz.messenger.app.data.model.imp.MediaContent;
import com.alexz.messenger.app.data.model.interfaces.IMediaContent;
import com.alexz.messenger.app.ui.activities.ChatActivity;
import com.alexz.messenger.app.ui.activities.FullscreenImageActivity;
import com.alexz.messenger.app.util.MetrixUtil;
import com.bumptech.glide.Glide;
import com.messenger.app.BuildConfig;
import com.messenger.app.R;

import java.util.ArrayList;

public class ContentGridLayout extends GridLayout implements ClickableItemContainer<IMediaContent> {

    private static final String TAG = ContentGridLayout.class.getSimpleName();

    private final ArrayList<IMediaContent> contents = new ArrayList<>();
    private ItemClickListener<IMediaContent> clickListener;
    private int margins;

    public ContentGridLayout(Context context) {
        super(context);
        init(context);
    }

    public ContentGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ContentGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void addContent(IMediaContent content){
        if (contents.size()>=10) {
            contents.remove(0);
        }
        contents.add(content);
    }

    public void reGroup(){
        removeAllViews();
        switch (contents.size()) {
            case 1: {
                setRowCount(1);
                setColumnCount(1);
                View view = createViewForContent(contents.get(0));
                LayoutParams params = new LayoutParams();
                params.width = 0;
                params.height = 0;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                addView(view, params);
                requestLayout();
                break;
            }
            case 2: {
                setRowCount(1);
                setColumnCount(2);
                for (int i = 0; i < 2; i++) {
                    View view = createViewForContent(contents.get(i));
                    LayoutParams params = (LayoutParams) view.getLayoutParams();
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    view.requestLayout();
                    addView(view);
                }
                break;
            }
            case 3:
            case 4: {
                setColumnCount(2);
                setRowCount(contents.size() - 1);
                View view = createViewForContent(contents.get(0));
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, contents.size() - 1);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, contents.size() - 1, contents.size() - 1);
                view.requestLayout();
                addView(view);
                for (int i = 1; i < contents.size(); i++) {
                    view = createViewForContent(contents.get(i));
                    params = (LayoutParams) view.getLayoutParams();
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    view.requestLayout();
                    addView(view);
                }
                break;
            }
            case 5: {
                setColumnCount(3);
                setRowCount(3);
                View view = createViewForContent(contents.get(0));
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 2f);
                view.requestLayout();
                addView(view);
                view = createViewForContent(contents.get(1));
                params = (LayoutParams) view.getLayoutParams();
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 2f);
                view.requestLayout();
                addView(view);
                for (int i = 2; i < 5; i++) {
                    view = createViewForContent(contents.get(i));
                    params = (LayoutParams) view.getLayoutParams();
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    view.requestLayout();
                    addView(view);
                }
                break;
            }
            case 6: {
                setColumnCount(3);
                setRowCount(3);
                View view = createViewForContent(contents.get(0));
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 2f);
                view.requestLayout();
                addView(view);
                for (int i = 1; i < 6; i++) {
                    view = createViewForContent(contents.get(i));
                    params = (LayoutParams) view.getLayoutParams();
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    view.requestLayout();
                    addView(view);
                }
                break;
            }
            case 7: {
                setColumnCount(3);
                setRowCount(4);
                View view = createViewForContent(contents.get(0));
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 3, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 3, 3f);
                view.requestLayout();
                addView(view);
                for (int i = 1; i < 7; i++) {
                    view = createViewForContent(contents.get(i));
                    params = (LayoutParams) view.getLayoutParams();
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    view.requestLayout();
                    addView(view);
                }
                break;
            }
            case 8: {
                setColumnCount(2);
                setRowCount(4);
                for (int i = 0; i < 8; i++) {
                    View view = createViewForContent(contents.get(i));
                    LayoutParams params = (LayoutParams) view.getLayoutParams();
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                    view.requestLayout();
                    addView(view);
                }
                break;
            }
            case 9: {
                setColumnCount(6);
                setRowCount(4);
                for (int i = 0; i < 9; i++) {
                    View view = createViewForContent(contents.get(i));
                    LayoutParams params = (LayoutParams) view.getLayoutParams();
                    if (i<6) {
                        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 3, 3f);
                        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 2f);
                    } else{

                        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 2f);
                        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1,1f);
                    }
                    view.requestLayout();
                    addView(view);
                }
                break;
            }
            case 10: {
                setColumnCount(6);
                setRowCount(4);
                for (int i = 0; i < 10; i++) {
                    View view = createViewForContent(contents.get(i));
                    LayoutParams params = (LayoutParams) view.getLayoutParams();
                    if (i<4) {
                        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 3, 3f);
                        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 2f);
                    } else{

                        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 2, 2f);
                        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1,1f);
                    }
                    view.requestLayout();
                    addView(view);
                }
                break;
            }
            default:
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "Invalid content count: " + contents.size());
                }
        }
    }

    private void init(Context context){
        setOrientation(HORIZONTAL);
        margins = MetrixUtil.dpToPx(context,1);
    }

    private View createViewForContent(IMediaContent content){
        View view;
        if (content.getType() == MediaContent.IMAGE){
            view = new ImageView(getContext());
            view.setTransitionName(getResources().getString(R.string.transition_image_fullscreen));
            Glide.with(this).load(content.getUrl()).centerCrop().into((ImageView)view);
        } else {
            view = new VideoView(getContext());
            ((VideoView)view).setVideoURI(Uri.parse(content.getUrl()));
        }
        LayoutParams params = new LayoutParams();
        view.setPadding(margins,margins,margins,margins);
        view.setLayoutParams(params);
        view.setClickable(true);
        view.setFocusable(true);
        view.setLongClickable(true);
        view.setOnClickListener(e -> {
            if (clickListener != null){
                clickListener.onItemClick(view,content);
            }
        });
        view.setOnLongClickListener(e -> {
            if (clickListener != null) {
                return clickListener.onLongItemClick(view, content);
            }
            return false;
        });

        return view;
    }

    @Override
    public void setItemClickListener(ItemClickListener<IMediaContent> listener) {
        this.clickListener = listener;
    }

    public void setFullscreenTransition(Activity transitionActivity) {
        setItemClickListener(new ItemClickListener<IMediaContent>() {
            @Override
            public void onItemClick(View view, IMediaContent data) {
                if (data.getType() == IMediaContent.IMAGE && transitionActivity != null){
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            transitionActivity,view,transitionActivity.getString(R.string.transition_image_fullscreen));
                    FullscreenImageActivity.startActivity(transitionActivity, data.getUrl(),options.toBundle());
                }
            }
        });
    }
}