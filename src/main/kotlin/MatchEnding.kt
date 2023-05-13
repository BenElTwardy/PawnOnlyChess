class MatchEnding {

    fun allPawnsCaptured(mutableList: MutableList<MutableList<String>>): Boolean {

        var whitePawnCounter = 0
        var blackPawnCounter = 0

        for (i in 0..mutableList.size-1) {
            for (j in 0..mutableList[i].size-1) {
                if (mutableList[i][j] == "W") whitePawnCounter++
                else if (mutableList[i][j] == "B") blackPawnCounter++
            }
        }
        if (whitePawnCounter == 0) {
            println("Black Wins!")
            return true
        }
        else if (blackPawnCounter == 0) {
            println("White Wins!")
            return true
        }

        return false
    }

    fun onePawnIsGoneToOtherSide(mutableList: MutableList<MutableList<String>>): Boolean {

        for (i in 0..mutableList.size-1) {
            for (j in 0..mutableList[i].size-1) {
                if (mutableList[0][j] == "B") {
                    println("Black Wins!")
                    return true
                }
                else if (mutableList[7][j] == "W") {
                    println("White Wins!")
                    return true
                }
            }
        }
        return false
    }

    fun stalemate(mutableList: MutableList<MutableList<String>>): Boolean {

        var whitePawnCounter = 0
        var blackPawnCounter = 0

        for (i in 0..mutableList.size-1) {
            for (j in 0..mutableList[i].size-1) {
                if (mutableList[i][j] == "W") {
                    whitePawnCounter++

                }
                else if (mutableList[i][j] == "B") {
                    blackPawnCounter++
                }
            }
        }

        if (whitePawnCounter == 1) {
            for (i in 0..mutableList.size-1) {
                for (j in 0..mutableList[i].size-1) {
                    if (mutableList[i][j] == "W") {
                        if(i == 7 ) continue
                        else if (j == 0) {
                            if (mutableList[i+1][j] == "B" && mutableList[i+1][j+1] == " "){
                                println("Stalemate!")
                                return true
                            }
                        }
                        else if (j == 7) {
                            if (mutableList[i+1][j] == "B" && mutableList[i+1][j-1] == " "){
                                println("Stalemate!")
                                return true
                            }
                        }
                        else if (mutableList[i+1][j] == "B" && (mutableList[i+1][j-1] == " ") && mutableList[i+1][j+1] == " "){
                            println("Stalemate!")
                            return true
                        }
                    }
                }
            }
        }
        if (blackPawnCounter == 1) {
            for (i in 0..mutableList.size-1) {
                for (j in 0..mutableList[i].size-1) {
                    if (mutableList[i][j] == "B") {
                            if(i == 0 ) continue
                            else if (j == 0) {
                                if (mutableList[i-1][j] == "W" && mutableList[i-1][j+1] == " "){
                                    println("Stalemate!")
                                    return true
                                }
                            }
                            else if (j == 7) {
                                if (mutableList[i-1][j] == "W" && mutableList[i-1][j-1] == " "){
                                    println("Stalemate!")
                                    return true
                                }
                            }
                            else if (mutableList[i-1][j] == "B" && (mutableList[i-1][j-1] == " ") && mutableList[i-1][j+1] == " "){
                                println("Stalemate!")
                                return true
                            }
                    }
                }
            }
        }
        return false
    }

}