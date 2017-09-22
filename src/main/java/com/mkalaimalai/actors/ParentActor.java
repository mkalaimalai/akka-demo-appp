package com.mkalaimalai.actors;


import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.commons.lang3.RandomStringUtils;


/**
 * Created by kalaimam on 10/19/16.
 */

public class ParentActor extends AbstractActor {


    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);


    private String instanceId;
    private String prelog;
    private ActorRef childActor;

    public static Props props() {
        return Props.create(ParentActor.class);
    }

    @Override
    public void preStart() throws Exception {
        instanceId = RandomStringUtils.randomAlphanumeric(6);
        prelog = "[" + instanceId + "] ";
        logger.info(prelog + "Starting Parent Actor...");
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, msg -> {
                    logger.debug(prelog + "Message received by Parent actor --> " + msg);
                    String instanceId = RandomStringUtils.randomAlphanumeric(6);
                    String childActorInstanceId = "child-actor-"+instanceId;

                    // Creating the Child Actor
                    childActor = getContext().system().actorOf(Props.create(ChildActor.class),childActorInstanceId);

                    // Sending message to Child Actor
                    childActor.tell(msg, getSender());
                })
                .build();
    }


    @Override
    public void postStop() throws Exception {
        logger.info(prelog + "Parent Actor Shutting down");
        super.postStop();
    }


}




