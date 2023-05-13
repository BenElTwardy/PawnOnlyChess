const val GAME_LENGTH = 7
const val BOARD_STRING = "+---+---+---+---+---+---+---+---+"
const val BOARD_STRING_LETTERS = "  a   b   c   d   e   f   g   h  "


val regex = "[a-h][1-8][a-h][1-8]".toRegex()
var gameloop: Boolean = true
var nameFirstPlayer = ""
var nameSecondPlayer: String = ""
var enPasantMove: Boolean = false
var columnOfPassantPawn: Int = -1
val mutable2DList = mutableListOf(
    mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
    mutableListOf("W", "W", "W", "W", "W", "W", "W", "W"),
    mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
    mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
    mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
    mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
    mutableListOf("B", "B", "B", "B", "B", "B", "B", "B"),
    mutableListOf(" ", " ", " ", " ", " ", " ", " ", " "),
)
val matchEnding = MatchEnding()

fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    print("> ")
    nameFirstPlayer = readln()
    println("Second Player's name:")
    print("> ")
    nameSecondPlayer = readln()
    chessBoardGame()
    while (gameloop) {
        askingForFirstPlayerInput(nameFirstPlayer, regex)
        chessBoardGame()
        checkMatchEnding()
        if (!gameloop) continue
        askingForSecondPlayerInput(nameSecondPlayer, regex)
        chessBoardGame()
        checkMatchEnding()
    }
}

fun checkMatchEnding() {
    val boolCaptureEnding = matchEnding.allPawnsCaptured(mutable2DList)
    val boolPawnToOtherSide = matchEnding.onePawnIsGoneToOtherSide(mutable2DList)
    val boolStalement = matchEnding.stalemate(mutable2DList)

    if (boolCaptureEnding || boolPawnToOtherSide || boolStalement) {
        gameloop = false
        println("Bye")
    }
}

fun askingForSecondPlayerInput(secondPlayer: String, regex: Regex) {
    println("$secondPlayer's turn: ")
    print("> ")
    val inputFirstPlayer: String = readln()
    validInput(inputFirstPlayer, secondPlayer, regex)
}

fun askingForFirstPlayerInput(firstPlayer: String, regex: Regex) {
    println("$firstPlayer's turn: ")
    print("> ")
    val inputFirstPlayer: String = readln()
    validInput(inputFirstPlayer,firstPlayer, regex)
}

fun validInput(inputPlayer: String, playerName: String, regex: Regex) {

    if (inputPlayer == "exit") {
        gameloop = false
        println("Bye!")
    }
    else if (inputPlayer.matches(regex)){
        checkIfInputIsPawn(inputPlayer, playerName)
    }
    else {
        println("Invalid Input")
        if (playerName == nameFirstPlayer)
            askingForFirstPlayerInput(nameFirstPlayer, regex)
        else askingForSecondPlayerInput(nameSecondPlayer, regex)
    }
}

fun checkIfInputIsPawn(inputPlayer: String, playerName: String) {

    val charOfPlayer = if (playerName == nameFirstPlayer)
        "W"
    else "B"
    val coloumnInput: String = inputPlayer.substring(0, 1)
    val rowOfInput: String = inputPlayer.substring(1, 2)
    val columnOfFuturePosition = inputPlayer.substring(2, 3)
    val rowOfFuturePosition = inputPlayer.substring(3, 4)
    val howManyFieldsFurther = rowOfFuturePosition.toInt() - rowOfInput.toInt()
    val boolIfThePawnCanCapture = canCapture(rowOfInput, rowOfFuturePosition, coloumnInput, columnOfFuturePosition, charOfPlayer)
    val boolEnPassantCapture = canEnPassantCapture(rowOfInput, coloumnInput, charOfPlayer)


    //checking if the predicted field is obtained by the pawn of the player & checking if its the same column
    if (charOfPlayer == mutable2DList[rowOfInput.toInt() - 1][coloumnInput.first().code - 97]) {
        //checking if the future field is obtained by some other pawn
        if ((mutable2DList[rowOfFuturePosition.toInt() - 1][columnOfFuturePosition.first().code - 97] != "W"
            && mutable2DList[rowOfFuturePosition.toInt() - 1][columnOfFuturePosition.first().code - 97] != "B")
            || boolIfThePawnCanCapture || boolEnPassantCapture) {

            if (boolEnPassantCapture) {
                enPasanteCapture(rowOfInput, rowOfFuturePosition, coloumnInput, columnOfFuturePosition, charOfPlayer)
            }
            //method if you want to capture someone
            else if (boolIfThePawnCanCapture) {
                captureTheEnemyPawn(rowOfInput, rowOfFuturePosition, coloumnInput, columnOfFuturePosition, charOfPlayer)
            }
            //extra method for player one that he can't go backwards
            else if (charOfPlayer == "W") {
                checkingForPlayerOne(
                    rowOfFuturePosition,
                    rowOfInput,
                    columnOfFuturePosition,
                    coloumnInput,
                    howManyFieldsFurther,
                    charOfPlayer
                )
            }
            //extra method for player two that he can't go backwards
            else {
                checkingForPlayerTwo(rowOfFuturePosition, rowOfInput, columnOfFuturePosition, coloumnInput, howManyFieldsFurther, charOfPlayer)
            }
        }
        else {
            println("Invalid Input")

            if (charOfPlayer == "W") askingForFirstPlayerInput(nameFirstPlayer, regex)

            else askingForSecondPlayerInput(nameSecondPlayer, regex)
        }
    }
    //When no pawn of the player is at the predicted position
    else {
        if (charOfPlayer == "W") {
            println("No white pawn at ${coloumnInput + rowOfInput}")
            askingForFirstPlayerInput(nameFirstPlayer, regex)
        } else {
            println("No black pawn at ${coloumnInput + rowOfInput}")
            askingForSecondPlayerInput(nameSecondPlayer, regex)
        }
    }
}

//When bool-var boolEnPassantCapture is true the enPassante can be accomplished
fun enPasanteCapture(rowOfInput: String, rowOfFuturePosition: String, coloumnInput: String, columnOfFuturePosition: String, charOfPlayer: String) {

    if (charOfPlayer == "W") {

        mutable2DList[rowOfInput.toInt() - 1][coloumnInput.first().code - 97] = " "
        mutable2DList[rowOfFuturePosition.toInt()-1][columnOfFuturePosition.first().code-97] = "W"
        mutable2DList[rowOfInput.toInt()-1][columnOfPassantPawn] = " "
        enPasantMove = false
    }
    else {
        mutable2DList[rowOfInput.toInt()-1][coloumnInput.first().code - 97] = " "
        mutable2DList[rowOfFuturePosition.toInt()-1][columnOfFuturePosition.first().code - 97] = "B"
        mutable2DList[rowOfInput.toInt()-1][columnOfPassantPawn] = " "
        enPasantMove = false
    }
}

fun canEnPassantCapture(rowOfInput: String, coloumnInput: String, charOfPlayer: String): Boolean {

    if (enPasantMove) {
        if (charOfPlayer == "W" && rowOfInput.toInt() == 5 && ((columnOfPassantPawn - (coloumnInput.first().code-97) == 1)
                    || (columnOfPassantPawn - (coloumnInput.first().code-97) == -1))) {
            return true
        }
        else if (charOfPlayer == "B" && rowOfInput.toInt() == 4 && ((columnOfPassantPawn - (coloumnInput.first().code-97) == 1)
            || (columnOfPassantPawn - (coloumnInput.first().code-97) == -1))) {
            return true
        }
    }
    return false
}

fun captureTheEnemyPawn(rowOfInput: String, rowOfFuturePosition: String, coloumnInput: String, columnOfFuturePosition: String, charOfPlayer: String) {

    if (charOfPlayer == "W") {
        mutable2DList[rowOfInput.toInt() - 1][coloumnInput.first().code - 97] = " "
        mutable2DList[rowOfFuturePosition.toInt() - 1][columnOfFuturePosition.first().code - 97] = "W"
        enPasantMove = false
    }
    else {
        mutable2DList[rowOfInput.toInt() - 1][coloumnInput.first().code - 97] = " "
        mutable2DList[rowOfFuturePosition.toInt() - 1][columnOfFuturePosition.first().code - 97] = "B"
        enPasantMove = false
    }
}

//Method if your coordinates allow you to capture an enemy
fun canCapture(rowOfInput: String, rowOfFuturePosition: String, coloumnInput: String, columnOfFuturePosition: String
                ,charOfPlayer: String): Boolean {

    if (charOfPlayer == "W") {
        if (rowOfFuturePosition.toInt() - rowOfInput.toInt() == 1 &&
            ((columnOfFuturePosition.first().code - 97) - (coloumnInput.first().code - 97) == 1 ||
            (columnOfFuturePosition.first().code - 97) - (coloumnInput.first().code - 97) == -1) &&
            mutable2DList[rowOfFuturePosition.toInt() -1][columnOfFuturePosition.first().code - 97] == "B")
            return true
    }
    else {
        if (rowOfFuturePosition.toInt() - rowOfInput.toInt() == -1 &&
            ((columnOfFuturePosition.first().code - 97) - (coloumnInput.first().code - 97) == 1 ||
            (columnOfFuturePosition.first().code - 97) - (coloumnInput.first().code - 97) == -1) &&
            mutable2DList[rowOfFuturePosition.toInt() -1][columnOfFuturePosition.first().code - 97] == "W")
            return true
    }
    return false
}

fun checkingForPlayerTwo(rowOfFuturePosition: String, rowOfInput: String, coloumnInput: String, columnOfFuturePosition: String, howManyFieldsFurther: Int, charOfPlayer: String) {

    if (rowOfInput.toInt() == 7 && howManyFieldsFurther == -2) {
        optionalFirstPawnMove(rowOfFuturePosition, rowOfInput, columnOfFuturePosition, charOfPlayer)
    }
    else if (rowOfFuturePosition.toInt() - rowOfInput.toInt() == -1 && (coloumnInput == columnOfFuturePosition)) {
        //update position of the pawn
        mutable2DList[rowOfFuturePosition.toInt() - 1][columnOfFuturePosition.first().code - 97] = "B"
        mutable2DList[rowOfInput.toInt() - 1][columnOfFuturePosition.first().code - 97] = " "
        enPasantMove = false
    }
    else if (rowOfFuturePosition.toInt() - rowOfInput.toInt() < -1){
        println("Invalid Input")
        askingForSecondPlayerInput(nameSecondPlayer, regex)
    }
    else {
        println("Invalid Input")
        askingForSecondPlayerInput(nameSecondPlayer, regex)
    }
}
//method for player one if the pawn is in the second row, and can go one or two fields further
fun optionalFirstPawnMove(rowOfFuturePosition: String, rowOfInput: String, columnOfFuturePosition: String, charOfPlayer: String) {

    if (charOfPlayer == "W") {
        mutable2DList[rowOfFuturePosition.toInt() - 1][columnOfFuturePosition.first().code - 97] = "W"
        mutable2DList[rowOfInput.toInt() - 1][columnOfFuturePosition.first().code - 97] = " "
        enPasantMove = true
        columnOfPassantPawn = columnOfFuturePosition.first().code - 97
    }
    else {
        mutable2DList[rowOfFuturePosition.toInt() - 1][columnOfFuturePosition.first().code - 97] = "B"
        mutable2DList[rowOfInput.toInt() - 1][columnOfFuturePosition.first().code - 97] = " "
        enPasantMove = true
        columnOfPassantPawn = columnOfFuturePosition.first().code - 97
    }
}

fun checkingForPlayerOne(rowOfFuturePosition: String, rowOfInput: String, coloumnInput: String, columnOfFuturePosition: String, howManyFieldsFurther: Int, charOfPlayer: String) {

    if (rowOfInput.toInt() == 2 && howManyFieldsFurther == 2) {
        optionalFirstPawnMove(rowOfFuturePosition, rowOfInput, columnOfFuturePosition, charOfPlayer)
    }
    else if (rowOfFuturePosition.toInt() - rowOfInput.toInt() == 1 && (coloumnInput == columnOfFuturePosition)) {
        //update position of the pawn
        mutable2DList[rowOfFuturePosition.toInt() - 1][columnOfFuturePosition.first().code - 97] = "W"
        mutable2DList[rowOfInput.toInt() - 1][columnOfFuturePosition.first().code - 97] = " "
        enPasantMove = false
    }
    else if (rowOfFuturePosition.toInt() - rowOfInput.toInt() > 1){
        println("Invalid Input")
        askingForFirstPlayerInput(nameFirstPlayer, regex)
    }
    else {
        println("Invalid Input")
        askingForFirstPlayerInput(nameFirstPlayer, regex)
    }
}

fun chessBoardGame() {
    for (i in GAME_LENGTH downTo -1) {
        if (i == -1) {
            println("  $BOARD_STRING")
            println("  $BOARD_STRING_LETTERS")
        } else {
            println("  $BOARD_STRING")
            println(
                "${i+1} " + "| ${mutable2DList[i][0]} | ${mutable2DList[i][1]} | ${mutable2DList[i][2]} | ${mutable2DList[i][3]} " +
                        "| ${mutable2DList[i][4]} | ${mutable2DList[i][5]} | ${mutable2DList[i][6]} | ${mutable2DList[i][7]} |"
            )
        }
    }
    println()
}
