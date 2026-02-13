package com.cr.countryrouting.routing;

import java.util.List;

public class RouteResponse {

  private List<String> route;

        public RouteResponse(List<String> route) {
            this.route = route;
        }

        public List<String> getRoute() {
            return route;
        }

        public void setRoute(List<String> route) {
            this.route = route;
        }

}
