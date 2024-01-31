package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    Epic epic1 = new Epic("Эпик 1", "...");
    Epic epic2 = new Epic("Эпик 2", "...");

    @Test
    void shouldBeEqualsEpic() {
        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики должны быть равны");
    }

}