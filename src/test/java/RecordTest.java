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
        list = List.of(new Record(Date.valueOf("12.12.2022"), 12, 1, 12),
        new Record(Date.valueOf("12.12.2023"), 140, 1, 12),
        new Record(Date.valueOf("12.11.2023"), 120, 1, 12));
    }

    @Test
    public void getLastRecordTest(){
        assertEquals(new Record(Date.valueOf("12.12.2023"), 140, 1, 12),Record.getLastRecord(list));
    }

}
