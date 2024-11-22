package bg.sofia.uni.fmi.mjt.olympics.competitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Athlete implements Competitor {

    private final String identifier;
    private final String name;
    private final String nationality;

    private final EnumMap<Medal, Integer> medals;

    public Athlete(String identifier, String name, String nationality) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Competitor's name cannot be null or blank");
        }
        this.identifier = identifier;
        this.name = name;
        this.nationality = nationality;
        this.medals = new EnumMap<>(Medal.class);
    }

    public void addMedal(Medal medal) {
        validateMedal(medal);

        medals.put(medal, medals.getOrDefault(medal, 0) + 1);
    }

    private void validateMedal(Medal medal) {
        if (medal == null) {
            throw new IllegalArgumentException("Medal cannot be null");
        }
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNationality() {
        return nationality;
    }

    @Override
    public Collection<Medal> getMedals() {
        List<Medal> allMedals = new ArrayList<>();
        for (Map.Entry<Medal, Integer> entry : medals.entrySet()) {
            Medal medal = entry.getKey();
            int count = entry.getValue();
            allMedals.addAll(Collections.nCopies(count, medal));
        }
        return Collections.unmodifiableList(allMedals);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Athlete athlete = (Athlete) o;
        return Objects.equals(name, athlete.name) && Objects.equals(nationality, athlete.nationality) &&
            Objects.equals(medals, athlete.medals);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }
}