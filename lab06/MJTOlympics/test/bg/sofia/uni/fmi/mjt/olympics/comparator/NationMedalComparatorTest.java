package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

class NationMedalComparatorTest {

    private MJTOlympics olympics;
    private NationMedalComparator comparator;

    @BeforeEach
    void setup() {
        olympics = mock(MJTOlympics.class);
        comparator = new NationMedalComparator(olympics);
    }

    @Test
    void testCompareDifferentMedals() {
        when(olympics.getTotalMedals("USA")).thenReturn(10);
        when(olympics.getTotalMedals("UK")).thenReturn(5);

        System.out.println("USA Medals: " + olympics.getTotalMedals("USA"));
        System.out.println("UK Medals: " + olympics.getTotalMedals("UK"));

        assertTrue(comparator.compare("USA", "UK") < 0);
        assertTrue(comparator.compare("UK", "USA") > 0);
    }

    @Test
    void countMedals() {
        Athlete a = new Athlete("a", "a", "a");

        a.addMedal(Medal.GOLD);
        a.addMedal(Medal.GOLD);
        a.addMedal(Medal.SILVER);

        System.out.println(olympics.getTotalMedals("a"));
    }

    @Test
    void testCompareSameMedalsReturnsLexicographically() {
        NationMedalComparator comparator = new NationMedalComparator(olympics);

        when(olympics.getTotalMedals("nation1")).thenReturn(10);
        when(olympics.getTotalMedals("nation2")).thenReturn(10);

        assertTrue(comparator.compare("nation1", "nation2") < 0);
    }


    @Test
    void testCompareEmptyMedals() {
        when(olympics.getTotalMedals("USA")).thenReturn(0);
        when(olympics.getTotalMedals("UK")).thenReturn(5);

        assertTrue(comparator.compare("USA", "UK") > 0);
        assertTrue(comparator.compare("UK", "USA") < 0);
    }

    @Test
    void testCompareEqualMedalsAndAlphabeticalOrder() {
        when(olympics.getTotalMedals("USA")).thenReturn(10);
        when(olympics.getTotalMedals("UK")).thenReturn(10);

        TreeSet<String> nations = new TreeSet<>(comparator);
        nations.add("UK");
        nations.add("USA");

        assertEquals("UK", nations.first());
        assertEquals("USA", nations.last());
    }

    @Test
    void testCompareWithNoMedals() {
        when(olympics.getTotalMedals("USA")).thenReturn(0);
        when(olympics.getTotalMedals("China")).thenReturn(0);

        assertTrue(comparator.compare("USA", "China") > 0);
        assertTrue(comparator.compare("China", "USA") < 0);
    }

    @Test
    void testCompareEqualMedalsReverseOrder() {
        when(olympics.getTotalMedals("USA")).thenReturn(10);
        when(olympics.getTotalMedals("China")).thenReturn(10);

        assertTrue(comparator.compare("USA", "China") > 0);
        assertTrue(comparator.compare("China", "USA") < 0);
    }

    @Test
    void testNationsRankListWithTiedMedalCounts() {
        when(olympics.getTotalMedals("NationA")).thenReturn(5);
        when(olympics.getTotalMedals("NationB")).thenReturn(5);
        when(olympics.getTotalMedals("NationC")).thenReturn(3);

        TreeSet<String> nationsRankList = new TreeSet<>(comparator);
        nationsRankList.add("NationA");
        nationsRankList.add("NationB");
        nationsRankList.add("NationC");

        assertEquals(List.of("NationA", "NationB", "NationC"), new ArrayList<>(nationsRankList));
    }
}
