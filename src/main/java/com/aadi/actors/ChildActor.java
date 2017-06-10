package com.aadi.actors;


import akka.actor.DeadLetter;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kalaimam on 10/19/16.
 */

public class ChildActor extends UntypedActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), "ChildActor");

    private String instanceId;

   private String prelog;
    @Override
    public void preStart() throws Exception{
    	instanceId  = RandomStringUtils.randomAlphanumeric(6);
    	prelog = "[" + instanceId + "] ";
       logger.info(prelog + "Starting Child Actor...");
        super.preStart();
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof String) {

            logger.debug(prelog + "Message received by Child Actor --> " + msg);

        } else if (msg instanceof DeadLetter) {
            logger.error(">>>>> DeadLetter encountered " + msg);
        }
    }

    @Override
    public void postStop() throws Exception {
        logger.info(prelog + "Child Actor Shutting down");
        super.postStop();
    }
}

