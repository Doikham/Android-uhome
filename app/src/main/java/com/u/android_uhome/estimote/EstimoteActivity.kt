package com.u.android_uhome.estimote

import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.u.android_uhome.R


class EstimoteActivity : AppCompatActivity(){
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimote)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult?> {
                override fun onComplete(@NonNull task: Task<InstanceIdResult?>) {
                    if (!task.isSuccessful) { //To do//
                        return
                    }
                    // Get the Instance ID token//
                    val token: String? = task.result?.token
                    val msg = getString(R.string.fcm_token, token)

                    Log.d(TAG, msg)
                }
            })
    }

}
