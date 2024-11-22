package bg.sofia.uni.fmi.mjt.olympics.competition;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class CompetitionTest {

    private Set<Competitor> competitors;

    @BeforeEach
    void setup() {
        competitors = new HashSet<>();
        competitors.add(new Athlete("Athlete1", "Name1", "Country1"));
    }

    @Test
    void testCompetitionWithBlankNameThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Competition(null, "Sport", competitors);
        }, "Competition's name cannot be null or blank");

        assertThrows(IllegalArgumentException.class, () -> {
            new Competition("  ", "Sport", competitors);
        }, "Competition's name cannot be null or blank");

        assertThrows(IllegalArgumentException.class, () -> {
            new Competition("", "Sport", competitors);
        }, "Competition's name cannot be null or blank");

        assertDoesNotThrow(() -> {
            new Competition("Olympics", "Sport", competitors);
        });
    }

    @Test
    void testBlankDisciplineThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("name", null, competitors),
            "Discipline is null");

        assertThrows(IllegalArgumentException.class, () -> new Competition("name", "  ", competitors),
            "Discipline is blank");

        assertThrows(IllegalArgumentException.class, () -> new Competition("name", "", competitors),
            "Discipline is blank");
        assertDoesNotThrow(() -> new Competition("name", "discipline", competitors));
    }

    @Test
    void testWithBlankCompetitorsThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("name", "sport", Set.of()),
            "Competitors is empty");
        assertDoesNotThrow(() -> new Competition("name", "sport", competitors));
        competitors = null;
        assertThrows(IllegalArgumentException.class, () -> new Competition("name", "sport", competitors),
            "Competitors is null");
    }


    @Test
    void testEquals_SameObject() {
        Competition competition = new Competition("Olympic Games", "Running", competitors);

        assertEquals(competition, competition, "Same object should be equal to itself");
    }

    @Test
    void testEqualsWithDifferentObjectsWithSameValues() {
        // Objects with the same values should be considered equal
        Competition competition1 = new Competition("Olympic Games", "Running", competitors);
        Competition competition2 = new Competition("Olympic Games", "Running", competitors);

        assertEquals(competition1, competition2, "Objects with the same values should be equal");
    }

    @Test
    void testEqualsWithDifferentValues() {
        // Objects with different values should not be equal
        Set<Competitor> competitors2 = new HashSet<>();
        competitors2.add(new Athlete("Athlete2", "Name2", "Country2"));

        Competition competition1 = new Competition("Olympic Games", "Running", competitors);
        Competition competition2 = new Competition("Olympic Games", "Swimming", competitors2);

        assertNotEquals(competition1, competition2, "Objects with different values should not be equal");
    }

    @Test
    void testEqualsNullObject() {
        // Comparison with null should return false
        Competition competition = new Competition("Olympic Games", "Running", competitors);

        assertNotEquals(null, competition, "Object should not be equal to null");
    }

    @Test
    void testEqualsDifferentClass() {
        // Comparison with an object of a different class should return false
        Competition competition = new Competition("Olympic Games", "Running", competitors);
        String nonCompetitionObject = "Not a Competition";

        assertNotEquals(competition, nonCompetitionObject,
            "Object should not be equal to an object of a different class");
    }

    @Test
    void testHashCodeWithEqualObjects() {
        // Two objects that are equal according to equals() should have the same hash code
        Competition competition1 = new Competition("Olympic Games", "Running", competitors);
        Competition competition2 = new Competition("Olympic Games", "Running", competitors);

        assertEquals(competition1.hashCode(), competition2.hashCode(), "Equal objects should have the same hash code");
    }

    @Test
    void testHashCodeWithDifferentObjects() {
        // Two objects that are not equal should have different hash codes
        Set<Competitor> competitors2 = new HashSet<>();
        competitors2.add(new Athlete("Athlete2", "Name2", "Country2"));

        Competition competition1 = new Competition("Olympic Games", "Running", competitors);
        Competition competition2 = new Competition("Olympic Games", "Swimming", competitors2);

        assertNotEquals(competition1.hashCode(), competition2.hashCode(),
            "Unequal objects should have different hash codes");
    }

    @Test
    void testHashCodeWithSameValues() {
        // Objects with the same values should have the same hash code
        Competition competition1 = new Competition("Olympic Games", "Running", competitors);
        Competition competition2 = new Competition("Olympic Games", "Running", competitors);

        assertEquals(competition1.hashCode(), competition2.hashCode(),
            "Objects with the same values should have the same hash code");
    }

    @Test
    void testEqualsWithDifferentCompetitors() {
        // Same competition but different competitors should not be equal
        Set<Competitor> competitors2 = new HashSet<>();
        competitors2.add(new Athlete("Athlete2", "Name2", "Country2"));

        Competition competition1 = new Competition("Olympic Games", "Running", competitors);
        Competition competition2 = new Competition("Olympic Games", "Running", competitors2);

        assertNotEquals(competition1, competition2, "Competitions with different competitors should not be equal");
    }
}
