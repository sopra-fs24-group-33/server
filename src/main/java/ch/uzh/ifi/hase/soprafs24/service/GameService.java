package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.GamePlayer;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Game;
import ch.uzh.ifi.hase.soprafs24.entity.GameLobby;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.*;
import java.util.Random;
import java.util.Set;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    private final GameLobbyService gamelobbyService;
    private final PlayerService playerService;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, PlayerRepository playerRepository, UserRepository userRepository, PlayerService playerService, GameLobbyService gamelobbyService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
        this.playerService = playerService;
        this.gamelobbyService = gamelobbyService;
    }

    public Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found with ID: " + id));
    }

    private Set<Integer> createStack() {
        Set<Integer> numbers = new HashSet<>();
        for (int i = 1; i < 100; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    public void deleteGame(Long id) {
        try {
            Game game = this.getGame(id);
            gamelobbyService.deleteReference(game.getGamepin());
            game.getPlayers().forEach(player -> {
                player.setGame(null);
                player.setCards(null);
                Player myPlayer = playerRepository.findById(id).get();
                if (myPlayer.getIsUser() != null) {
                    User myUser = userRepository.findById(id).get();
                    // System.out.println(myUser.getGamesPlayed());
                    myUser.setGamesPlayed((myUser.getGamesPlayed() + 1));
                    userRepository.save(myUser);
                    userRepository.flush();
                    // System.out.println(myUser.getGamesPlayed());
                }
            });
            gameRepository.delete(game);
            gameRepository.flush();
        } catch (ResponseStatusException ex) {
            throw ex;
        }
    }

    public Game startGame(GameLobby lobby) {
        try {
            Game game = new Game();
            game.setGamepin(lobby.getPin());
            Set<GamePlayer> lobbyPlayers = lobby.getGamePlayers();
            for (GamePlayer gamePlayer : lobbyPlayers) {
                game.getPlayers().add(gamePlayer);
                gamePlayer.setGame(game);
                gamePlayer.setShame_tokens(0);
            }
            game.setLevel(1);
            game.setSuccessfulMove(0);
            game.setCurrentCard(0);
            gameRepository.save(game);
            gameRepository.flush();
            return game;
        }
        catch (ResponseStatusException ex) {
            throw ex;
        }
    }

    public void doRound(Game game) {
        if (game.getCurrentCard() == 0) {
            game.setCards(createStack());
            distributeCards(game);
        } else if (game.getSuccessfulMove() == 2) {
            if (!game.getPlayingCards().isEmpty()) {
                for (Integer toBeDeleted : game.getPlayingCards()) {
                    game.setCurrentCard(toBeDeleted);
                    deleteCard(game);
                }
            }
            game.setSuccessfulMove(1);
            distributeCards(game);
        } else if (game.getSuccessfulMove() == 3) {
            this.deleteGame(game.getId());
        } else {
            doMove(game);
        }
    }

    public Game updateGamestatus(Long id, Integer playedCard) {
        try {
            Game game = this.getGame(id);
            game.setCurrentCard(playedCard);
            doRound(game);
            gameRepository.save(game);
            gameRepository.flush();
            return game;
        } catch (ResponseStatusException ex) {
            throw ex;
        }
    }

    private void distributeShameToken(Game game) {
        for (GamePlayer player : game.getPlayers()) {
            Set<Integer> myCards = player.getCards();
            for (Integer card : myCards) {
                if (card.equals(game.getCurrentCard())) {
                    Integer currentShameToken = player.getShame_tokens();
                    player.setShame_tokens(currentShameToken + 1);
                    playerService.addShame_token(player.getId());
                }
            }
        }
    }

    private void deleteCard(Game game) {
        for (GamePlayer player : game.getPlayers()) {
            if (player.getCards().contains(game.getCurrentCard())) {
                player.getCards().remove(game.getCurrentCard());
                break;
            }
        }
        game.getCards().remove(game.getCurrentCard());
    }

    private void doMove(Game game) {
        Set<Integer> playingCards = game.getPlayingCards();
        Integer minimum = Collections.min(playingCards);
        if (game.getCurrentCard().equals(minimum)) {
            game.setSuccessfulMove(1);
            deleteCard(game);
            if (playingCards.size() == 1) {
                game.setLevel(game.getLevel() + 1);
                distributeCards(game);
            }
        } else {
            distributeShameToken(game);
            deleteCard(game);
            game.setSuccessfulMove(2);
        }
    }

    private void distributeCards(Game game) {
        if (game.getCards().size() >= game.getLevel() * game.getPlayers().size()) {
            Random rand = new Random();
            Set<Integer> cardStack = new HashSet<>(game.getCards());

            for (GamePlayer player : game.getPlayers()) {
                if (player.getCards() == null) {
                    player.setCards(new HashSet<>());
                }
                for (int j = 0; j < game.getLevel(); j++) {
                    if (!cardStack.isEmpty()) {
                        int randomIndex = rand.nextInt(cardStack.size());
                        Integer selectedCard = new ArrayList<>(cardStack).get(randomIndex);
                        player.getCards().add(selectedCard);
                        cardStack.remove(selectedCard);
                    }
                }
            }
            game.setCards(cardStack);
        } else {
            game.setSuccessfulMove(3); // not enough cards -> end game
        }
    }
}

