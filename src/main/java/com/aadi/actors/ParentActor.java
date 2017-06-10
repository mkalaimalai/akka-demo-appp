package com.aadi.actors;


import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.commons.lang3.RandomStringUtils;


/**
 * Created by kalaimam on 10/19/16.
 */

public class ParentActor extends UntypedActor {


    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), "ParentActor");


    private String instanceId;
    private String prelog;
    private ActorRef childActor;

    @Override
    public void preStart() throws Exception {
        instanceId = RandomStringUtils.randomAlphanumeric(6);
        prelog = "[" + instanceId + "] ";
        logger.info(prelog + "Starting Parent Actor...");
        super.preStart();
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof String) {
            logger.debug(prelog + "Message received by Parent actor --> " + msg);
            String instanceId = RandomStringUtils.randomAlphanumeric(6);
            String childActorInstanceId = "child-actor-"+instanceId;

            // Creating the Child Actor
            childActor = getContext().system().actorOf(Props.create(ChildActor.class), childActorInstanceId);

            // Sending message to Child Actor
            childActor.tell(msg, getSender());

        } else if (msg instanceof DeadLetter) {
            logger.error(">>>>> DeadLetter encountered " + msg);
        }
    }

    @Override
    public void postStop() throws Exception {
        logger.info(prelog + "Parent Actor Shutting down");
        super.postStop();
    }
}




