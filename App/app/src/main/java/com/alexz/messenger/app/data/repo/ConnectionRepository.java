//package com.alexz.messenger.app.data.repo;
//
//import com.alexz.messenger.app.data.connection.Connection;
//import com.alexz.messenger.app.data.model.Dialog;
//import com.alexz.messenger.app.data.model.Message;
//import com.alexz.messenger.app.data.model.Result;
//import com.alexz.messenger.app.data.model.User;
//import com.alexz.messenger.app.util.MyGoogleUtils;
//import com.alexz.messenger.protocol.DialogData;
//import com.alexz.messenger.protocol.MessageData;
//import com.alexz.messenger.protocol.UserData;
//import com.alexz.messenger.protocol.request.Request;
//import com.alexz.messenger.protocol.request.RequestDialog;
//import com.alexz.messenger.protocol.request.RequestDialogList;
//import com.alexz.messenger.protocol.request.RequestSendMessage;
//import com.alexz.messenger.protocol.response.IResponse;
//import com.alexz.messenger.protocol.response.Response;
//import com.alexz.messenger.protocol.response.ResponseDialog;
//import com.alexz.messenger.protocol.response.ResponseDialogList;
//import com.alexz.messenger.protocol.response.ResponseSendMessage;
//import com.messenger.app.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ConnectionRepository {
//
//    private static volatile ConnectionRepository instance;
//
//
//    public static ConnectionRepository getInstance() {
//        if (instance == null){
//            instance = new ConnectionRepository();
//        }
//        return instance;
//    }
//
//    private ConnectionRepository(){}
//
//    public Result<List<Dialog>> getDialogList() {
//        com.alexz.messenger.protocol.request.Request req = new RequestDialogList(MyGoogleUtils.getAccount().getIdToken());
//        com.alexz.messenger.protocol.response.Response res = Connection.getInstance().sendRequest(req);
//        if (res instanceof com.alexz.messenger.protocol.response.ResponseDialogList) {
//            if (res.getResponseCode() == com.alexz.messenger.protocol.response.IResponse.RES_OK) {
//                List<Dialog> dialogs = new ArrayList<>();
//                for (DialogData d : ((ResponseDialogList) res).getDialogs()) {
//                    com.alexz.messenger.protocol.UserData userData = d.getLastSender();
//                    dialogs.add(new Dialog(d));
//                }
//                return new Result.Success<>(dialogs);
//            } else {
//                //TODO error variations 1
//            }
//        }
//        return new Result.Error(R.string.error_load_dialogs);
//    }
//
//    public Result<List<Message>> getDialog(Integer id){
//        com.alexz.messenger.protocol.request.Request req  = new RequestDialog(User.getToken(),id);
//        com.alexz.messenger.protocol.response.Response res = Connection.getInstance().sendRequest(req);
//        if (res instanceof com.alexz.messenger.protocol.response.ResponseDialog){
//            if (res.getResponseCode() == com.alexz.messenger.protocol.response.IResponse.RES_OK) {
//                com.alexz.messenger.protocol.MessageData[] messageData = ((ResponseDialog) res).getMessages();
//                List<Message> messages = new ArrayList<>(messageData.length);
//                for (com.alexz.messenger.protocol.MessageData m : messageData) {
//                    messages.add(new Message(m));
//                }
//                return new Result.Success<>(messages);
//            }else {
//                //TODO error variations 2
//            }
//        }
//        return new Result.Error(R.string.error_load_dialog);
//    }
//
//    public Result<Integer> sendMessage(Message message, Integer dialogId) {
//        User sender = message.getSender();
//        com.alexz.messenger.protocol.MessageData m = new com.alexz.messenger.protocol.MessageData(
//                -1, dialogId,
//                new UserData(sender.getId(), sender.getImageUri(), sender.getFirstName(), sender.getLastName(), sender.isOnline()),
//                new MessageData.MessageContent(message.getText(), message.getImageUrl()),
//                message.getDate());
//        Request req = new RequestSendMessage(User.getToken(), m);
//        Response res = Connection.getInstance().sendRequest(req);
//        if (res instanceof com.alexz.messenger.protocol.response.ResponseSendMessage) {
//            if (res.getResponseCode() == IResponse.RES_OK) {
//                return new Result.Success<>(((ResponseSendMessage) res).getMsgId());
//            } else {
//                //TODO error variations 3
//            }
//        }
//        return new Result.Error(R.string.error_send_message);
//    }
//
//
//}
