package com.example.uber.stepikapp1

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var vText:TextView
    var request:Disposable?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)

        vText = findViewById<TextView>(R.id.act1_text)
        vText.setTextColor(0xFFFF0000.toInt())
        vText.setOnClickListener{
            Log.e("tag","Нажата кнопка")
            val i = Intent(this,SecondActivity::class.java)
            i.putExtra("tag1", vText.text)

            startActivityForResult(i,0)



            val o = Observable.create<String> {
                //net

                it.onNext("Something")}.
                    flatMap { Observable.create<String>{}}
                    .zipWith(Observable.create<String>{})
                    .map{it.second + it.first}
                    .subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread())

            request = o.subscribe({},{


            })


//            val t=object:Thread(){
//                override fun run() {
//                    //TODO обращение в сеть
//                    this@MainActivity.runOnUiThread{
//
//
//
//                    }
//                }
//            }
//
//            AT(this).execute()  Плохой пример с утечкой памяти


        }
        Log.v("tag","Запущен onCreate")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null){
        val v = data.getStringExtra("text")
        vText.text = v
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.dispose()
    }


}

//class AT(val act:MainActivity): AsyncTask<String,Int,String>(){
//    override fun doInBackground(vararg p0: String?): String {
//        return "XXX"
//    }
//
//    override fun onPostExecute(result: String?) {
//        super.onPostExecute(result)
//
//    }
//} Даже вывод в отдельный класс не решает проблему
