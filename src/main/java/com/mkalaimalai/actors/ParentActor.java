package com.mkalaimalai.actors;


import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import akka.japi.pf.DeciderBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import scala.concurrent.duration.Duration;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.stop;
import static akka.actor.SupervisorStrategy.escalate;


import java.util.concurrent.TimeUnit;


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
    public SupervisorStrategy supervisorStrategy() {
        return new AllForOneStrategy(10, Duration.create(1, TimeUnit.SECONDS), DeciderBuilder.
                match(ChildActor.ResumeException.class, e -> resume()).
                match(ChildActor.RestartException.class, e -> restart()).
                match(ChildActor.StopException.class, e -> stop()).
                matchAny(o -> escalate())
                .build());
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
                    childActor = getContext().actorOf(Props.create(ChildActor.class),childActorInstanceId);

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

    @Override
    public void postRestart(Throwable reason) throws Exception{
        logger.info(prelog + "Parent Actor post Restarting {}",reason);
        super.postRestart(reason);
    }





}




