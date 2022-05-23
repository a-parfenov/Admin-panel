package com.game.service;

import com.game.entity.Player;
import com.game.exceptions.ValueNotValidException;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Iterable<Player> findAll(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable).getContent();
    }

    @Override
    public Optional<Player> findById(Long id) throws RuntimeException {
        return playerRepository.findById(id);
    }

    @Override
    public Integer getCount(Specification<Player> specification) {
        return (int) playerRepository.count(specification);
    }

    @Override
    public Player create(Player player) {
        if (player.getBanned() == null)
            player.setBanned(false);

        untilNextLevelValue(player);
        return playerRepository.save(player);
    }

    @Override
    public Player update(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public void delete(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public boolean allValuesAreValidAndFilled(Player player) {

        return requiredFieldsFilled(player) && validName(player)
                && checkDate(player) && checkExperienceValue(player)
                && checkWordLength(player);
    }

    @Override
    public boolean allValuesAreValid(Player player) {

        return validName(player) && checkDate(player)
                && checkExperienceValue(player)
                && checkWordLength(player);
    }

    private boolean requiredFieldsFilled(Player player) throws ValueNotValidException {
        if (player.getName() == null || player.getTitle() == null
                || player.getRace() == null || player.getProfession() == null
                || player.getBirthday() == null || player.getExperience() == null)
            throw new ValueNotValidException("Some required fields are empty");
        return true;
    }

    private boolean validName(Player player) throws ValueNotValidException {
        if (player.getName().isEmpty())
            throw new ValueNotValidException("Name value is empty");
        return true;
    }

    private boolean checkExperienceValue(Player player) throws ValueNotValidException {
        if (player.getExperience() < 0 || player.getExperience() >= 10_000_000)
            throw new ValueNotValidException("Incorrect experience value");
        return true;
    }

    private boolean checkDate(Player player) throws ValueNotValidException {
        if (player.getBirthday() != null) {
            if (player.getBirthday().getTime() < 0 || player.getBirthday().getTime() > 1_653_061_732_000L) {
                throw new ValueNotValidException("Incorrect date value");
            }
        }
        return true;
    }

    private boolean checkWordLength(Player player) throws ValueNotValidException {
        if (player.getName().length() > 12 && player.getTitle().length() > 30)
            throw new ValueNotValidException("Incorrect name or title length");
        return true;
    }

    private void untilNextLevelValue(Player player) {
        int lvl = (int) (Math.sqrt(2500 + (200 * player.getExperience())) - 50) / 100;
        int untilNextLevel = 50 * (lvl + 1) * (lvl + 2) - player.getExperience();
        player.setLevel(lvl);
        player.setUntilNextLevel(untilNextLevel);
    }

    @Override
    public void assignFields(Player fromPlayer, Player toPlayer) {
        if (fromPlayer.getName() != null) toPlayer.setName(fromPlayer.getName());
        if (fromPlayer.getTitle() != null) toPlayer.setTitle(fromPlayer.getTitle());
        if (fromPlayer.getRace() != null) toPlayer.setRace(fromPlayer.getRace());
        if (fromPlayer.getProfession() != null) toPlayer.setProfession(fromPlayer.getProfession());
        if (fromPlayer.getBirthday() != null) toPlayer.setBirthday(fromPlayer.getBirthday());
        if (fromPlayer.getBanned() != null) toPlayer.setBanned(fromPlayer.getBanned());
        if (fromPlayer.getExperience() != null) toPlayer.setExperience(fromPlayer.getExperience());
        untilNextLevelValue(toPlayer);
    }
}
