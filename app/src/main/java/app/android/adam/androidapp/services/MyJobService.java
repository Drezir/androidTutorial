package app.android.adam.androidapp.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class MyJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("Job", "Job has started successfully");
        // boolean isFail=true; // getting from HTTP
        jobFinished(params, true);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("Job", "Job has stopped");
        return false;
    }
}
