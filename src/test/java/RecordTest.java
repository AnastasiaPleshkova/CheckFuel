import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pleshkova.checkfuel.Record;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordTest {

    List<Record> list;

    @BeforeEach
    void setUp() {
        list = List.of(new Record(new Date(104,5,17), 12, 1, 12),
        new Record(new Date(105,6,17), 140, 1, 12),
        new Record(new Date(105,5,17), 120, 1, 12));
    }

    @Test
    public void getLastRecordTest(){
        assertEquals(list.get(1),Record.getLastRecord(list));
    }

}
