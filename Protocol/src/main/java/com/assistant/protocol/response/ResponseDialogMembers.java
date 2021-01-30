package com.assistant.protocol.response;

import com.messenger.protocol.request.Request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Arrays;

@Root
public class ResponseDialogMembers extends Response {

	private static final long serialVersionUID = 1L;

	@Element(required = false)
	private Integer dialogMemberId[];

	private ResponseDialogMembers() {}

	public ResponseDialogMembers(byte id, @Element(name = "dialogMemberId")Integer dialogMemberId[])  {
		super(Request.REQ_INVALID, id);
		setDialogMemberIds(dialogMemberId);
	}

	public ResponseDialogMembers(byte id, @Element(name = "dialogMemberId")Integer dialogMemberId[], String errorMsg){
		super(Request.REQ_INVALID, id, errorMsg);
		setDialogMemberIds(dialogMemberId);
	}

	public Integer[] getDialogMemberIds() {
		return dialogMemberId!=null? Arrays.copyOf(dialogMemberId,dialogMemberId.length):null;
	}

	public void setDialogMemberIds(Integer[] dialogMemberId) {
		this.dialogMemberId = dialogMemberId!=null?Arrays.copyOf(dialogMemberId, dialogMemberId.length):null;
	}
}
