package model.telegram;

import model.telegram.api.TeleFile;
import model.telegram.api.UpdateRef;
import utils.CallCmd;
import utils.TextUtils;

/**
 * @author Denis Danilin | denis@danilin.name
 * 14.05.2020
 * tfs ☭ sweat and blood
 */
public class Request {
    public final long id;
    public final String text;
    public final String callback;
    public final long callbackId, callbackReplyId;
    public final TeleFile file;

    public final CallCmd callbackCmd;

    public Request(final UpdateRef input) {
        id = input.getMessage() != null ? input.getMessage().getMessageId() : input.getEditedMessage() != null ? input.getEditedMessage().getMessageId() : 0;
        text = input.getMessage() != null ? input.getMessage().getText() : input.getEditedMessage() != null ? input.getEditedMessage().getText() : null;
        callback = input.getCallback() != null ? input.getCallback().getData() : null;
        callbackReplyId = callback == null ? 0 : input.getCallback().getId();
        callbackId = callback == null ? 0 : TextUtils.getLong(callback);
        callbackCmd = callback == null ? null : CallCmd.ofId(callback);
        file = input.getMessage() != null ? input.getMessage().getTeleFile() : input.getEditedMessage() != null ? input.getEditedMessage().getTeleFile() : null;
    }

    public boolean isCallback() {
        return callback != null;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", callback='" + callback + '\'' +
                ", callbackId=" + callbackId +
                ", callbackReplyId=" + callbackReplyId +
                ", file=" + file +
                ", callbackCmd=" + callbackCmd +
                '}';
    }
}
