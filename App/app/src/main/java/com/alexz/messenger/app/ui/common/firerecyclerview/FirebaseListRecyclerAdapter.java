package com.alexz.messenger.app.ui.common.firerecyclerview;

import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alexz.messenger.app.data.model.interfaces.IBaseModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public abstract class FirebaseListRecyclerAdapter <Model extends IBaseModel, VH extends FirebaseViewHolder<Model>>
        extends FirebaseDefaultRecyclerAdapter<Model,VH>
        implements IFirebaseListRecyclerAdapter<Model,VH>, Listenable {

    private static final String TAG = FirebaseListRecyclerAdapter.class.getSimpleName();

    private Query modelQuery;
    private ChildEventListener childEventListener;

    private boolean listening = false;
    private long chatsCount= -1;
    boolean loading = false;

    /**
     * Model class for {@link com.google.firebase.database.DataSnapshot} parsing
     *
     * @param modelClass - class of the adapter Model
     * @see IBaseModel
     */
    public FirebaseListRecyclerAdapter(Class<Model> modelClass) {
        super(modelClass);
    }

    @Override
    public void startListening() {
        if (!listening) {
            if (modelQuery == null) {
                loading = true;
                if (loadingCallback != null) {
                    uiHandler.post(() ->loadingCallback.onStartLoading());
                }
                childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Model model = parse(snapshot);
                        int idx = add(model);
                        notifyItemInserted(idx);
                        if (adapterCallback != null) {
                            adapterCallback.onItemAdded(model);
                        }
                        checkLoadingEnd();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Model model = parse(snapshot);
                        int idx = add(model);
                        notifyItemChanged(idx);
                        if (adapterCallback != null) {
                            adapterCallback.onItemChanged(model);
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Model model = parse(snapshot);
                        Pair<Model,Integer> removed = remove(model.getId());
                        if (removed != null) {
                            notifyItemRemoved(removed.second);
                            if (adapterCallback != null) {
                                adapterCallback.onItemChanged(removed.first);
                            }
                            chatsCount--;
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        onChildRemoved(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                modelQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatsCount = snapshot.getChildrenCount();
                        modelQuery = onCreateModelsQuery();
                        modelQuery.addChildEventListener(childEventListener);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "ChildEventListener error:\n" + error);
                        if (loadingCallback != null) {
                            loadingCallback.onEndLoading();
                        }
                        loading = false;
                    }
                });
            } else{
                modelQuery.addChildEventListener(childEventListener);
            }
        }
    }

    @Override
    public void stopListening() {
        if (listening){
            modelQuery.removeEventListener(childEventListener);
        }
    }

    private void checkLoadingEnd(){
        if (loading && realItemCount() >= chatsCount){
            loading = false;
            if (loadingCallback != null) {
                uiHandler.post(() -> loadingCallback.onEndLoading());
            }
        }
    }

    @Override
    protected final int add(Model m) {
        return super.add(m);
    }

    @Nullable
    @Override
    protected final Pair<Model, Integer> remove(String id) {
        return super.remove(id);
    }

    @Override
    protected final void removeAll() {
        super.removeAll();
    }

    @Override
    protected final int realItemCount() {
        return super.realItemCount();
    }

}
