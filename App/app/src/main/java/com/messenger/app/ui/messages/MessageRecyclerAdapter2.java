package com.messenger.app.ui.messages;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.messenger.app.R;
import com.messenger.app.data.model.IMessage;
import com.messenger.app.ui.common.AvatarImageView;
import com.messenger.app.util.DateUtil;
import com.messenger.app.util.MetrixUtil;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageRecyclerAdapter2
        extends RecyclerView.Adapter<MessageRecyclerAdapter2.MessageViewHolder> {

    private final List<IMessage> messages = new ArrayList<>();

    public void insert(int idx, IMessage item) {
        messages.add(idx, item);
        notifyItemInserted(idx);
    }

    public int size() {
        return 0;
    }


    public boolean add(IMessage item) {
        messages.add(item);
        notifyItemInserted(getItemCount() - 1);
        return false;
    }

    public void set(int idx, IMessage item) {
        messages.set(idx, item);
        notifyItemChanged(idx);
    }

    public boolean addAll(Collection<? extends IMessage> dialog) {
        int prev = getItemCount();
        messages.addAll(dialog);
        notifyItemRangeInserted(prev, dialog.size());
        return true;
    }

    public void clear() {
        int l = getItemCount();
        messages.clear();
        notifyItemRangeRemoved(0, l);
    }

    public void remove(int position) {
        messages.remove(position);
        notifyItemRemoved(position);
    }

    public IMessage get(int idx){
        return messages.get(idx);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        final ImageView imageview = root.findViewById(R.id.message_image);
        if (imageview != null) {
            imageview.setOnClickListener(view ->
                    Toast.makeText(view.getContext(), "Clicled", Toast.LENGTH_SHORT).show());
        }
        return new MessageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private int id;
        private final AvatarImageView avatar;
        private final RelativeLayout msgData;
        private final TextView name;
        private final TextView text;
        private final ImageView image;
        private final TextView date;

        private String senderName;

        private int topMargin;

        public MessageViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.message_avatar);
            name = itemView.findViewById(R.id.message_sender);
            text = itemView.findViewById(R.id.message_text);
            image = itemView.findViewById(R.id.message_image);
            date = itemView.findViewById(R.id.message_date);
            msgData = itemView.findViewById(R.id.message_data_layout);
            topMargin = MetrixUtil.dpToPx(itemView.getContext(), 10);
        }

        public void bind(IMessage message) {

            id = message.getId();
            senderName = message.getSender().getFullName();

            name.setText("");
            if (!message.isOutcoming() && !message.isPrivate()) {
                String uri = message.getSender().getImageUri();
                if (uri != null) {
                    avatar.setImageURI(Uri.parse(uri));
                }
                if (senderName != null) {
                    name.setText(senderName);
                }
            }
            if (message.getText() != null) {
                text.setText(message.getText());
            }
            if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()){
                Glide.with(image).load(message.getImageUrl()).transform(new Transformation<Bitmap>() {
                    @NonNull
                    @Override
                    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
                        double width = resource.get().getWidth();
                        double height = resource.get().getHeight();
                        double scale = width / MetrixUtil.dpToPx(image.getContext(),300);
                        Bitmap scaled = Bitmap.createScaledBitmap(resource.get(),
                                (int)(width/scale),
                                (int)(height/scale),
                                true);
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
                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

                    }
                }).into(image);
                image.setVisibility(View.VISIBLE);
            }
            else{
                image.setVisibility(View.INVISIBLE);
            }
            if (message.getDate() != null) {
                date.setText(DateUtil.getTime(message.getDate()));
            }

            transformLayoutParams(message);
        }

        private void transformLayoutParams(IMessage message){

            RelativeLayout.LayoutParams dataParams = (RelativeLayout.LayoutParams) msgData.getLayoutParams();
            RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) text.getLayoutParams();

            dataParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            dataParams.removeRule(RelativeLayout.END_OF);
            dataParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
            textParams.removeRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY);
            textParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            textParams.setMargins(0, 0, 0, 0);
            dataParams.setMarginEnd(0);
            dataParams.setMarginStart(0);

            if (message.isOutcoming()) {
                msgData.setBackgroundColor(msgData.getResources().getColor(R.color.message_outcoming));
                msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_outcoming));
                name.setVisibility(View.INVISIBLE);
                avatar.setVisibility(View.INVISIBLE);
                dataParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                textParams.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, GravityCompat.END);
                textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                textParams.setMargins(0, topMargin, 0, 0);
                dataParams.setMarginStart(MetrixUtil.dpToPx(msgData.getContext(), 50));
            } else {
                msgData.setBackgroundColor(msgData.getResources().getColor(R.color.message_incoming));
                dataParams.setMarginEnd(MetrixUtil.dpToPx(msgData.getContext(), 50));
                textParams.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, GravityCompat.START);
                if (message.isPrivate()) {
                    msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_incoming_private));
                    avatar.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    dataParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                    textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    textParams.setMargins(0, topMargin, 0, 0);
                    dataParams.addRule(RelativeLayout.END_OF, R.id.message_avatar);
                } else {
                    msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_incoming_group));
                    avatar.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                    dataParams.addRule(RelativeLayout.END_OF, R.id.message_avatar);
                }
            }
            msgData.requestLayout();
        }
    }
}