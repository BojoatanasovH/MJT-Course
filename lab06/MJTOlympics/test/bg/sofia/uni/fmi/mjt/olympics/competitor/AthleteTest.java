package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AthleteTest {

    private Athlete athlete;

    @BeforeEach
    void setup() {
        athlete = new Athlete("Athlete1", "Name1", "Country1");
    }

    @Test
    void testUnmodifiableMedals() {
        assertThrows(UnsupportedOperationException.class, () -> athlete.getMedals().add(Medal.SILVER));
    }

    @Test
    void testAddMedalIncreasesMedalCount() {
        assertEquals(0, athlete.getMedals().size(), "Initial medal collection should be empty");

        athlete.addMedal(Medal.GOLD);
        assertEquals(1, athlete.getMedals().size(), "Medal count should increase after adding a medal");
        assertTrue(athlete.getMedals().contains(Medal.GOLD), "Gold medal should be added");
    }

    @Test
    void testAddNullMedalThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> athlete.addMedal(null), "Adding null medal should throw IllegalArgumentException");

        assertEquals("Medal cannot be null", exception.getMessage());
    }

    @Test
    void testAthletePropertiesAreStoredCorrectly() {
        assertEquals("Athlete1", athlete.getIdentifier(), "Id should be store correctly");
        assertEquals("Name1", athlete.getName(), "Name should be stored correctly");
        assertEquals("Country1", athlete.getNationality(), "Nationality should be stored correctly");
    }

    @Test
    void testEqualAndHashcode() {
        Athlete athlete1 = new Athlete("123", "Pesho", "BG");
        Athlete athlete2 = new Athlete("123", "Pesho", "BG");
        Athlete athlete3 = new Athlete("456", "Gosho", "Greece");

        assertEquals(athlete1, athlete2, "Athletes with the same identifier should be equal");
        assertNotEquals(athlete1, athlete3, "Athletes with different identifiers should not be equal");

        assertEquals(athlete1.hashCode(), athlete2.hashCode(),
            "Athletes with the same identifier should have the same hash code");
        assertNotEquals(athlete1.hashCode(), athlete3.hashCode(),
            "Athletes with different identifiers should have different hash codes");
    }

    @Test
    void testAddMedalsSameType() {
        Athlete athlete = new Athlete("1", "Test Athlete", "NationX");
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);

        Collection<Medal> medals = athlete.getMedals();
        int goldCount = 0;
        for (Medal medal : medals) {
            if (medal == Medal.GOLD) {
                goldCount++;
            }
        }
        assertEquals(3, goldCount, "Expected 3 GOLD medals");
    }
}
