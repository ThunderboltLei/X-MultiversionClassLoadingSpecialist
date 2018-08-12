/**
 * 
 */
package com.pyramid.pools;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;

/**
 * @author thunderbolt.lei
 *
 *         This thread pool is used for execution and can build multiple maven
 *         projects.
 */
@Singleton
public class XClassPool {

    private static final Logger LOG = LoggerFactory.getLogger(XClassPool.class);

    private static XClassPool pool;

    private ExecutorService es;

    private List<String> futureTasklist;

    private XClassPool() {
        es = Executors.newFixedThreadPool(10);
        futureTasklist = Lists.newArrayList();
    }

    public static XClassPool getInstance() {
        if (pool == null) {
            pool = new XClassPool();
        }
        return pool;
    }

    public void executeTask(FutureTask... tasks) {
        try {
            for (FutureTask task : tasks) {
                es.execute(task);
                if (task.isDone()) {
                    futureTasklist.add(JSON.toJSONString(task.get()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllResults() {
        return futureTasklist;
    }

}
