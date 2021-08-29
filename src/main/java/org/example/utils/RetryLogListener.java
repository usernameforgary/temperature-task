package org.example.utils;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryLogListener implements RetryListener {
    private static final Logger logger = LoggerFactory.getLogger(RetryLogListener.class);

    @Override
    public <V> void onRetry(Attempt<V> attempt) {
        // number of retry times. number = 1 means first time try, not retry
        // why exception caused
        if (attempt.hasException() && attempt.getAttemptNumber() > 1) {
            logger.info("causeBy = " + attempt.getExceptionCause().toString());
            logger.info("retry time = " + (attempt.getAttemptNumber() - 1));
        } else {
            // successful return, no exception
            //logger.info("result=" + attempt.getResult());
        }
    }
}
