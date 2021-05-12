package net.sinasoheili.best_sellers.model

data class Answer constructor(val idUser: Int, val idQuestion: Int, val idShop: Int, var score: Int = 0)