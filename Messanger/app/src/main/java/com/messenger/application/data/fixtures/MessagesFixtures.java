package com.messenger.application.data.fixtures;

import com.messenger.application.data.model.DialogMessage;
import com.messenger.application.data.model.MessangerUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*
 * Created by troy379 on 12.12.16.
 */
public final class MessagesFixtures extends FixturesData {
    private MessagesFixtures() {
        throw new AssertionError();
    }

    public static DialogMessage getImageMessage() {
        DialogMessage message = new DialogMessage(getRandomId(), getUser(), null);
        message.setImage(new DialogMessage.Image(getRandomImage()));
        return message;
    }

    public static DialogMessage getVoiceMessage() {
        DialogMessage message = new DialogMessage(getRandomId(), getUser(), null);
        message.setVoice(new DialogMessage.Voice("http://example.com", rnd.nextInt(200) + 30));
        return message;
    }

    public static DialogMessage getTextMessage() {
        return getTextMessage(getRandomMessage());
    }

    public static DialogMessage getTextMessage(String text) {
        return new DialogMessage(getRandomId(), getUser(), text);
    }

    public static ArrayList<DialogMessage> getMessages(Date startDate) {
        ArrayList<DialogMessage> messages = new ArrayList<>();
        for (int i = 0; i < 10/*days count*/; i++) {
            int countPerDay = rnd.nextInt(5) + 1;

            for (int j = 0; j < countPerDay; j++) {
                DialogMessage message;
                if (i % 2 == 0 && j % 3 == 0) {
                    message = getImageMessage();
                } else {
                    message = getTextMessage();
                }

                Calendar calendar = Calendar.getInstance();
                if (startDate != null) calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, -(i * i + 1));

                message.setCreatedAt(calendar.getTime());
                messages.add(message);
            }
        }
        return messages;
    }

    private static MessangerUser getUser() {
        boolean even = rnd.nextBoolean();
        return new MessangerUser(
                even ? "0" : "1",
                even ? names.get(0) : names.get(1),
                even ? avatars.get(0) : avatars.get(1),
                true);
    }
}
