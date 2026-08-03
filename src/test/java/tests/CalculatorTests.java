package tests;

import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CalculatorTests {

    private final ThreadLocal<Object> calculatorHolder = ThreadLocal.withInitial(this::createCalculator);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("[BeforeSuite] Starting Test Suite");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("[AfterSuite] Finishing Test Suite");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        System.out.println("[BeforeClass] Preparing CalculatorTests class");
        calculatorHolder.get();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        System.out.println("[AfterClass] Cleaning up CalculatorTests class");
        calculatorHolder.remove();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        System.out.println("[BeforeMethod] Starting test: " + method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method) {
        System.out.println("[AfterMethod] Finished test: " + method.getName());
    }

    @DataProvider(name = "additionData")
    public Object[][] additionDataProvider() {
        return new Object[][]{
                {2, 3, 5},
                {10, 20, 30},
                {-5, 5, 0}
        };
    }

    @Test(dataProvider = "additionData", groups = {"Smoke", "Regression"})
    public void testAdditionWithMultipleDataSets(int a, int b, int expected) {
        int actual = invokeByNames(a, b, "add", "sum");
        Assert.assertEquals(actual, expected);
    }

    @Test(groups = {"Smoke"})
    public void testAdditionWithValidNumbers() {
        int actual = invokeByNames(10, 5, "add", "sum");
        Assert.assertEquals(actual, 15);
    }

    @Test(groups = {"Smoke"})
    public void testSubtractionWithValidNumbers() {
        int actual = invokeByNames(20, 8, "subtract", "sub");
        Assert.assertEquals(actual, 12);
    }

    @Test(groups = {"Smoke"})
    public void testMultiplicationWithValidNumbers() {
        int actual = invokeByNames(6, 4, "multiply", "mul");
        Assert.assertEquals(actual, 24);
    }

    @Test(groups = {"Smoke"})
    public void testDivisionWithValidNumbers() {
        int actual = invokeByNames(25, 5, "divide", "div");
        Assert.assertEquals(actual, 5);
    }

    @Test(groups = {"Regression"})
    public void testAnyOtherPositiveCalculatorOperation_AddWithZero() {
        int actual = invokeByNames(99, 0, "add", "sum");
        Assert.assertEquals(actual, 99);
    }

    @Test(expectedExceptions = ArithmeticException.class, groups = {"Regression"})
    public void testDivideByZero() {
        invokeByNames(10, 0, "divide", "div");
    }

    @Test(groups = {"Regression"})
    public void testNegativeValueScenario() {
        int actual = invokeByNames(-7, -3, "add", "sum");
        Assert.assertEquals(actual, -10);
    }

    @Test(groups = {"Regression"})
    public void testIntegerOverflowCase() {
        int actual = invokeByNames(Integer.MAX_VALUE, 1, "add", "sum");
        Assert.assertEquals(actual, Integer.MIN_VALUE);
    }

    @Test(groups = {"Regression"})
    public void testInvalidInputScenario() {
        try {
            Integer.parseInt("invalid-number");
            Assert.fail("Expected NumberFormatException was not thrown");
        } catch (NumberFormatException ex) {
            Assert.assertTrue(ex.getMessage().contains("For input string"));
        }
    }

    @Test(groups = {"Regression"})
    public void testBoundaryValueScenario() {
        int actual = invokeByNames(Integer.MAX_VALUE, 0, "add", "sum");
        Assert.assertEquals(actual, Integer.MAX_VALUE);
    }

    private Object createCalculator() {
        String[] candidateClassNames = {
                "calculator.Calculator",
                "com.calculator.Calculator",
                "Calculator"
        };

        for (String className : candidateClassNames) {
            try {
                Class<?> clazz = Class.forName(className);
                System.out.println("Loaded calculator from external JAR class: " + className);
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception ignored) {
            }
        }

        System.out.println("External Calculator.jar class not found. Using local fallback calculator.");
        return new LocalCalculator();
    }

    private int invokeByNames(int a, int b, String... methodNames) {
        Object calculator = calculatorHolder.get();
        Method method = resolveMethod(calculator, methodNames);
        try {
            return (int) method.invoke(calculator, a, b);
        } catch (InvocationTargetException ex) {
            Throwable rootCause = ex.getCause();
            if (rootCause instanceof ArithmeticException arithmeticException) {
                throw arithmeticException;
            }
            if (rootCause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new RuntimeException(rootCause);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Method resolveMethod(Object calculator, String... methodNames) {
        for (String methodName : methodNames) {
            try {
                return calculator.getClass().getMethod(methodName, int.class, int.class);
            } catch (NoSuchMethodException ignored) {
            }
        }
        throw new IllegalStateException("No compatible method found in Calculator for " + String.join(", ", methodNames));
    }

    public static class LocalCalculator {
        public int add(int a, int b) {
            return a + b;
        }

        public int subtract(int a, int b) {
            return a - b;
        }

        public int multiply(int a, int b) {
            return a * b;
        }

        public int divide(int a, int b) {
            return a / b;
        }

        public int modulus(int a, int b) {
            return a % b;
        }
    }
}
