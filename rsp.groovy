import groovyx.gpars.actor.DefaultActor

/*
http://www.ibm.com/developerworks/ru/library/j-gpars/
исправленная версия скрипта "Камень-Ножницы-Бумага" для Gpars 1.1.0
*/

@Grab(group='org.codehaus.gpars', module='gpars', version='1.1.0')
enum Move {
    ROCK, PAPER, SCISSORS
}

class Player1 extends DefaultActor {
    String name
    def random = new Random()

    void act() {
        loop {
            react {
                // игрок отвечает случайным ходом
                reply Move.values()[random.nextInt(Move.values().length)]
            }
        }
    }
}

class Coordinator extends DefaultActor {
    Player1 player1
    Player1 player2
    int games

    void act() {
        loop {
            react {
                // начало игры
                player1.send("play")
                player2.send("play")

                // определение победителя
                react {msg1 ->
                    def sender1 = sender.name
                    react {msg2 ->
                        def sender2 = sender.name
                        announce(sender1, msg1, sender2, msg2)

                        // продолжение игры
                        if(games--)
                            send("start")
                        else
                            stop()
                    }
                }
            }
        }
    }

    void announce(p1, m1, p2, m2) {
        String winner = "tie"
        if(firstWins(m1, m2) && ! firstWins(m2, m1)) {
            winner = p1
        } else if(firstWins(m2, m1) && ! firstWins(m1, m2)) {
            winner = p2
        } // в противном случае, ничья

        if(p1 == 'Player 2'){
            (p1,p2,m1,m2)=[p2,p1,m2,m1]
        }
        println toString(p1, m1) + toString(p2, m2) + ": winner = " + winner
    }

    String toString(player, move) {
        "$player ($move), ".padRight(22,' ')
    }

    boolean firstWins(Move m1, Move m2) {
        return (m1 == Move.ROCK && m2 == Move.SCISSORS) ||
        (m1 == Move.PAPER && m2 == Move.ROCK) ||
        (m1 == Move.SCISSORS && m2 == Move.PAPER)
    }
}


final def player1 = new Player1(name: "Player 1")
final def player2 = new Player1(name: "Player 2")
final def coordinator = new Coordinator(player1: player1, player2: player2, games: 10)

[player1, player2, coordinator]*.start()
coordinator << "start"
coordinator.join()
[player1, player2]*.terminate()
