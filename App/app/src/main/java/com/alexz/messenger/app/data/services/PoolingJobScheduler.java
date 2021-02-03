package com.alexz.messenger.app.data.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PoolingJobScheduler extends JobScheduler {
    @Override
    public int schedule(@NonNull JobInfo jobInfo) {
        return RESULT_SUCCESS;
    }

    @Override
    public int enqueue(@NonNull JobInfo jobInfo, @NonNull JobWorkItem jobWorkItem) {
        return RESULT_SUCCESS;
    }

    @Override
    public void cancel(int i) {

    }

    @Override
    public void cancelAll() {

    }

    @NonNull
    @Override
    public List<JobInfo> getAllPendingJobs() {
        return null;
    }

    @Nullable
    @Override
    public JobInfo getPendingJob(int i) {
        return null;
    }
}
