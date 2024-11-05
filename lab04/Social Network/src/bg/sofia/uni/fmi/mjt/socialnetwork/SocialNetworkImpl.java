package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {
    HashSet<UserProfile> users = new HashSet<>();
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null!");
        }
        if (users.contains(userProfile)) {
            throw new UserRegistrationException("User is already registered!");
        }
        users.add(userProfile);
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        if (users == null || users.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null || content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Invalid user profile or content!");
        }
        if (!users.contains(userProfile)) {
            throw new UserRegistrationException("User is not registered!");
        }
        Post newPost = new SocialFeedPost(userProfile, content);
        posts.add(newPost);
        return newPost;
    }

    @Override
    public Collection<Post> getPosts() {
        if (posts == null || posts.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableCollection(posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null!");
        }
        Set<UserProfile> reachedUsers = new HashSet<>();
        UserProfile author = post.getAuthor();
        for (UserProfile user : users) {
            if (user != author &&
                (user.isFriend(author) || !Collections.disjoint(user.getInterests(), author.getInterests()) ||
                    !Collections.disjoint(user.getFriends(), author.getFriends()))) {
                reachedUsers.add(user);
            }
        }
        return reachedUsers;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
        throws UserRegistrationException {
        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("User profiles cannot be null!");
        }
        if (!users.contains(userProfile1) || !users.contains(userProfile2)) {
            throw new UserRegistrationException("One or both users are not registered!");
        }
        HashSet<UserProfile> mutualFriends = new HashSet<>(userProfile1.getFriends());
        mutualFriends.retainAll(userProfile2.getFriends());
        return mutualFriends;
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sortedProfiles = new TreeSet<>(new Comparator<UserProfile>() {
            @Override
            public int compare(UserProfile u1, UserProfile u2) {
                int friendsCountComparison = Integer.compare(u2.getFriends().size(), u1.getFriends().size());
                if (friendsCountComparison == 0) {
                    return u1.getUsername().compareTo(u2.getUsername());
                }
                return friendsCountComparison;
            }
        });
        sortedProfiles.addAll(users);
        return sortedProfiles;
    }
}