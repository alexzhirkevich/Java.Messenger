package com.messenger.protocol.request;

import com.google.gson.Gson;

public class RequestDialog extends Request {

	private static final long serialVersionUID = 1L;

	private Integer dialogId;


	public RequestDialog(String token, Integer dialogId) {
		super(REQ_DIALOG,token);
		setDialogId(dialogId);
	}

	public Integer getDialogId() {
		return dialogId;
	}
	public void setDialogId(Integer dialogId) {
		this.dialogId = dialogId;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return REQ_DIALOG + gson.toJson(this);
	}
}

