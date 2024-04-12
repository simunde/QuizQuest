package com.msid.quizquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.msid.quizquest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList : MutableList<QuizModel>
    lateinit var adapter: QuizListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFireBase()

    }

    private fun getDataFromFireBase(){
        //dummy data
        binding.progressBar.progress= View.VISIBLE
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener {
                dataSnapShot->
                if(dataSnapShot.exists()){
                    for (snapshot in dataSnapShot.children){
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                }
                setUpRecyclerView()
            }


    }

    private fun setUpRecyclerView() {
        binding.progressBar.progress= View.GONE
         binding.recylerView.adapter=QuizListAdapter(quizModelList)
        binding.recylerView.layoutManager=LinearLayoutManager(this)
    }
}