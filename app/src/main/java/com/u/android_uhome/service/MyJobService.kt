package com.u.android_uhome.service

import android.app.job.JobParameters
import android.app.job.JobService


class MyJobService : JobService() {
    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        return false
    }

    override fun onStopJob(jobParameters: JobParameters?): Boolean {
        return false
    }
}