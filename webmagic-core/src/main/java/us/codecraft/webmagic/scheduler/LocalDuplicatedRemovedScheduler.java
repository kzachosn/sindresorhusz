package us.codecraft.webmagic.scheduler;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.monitor.MonitorableScheduler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base Scheduler with duplicated urls removed locally.
 *
 * @author code4crafter@gmail.com
 * @since 0.5.0
 */
public abstract class LocalDuplicatedRemovedScheduler implements MonitorableScheduler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private Set<String> urls = Sets.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    @Override
    public void push(Request request, Task task) {
        logger.trace("get a candidate url {}", request.getUrl());
        if (isDuplicate(request) || shouldReserved(request)) {
            logger.debug("push to queue {}", request.getUrl());
            pushWhenNoDuplicate(request, task);
        }
    }

    protected boolean isDuplicate(Request request) {
        return urls.add(request.getUrl());
    }

    protected boolean shouldReserved(Request request) {
        return request.getExtra(Request.CYCLE_TRIED_TIMES) != null;
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return urls.size();
    }

    protected abstract void pushWhenNoDuplicate(Request request, Task task);
}
