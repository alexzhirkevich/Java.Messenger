package com.alexz.messenger.app.ui.common.firerecyclerview;

import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.interfaces.IBaseModel;
import com.alexz.messenger.app.ui.common.ClickableItemContainer;
import com.alexz.messenger.app.ui.common.ItemClickListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class FirebaseDefaultRecyclerAdapter<Model extends IBaseModel, VH extends FirebaseViewHolder<Model>>
        extends RecyclerView.Adapter<VH>
        implements IFirebaseRecyclerAdapter<Model,VH>,
        ClickableItemContainer<Model>,
        AdapterCallbackHandler<Model>,
        LoadingCallbackHandler {

    private static final String TAG = FirebaseDefaultRecyclerAdapter.class.getSimpleName();

    protected final Class<Model> modelClass;

    protected final Handler uiHandler = new Handler(Looper.getMainLooper());

    private final List<Model> models = new CopyOnWriteArrayList<>();
    private final Set<String> keys = new HashSet<>();
    private List<Model> selected = models;

    protected ItemClickListener<Model> itemClickListener;
    protected AdapterCallback<Model> adapterCallback;
    protected LoadingCallback loadingCallback;

    private boolean isSearching = false;

    /**
     * Model class for {@link DataSnapshot} parsing
     * @param modelClass - class of the adapter Model
     * @see IBaseModel
     */
    public FirebaseDefaultRecyclerAdapter(Class<Model> modelClass){
        this.modelClass = modelClass;
    }




    /**
     Makes view clickable and sets click listener
     Use {@link IFirebaseRecyclerAdapter#onCreateClickableViewHolder(ViewGroup, int)} to create ViewHolder
     */
    @NonNull
    @Override
    public final VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH holder = onCreateClickableViewHolder(parent,viewType);
        holder.itemView.setClickable(true);
        holder.itemView.setFocusable(true);
        holder.itemView.setLongClickable(true);
        holder.itemView.requestLayout();
        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
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
     {@link IFirebaseViewHolder#bind(Object)} used to bind a ViewHolder
     */
    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(selected.get(position));
    }



    /**
     * @see IFirebaseRecyclerAdapter#parse(DataSnapshot)
     * */
    @Override
    public Model parse(DataSnapshot snapshot) {
        return snapshot.getValue(modelClass);
    }



    /**
     * @see IFirebaseRecyclerAdapter#onSelect(String, IBaseModel)
     * */
    @Override
    public boolean onSelect(@Nullable String selectionKey, @NonNull Model model) {
        return true;
    }

    
    
    /**
     * @return current selected items count
     *
     * @see IFirebaseRecyclerAdapter#select(String)
     * @see IFirebaseRecyclerAdapter#selectAll()
     *
     */
    @Override
    public final int getItemCount() {
        return selected.size();
    }



    /**
     * @see ClickableItemContainer#setItemClickListener(ItemClickListener)
     * @see ItemClickListener
     */
    @Override
    public final void setItemClickListener(ItemClickListener<Model> listener) {
        this.itemClickListener = listener;
    }

    /**
     * @see AdapterCallback
     * */
    @Override
    public final void setAdapterCallback(AdapterCallback<Model> callback) {
        this.adapterCallback = callback;
    }

    /**
     * @see LoadingCallbackHandler
     */
    @Override
    public final void setLoadingCallback(LoadingCallback callback) {
        this.loadingCallback = callback;
    }



    /**
     * Clears all elements.
     *
     * <b>[!] Elements can return if adapter is listening</b>
     * <b>To prevent this, use {@link Listenable#stopListening()} before clearing</b>
     * @see Listenable
     */
    public final void clear(){
        removeAll();
        notifyDataSetChanged();
    }



    /**
     * @see IFirebaseRecyclerAdapter#select(String) ()
     */
    @Override
    public final int select(@Nullable String selectionKey) {
        if (selectionKey == null){
            selectAll();
        } else {
            isSearching = true;
            selected = new ArrayList<>();
            for (Model m : models) {
                if (onSelect(selectionKey, m)) {
                    selected.add(m);
                }
            }
            notifyDataSetChanged();
        }
        return selected.size();
    }

    /**
     * @see IFirebaseRecyclerAdapter#selectAll()
     */
    @Override
    public final void selectAll() {
        if (selected != models) {
            selected = models;
            notifyDataSetChanged();
            isSearching = false;
        }
    }

    protected int realItemCount(){
        return models.size();
    }

    protected int add(Model m){
        if (keys.contains(m.getId())){
            int i;
            for (i =0 ; i< models.size(); i++){
                if (models.get(i).getId().equals(m.getId())){
                    models.set(i,m);
                    break;
                }
            }
            return i;
        } else{
            keys.add(m.getId());
            models.add(m);
            return models.size();
        }
    }

    @Nullable
    protected Pair<Model,Integer> remove(String id){
        if (!keys.contains(id)){
            return null;
        } else{
            Model found = null;
            int idx = 0;
            for (Model m : models){
                if (m.getId().equals(id)){
                    idx = models.indexOf(m);
                    models.remove(m);
                    found = m;
                }
            }
            keys.remove(id);
            return new Pair<>(found,idx);
        }
    }

    protected void removeAll(){
        keys.clear();
        models.clear();;
    }

    protected boolean isSearching() {
        return isSearching;
    }
}
