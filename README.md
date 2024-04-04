GameTest

    public class GameTest {
        public static void main(String[] args) {
            Player player1 = new Player();
            Player player2 = new Player();
            Player player3 = new Player();
            player1.setUsername("player1");
            player2.setUsername("player2");
            player3.setUsername("player3");
            List<Player> myPlayers = new ArrayList<>();
            myPlayers.add(player1);
            myPlayers.add(player2);
            myPlayers.add(player3);
            GameLobby myLobby = new GameLobby();
            myLobby.setPlayers(myPlayers);
            myLobby.startGame();
            System.out.println(myLobby.getGamestatus().getSuccessfulMove());
            System.out.println(myLobby.getGamestatus().getPlayingCards());
            while(myLobby.getGamestatus().getSuccessfulMove() != 3) {
                Console console = System.console();
                Integer inputInt = Integer.valueOf(console.readLine("Next Move: "));
                myLobby.setGamestatus(myLobby.getGamestatus().updateGamestatus(inputInt));
                System.out.println(myLobby.getGamestatus().getSuccessfulMove());
                // System.out.println(myLobby.getGamestatus().getCards());
                System.out.println(myLobby.getGamestatus().getPlayingCards());
            }
        }  
    }