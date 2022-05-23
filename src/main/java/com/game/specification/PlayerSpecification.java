package com.game.specification;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PlayerSpecification {
    public Specification<Player> filterLikeByName(String name) {
        return ((root, query, criteriaBuilder) -> {
            if (name == null) {
                return null;
            }
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        });
    }

    public Specification<Player> filterLikeByTitle(String title) {
        return ((root, query, criteriaBuilder) -> {
            if (title == null) {
                return null;
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        });
    }

    public Specification<Player> filterEqualsByRace(Race race) {
        return ((root, query, criteriaBuilder) -> {
            if (race == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("race"), race);
        });

    }

    public Specification<Player> filterEqualsByProfession(Profession profession) {
        return ((root, query, criteriaBuilder) -> {
            if (profession == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("profession"), profession);
        });

    }

    public Specification<Player> filterBetweenByBirthday(Long after, Long before) {
        return ((root, query, criteriaBuilder) -> {
            if (after == null && before == null) {
                return null;
            } else if (after == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(before));
            } else if (before == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after));
            } else {
                return criteriaBuilder.between(root.get("birthday"), new Date(after), new Date(before));
            }
        });
    }

    public Specification<Player> filterByBanned(Boolean banned) {
        return ((root, query, criteriaBuilder) -> {
            if (banned == null) {
                return null;
            }
            return banned ? criteriaBuilder.isTrue(root.get("banned")) : criteriaBuilder.isFalse(root.get("banned"));
        });
    }

    public Specification<Player> filterBetweenByExperience(Integer minExperience, Integer maxExperience) {
        return ((root, query, criteriaBuilder) -> {
            if (minExperience == null && maxExperience == null) {
                return null;
            } else if (minExperience == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience);
            } else if (maxExperience == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience);
            } else {
                return criteriaBuilder.between(root.get("experience"), minExperience, maxExperience);
            }
        });
    }

    public Specification<Player> filterBetweenByLevel(Integer minLevel, Integer maxLevel) {
        return ((root, query, criteriaBuilder) -> {
            if (minLevel == null && maxLevel == null) {
                return null;
            } else if (minLevel == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel);
            } else if (maxLevel == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel);
            } else {
                return criteriaBuilder.between(root.get("level"), minLevel, maxLevel);
            }
        });
    }
}
