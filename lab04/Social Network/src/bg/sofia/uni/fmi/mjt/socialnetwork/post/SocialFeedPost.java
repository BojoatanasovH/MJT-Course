package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Collections;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SocialFeedPost implements Post {
    String uniqueId;
    UserProfile author;
    LocalDateTime publishedOn;
    String content;
    HashMap<ReactionType, Set<UserProfile>> reactions = new HashMap<>();

    public SocialFeedPost(UserProfile author, String content) {
        this.uniqueId = UUID.randomUUID().toString();
        this.author = author;
        this.content = content;
        this.publishedOn = LocalDateTime.now();
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null || reactionType == null) {
            throw new IllegalArgumentException("User and reaction cannot be null!");
        }
        reactions.putIfAbsent(reactionType, new HashSet<>());
        Set<UserProfile> reactionUsers = reactions.get(reactionType);
        boolean updated = !reactionUsers.contains(userProfile);
        reactionUsers.add(userProfile);
        return updated;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null!");
        }
        boolean reactionRemoved = false;

        for (Set<UserProfile> userProfiles : reactions.values()) {
            if (userProfiles.contains(userProfile)) {
                userProfiles.remove(userProfile);
                reactionRemoved = true;
            }
        }
        return reactionRemoved;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        if (reactions == null || reactions.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(reactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("Reaction type cannot be null!");
        }
        if (reactions == null) {
            throw new IllegalArgumentException("Reactions cannot be null!");
        }
        if (reactions.containsKey(reactionType)) {
            return reactions.get(reactionType).size();
        }
        return 0;
    }

    @Override
    public int totalReactionsCount() {
        int reactionCount = 0;
        for (Set<UserProfile> users : reactions.values()) {
            for (UserProfile _ : users) {
                reactionCount++;
            }
        }
        return reactionCount;
    }
}