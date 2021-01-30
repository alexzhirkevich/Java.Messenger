package com.messenger.app.data.repo;

import com.messenger.app.R;
import com.messenger.app.data.connection.Connection;
import com.messenger.app.data.model.Dialog;
import com.messenger.app.data.model.Message;
import com.messenger.app.data.model.Result;
import com.messenger.app.data.model.User;
import com.messenger.app.util.MyGoogleUtils;
import com.messenger.protocol.DialogData;
import com.messenger.protocol.MessageData;
import com.messenger.protocol.UserData;
import com.messenger.protocol.request.Request;
import com.messenger.protocol.request.RequestDialog;
import com.messenger.protocol.request.RequestDialogList;
import com.messenger.protocol.request.RequestSendMessage;
import com.messenger.protocol.response.IResponse;
import com.messenger.protocol.response.Response;
import com.messenger.protocol.response.ResponseDialog;
import com.messenger.protocol.response.ResponseDialogList;
import com.messenger.protocol.response.ResponseSendMessage;

import java.util.ArrayList;
import java.util.List;

public class ConnectionRepository {

    private static volatile ConnectionRepository instance;


    public static ConnectionRepository getInstance() {
        if (instance == null){
            instance = new ConnectionRepository();
        }
        return instance;
    }

    private ConnectionRepository(){}

    public Result<List<Dialog>> getDialogList() {
        Request req = new RequestDialogList(MyGoogleUtils.getAccount().getIdToken());
        Response res = Connection.getInstance().sendRequest(req);
        if (res instanceof ResponseDialogList) {
            if (res.getResponseCode() == IResponse.RES_OK) {
                List<Dialog> dialogs = new ArrayList<>();
                for (DialogData d : ((ResponseDialogList) res).getDialogs()) {
                    UserData userData = d.getLastSender();
                    dialogs.add(new Dialog(d));
                }
                return new Result.Success<>(dialogs);
            } else {
                //TODO error variations 1
            }
        }
        return new Result.Error(R.string.error_load_dialogs);
    }

    public Result<List<Message>> getDialog(Integer id){
        Request req  = new RequestDialog(User.getToken(),id);
        Response res = Connection.getInstance().sendRequest(req);
        if (res instanceof ResponseDialog){
            if (res.getResponseCode() == IResponse.RES_OK) {
                MessageData[] messageData = ((ResponseDialog) res).getMessages();
                List<Message> messages = new ArrayList<>(messageData.length);
                for (MessageData m : messageData) {
                    messages.add(new Message(m));
                }
                return new Result.Success<>(messages);
            }else {
                //TODO error variations 2
            }
        }
        return new Result.Error(R.string.error_load_dialog);
    }

    public Result<Integer> sendMessage(Message message, Integer dialogId) {
        User sender = message.getSender();
        MessageData m = new MessageData(
                -1, dialogId,
                new UserData(sender.getId(), sender.getImageUri(), sender.getFirstName(), sender.getLastName(), sender.isOnline()),
                new MessageData.MessageContent(message.getText(), message.getImageUrl()),
                message.getDate());
        Request req = new RequestSendMessage(User.getToken(), m);
        Response res = Connection.getInstance().sendRequest(req);
        if (res instanceof ResponseSendMessage) {
            if (res.getResponseCode() == IResponse.RES_OK) {
                return new Result.Success<>(((ResponseSendMessage) res).getMsgId());
            } else {
                //TODO error variations 3
            }
        }
        return new Result.Error(R.string.error_send_message);
    }


}
