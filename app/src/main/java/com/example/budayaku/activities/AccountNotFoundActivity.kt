package com.example.budayaku.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.budayaku.R
import kotlinx.android.synthetic.main.activity_account_not_found.*

class AccountNotFoundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_not_found)

        tv_linkLoginA.setOnClickListener {
            startActivity(Intent(this@AccountNotFoundActivity, LoginActivity::class.java))
        }
    }
}
