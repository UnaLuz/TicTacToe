import kotlin.system.exitProcess

fun main() {
    // General constants
    val positions = listOf(
            "TL", "TC", "TR",
            "ML", "MC", "MR",
            "BL", "BC", "BR")

    println("<Welcome to my little Tic Tac Toe game in Kotlin>")

    while (true) {
        println("""<The board is as follows>

+-------+-------+-------+
|       |       |       |
|  T L  |  T C  |  T R  |
|       |       |       |
+-------+-------+-------+
|       |       |       |
|  M L  |  M C  |  M R  |
|       |       |       |
+-------+-------+-------+
|       |       |       |
|  B L  |  B C  |  B R  |
|       |       |       |
+-------+-------+-------+

<Now please choose one option>
(1) Single Player
(2) Two Players
(3) Quit
""")

        // Game specific variables
        val ttt = intArrayOf(
                0, 0, 0,
                0, 0, 0,
                0, 0, 0)
        var turns = 0
        var playerWon = false

        var gameOption = readLine() ?: "Error"
        while (gameOption != "1" && gameOption != "2" && gameOption != "3") {
            println("Please enter 1, 2 or 3")
            gameOption = readLine() ?: "Error"
        }

        if (gameOption == "3") {
            println("Ok see ya")
            exitProcess(0) // Finish the game
        }

        println("""<To play, just enter the corresponding pair of letters
for where you want to place the piece without spaces>""")

        printTTT(ttt)
        while (!playerWon && turns < 9) {
            val player = if (turns % 2 == 0) 'X' else 'O'
            var pos: Int

            if (gameOption == "1" && player == 'O') { // Computer makes a move
                println("\n<Computer's turn>")
                // Get all positions available, update the board and display it
                val availableIndex = getAvailablePositions(ttt)
                val computerChoice = availableIndex.random()
                ttt[computerChoice] = -1
                printTTT(ttt)

                pos = computerChoice
            }
            else { // Human makes a move
                println("\n<Player ${(turns % 2) + 1}>")
                println("<What's your next move?>")
                var playerChoice = readLine()?.toUpperCase() ?: "Error"
                while (playerChoice !in positions) {
                    println("<That position doesn't exist>")
                    playerChoice = readLine()?.toUpperCase() ?: "Error"
                }

                pos = positions.indexOf(playerChoice)
                if (ttt[pos] != 0) {
                    println("That position is already taken, try again")
                    continue
                }

                // Update the board and print it
                ttt[pos] = if (player == 'X') 1 else -1
                printTTT(ttt)
            }

            // Check if player won with that move
            playerWon = checkRow(ttt, pos / 3) || checkColumn(ttt, pos % 3)
            playerWon = playerWon || checkLRDiagonal(ttt) || checkRLDiagonal(ttt)

            if(playerWon) println("\n<Player ${(turns % 2) + 1} wins!>\n")

            turns += 1
        }
        println("<Game over>")
        println("\n\n<Enter anything to continue>")
        readLine()
        println("\n\n\n")
    }
}

/**
 * A position is available if there is a zero in it
 * returns a list of the indices of all available positions
 */
fun getAvailablePositions(board: IntArray): List<Int> = board.withIndex().filter {it.value == 0}.map { it.index }

/**
 * Checks in [row] if either player won
 * [arr] must be an IntArray of length 9
 * [row] must be between 0 and 2
 */
fun checkRow(arr: IntArray, row: Int): Boolean{
    var gameWon = 0
    for(j in 0..2){
        gameWon += arr[row*3 + j]
    }
    return (gameWon == 3 || gameWon == -3)
}

/**
 * Checks in the column [col] if either player won
 * [arr] must be an IntArray of length 9
 * [col] must be between 0 and 2
 */
fun checkColumn(arr: IntArray, col: Int): Boolean{
    var gameWon = 0
    for(i in 0..2){
        gameWon += arr[i*3 + col]
    }
    return (gameWon == 3 || gameWon == -3)
}

/**
 * Checks diagonal from top left to bottom right if either player won
 * [arr] must be an IntArray of length 9
 */
fun checkLRDiagonal(arr: IntArray): Boolean{
    var gameWon = 0
    for(i in 0..2){
        gameWon += arr[i*3 + i]
    }
    return (gameWon == 3 || gameWon == -3)
}

/**
 * Checks in the diagonal from top right to bottom left if either player won
 * [arr] must be an IntArray of length 9
 */
fun checkRLDiagonal(arr: IntArray): Boolean{
    var gameWon = 0
    for(i in 0..2){
        gameWon += arr[i*3 + 2 - i]
    }
    return (gameWon == 3 || gameWon == -3)
}

/**
 * Prints the current state of [arr] as a Tic Tac Toe board
 * [arr] must be an IntArray of length 9, 1's will be translated to X's and -1's to O's
 */
fun printTTT(arr: IntArray){
    if (arr.size != 9) return
    for(i in 0..2){
        println("+---+---+---+")
        for(j in 0..2){
            val n = arr[i*3 + j]
            val mark = if (n == 1) "X" else if (n == -1) "O" else " "
            print("| $mark ")
        }
        println("|")
    }
    println("+---+---+---+")
}