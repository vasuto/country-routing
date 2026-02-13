package com.cr.countryrouting.routing;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutingController {

  private final RoutingService routingService;

  public RoutingController(RoutingService routingService) {
      this.routingService = routingService;
  }

  @GetMapping("/routing/{origin}/{destination}")
    public ResponseEntity<?> getRoute(@PathVariable String origin,
                                 @PathVariable String destination) {
        List<String> route = routingService.findShortestRoute(origin, destination);

        if (route.isEmpty()) {
            return ResponseEntity.badRequest().body("No land route found");
        }

        return ResponseEntity.ok(new RouteResponse(route));
    }

}
