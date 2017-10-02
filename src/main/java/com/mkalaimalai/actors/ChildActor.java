package com.mkalaimalai.actors;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Optional;

/**
 * Created by kalaimam on 10/19/16.
 */

public class ChildActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private String instanceId;

    private String prelog;

    @Override
    public void preStart() throws Exception {
        instanceId = RandomStringUtils.randomAlphanumeric(6);
        prelog = "[" + instanceId + "] ";
        logger.info(prelog + "Starting Child Actor...");
        super.preStart();
    }

    public void preRestart(Throwable reason, Optional<Object> message)  throws Exception{
        logger.info(prelog + "Child Actor Pre Restarting reason: {} message: {}",reason,message);
        super.preRestart(reason,message);
    }



    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, msg -> {
                    logger.debug(prelog + "Message received by Child Actor --> " + msg);
                    if (msg.equals("resume")) {
                        throw new ResumeException("ResumeException Thrown");
                    } else if (msg.equals("stop")) {
                        throw new StopException("StopException Thrown");
                    } else if (msg.equals("restart")) {
                        throw new RestartException("RestartException Thrown");
                    }
                })
                .build();
    }


    @Override
    public void postStop() throws Exception {
        logger.info(prelog + "Child Actor Shutting down");
        super.postStop();
    }


    @Override
    public void postRestart(Throwable reason) throws Exception{
        logger.info(prelog + "Child Actor Post Restarting {}",reason);
        super.postRestart(reason);
    }

    class ResumeException extends Exception {
        public ResumeException( String message) {
            super(message);
        }
    }

    class StopException extends Exception {
        public StopException( String message) {
            super(message);
        }
    }
        class RestartException extends Exception {
            public RestartException( String message) {
                super(message);
            }
        }

    }

