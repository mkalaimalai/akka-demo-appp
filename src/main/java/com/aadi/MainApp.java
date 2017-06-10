package com.aadi;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import akka.routing.FromConfig;
import com.aadi.actors.ParentActor;
import com.aadi.actors.cluster.ClusterControllerActor;
import com.aadi.actors.*;
import com.aadi.actors.cluster.ClusterControllerActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MainApp {


    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public static void main(String... args) throws Exception {

        Config config =  ConfigFactory.load().
                withFallback(ConfigFactory.parseString("config.resource=application.conf"));

        ActorSystem system = ActorSystem.create("akka-demo", config);

        ActorRef parentActor = system.actorOf(Props.create(ParentActor.class), "parent-actor");

        ActorRef restAPIActor = null;

        try {
            if (config.getString("config.resource") != null && config.getString("config.resource").contains("cluster")) {
                ActorRef parentActorRouter = system.actorOf(Props.create(ParentActor.class).withRouter(new FromConfig()), "parent-actor-router");
                ActorRef clusterController = system.actorOf(Props.create(ClusterControllerActor.class), "cluster-controller-actor");
                restAPIActor = system.actorOf(Props.create(RestApi.class, parentActorRouter, config.getInt("http.port")), "restApi");
            }

        } catch (Exception e) {
            logger.error("Exception", e);
        }

        if(restAPIActor ==null){
            restAPIActor = system.actorOf(Props.create(RestApi.class, parentActor, config.getInt("http.port")), "restApi");
        }

        logger.debug("Akka Demo Actor System Started-->" + system.name());
    }
}




























//    final ActorMaterializer materializer = ActorMaterializer.create(system);

//        ActorRef childActor = system.actorOf(Props.create(ChildActor.class),"child-actor");


//        final Http http = Http.get(system);
//        //In order to access all directives we need an instance where the routes are define.
//        RestApiActor app = new RestApiActor();
//
//        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
//        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
//
//                ConnectHttp.toHost("localhost", 8095), materializer);
//
//        binding
//                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
//                .thenAccept(unbound -> system.terminate()); // and shutdown when done

//    ActorRef parentActor = system.actorOf(Props.create(ParentActor.class).withRouter(new FromConfig()), "parent-actor");
//    ActorRef childActor = system.actorOf(Props.create(ChildActor.class).withRouter(new FromConfig()), "child-actor");


//                for (int i = 1; i < 1000; i++) {
//            parentActor.tell("hello-world-" + i, null);
//        }

//       try {
//               if (config.getString("config.resource") != null && config.getString("config.resource").contains("cluster")) {
//               ActorRef clusterController = system.actorOf(Props.create(ClusterControllerActor.class), "cluster-controller-actor");
//        }
//        } catch (Exception e) {
//        logger.error("Exception", e);
//        }
