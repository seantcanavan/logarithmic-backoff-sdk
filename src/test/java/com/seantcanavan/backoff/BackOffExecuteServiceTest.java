package com.seantcanavan.backoff;


import com.seantcanavan.backoff.exception.AttemptsExceededException;
import com.seantcanavan.backoff.exception.UnexpectedExceptionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BackOffExecuteServiceTest {

    @Test
    public void execute_With100PercentChanceToFailAndNoAllowedExceptionDefined_ShouldThrowUnexpectedExceptionException() {
        BackOffService backOffService = new BackOffService();
        BackOffExecuteTestImplementation backOffFunctionTestImplementation = new BackOffExecuteTestImplementation();
        backOffFunctionTestImplementation.setRequiredFails(10);
        backOffFunctionTestImplementation.setFailChance(100);

        assertThrows(UnexpectedExceptionException.class,
                ()->{
                    backOffService.execute(backOffFunctionTestImplementation);
                });
    }

    @Test
    public void execute_With100PercentChanceToFailAndAllowedExceptionDefined_ShouldThrowAttemptsExceededException() {
        BackOffService backOffService = new BackOffService(UnsupportedOperationException.class.getSimpleName());
        backOffService.setBackoffSequence(new int[]{1, 1, 1, 1, 1, 1});
        BackOffExecuteTestImplementation backOffFunctionTestImplementation = new BackOffExecuteTestImplementation();
        backOffFunctionTestImplementation.setRequiredFails(10);
        backOffFunctionTestImplementation.setFailChance(100);

        assertThrows(AttemptsExceededException.class,
                ()->{
                    backOffService.execute(backOffFunctionTestImplementation);
                });
    }

    @Test
    public void execute_WithHighChanceToFailAndAllowedExceptionDefined_ShouldEventuallySucceedAndReturnResult() {
        BackOffService backOffService = new BackOffService(UnsupportedOperationException.class.getSimpleName());
        backOffService.setBackoffSequence(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        BackOffExecuteTestImplementation backOffFunctionTestImplementation = new BackOffExecuteTestImplementation();
        backOffFunctionTestImplementation.setFailChance(75);
        backOffFunctionTestImplementation.setOperand(1);
        backOffFunctionTestImplementation.setValue(0);

        Result result = backOffService.execute(backOffFunctionTestImplementation);
        assertEquals(result.getIntResult(), 1);
    }
}
