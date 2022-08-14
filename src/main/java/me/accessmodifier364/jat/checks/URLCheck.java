package me.accessmodifier364.jat.checks;

import me.accessmodifier364.jat.models.AbstractCheck;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import org.objectweb.asm.tree.LdcInsnNode;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:44
 */

public final class URLCheck extends AbstractCheck {

    public URLCheck() {
        super("URL Check", "Detect URLs in the application.");
    }

    @Override
    public void run() {
        ClassRepoImpl.getInstance().listAll().forEach(c -> {
            c.methods.forEach(m -> m.instructions.forEach(i -> {
                if (i instanceof LdcInsnNode) {
                    if (((LdcInsnNode) i).cst instanceof String) {
                        final String str = (String) ((LdcInsnNode) i).cst;

                        if (isValidURL(str)) {
                            addAlert(Severity.HIGH, c, "Found URL in Method -> \"" + str + "\"");
                        } else if (isBase64Webhook(str)) {
                            addAlert(Severity.HIGH, c, "Decrypted webhook in Method -> \"" + new String(Base64.getDecoder().decode(str)) + "\"");
                        }
                    }
                }
            }));

            c.fields.stream()
                    .filter(f -> f.value != null)
                    .forEach(f -> {
                        if (f.desc.equals("Ljava/net/URL;")) {
                            addAlert(Severity.HIGH, c, "Found URL in Field -> \"" + f.value + "\"");
                        } else if (f.desc.equals("Ljava/lang/String;")) {
                            final String str = (String) f.value;

                            if (isValidURL(str)) {
                                addAlert(Severity.HIGH, c, "Found URL in Field -> \"" + str + "\"");
                            } else if (isBase64Webhook(str)) {
                                addAlert(Severity.HIGH, c, "Decrypted webhook in Field -> \"" + new String(Base64.getDecoder().decode(str)) + "\"");
                            }
                        }
                    });
        });
    }

    private boolean isValidURL(String string) {
        try {
            new URL(string).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException ignored) {
        }

        return false;
    }

    private boolean isBase64Webhook(String string) {
        return string.startsWith("aHR0cHM6Ly9kaXNjb3JkLmNvbS9hcGkvd2ViaG9va3Mv"); //
    }
}