package test.mcts;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class MainTest {

     public static void main(String[] args) {
        Result result = JUnitCore.runClasses(StateTest.class,NodeMctsTest.class,CacheMapTest.class,MCTSTest.class);
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
