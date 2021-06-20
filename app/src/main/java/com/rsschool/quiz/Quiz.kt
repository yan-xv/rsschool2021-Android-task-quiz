package com.rsschool.quiz

class Quiz {
    public val questions = listOf<Question>(
        Question(1, -1,"Q1", arrayOf<String>("1","2","3","4","5") ),
        Question(2, -1,"Q2", arrayOf<String>("1","2","3","4","5") ),
        Question(3, -1,"Q3", arrayOf<String>("1","2","3","4","5") ),
        Question(4, -1,"Q4", arrayOf<String>("1","2","3","4","5") ),
        Question(0, -1,"Q5", arrayOf<String>("1","2","3","4","5") )
    )

    /*
    public val questions = mutableListOf<Question>()

    constructor() {
        questions.add(Question(1, -1,"Q1", arrayOf<String>("1","2","3","4","5") ))
        questions.add(Question(2, -1,"Q2", arrayOf<String>("1","2","3","4","5") ))
        questions.add(Question(3, -1,"Q3", arrayOf<String>("1","2","3","4","5") ))
        questions.add(Question(4, -1,"Q4", arrayOf<String>("1","2","3","4","5") ))
        questions.add(Question(0, -1,"Q5", arrayOf<String>("1","2","3","4","5") ))
    }

     */
}