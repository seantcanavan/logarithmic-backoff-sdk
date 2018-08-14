package com.seantcanavan.backoff;

import java.security.SecureRandom;

public class BackOffExecuteTestImplementation implements BackOffExecute<Result> {

    private SecureRandom secureRandom = new SecureRandom();
    private Operation operation = Operation.ADD;
    private int value;
    private int operand;
    private int count;
    private int failCount;
    private int requiredFails;
    private int failChance;

    @Override
    public Result execute() {

        int failCalculation = secureRandom.nextInt(100);

        if (failCalculation > failChance &&  (failCount < requiredFails || requiredFails == 0)) {
            failCount++;
            throw new UnsupportedOperationException("this is an expected exception");
        }

        if (operation == Operation.ADD) {
            value = value + operand;
        } else {
            value = value - operand;
        }

        return new Result(value);
    }

    public Operation getOperation() { return operation; }

    public void setOperation(Operation operation) { this.operation = operation; }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

    public int getFailCount() { return failCount; }

    public void setFailCount(int failCount) { this.failCount = failCount; }

    public int getOperand() { return operand; }

    public void setOperand(int operand) { this.operand = operand; }

    public int getFailChance() { return failChance; }

    public void setFailChance(int failChance) { this.failChance = failChance; }

    public int getRequiredFails() { return requiredFails; }

    public void setRequiredFails(int requiredFails) { this.requiredFails = requiredFails; }
}
