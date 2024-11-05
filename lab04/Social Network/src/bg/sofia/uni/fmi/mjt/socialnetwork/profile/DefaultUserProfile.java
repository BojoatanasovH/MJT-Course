package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {
    String username;
    Set<UserProfile> friends = new HashSet<>();
    Set<Interest> interests = new HashSet<>();

    public DefaultUserProfile(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUserProfile that = (DefaultUserProfile) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        if (interests == null || interests.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest cannot be null!");
        }
        return interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest cannot be null!");
        }
        return interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        if (friends == null) {
            throw new IllegalArgumentException("Friends is null");
        }
        return Collections.unmodifiableSet(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null || this.equals(userProfile)) {
            throw new IllegalArgumentException("Cannot add self or null as a friend!");
        }
        if (friends.contains(userProfile)) {
            return false;
        }

        friends.add(userProfile);
        userProfile.addFriend(this);
        return true;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null!");
        }
        if (!friends.contains(userProfile)) {
            return false;
        }
        friends.remove(userProfile);
        userProfile.unfriend(this);
        return true;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User cannot be null!");
        }
        return friends.contains(userProfile);
    }
}