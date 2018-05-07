package edu.depaul.se.calculator;

/**
 * Performs "business" logic
 */
public final class Calculator {
    /**
     * Adds two numbers together
     * @param lhs
     * @param rhs
     * @return
     */
    public static float add(float lhs, float rhs) {
        return lhs + rhs;
    }

    /**
     * Multiplies two numbers together
     * @param lhs
     * @param rhs
     * @return
     */
    public static float multiply(float lhs, float rhs) {
        return lhs * rhs;
    }

    /**
     * Divide lhs by rhs (lhs/rhs)
     * @param lhs
     * @param rhs
     * @return
     */
    public static float divide(float lhs, float rhs) {
        return lhs / rhs;
    }

    /**
     * Subtract rhs from lhs (lhs - rhs)
     * @param lhs
     * @param rhs
     * @return
     */
    public static float subtract(float lhs, float rhs) {
        return lhs - rhs;
    }
}
