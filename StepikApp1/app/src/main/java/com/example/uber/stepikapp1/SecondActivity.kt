package com.example.uber.stepikapp1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText

class SecondActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.second_activity)
        val str = intent.getStringExtra("tag1")
        val vEdit = findViewById<EditText>(R.id.act2_edit)
        vEdit.setText(str)

        findViewById<Button>(R.id.act2_button).setOnClickListener {

            val i1 = Intent(this,MainActivity::class.java)
            i1.putExtra("text",vEdit.text.toString())
            setResult(0,i1)

            finish(); }

    }

}
