package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exceptions.EntityNotFoundException;
import com.game.exceptions.ValueNotValidException;
import com.game.service.PlayerService;
import com.game.specification.PlayerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerSpecification playerSpecification;
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerSpecification playerSpecification) {
        this.playerService = playerService;
        this.playerSpecification = playerSpecification;
    }

    @GetMapping("/players")
    public Iterable<Player> getPlayersList(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "race", required = false) Race race,
                                @RequestParam(value = "profession", required = false) Profession profession,
                                @RequestParam(value = "after", required = false) Long after,
                                @RequestParam(value = "before", required = false) Long before,
                                @RequestParam(value = "banned", required = false) Boolean banned,
                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Specification<Player> specification = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Specification
                                                .where(playerSpecification.filterLikeByName(name))
                                                .and(playerSpecification.filterLikeByTitle(title)))
                                        .and(playerSpecification.filterEqualsByRace(race)))
                                .and(playerSpecification.filterEqualsByProfession(profession)))
                        .and(playerSpecification.filterBetweenByBirthday(after, before)))
                .and(playerSpecification.filterByBanned(banned))
                .and(playerSpecification.filterBetweenByExperience(minExperience, maxExperience))
                .and(playerSpecification.filterBetweenByLevel(minLevel, maxLevel));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return playerService.findAll(specification, pageable);
    }

    @GetMapping("/players/count")
    public int getPlayersCount(@RequestParam(value = "name", required = false) String name,
                     @RequestParam(value = "title", required = false) String title,
                     @RequestParam(value = "race", required = false) Race race,
                     @RequestParam(value = "profession", required = false) Profession profession,
                     @RequestParam(value = "after", required = false) Long after,
                     @RequestParam(value = "before", required = false) Long before,
                     @RequestParam(value = "banned", required = false) Boolean banned,
                     @RequestParam(value = "minExperience", required = false) Integer minExperience,
                     @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                     @RequestParam(value = "minLevel", required = false) Integer minLevel,
                     @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        Specification<Player> specification = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Specification
                                                                .where(playerSpecification.filterLikeByName(name))
                                                                .and(playerSpecification.filterLikeByTitle(title)))
                                                        .and(playerSpecification.filterEqualsByRace(race)))
                                                .and(playerSpecification.filterEqualsByProfession(profession)))
                                        .and(playerSpecification.filterBetweenByBirthday(after, before)))
                                .and(playerSpecification.filterByBanned(banned)))
                        .and(playerSpecification.filterBetweenByExperience(minExperience, maxExperience)))
                .and(playerSpecification.filterBetweenByLevel(minLevel, maxLevel));

        return playerService.getCount(specification);
    }

    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player player) {
        if (!playerService.allValuesAreValidAndFilled(player))
            throw new RuntimeException("Incorrect new player values!");
        return playerService.create(player);
    }

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable("id") long id) {
        if (id < 1 && Math.floor(id) == id)
            throw new ValueNotValidException("Incorrect id number " + id + "!");
        Optional<Player> player = playerService.findById(id);
         if (!player.isPresent())
             throw new EntityNotFoundException("Player with id " + id + " not found!");
         return player.get();
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@PathVariable("id") long id, @RequestBody Player player) {
        if (id < 1 && Math.floor(id) == id)
            throw new ValueNotValidException("Incorrect id number " + id + "!");

        Optional<Player> tmpPlayer = playerService.findById(id);
        if (!tmpPlayer.isPresent())
            throw new EntityNotFoundException("Player with id " + id + " not found!");

        Player toPlayer = tmpPlayer.get();
        playerService.assignFields(player, toPlayer);
        if (!playerService.allValuesAreValid(toPlayer))
            throw new RuntimeException("Incorrect new player values!");
        return playerService.update(toPlayer);
    }

    @DeleteMapping("/players/{id}")
    public String deletePlayer(@PathVariable ("id") long id) {
        if (id < 1 && Math.floor(id) == id)
            throw new ValueNotValidException("Incorrect id number " + id + "!");

        Optional<Player> tmpPlayer = playerService.findById(id);
        if (!tmpPlayer.isPresent())
            throw new EntityNotFoundException("Player with id " + id + " not found!");
        playerService.delete(id);
        return "Player " + id + " was deleted!";
    }
}
