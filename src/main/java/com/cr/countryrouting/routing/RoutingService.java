package com.cr.countryrouting.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cr.countryrouting.CountryService;

import tools.jackson.databind.JsonNode;

@Service
public class RoutingService {

  private final CountryService countryService;

  public RoutingService(CountryService countryService) {
      this.countryService = countryService;
  }

  public List<String> findShortestRoute(String origin, String destination) {
    try {
      Map<String, JsonNode> countries = countryService.getCountries();
      if (!countries.containsKey(origin) || !countries.containsKey(destination)) {
          throw new IllegalArgumentException("Origin or destination not found");
      }

      if (origin.equals(destination)) {
          return Collections.singletonList(origin);
      }

      Set<String> visited = new HashSet<>();
      Queue<List<String>> queue = new LinkedList<>();
      visited.add(origin);
      queue.add(Collections.singletonList(origin));

      while (!queue.isEmpty()) {
          List<String> path = queue.poll();
          String currentCountry = path.get(path.size() - 1);

          JsonNode currentData = countries.get(currentCountry);
          JsonNode borders = currentData.get("borders");

          if (borders != null) {
              for (JsonNode border : borders) {
                  String borderCountry = border.asString();
                  if (visited.contains(borderCountry)) {
                      continue;
                  }

                  List<String> newPath = new ArrayList<>(path);
                  newPath.add(borderCountry);

                  if (borderCountry.equals(destination)) {
                      return newPath;
                  }

                  visited.add(borderCountry);
                  queue.add(newPath);
              }
          }
      }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return Collections.emptyList();
  }

}
