package com.example.mvvm

class Model {

    private val characterList = mutableListOf<MyCharacter>()
    private lateinit var character: MyCharacter

    init {
        fillCharacter()
    }

    private fun fillCharacter() {
        characterList.add(MyCharacter("유비", 80, 80))
        characterList.add(MyCharacter("조조", 85, 95))
        characterList.add(MyCharacter("손권", 70, 75))
        characterList.add(MyCharacter("제갈량", 30, 100))
        characterList.add(MyCharacter("여포", 100, 30))
    }

    fun getRandomCharacter(): MyCharacter {
        character = characterList[(Math.random() * 5).toInt()]
        return character
    }

}