package com.pyramid.testA.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author thunderbolt.lei <br>
 *
 * @description
 *
 */
public class PyramidUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private static final Log LOG = LogFactory.getLog(PyramidUncaughtExceptionHandler.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread,
     * java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // TODO Auto-generated method stub
        if (ShutdownHookManager.get().isShutdownInProgress()) {
            LOG.error("Thread " + t + " threw an Throwable, but we are shutting " + "down, so ignoring this", e);
        } else if (e instanceof Error) {
            try {
                LOG.fatal("Thread " + t + " threw an Error.  Shutting down now...", e);
            } catch (Throwable err) {
                // We don't want to not exit because of an issue with logging
            }
            if (e instanceof OutOfMemoryError) {
                // After catching an OOM java says it is undefined behavior, so don't
                // even try to clean up or we can get stuck on shutdown.
                try {
                    System.err.println("Halting due to Out Of Memory Error...");
                } catch (Throwable err) {
                    // Again we done want to exit because of logging issues.
                }
                ExitUtil.halt(-1);
            } else {
                ExitUtil.terminate(-1);
            }
        } else {
            LOG.error("Thread " + t + " threw an Exception.", e);
        }
    }

}
