package tk.glucodata;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class BroadcastDispatchTests {
    @Test public void failingReceiverDoesNotBlockTheOthers() {
        List<String> attempted = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        boolean success = BroadcastDispatch.all(new String[]{"bad", "aaps", "other"}, name -> {
            attempted.add(name);
            if (name.equals("bad")) throw new SecurityException("denied");
        }, (name, error) -> failed.add(name));
        assertFalse(success);
        assertEquals(List.of("bad", "aaps", "other"), attempted);
        assertEquals(List.of("bad"), failed);
    }

    @Test public void emptyTargetsAreNotSuccessAndDuplicatesAreSentOnce() {
        assertFalse(BroadcastDispatch.all(null, name -> fail(), (name, error) -> fail()));
        assertFalse(BroadcastDispatch.all(new String[]{null, " "}, name -> fail(), (name, error) -> fail()));
        List<String> attempted = new ArrayList<>();
        assertTrue(BroadcastDispatch.all(new String[]{"aaps", "aaps"}, attempted::add, (name, error) -> fail()));
        assertEquals(List.of("aaps"), attempted);
    }
}
