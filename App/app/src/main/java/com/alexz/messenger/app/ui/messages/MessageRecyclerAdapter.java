//package com.alexz.messenger.app.ui.messages;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.content.res.AppCompatResources;
//import androidx.core.view.GravityCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.alexz.messenger.app.data.model.Message;
//import com.alexz.messenger.app.util.DateUtil;
//import com.alexz.messenger.app.util.FirebaseUtil;
//import com.alexz.messenger.app.util.MetrixUtil;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.Transformation;
//import com.bumptech.glide.load.engine.Resource;
//import com.messenger.app.R;;
//import com.alexz.messenger.app.ui.common.AvatarImageView;
//import com.alexz.messenger.app.ui.common.RecyclerItemClickListener;
//
//import java.security.MessageDigest;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//public class MessageRecyclerAdapter
//        extends RecyclerView.Adapter<FirebaseMessageRecyclerAdapter.MessageViewHolder> {
//
//    private final List<Message> messages = new ArrayList<>();
//
//    private RecyclerItemClickListener<Message> imageClickListener = null;
//    private RecyclerItemClickListener<Message> avatarClickListener = null;
//    private RecyclerItemClickListener<Message> nameClickListener = null;
//    private RecyclerItemClickListener<Message> onItemClickListener = null;
//
//
//    public void insert(int idx, Message item) {
//        messages.add(idx, item);
//        notifyItemInserted(idx);
//    }
//
//    public int indexOf(Message item){
//        return messages.indexOf(item);
//    }
//
//    public int size() {
//        return 0;
//    }
//
//    public boolean add(Message item) {
//        messages.add(item);
//        notifyItemInserted(getItemCount() - 1);
//        return false;
//    }
//
//    public void set(int idx, Message item) {
//        messages.set(idx, item);
//        notifyItemChanged(idx);
//    }
//
//    public boolean addAll(Collection<? extends Message> dialog) {
//        int prev = getItemCount();
//        messages.addAll(dialog);
//        notifyItemRangeInserted(prev, dialog.size());
//        return true;
//    }
//
//    public void clear() {
//        int l = getItemCount();
//        messages.clear();
//        notifyItemRangeRemoved(0, l);
//    }
//
//    public void remove(Message message) {
//        int idx = messages.indexOf(message);
//        if (idx != -1) {
//            messages.remove(idx);
//            notifyItemRemoved(idx);
//        }
//    }
//
//    public void remove(int position) {
//        messages.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public List<Message> getAll() {
//        return new ArrayList<>(messages);
//    }
//
//    public void setOnItemClickListener(RecyclerItemClickListener<Message> onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    public RecyclerItemClickListener<Message> getOnItemClickListener() {
//        return onItemClickListener;
//    }
//
//    public void setImageClickListener(RecyclerItemClickListener<Message> imageClickListener) {
//        this.imageClickListener = imageClickListener;
//    }
//
//    public RecyclerItemClickListener<Message> getImageClickListener() {
//        return imageClickListener;
//    }
//
//    public void setNameClickListener(RecyclerItemClickListener<Message> nameClickListener) {
//        this.nameClickListener = nameClickListener;
//    }
//
//    public RecyclerItemClickListener<Message> getNameClickListener() {
//        return nameClickListener;
//    }
//
//    public void setAvatarClickListener(RecyclerItemClickListener<Message> avatarClickListener) {
//        this.avatarClickListener = avatarClickListener;
//    }
//
//    public RecyclerItemClickListener<Message> getAvatarClickListener() {
//        return avatarClickListener;
//    }
//
//    public Message get(int idx){
//        return messages.get(idx);
//    }
//
//    @NonNull
//    @Override
//    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        final View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
//        return new MessageViewHolder(root);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        holder.bind(messages.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return messages.size();
//    }
//
//    public class MessageViewHolder extends RecyclerView.ViewHolder {
//
//        private Message message;
//        private final AvatarImageView avatar;
//        private final RelativeLayout msgData;
//        private final TextView name;
//        private final TextView text;
//        private final ImageView image;
//        private final TextView date;
//
//        private final int topMargin;
//
//        public MessageViewHolder(View itemView) {
//            super(itemView);
//            itemView.setClickable(true);
//            itemView.setLongClickable(true);
//            itemView.setOnClickListener(view -> {
//                if (onItemClickListener != null){
//                    onItemClickListener.onItemClick(view, message);
//                }
//            });
//            itemView.setOnLongClickListener(view ->{
//                if (onItemClickListener != null){
//                    onItemClickListener.onLongItemClick(view, message);
//                    return true;
//                }
//                return false;
//            });
//            avatar = itemView.findViewById(R.id.message_avatar);
//            if (avatar != null){
//                avatar.setOnClickListener(view ->{
//                    if (avatarClickListener!=null){
//                        avatarClickListener.onItemClick(view, message);
//                    }
//                });
//                avatar.setOnLongClickListener(view ->{
//                    if (avatarClickListener!=null){
//                       avatarClickListener.onLongItemClick(view,message);
//                       return true;
//                    }
//                    return false;
//                });
//            }
//            name = itemView.findViewById(R.id.message_sender);
//            if (name != null){
//                name.setOnClickListener(view ->{
//                    if (nameClickListener!=null){
//                        nameClickListener.onItemClick(view,message);
//                    }
//                });
//                name.setOnLongClickListener(view ->{
//                    if (nameClickListener!=null){
//                        nameClickListener.onLongItemClick(view,message);
//                        return true;
//                    }
//                    return false;
//                });
//            }
//            image = itemView.findViewById(R.id.message_image);
//            if (image != null){
//                image.setOnClickListener(view ->{
//                    if (imageClickListener!=null){
//                        imageClickListener.onItemClick(view,message);
//                    }
//                });
//                image.setOnLongClickListener(view ->{
//                    if (imageClickListener!=null){
//                        imageClickListener.onLongItemClick(view,message);
//                        return true;
//                    }
//                    return false;
//                });
//            }
//            text = itemView.findViewById(R.id.message_text);
//            date = itemView.findViewById(R.id.message_date);
//            msgData = itemView.findViewById(R.id.message_data_layout);
//            topMargin = MetrixUtil.dpToPx(itemView.getContext(), 10);
//        }
//
//        private void bind(Message message) {
//
//            this.message = message;
//            String senderName = message.getSender().getName();
//
//            name.setText("");
//            if (!message.getId().equals(FirebaseUtil.getCurrentUser().getUid()) && !message.isPrivate()) {
//                String uri = message.getSender().getImageUri();
//                if (uri != null) {
//                    avatar.setImageURI(Uri.parse(uri));
//                }
//                if (senderName != null) {
//                    name.setText(senderName);
//                }
//            }
//            if (message.getText() != null) {
//                text.setText(message.getText());
//            }
//
//            if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()){
//                Glide.with(image).load(message.getImageUrl()).transform(new Transformation<Bitmap>() {
//                    @NonNull
//                    @Override
//                    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
//                        double width = resource.get().getWidth();
//                        double height = resource.get().getHeight();
//                        double scale = MetrixUtil.dpToPx(image.getContext(),300)/width;
//                        Bitmap scaled = Bitmap.createScaledBitmap(resource.get(),
//                                (int)(width*scale),
//                                (int)(height*scale),
//                                scale > 1);
//                        return new Resource<Bitmap>() {
//                            @NonNull
//                            @Override
//                            public Class<Bitmap> getResourceClass() {
//                                return Bitmap.class;
//                            }
//
//                            @NonNull
//                            @Override
//                            public Bitmap get() {
//                                return scaled;
//                            }
//
//                            @Override
//                            public int getSize() {
//                                return scaled.getHeight()*scaled.getWidth();
//                            }
//
//                            @Override
//                            public void recycle() {
//                                scaled.recycle();
//                            }
//                        };
//                    }
//
//                    @Override
//                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
//
//                    }
//                }).into(image);
//                image.setVisibility(View.VISIBLE);
//            }
//            else{
//                image.setVisibility(View.INVISIBLE);
//                image.setImageDrawable(null);
//            }
//            if (message.getTime() != null) {
//                date.setText(DateUtil.getTime(new Date(message.getTime())));
//            }
//
//            transformLayoutParams(message);
//        }
//
//        private void transformLayoutParams(Message message){
//
//            RelativeLayout.LayoutParams dataParams = (RelativeLayout.LayoutParams) msgData.getLayoutParams();
//            RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) text.getLayoutParams();
//
//            dataParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
//            dataParams.removeRule(RelativeLayout.END_OF);
//            dataParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
//            textParams.removeRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY);
//            textParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
//            textParams.setMargins(0, 0, 0, 0);
//            dataParams.setMarginEnd(0);
//            dataParams.setMarginStart(0);
//
//            if (message.getId().equals(FirebaseUtil.getCurrentUser().getUid())) {
//                msgData.setBackgroundColor(msgData.getResources().getColor(R.color.message_outcoming));
//                msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_outcoming));
//                name.setVisibility(View.INVISIBLE);
//                avatar.setVisibility(View.INVISIBLE);
//                dataParams.addRule(RelativeLayout.ALIGN_PARENT_END);
//                textParams.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, GravityCompat.END);
//                textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                textParams.setMargins(0, topMargin, 0, 0);
//                dataParams.setMarginStart(MetrixUtil.dpToPx(msgData.getContext(), 50));
//            } else {
//                msgData.setBackgroundColor(msgData.getResources().getColor(R.color.message_incoming));
//                dataParams.setMarginEnd(MetrixUtil.dpToPx(msgData.getContext(), 50));
//                textParams.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, GravityCompat.START);
//                if (message.isPrivate()) {
//                    msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_incoming_private));
//                    avatar.setVisibility(View.INVISIBLE);
//                    name.setVisibility(View.INVISIBLE);
//                    dataParams.addRule(RelativeLayout.ALIGN_PARENT_START);
//                    textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                    textParams.setMargins(0, topMargin, 0, 0);
//                    dataParams.addRule(RelativeLayout.END_OF, R.id.message_avatar);
//                } else {
//                    msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_incoming_group));
//                    avatar.setVisibility(View.VISIBLE);
//                    name.setVisibility(View.VISIBLE);
//                    dataParams.addRule(RelativeLayout.END_OF, R.id.message_avatar);
//                }
//            }
//            msgData.requestLayout();
//        }
//    }
//}