package com.seantcanavan.backoff;


import com.seantcanavan.backoff.exception.AttemptsExceededException;
import com.seantcanavan.backoff.exception.UnexpectedExceptionException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * BackOffService to help throttle requests to resources with limited bandwidth
 * Inspired by: https://carlosbecker.com/posts/exponential-backoff-java8/
 */
public class BackOffService {
    private int[] backoffSequence = new int[] {1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610};
    private Set<String> expectedExceptions = new HashSet<>();

    public BackOffService() { }

    public BackOffService(String exceptionSimpleName) {
        this.expectedExceptions.add(exceptionSimpleName);
    }

    public BackOffService(Set<String> expectedExceptions) {
        this.expectedExceptions = expectedExceptions;
    }

    public BackOffService(int[] backoffSequence, Set<String> expectedExceptions) {
        this.backoffSequence = backoffSequence;
        this.expectedExceptions = expectedExceptions;
    }

    public <T> T execute(BackOffExecute<T> impl) {
        for (int attempt = 0; attempt < backoffSequence.length; attempt++) {
            try {
                return impl.execute();
            } catch (Exception e) {
                handleFailure(attempt, e);
            }
        }
        // if we've run out of attempts - throw a runtime exception
        throw new AttemptsExceededException(String.format("Exceeded the maximum number of throttled execution attempts (%s)", backoffSequence.length));
    }

    private void handleFailure(int attempt, Exception e) {
        // if the exception we received is not expected, then we throw an UnexpectedExceptionException to the caller
        if (!expectedExceptions.contains(e.getClass().getSimpleName())) {
            throw new UnexpectedExceptionException(String.format("Encountered unexpected Exception while trying to throttle: (%s). Was expecting one of: (%s).", e.getClass().getSimpleName(), Arrays.toString(expectedExceptions.toArray())));
        }

        // otherwise - we expect that Exception and we wait before trying again
        doWait(attempt);
    }

    private void doWait(int attempt) {
        try {
            // attempt to sleep before executing again - taking into account previous attempts to execute
            Thread.sleep(backoffSequence[attempt] * 1000);
        } catch (InterruptedException e) {
            // if we're interrupted while sleeping - throw a runtime exception
            throw new RuntimeException(e);
        }
    }

    public int[] getBackoffSequence() { return backoffSequence; }

    public void setBackoffSequence(int[] backoffSequence) { this.backoffSequence = backoffSequence; }

    public Set<String> getExpectedExceptions() { return expectedExceptions; }

    public void setExpectedExceptions(Set<String> expectedExceptions) { this.expectedExceptions = expectedExceptions; }
}
