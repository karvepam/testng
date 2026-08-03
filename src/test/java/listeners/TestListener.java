package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("[Listener] Test execution started: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[Listener] Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[Listener] Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("[Listener] Test Failed: " + result.getMethod().getMethodName());
        System.out.println("[Listener] Reason: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("[Listener] Test execution finished: " + context.getName());
    }
}
