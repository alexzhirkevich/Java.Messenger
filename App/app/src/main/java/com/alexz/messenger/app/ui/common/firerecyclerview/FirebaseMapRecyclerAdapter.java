package com.alexz.messenger.app.ui.common.firerecyclerview;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.interfaces.IBaseModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class FirebaseMapRecyclerAdapter<Model extends IBaseModel, VH extends FirebaseMapViewHolder<Model>>
        extends RecyclerView.Adapter<VH>
        implements IFirebaseMapRecyclerAdapter<Model,VH>, ClickableItemContainer<Model>, Listenable, ObservableLoader {

    private static final String TAG = FirebaseMapRecyclerAdapter.class.getSimpleName();

    private final Class<Model> modelClass;
    private Query keysQuery;

    private final Map<String, ObservableModelInfo> modelsInfo = new ConcurrentHashMap<>();
    private final List<Model> models = new CopyOnWriteArrayList<>();
    private List<Model> selected = models;

    private ChildEventListener keysQueryListener;
    private RecyclerItemClickListener<Model> itemClickListener;
    private Runnable onStartLoading, onEndLoading;

    private boolean listening = false;
    private long chatsCount = -1;

    /**
     * Model class for {@link DataSnapshot} parsing
     * @param modelClass - class of the adapter Model
     * @see IBaseModel
     */
    public FirebaseMapRecyclerAdapter(Class<Model> modelClass){
        this.modelClass = modelClass;
    }

    /**
     Makes view clickable and sets click listener
     Use {@link IFirebaseMapRecyclerAdapter#onCreateClickableViewHolder(ViewGroup, int)} to create ViewHolder
     */
    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH holder = onCreateClickableViewHolder(parent,viewType);
        holder.itemView.setClickable(true);
        holder.itemView.setFocusable(true);
        holder.itemView.setLongClickable(true);
        holder.itemView.requestLayout();
        holder.itemView.setOnClickListener(view ->{
            if (itemClickListener != null){
                itemClickListener.onItemClick(view,holder.getModel());
            }
        });
        holder.itemView.setOnLongClickListener(view ->{
            if (itemClickListener!=null){
                return itemClickListener.onLongItemClick(view,holder.getModel());
            }
            return false;
        });
        return holder;
    }



    /**
     {@link IFirebaseMapViewHolder#bind(IBaseModel)} used to bind a ViewHolder
     */
    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(selected.get(position));
    }


    /**
     * @return current selected items count
     *
     * @see IFirebaseMapRecyclerAdapter#select(String, boolean)
     * @see IFirebaseMapRecyclerAdapter#selectAll()
     *
     */
    @Override
    public final int getItemCount() {
        return selected.size();
    }



    /**
     * @see ClickableItemContainer#setOnItemClickListener(RecyclerItemClickListener)
     * @param listener - {@link RecyclerItemClickListener}
     */
    @Override
    public final void setOnItemClickListener(RecyclerItemClickListener<Model> listener) {
        this.itemClickListener = listener;
    }



    /**
     * @see ObservableLoader
     * @param runnable - callback
     */
    @Override
    public void setOnStartLoadingListener(Runnable runnable) {
        this.onStartLoading = runnable;
    }



    /**
     * @see ObservableLoader
     * @param runnable - callback
     */
    @Override
    public void setOnEndLoadingListener(Runnable runnable) {
        this.onEndLoading = runnable;
    }



    @CallSuper
    @Override
    protected void finalize() throws Throwable {
        stopListening();
        super.finalize();
    }



    /**
     * Clears all elements.
     *
     * <b>[!] Elements can return if adapter is listening</b>
     * <b>To prevent this, use {@link FirebaseMapRecyclerAdapter#stopListening()} before clearing</b>
     * @see Listenable
     */
    public final void clear(){
        models.clear();
        selected.clear();
        notifyDataSetChanged();
    }



    /**
     * @see IFirebaseMapRecyclerAdapter#select(String, boolean) ()
     */
    @Override
    public final int select(String containsString, boolean ignoreCase) {
        if (containsString == null){
            selectAll();
            return selected.size();
        }
        selected = new ArrayList<>();
        for (Model m : models){

            String selField = onGetFieldForSelection(m);

            if (ignoreCase) {
                if (selField.toLowerCase().contains(containsString.toLowerCase())) {
                    selected.add(m);
                }
            } else {
                if (selField.contains(containsString)) {
                    selected.add(m);
                }
            }
        }
        notifyDataSetChanged();
        return selected.size();
    }



    /**
     * @see IFirebaseMapRecyclerAdapter#selectAll()
     */
    @Override
    public final void selectAll() {
        if (selected != models) {
            selected = models;
            notifyDataSetChanged();
        }
    }



    /**
     * @see Listenable#startListening() ()
     */
    @Override
    public final void startListening() {
        if (!listening) {
            listening = true;
            if (keysQueryListener == null) {

                if (onStartLoading!=null) {
                    onStartLoading.run();
                }

                keysQueryListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        observeModel(snapshot.getKey());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (previousChildName != null) {
                            stopObserve(previousChildName);
                        }
                        observeModel(snapshot.getKey());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        stopObserve(snapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        stopObserve(snapshot.getKey());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "Failed to get keys");
                    }
                };
                keysQuery = onCreateKeyQuery();
                keysQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            chatsCount = snapshot.getChildrenCount();
                            keysQuery.addChildEventListener(keysQueryListener);
                            Log.w(TAG, "Loading " + chatsCount + " chats");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "Failed to get keys count");
                    }
                });

            } else {
                keysQuery.addChildEventListener(keysQueryListener);
                for (String key : modelsInfo.keySet()) {
                    ObservableModelInfo info = modelsInfo.get(key);
                    if (info != null) {
                        info.startListening();
                    }
                }
                keysQuery.addChildEventListener(keysQueryListener);
            }
        }
    }



    /**
     * @see Listenable#stopListening()
     */
    @Override
    public final void stopListening() {
        if (listening) {
            keysQuery.removeEventListener(keysQueryListener);
            listening = false;
            for (String key : modelsInfo.keySet()){
                ObservableModelInfo info  = modelsInfo.get(key);
                if (info!=null) {
                    info.stopListening();
                }
            }
        }
    }

    private void observeModel(String modelId) {
        if (modelsInfo.containsKey(modelId) && modelsInfo.get(modelId) != null) {
            Objects.requireNonNull(modelsInfo.get(modelId)).startListening();
        } else {
            ObservableModelInfo info = new ObservableModelInfo(modelId, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        Model newModel = snapshot.getValue(modelClass);
                        if (newModel != null) {
                            for (Model m : models) {
                                if (m.getId().equals(newModel.getId())) {
                                    int i = models.indexOf(m);
                                    models.set(i, newModel);
                                    notifyItemChanged(i);
                                    return;
                                }
                            }
                            models.add(snapshot.getValue(modelClass));
                            if (models.size() >= chatsCount && onEndLoading != null){
                                new Handler(Looper.getMainLooper()).post(onEndLoading);
                            }
                            notifyItemInserted(models.size());
                        }
                    } else {
                        Log.w(TAG, "Failed to observe model. Id: " + modelId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "Failed to observe model. Id: " + modelId);
                }
            });
            modelsInfo.put(modelId, info);
            if (listening) {
                info.startListening();
            }
        }
    }

    private void stopObserve(String modelId){
        ObservableModelInfo info = modelsInfo.remove(modelId);
        if (info != null){
            info.stopListening();
            for (Model m : models){
                if (m.getId().equals(modelId)){
                    models.remove(m);
                }
            }
        }
    }

    private class ObservableModelInfo implements Listenable {

        boolean listening = false;
        private final Query query;
        private final ValueEventListener listener;

        private ObservableModelInfo(String id, ValueEventListener listener){
            this.query = onCreateModelQuery(id);
            this.listener = listener;
        }

        @Override
        public void startListening(){
            if (!listening && listener != null) {
                listening = true;
                query.addValueEventListener(listener);
            }
        }

        @Override
        public void stopListening(){
            if (listening && listener != null) {
                listening = false;
                query.removeEventListener(listener);
            }
        }

        @Override
        protected void finalize() throws Throwable {
            stopListening();
            super.finalize();
        }
    }

}
