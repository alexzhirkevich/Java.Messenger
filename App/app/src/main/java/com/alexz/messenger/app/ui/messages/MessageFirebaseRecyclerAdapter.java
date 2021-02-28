package com.alexz.messenger.app.ui.messages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.imp.Message;
import com.alexz.messenger.app.util.DateUtil;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.alexz.messenger.app.util.MetrixUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.messenger.app.R;;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.firerecyclerview.RecyclerItemClickListener;

import java.security.MessageDigest;
import java.util.Date;

public class MessageFirebaseRecyclerAdapter
        extends FirebaseRecyclerAdapter<Message, MessageFirebaseRecyclerAdapter.MessageViewHolder> {

    private RecyclerItemClickListener<Message> imageClickListener = null;
    private RecyclerItemClickListener<Message> avatarClickListener = null;
    private RecyclerItemClickListener<Message> nameClickListener = null;
    private RecyclerItemClickListener<Message> onItemClickListener = null;

    public MessageFirebaseRecyclerAdapter(@NonNull FirebaseRecyclerOptions<Message> options) {
        super(options);
    }

    public void setOnItemClickListener(RecyclerItemClickListener<Message> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public RecyclerItemClickListener<Message> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setImageClickListener(RecyclerItemClickListener<Message> imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public RecyclerItemClickListener<Message> getImageClickListener() {
        return imageClickListener;
    }

    public void setNameClickListener(RecyclerItemClickListener<Message> nameClickListener) {
        this.nameClickListener = nameClickListener;
    }

    public RecyclerItemClickListener<Message> getNameClickListener() {
        return nameClickListener;
    }

    public void setAvatarClickListener(RecyclerItemClickListener<Message> avatarClickListener) {
        this.avatarClickListener = avatarClickListener;
    }

    public RecyclerItemClickListener<Message> getAvatarClickListener() {
        return avatarClickListener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);

        return new MessageViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
        holder.bind(model);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = MessageViewHolder.class.getSimpleName();
        private Message message;
        private final AvatarImageView avatar;
        private final RelativeLayout msgData;
        private final TextView name;
        private final TextView text;
        private final ImageView image;
        private final TextView date;

        private final int topMargin;

        public MessageViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setLongClickable(true);
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view, message);
                }
            });
            itemView.setOnLongClickListener(view ->{
                if (onItemClickListener != null){
                    onItemClickListener.onLongItemClick(view, message);
                    return true;
                }
                return false;
            });
            avatar = itemView.findViewById(R.id.message_avatar);
            if (avatar != null){
                avatar.setOnClickListener(view ->{
                    if (avatarClickListener!=null){
                        avatarClickListener.onItemClick(view, message);
                    }
                });
                avatar.setOnLongClickListener(view ->{
                    if (avatarClickListener!=null){
                       avatarClickListener.onLongItemClick(view,message);
                       return true;
                    }
                    return false;
                });
            }
            name = itemView.findViewById(R.id.message_sender);
            if (name != null){
                name.setOnClickListener(view ->{
                    if (nameClickListener!=null){
                        nameClickListener.onItemClick(view,message);
                    }
                });
                name.setOnLongClickListener(view ->{
                    if (nameClickListener!=null){
                        nameClickListener.onLongItemClick(view,message);
                        return true;
                    }
                    return false;
                });
            }
            image = itemView.findViewById(R.id.message_image);
            if (image != null){
                image.setOnClickListener(view ->{
                    if (imageClickListener!=null){
                        imageClickListener.onItemClick(view,message);
                    }
                });
                image.setOnLongClickListener(view ->{
                    if (imageClickListener!=null){
                        imageClickListener.onLongItemClick(view,message);
                        return true;
                    }
                    return false;
                });
            }
            text = itemView.findViewById(R.id.message_text);
            date = itemView.findViewById(R.id.message_date);
            msgData = itemView.findViewById(R.id.message_data_layout);
            topMargin = MetrixUtil.dpToPx(itemView.getContext(), 10);
        }

        private void bind(Message message) {

            this.message = message;

            boolean outcoming = message.getSenderId().equals(FirebaseUtil.getCurrentUser().getId());

            if (!outcoming && !message.isPrivate()) {
                String uri = message.getSenderPhotoUrl();
                if (uri != null) {
                    avatar.setVisibility(View.VISIBLE);
                    avatar.setImageURI(Uri.parse(uri));
                }
                name.setText(message.getSenderName());
                name.setVisibility(View.VISIBLE);
            }else {
                avatar.setVisibility(View.INVISIBLE);
                name.setVisibility(View.INVISIBLE);
                name.setText("");
            }
            if (message.getText() != null) {
                text.setText(message.getText());
            }
            if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()){
                Glide.with(image).asBitmap().load(message.getImageUrl()).transform(new Transformation<Bitmap>() {
                    @NonNull
                    @Override
                    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
                        double width = resource.get().getWidth();
                        double height = resource.get().getHeight();
                        double scale = MetrixUtil.dpToPx(image.getContext(),300)/width;
                        Bitmap scaled = Bitmap.createScaledBitmap(resource.get(),
                                (int)(width*scale),
                                (int)(height*scale),
                                scale > 1);
                        return new Resource<Bitmap>() {
                            @NonNull
                            @Override
                            public Class<Bitmap> getResourceClass() {
                                return Bitmap.class;
                            }

                            @NonNull
                            @Override
                            public Bitmap get() {
                                return scaled;
                            }

                            @Override
                            public int getSize() {
                                return scaled.getHeight()*scaled.getWidth();
                            }

                            @Override
                            public void recycle() {
                                scaled.recycle();
                            }
                        };
                    }

                    @Override
                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) { }
                }).addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Log.d(TAG, "Failed to load image: " + e.getMessage());
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            image.getLayoutParams().width = resource.getWidth();
                            image.getLayoutParams().height = resource.getHeight();
                            image.requestLayout();
                            image.setImageBitmap(resource);
                        });
                        return true;
                    }
                }).diskCacheStrategy(DiskCacheStrategy.ALL).submit();

                image.setVisibility(View.VISIBLE);
            }  else{
                image.setImageDrawable(null);
                image.getLayoutParams().height= 0;
                image.getLayoutParams().width = 0;
                image.requestLayout();
                image.setVisibility(View.INVISIBLE);
            }
            if (message.getTime() != null) {
                date.setText(DateUtil.getTime(new Date(message.getTime())));
            }

            transformLayoutParams(message, outcoming);
        }

        private void transformLayoutParams(Message message,boolean outcoming){

            RelativeLayout.LayoutParams dataParams = (RelativeLayout.LayoutParams) msgData.getLayoutParams();
            RelativeLayout.LayoutParams dateParams = (RelativeLayout.LayoutParams) date.getLayoutParams();
            RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) text.getLayoutParams();
            RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) image.getLayoutParams();

            dateParams.addRule(RelativeLayout.BELOW,R.id.message_image);
            imageParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            imageParams.addRule(RelativeLayout.BELOW,R.id.message_text);
            dataParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            dataParams.removeRule(RelativeLayout.END_OF);
            dataParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
            textParams.removeRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY);
            textParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            textParams.setMargins(0, 0, 0, 0);
            dataParams.setMarginEnd(0);
            dataParams.setMarginStart(0);

            if (outcoming) {
                msgData.setBackgroundColor(msgData.getResources().getColor(R.color.message_outcoming));
                msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_outcoming));
                dataParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                textParams.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, GravityCompat.END);
                textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                textParams.setMargins(0, topMargin, 0, 0);
                dataParams.setMarginStart(MetrixUtil.dpToPx(msgData.getContext(), 50));
                if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
                    if (message.getText() == null || message.getText().isEmpty()) {
                        imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    } else{
                        imageParams.addRule(RelativeLayout.BELOW, R.id.message_text);
                    }
                } else {
                    dateParams.addRule(RelativeLayout.BELOW,R.id.message_text);
                }
            } else {
                msgData.setBackgroundColor(msgData.getResources().getColor(R.color.message_incoming));
                dataParams.setMarginEnd(MetrixUtil.dpToPx(msgData.getContext(), 50));
                textParams.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, GravityCompat.START);
                dataParams.addRule(RelativeLayout.END_OF, R.id.message_avatar);
                if (message.isPrivate()) {
                    msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_incoming_private));
                    dataParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                    textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    textParams.setMargins(0, topMargin, 0, 0);
                } else {
                    msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_incoming_group));
                }
                if (message.getText() == null || message.getText().isEmpty()){
                    imageParams.addRule(RelativeLayout.BELOW,R.id.message_sender);
                } else {
                    dateParams.addRule(RelativeLayout.BELOW,R.id.message_text);
                }
            }
            msgData.requestLayout();
            text.requestLayout();
            image.requestLayout();
            date.requestLayout();
        }
    }
}