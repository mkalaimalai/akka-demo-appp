package com.mkalaimalai.actors;


import akka.actor.AbstractActor;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.mkalaimalai.actors.cluster.ClusterControllerActor;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by kalaimam on 10/19/16.
 */

public class ChildActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

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
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, msg -> {
                    logger.debug(prelog + "Message received by Child Actor --> " + msg);
                })
                .build();
    }


    @Override
    public void postStop() throws Exception {
        logger.info(prelog + "Child Actor Shutting down");
        super.postStop();
    }
}

