package test.rave;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import test.model.*;

public class MainTest {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(SimulationResultTest.class, RAVETest.class, NodeRaveTest.class);
    for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("All Tests Passed !");
        } else {
            System.out.println("Test Failed.");
        }
  }
    
}
