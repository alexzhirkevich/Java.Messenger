package com.messenger.application.data.fixtures;

import com.messenger.application.data.model.Dialog;
import com.messenger.application.data.model.DialogMessage;
import com.messenger.application.data.model.MessageUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public final class DialogsFixtures extends FixturesData {
    private DialogsFixtures() {
        throw new AssertionError();
    }

    public static ArrayList<Dialog> getDialogs() {

        ArrayList<Dialog> chats = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -(i * i));
            calendar.add(Calendar.MINUTE, -(i * i));

            chats.add(getDialog(i, calendar.getTime()));
        }

        return chats;
    }

    private static Dialog getDialog(int i, Date lastMessageCreatedAt) {
        ArrayList<MessageUser> users = getUsers();
        return new Dialog(
                getRandomId(),
                users.size() > 1 ? groupChatTitles.get(users.size() - 2) : users.get(0).getFirstName(),
                users.size() > 1 ? groupChatImages.get(users.size() - 2) : getRandomAvatar(),
                users,
                getMessage(lastMessageCreatedAt),
                i < 3 ? 3 - i : 0);
    }

    private static ArrayList<MessageUser> getUsers() {
        ArrayList<MessageUser> users = new ArrayList<>();
        int usersCount = 1 + rnd.nextInt(4);

        for (int i = 0; i < usersCount; i++) {
            users.add(getUser());
        }

        return users;
    }

    private static MessageUser getUser() {
        return new MessageUser(
                getRandomId(),
                getRandomName(),
                getRandomAvatar(),
                getRandomBoolean());
    }

    private static DialogMessage getMessage(final Date date) {
        return new DialogMessage(
                getRandomId(),
                getUser(),
                getRandomMessage(),
                date);
    }
}
