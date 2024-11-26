package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MJTOlympicsTest {

    private CompetitionResultFetcher competitionResultFetcher;
    private MJTOlympics mjtOlympics;

    private Set<Competitor> competitors;

    @BeforeEach
    void setup() {
        competitionResultFetcher = Mockito.mock(CompetitionResultFetcher.class);

        competitors = new HashSet<>();
        competitors.add(new Athlete("1", "Athlete A", "NationA"));
        competitors.add(new Athlete("2", "Athlete B", "NationB"));
        competitors.add(new Athlete("3", "Athlete C", "NationC"));

        mjtOlympics = new MJTOlympics(competitors, competitionResultFetcher);
    }

    @Test
    void testUpdateMedalStatisticsLessThanThreeCompetitors() {
        Set<Competitor> fewCompetitors = Set.of(
            new Athlete("1", "Athlete A", "NationA"),
            new Athlete("2", "Athlete B", "NationB")
        );

        Competition competition = new Competition("Olympics", "Swimming", fewCompetitors);
        TreeSet<Competitor> ranking = new TreeSet<>(Comparator.comparing(Competitor::getName));
        ranking.addAll(fewCompetitors);

        when(competitionResultFetcher.getResult(competition)).thenReturn(ranking);

        mjtOlympics.updateMedalStatistics(competition);

        Map<String, EnumMap<Medal, Integer>> medalTable = mjtOlympics.getNationsMedalTable();

        assertEquals(1, medalTable.get("NationA").get(Medal.GOLD));
        assertEquals(1, medalTable.get("NationB").get(Medal.SILVER));
    }

    @Test
    void testNationsRankListWithTiedMedalCounts() {
        Set<Competitor> registeredCompetitors = new HashSet<>();
        Athlete athleteA = new Athlete("1", "Athlete A", "NationA"); // Gold in Cycling, Silver in Running
        Athlete athleteB = new Athlete("2", "Athlete B", "NationB"); // Silver in Cycling
        Athlete athleteC = new Athlete("3", "Athlete C", "NationC"); // Gold in Running
        registeredCompetitors.add(athleteA);
        registeredCompetitors.add(athleteB);
        registeredCompetitors.add(athleteC);

        mjtOlympics = new MJTOlympics(registeredCompetitors, competitionResultFetcher);

        Set<Competitor> competitorsCycling = new HashSet<>();
        competitorsCycling.add(athleteA);
        competitorsCycling.add(athleteB);

        Competition cyclingCompetition = new Competition("Olympics", "Cycling", competitorsCycling);
        TreeSet<Competitor> cyclingResults = new TreeSet<>(new Comparator<>() {
            @Override
            public int compare(Competitor c1, Competitor c2) {
                return c1.getIdentifier().compareTo(c2.getIdentifier());
            }
        });
        cyclingResults.add(athleteA); // Gold
        cyclingResults.add(athleteB); // Silver
        when(competitionResultFetcher.getResult(cyclingCompetition)).thenReturn(cyclingResults);

        mjtOlympics.updateMedalStatistics(cyclingCompetition);

        Set<Competitor> competitorsRunning = new HashSet<>();
        competitorsRunning.add(athleteA);
        competitorsRunning.add(athleteC);

        Competition runningCompetition = new Competition("Olympics", "Running", competitorsRunning);
        TreeSet<Competitor> runningResults = new TreeSet<>(new Comparator<>() {
            @Override
            public int compare(Competitor c1, Competitor c2) {
                return c1.getIdentifier().compareTo(c2.getIdentifier());
            }
        });
        runningResults.add(athleteC); // Gold
        runningResults.add(athleteA); // Silver
        when(competitionResultFetcher.getResult(runningCompetition)).thenReturn(runningResults);

        mjtOlympics.updateMedalStatistics(runningCompetition);

        TreeSet<String> nationsRankList = mjtOlympics.getNationsRankList();
        assertEquals(List.of("NationA", "NationB", "NationC"), new ArrayList<>(nationsRankList),
            "Nations should be ranked by total medals with ties broken alphabetically.");
    }

    @Test
    void testUpdateMedalStatisticsNullCompetition() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> mjtOlympics.updateMedalStatistics(null));

        assertEquals("Competition cannot be null", exception.getMessage());
    }

    @Test
    void testUpdateMedalStatisticsUnregisteredCompetitor() {
        Set<Competitor> unregisteredCompetitors = Set.of(
            new Athlete("4", "Athlete D", "NationD")
        );

        Competition competition = new Competition("Olympics", "Archery", unregisteredCompetitors);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> mjtOlympics.updateMedalStatistics(competition));

        assertEquals("Not all competitors are registered for the Olympics", exception.getMessage());
    }

    @Test
    void testTotalMedalsByNation() {
        TreeSet<Competitor> ranking = new TreeSet<>(Comparator.comparing(Competitor::getName));
        ranking.add(new Athlete("1", "Athlete A", "NationA")); // Gold

        Competition competition = new Competition("Olympics", "Rowing", competitors);
        when(competitionResultFetcher.getResult(competition)).thenReturn(ranking);

        mjtOlympics.updateMedalStatistics(competition);

        int totalMedalsNationA = mjtOlympics.getTotalMedals("NationA");

        assertEquals(1, totalMedalsNationA);
    }

    @Test
    void testUpdateMedalStatistics() {
        CompetitionResultFetcher fetcher = Mockito.mock(CompetitionResultFetcher.class);

        Competitor athlete1 = new Athlete("1", "Athlete 1", "USA");
        Competitor athlete2 = new Athlete("2", "Athlete 2", "Canada");

        Set<Competitor> competitors = Set.of(athlete1, athlete2);
        Competition competition = new Competition("Running", "Athletics", competitors);

        TreeSet<Competitor> ranking = new TreeSet<>(Comparator.comparing(Competitor::getName));
        ranking.add(athlete1);
        ranking.add(athlete2);

        when(fetcher.getResult(competition)).thenReturn(ranking);

        MJTOlympics olympics = new MJTOlympics(competitors, fetcher);
        olympics.updateMedalStatistics(competition);

        assertEquals(1, olympics.getTotalMedals("USA"), "USA should have 1 medal");
        assertEquals(1, olympics.getTotalMedals("Canada"), "Canada should have 1 medal");
    }

    @Test
    void testInvalidCompetitionThrowsException() {
        CompetitionResultFetcher fetcher = Mockito.mock(CompetitionResultFetcher.class);
        MJTOlympics olympics = new MJTOlympics(new HashSet<>(), fetcher);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> olympics.updateMedalStatistics(null));

        assertEquals("Competition cannot be null", exception.getMessage());
    }
}
