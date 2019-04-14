package fr.pierrezemb.beacon.api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.queryablestate.client.QueryableStateClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class MainVerticle extends AbstractVerticle {

    private QueryableStateClient flinkStateClient;

    private OkHttpClient httpClient = new OkHttpClient();

    private final MapStateDescriptor<String, Long> stateDescriptor = new MapStateDescriptor<String, Long>(
            "running-alerts",
            TypeInformation.of(new TypeHint<String>(){}),
            TypeInformation.of(new TypeHint<Long>(){})
    );

  private String jobManagerHost;
  private Integer jobManagerPort;

  public void start(Future<Void> fut) {

      JsonObject config = context.config();

      // port for API
      Integer port = config.getInteger("port", 8080);

      jobManagerHost = config.getString("job-manager-host", "127.0.0.1");
      jobManagerPort = config.getInteger("job-manager-port", 8081);

      // Getting configuration for flinkStateClient
      String tmHostname = config.getString("task-manager-host", "127.0.0.1");
      int proxyPort =  config.getInteger("proxy-port", 9069);

      try {
        flinkStateClient = new QueryableStateClient(tmHostname, proxyPort);
      } catch (UnknownHostException e) {
        fut.fail(e);
      }

      // Create a router object.
      Router router = Router.router(vertx);

      // Health routes
      router.get("/health").handler(this::getHealth);


      router.get("/api/v0/:namespace").handler(this::getAlertState);
      vertx
              .createHttpServer()
              .requestHandler(router::accept)
              .listen(port,
                      result -> {
                          if (result.succeeded()) {
                              fut.complete();
                          } else {
                              fut.fail(result.cause());
                          }
                      }
              );
      System.out.println("Starting Beacon-API on " + port);

  }

  private void getAlertState(RoutingContext routingContext) {

    String namespace = routingContext.request().getParam("namespace");
    if (namespace == null) {
      routingContext.response().setStatusCode(400).end();
      return;
    }

      JobID jobID;
      try {
          jobID = getcurrentJobID();
      } catch (IOException e) {
          e.printStackTrace();
          routingContext.response().setStatusCode(500).end(e.getMessage());
          return;
      }

      CompletableFuture<MapState<String, Long>> resultFuture =
              flinkStateClient.getKvState(
                      jobID,
                      "running-alerts",
                      namespace,
                      BasicTypeInfo.STRING_TYPE_INFO,
                      stateDescriptor
              );

      vertx.executeBlocking(future -> {

          MapState<String, Long> state = null;
          try {
              state = resultFuture.get(3, TimeUnit.SECONDS);
              HashMap<String, Long> res = new HashMap<>();
              for (Map.Entry<String, Long> entry : state.entries()) {
                  res.put(entry.getKey(), entry.getValue());
              }
              future.complete(res);
          } catch (Exception e) {
              future.fail(e);
          }


      }, res -> {
          if (res.succeeded()) {
              routingContext.response().end(Json.encode(res.result()));
          } else {
              routingContext.response().setStatusCode(500).end(res.cause().getMessage());
          }

      });
  }

  private JobID getcurrentJobID() throws IOException {

    String url = "http://" + this.jobManagerHost + ":" + this.jobManagerPort + "/jobs/overview";

    Request request = new Request.Builder()
            .url(url)
            .build();

    Response response = httpClient.newCall(request).execute();

    final FlinkMonitoringJobOverview jobs = Json.decodeValue(response.body().string(), FlinkMonitoringJobOverview.class);

    Stream<FlinkJobInformation> validJob = jobs.getJobs()
            .stream()
            .filter(j -> j.getName().equals("beacon"));

    String id = validJob.findFirst().get().getJid();

    return JobID.fromHexString(id);
  }

  private void getHealth(RoutingContext routingContext) {
        routingContext.response().putHeader("content-type", "text/plain");
        routingContext.response().end("Beeeeep beeeeep");
    }
}
