package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.PlayerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GameLobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

public class GameServiceTest {

	@Mock
	private GameRepository gameRepository;

	@Mock
	private PlayerRepository playerRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private GameLobbyRepository gamelobbyRepository;

	@Mock
	private PlayerService playerService;

	@Mock
	private GameLobbyService gameLobbyService;

	@InjectMocks
	private GameService gameService;

	private Game testGame;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		gameService = new GameService(gameRepository, playerRepository, userRepository, playerService, gameLobbyService);
		testGame = new Game();
		testGame.setId(1L);
		testGame.setGamepin(111111);
		testGame.setLevel(1);
	}

	@Test
	public void startGame_validInputs_success() {
		// Given
		GameLobby testLobby = new GameLobby();
		testLobby.setPin(111111);

		Player player1 = new Player();
		player1.setId(1L);
		player1.setName("Player1");

		Player player2 = new Player();
		player2.setId(2L);
		player2.setName("Player2");

		Set<GamePlayer> players = new HashSet<>();
		GamePlayer gamePlayer1 = new GamePlayer();
		gamePlayer1.setId(player1.getId());
		gamePlayer1.setName(player1.getName());
		players.add(gamePlayer1);

		GamePlayer gamePlayer2 = new GamePlayer();
		gamePlayer2.setId(player2.getId());
		gamePlayer2.setName(player2.getName());
		players.add(gamePlayer2);

		testLobby.setGamePlayers(players);
		testLobby.setAdmin(player1.getId());
		testGame.setPlayers(players);

		// Mock behavior of the repository
		Mockito.when(gamelobbyRepository.save(Mockito.any())).thenReturn(testLobby);
		Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);

		// Mock behavior of the player service
		Mockito.doNothing().when(playerService).addShame_token(Mockito.anyLong());

		// When
		Game createdGame = gameService.startGame(testLobby);

		// Then
		assertNotNull(createdGame);
		assertEquals(111111, createdGame.getGamepin());
		assertEquals(players, createdGame.getPlayers());
		assertEquals(1, createdGame.getLevel());
		assertEquals(0, createdGame.getSuccessfulMove());
		assertEquals(0, createdGame.getCurrentCard());
		// Additional assertions as needed
	}
	@Test
	public void getGame_nonExistentId_throwsException() {
		Long nonExistentId = 999L;
		Mockito.when(gameRepository.findById(nonExistentId)).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> gameService.getGame(nonExistentId));
	}

	@Test
	public void getGame_validId_returnsGame() {
		Mockito.when(gameRepository.findById(testGame.getId())).thenReturn(Optional.of(testGame));
		Game retrievedGame = gameService.getGame(testGame.getId());
		assertNotNull(retrievedGame);
		assertEquals(testGame.getId(), retrievedGame.getId());
	}

	@Test
	public void startGame_nullLobby_throwsNullPointerException() {
		assertThrows(NullPointerException.class, () -> gameService.startGame(null));
	}


	@Test
	public void testDistributeCards() throws Exception {
		// Setup
		GameService gameService = new GameService(gameRepository, playerRepository, userRepository, playerService, gameLobbyService);
		Game game = new Game();
		game.setLevel(1);
		Set<Integer> cards = new HashSet<>();
		for (int i = 1; i <= 10; i++) {  // Assuming a small deck for simplicity
			cards.add(i);
		}
		game.setCards(cards);

		Set<GamePlayer> players = new HashSet<>();
		GamePlayer player1 = new GamePlayer();
		player1.setCards(new HashSet<>());
		players.add(player1);

		GamePlayer player2 = new GamePlayer();
		player2.setCards(new HashSet<>());
		players.add(player2);

		game.setPlayers(players);

		// Using reflection to access the private method
		Method method = GameService.class.getDeclaredMethod("distributeCards", Game.class);
		method.setAccessible(true);

		// Invoke the private method
		method.invoke(gameService, game);

		// Assertions
		assertTrue(player1.getCards().size() > 0, "Player 1 should have cards");
		assertTrue(player2.getCards().size() > 0, "Player 2 should have cards");
		assertEquals(8, game.getCards().size(), "Two cards should be removed from the deck");

		// Clean up
		method.setAccessible(false);
	}

	@Test
	void testDistributeShameToken() throws Exception {
		// Setup
		Game game = new Game();
		game.setCurrentCard(5);

		GamePlayer player1 = new GamePlayer();
		player1.setId(1L);
		Set<Integer> cardsPlayer1 = new HashSet<>(Arrays.asList(3, 5));
		player1.setCards(cardsPlayer1);
		player1.setShame_tokens(0);

		GamePlayer player2 = new GamePlayer();
		player2.setId(2L);
		Set<Integer> cardsPlayer2 = new HashSet<>(Arrays.asList(1, 2));
		player2.setCards(cardsPlayer2);
		player2.setShame_tokens(0);

		game.setPlayers(new HashSet<>(Arrays.asList(player1, player2)));

		// Reflection to call private method
		Method method = GameService.class.getDeclaredMethod("distributeShameToken", Game.class);
		method.setAccessible(true);

		// Execute
		method.invoke(gameService, game);

		// Verify
		assertEquals(1, player1.getShame_tokens());
		assertEquals(0, player2.getShame_tokens());  // Player 2 should not get a shame token
		verify(playerService, times(1)).addShame_token(player1.getId());
		verify(playerService, never()).addShame_token(player2.getId());

		// Cleanup
		method.setAccessible(false);
	}

	@Test
	void testUpdateGamestatus_Success() {
		// Given
		Long gameId = 1L;
		Integer playedCard = 5;
		Game game = new Game();
		game.setId(gameId);
		game.setLevel(1); // Ensure that the game level is set if it affects card distribution

		// Set up non-empty playing cards
		Set<GamePlayer> players = new HashSet<>();
		GamePlayer player = new GamePlayer();
		Set<Integer> cards = new HashSet<>();
		cards.add(playedCard);  // Ensure at least one card is present that matches the playedCard
		player.setCards(cards);
		players.add(player);
		game.setPlayers(players);

		when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
		when(gameRepository.save(any())).thenReturn(game);
		doNothing().when(gameRepository).flush();

		// When
		Game updatedGame = gameService.updateGamestatus(gameId, playedCard);

		// Then
		assertEquals(playedCard, updatedGame.getCurrentCard()); // Assert that the current card was updated
		verify(gameRepository).save(game); // Verify save was called
		verify(gameRepository).flush(); // Ensure flush was called
	}

	private Set<Integer> createStack() {
		Set<Integer> numbers = new HashSet<>();
		for (int i = 1; i < 100; i++) {
			numbers.add(i);
		}
		return numbers;
	}

	private Set<GamePlayer> preparePlayersWithCards() {
		Set<GamePlayer> players = new HashSet<>();
		GamePlayer player = new GamePlayer();
		Set<Integer> cards = new HashSet<>(Arrays.asList(10, 20));  // Ensure these include the initial currentCard if needed
		player.setCards(cards);
		players.add(player);
		return players;
	}
}