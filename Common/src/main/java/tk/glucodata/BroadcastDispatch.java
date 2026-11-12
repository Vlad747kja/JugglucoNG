package tk.glucodata;

import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Local Android dispatch only: success is not an acknowledgement from a receiver. */
final class BroadcastDispatch {
    static boolean all(String[] receivers, Consumer<String> send,
            BiConsumer<String, RuntimeException> onError) {
        LinkedHashSet<String> targets = new LinkedHashSet<>();
        if (receivers != null) for (String receiver : receivers) {
            if (receiver != null && !receiver.trim().isEmpty()) targets.add(receiver);
        }
        boolean success = !targets.isEmpty();
        for (String target : targets) {
            try {
                send.accept(target);
            } catch (RuntimeException error) {
                success = false;
                onError.accept(target, error);
            }
        }
        return success;
    }
}
