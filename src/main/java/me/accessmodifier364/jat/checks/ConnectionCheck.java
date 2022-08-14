package me.accessmodifier364.jat.checks;

import me.accessmodifier364.jat.models.AbstractCheck;
import me.accessmodifier364.jat.repo.impl.ClassRepoImpl;
import me.accessmodifier364.jat.util.ASMUtil;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

/**
 * @author accessmodifier364
 * @since 4/18/2022 at 18:56
 */

public final class ConnectionCheck extends AbstractCheck {

    public ConnectionCheck() {
        super("Connection Check", "Detect outgoing connections.");
    }

    @Override
    public void run() {
        ClassRepoImpl.getInstance().listAll().forEach(c -> ASMUtil.getInstructionsInClass(c).forEach((wrapper) -> {
            if (wrapper.getAbstractInsnNode() instanceof TypeInsnNode) {
                switch (((TypeInsnNode) wrapper.getAbstractInsnNode()).desc) {
                    case "java/net/URLConnection":
                    case "java/net/HttpURLConnection":
                    case "java/net/HttpsURLConnection":
                    case "org/apache/http/impl/client/CloseableHttpClient":
                    case "okhttp3/Request":
                    case "java/net/Socket":
                    case "java/net/InetSocketAddress":
                    case "org/apache/http/impl/client/HttpClientBuilder":
                    case "org/apache/http/client/methods/HttpPost":
                        addAlert(Severity.HIGH, wrapper, "Outgoing connection.");
                        break;
                }
            } else if (wrapper.getAbstractInsnNode() instanceof MethodInsnNode) {
                switch (((MethodInsnNode) wrapper.getAbstractInsnNode()).owner +
                        ":" +
                        ((MethodInsnNode) wrapper.getAbstractInsnNode()).name) {
                    case "java/net/URL:openConnection":
                    case "java/net/URL:openStream":
                        addAlert(Severity.HIGH, wrapper, "Outgoing connection.");
                        break;
                }
            }
        }));
    }
}