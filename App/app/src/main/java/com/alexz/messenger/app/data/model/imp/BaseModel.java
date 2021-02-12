package com.alexz.messenger.app.data.model.imp;

import com.alexz.messenger.app.data.model.interfaces.IBaseModel;

public class BaseModel implements IBaseModel {

    private String id;

    protected BaseModel(String id){
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseModel)) return false;
        BaseModel baseModel = (BaseModel) o;
        return id.equals(baseModel.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
