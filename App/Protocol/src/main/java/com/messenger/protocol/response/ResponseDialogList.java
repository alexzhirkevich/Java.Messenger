package com.messenger.protocol.response;


import com.google.gson.Gson;
import com.messenger.protocol.DialogData;
import com.messenger.protocol.request.IRequest;

public class ResponseDialogList extends Response {

	private static final long serialVersionUID = 1L;

	private final DialogData[] dialogs;

	public ResponseDialogList(int req, DialogData[] dialogs) {
		super(IRequest.REQ_INVALID, req);
		this.dialogs = dialogs;
	}

	public ResponseDialogList(int req, DialogData[] dialogs, String errorMsg) {
		super(IRequest.REQ_INVALID, req, errorMsg);
		this.dialogs = dialogs;
	}

	public DialogData[] getDialogs() {
		return dialogs;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return IRequest.REQ_DIALOGLIST + gson.toJson(this);
	}
}
