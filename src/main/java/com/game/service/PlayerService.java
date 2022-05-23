package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public interface PlayerService {
    Iterable<Player> findAll(Specification<Player> specification, Pageable pageable);
    Optional<Player> findById(Long id) throws NoSuchElementException;
    Integer getCount (Specification<Player> specification);
    Player create(Player player);
    Player update(Player player);
    void delete(Long id);

    boolean allValuesAreValidAndFilled(Player player);
    boolean allValuesAreValid(Player player);
    void assignFields(Player fromPlayer, Player toPlayer);
}
