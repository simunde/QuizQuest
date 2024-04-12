package com.msid.quizquest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.msid.quizquest.databinding.ActivityQuizActiivityBinding
import com.msid.quizquest.databinding.ItemQuizBinding
import com.msid.quizquest.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        var questionModelList : List<QuestionModel> = listOf()
        var time : String = ""
    }

    lateinit var quizBinding: ActivityQuizActiivityBinding
    var currentQuestionIndex = 0
    var selectedAnswer = ""
    var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        quizBinding = ActivityQuizActiivityBinding.inflate(layoutInflater)
        setContentView(quizBinding.root)
        quizBinding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            btnNext.setOnClickListener(this@QuizActivity)
        }

        loadQuestions()
        startTimer()


    }

    private fun startTimer() {
        val totalTimeInMillis = time.toInt() * 60 * 1000L
        object : CountDownTimer(totalTimeInMillis, 1000L){
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished /1000L
                val minutes = seconds/60
                val remainingSeconds = seconds % 60
                quizBinding.tvTimerIndicator.text = String.format("%02d:%02d",minutes,remainingSeconds)
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }

        }.start()
    }

    private fun loadQuestions(){
        selectedAnswer=""
        if (currentQuestionIndex== questionModelList.size){
            finishQuiz()
            return
        }
        quizBinding.apply {
            tvQuestionIndicator.text= "Question ${currentQuestionIndex+1}/${questionModelList.size}"
            questionProgressIndicator.progress=
                (currentQuestionIndex.toFloat()/ questionModelList.size.toFloat() * 100).toInt()
            tvQuestion.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }
    }

    override fun onClick(view: View?) {

        quizBinding.apply {
            btn0.setBackgroundColor(getColor(R.color.gray))
            btn1.setBackgroundColor(getColor(R.color.gray))
            btn2.setBackgroundColor(getColor(R.color.gray))
            btn3.setBackgroundColor(getColor(R.color.gray))
        }
        val clickedBtn = view as Button
        if (clickedBtn.id == R.id.btnNext){
            if (selectedAnswer== questionModelList[currentQuestionIndex].correct){
                score++
            }
            currentQuestionIndex++
            loadQuestions()

        } else{
            selectedAnswer = clickedBtn.text.toString()
            clickedBtn.setBackgroundColor(getColor(R.color.orange))
        }

    }

    private fun finishQuiz(){

        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat())*100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress = percentage
            tvScoreProgress.text = "$percentage %"
            if (percentage>60){
                tvScoreTitle.text="Congrats! You have passed"
                tvScoreTitle.setTextColor(Color.BLUE)
            }
            else{
                tvScoreTitle.text="Oops! You have failed"
                tvScoreTitle.setTextColor(Color.RED)

            }
            tvScoreSubTitle.text= "$score out of $totalQuestions are correct"
            btnFinish.setOnClickListener {
                finish()
            }

        }
        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }
}