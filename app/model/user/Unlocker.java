package model.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Command;
import model.TFile;
import services.TfsService;
import services.TgApi;
import services.UserService;
import utils.LangMap;

import static utils.TextUtils.notNull;

/**
 * @author Denis Danilin | denis@danilin.name
 * 17.06.2020
 * tfs ☭ sweat and blood
 */
public class Unlocker extends ARole implements InputSink, CallbackSink {
    private String password;

    public Unlocker(final TgApi api, final TfsService tfs, final UserService us, final JsonNode node) {
        super(api, tfs, us, node);
    }

    @Override
    public void onCallback(final Command command) {
        fallback();
    }

    @Override
    public JsonNode dump() {
        final ObjectNode parent = rootDump();

        parent.put("password", notNull(password));

        return parent;
    }

    @Override
    public void onInput(final String input) {
        password = input;
        fallback();
    }

    private void fallback() {
        final TFile file = tfs.get(entryId, user);

        if (file.isFile())
            us.morphTo(FileViewer.class, user).doView(file);
        else
            us.morphTo(DirViewer.class, user).doView(file);
    }

    @Override
    public void doView() {
        doView(tfs.get(entryId, user));
    }

    public void doView(final TFile entry) {
        entryId = entry.getId();
        api.dialog(entry.isDir() ? LangMap.Value.TYPE_PASSWORD_DIR : LangMap.Value.TYPE_PASSWORD_FILE, user, entry.getName());
    }

    @Override
    public LangMap.Value helpValue() {
        return tfs.get(entryId, user).isDir() ? LangMap.Value.SHARE_DIR_HELP : LangMap.Value.SHARE_FILE_HELP;
    }
}
