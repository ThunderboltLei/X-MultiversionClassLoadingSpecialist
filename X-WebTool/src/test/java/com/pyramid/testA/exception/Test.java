package com.pyramid.testA.exception;

/**
 * @author thunderbolt.lei <br>
 *
 * @description This example codes get from Yarn NodeManager.<br>
 *
 */
public class Test {

    private Test() {
        // not allow to construct
    }

    public static void catchAllExceptions() {
        Thread.setDefaultUncaughtExceptionHandler(new PyramidUncaughtExceptionHandler());
    }

    public static void main(String[] args) {

        Test.catchAllExceptions();

        try {
            Thread testTask = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(1 / 0);
                }
            });
            // testTask.setDefaultUncaughtExceptionHandler(new
            // PyramidUncaughtExceptionHandler());
            testTask.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        for (;;) {
            System.out.println("--- waiting for shuttding down ... ---");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
