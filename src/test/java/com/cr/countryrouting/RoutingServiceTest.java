package com.cr.countryrouting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cr.countryrouting.routing.RoutingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.JsonNodeFactory;

public class RoutingServiceTest {

    @Mock
    private CountryService countryService;

    private RoutingService routingService;

    private Map<String, JsonNode> countries;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        routingService = new RoutingService(countryService);

        countries = new HashMap<>();

        JsonNode czechRepublic = mock(JsonNode.class);
        JsonNode germany = mock(JsonNode.class);
        JsonNode austria = mock(JsonNode.class);
        JsonNode italy = mock(JsonNode.class);
        JsonNode poland = mock(JsonNode.class);
        JsonNode slovakia = mock(JsonNode.class);

        ArrayNode czechRepBorders = JsonNodeFactory.instance.arrayNode();
        czechRepBorders.add("DEU").add("AUT").add("POL").add("SVK");
        
        ArrayNode germanyBorders = JsonNodeFactory.instance.arrayNode();
        germanyBorders.add("CZE").add("AUT").add("POL").add("FRA");
        
        ArrayNode austriaBorders = JsonNodeFactory.instance.arrayNode();
        austriaBorders.add("CZE").add("DEU").add("POL").add("ITA");

        ArrayNode italyBorders = JsonNodeFactory.instance.arrayNode();
        italyBorders.add("AUT");

        ArrayNode polandBorders = JsonNodeFactory.instance.arrayNode();
        polandBorders.add("CZE").add("DEU").add("SVK").add("UKR");

        ArrayNode slovakiaBorders = JsonNodeFactory.instance.arrayNode();
        slovakiaBorders.add("CZE").add("POL").add("AUT").add("HUN");

        when(czechRepublic.get("borders")).thenReturn(czechRepBorders);
        when(germany.get("borders")).thenReturn(germanyBorders);
        when(austria.get("borders")).thenReturn(austriaBorders);
        when(italy.get("borders")).thenReturn(italyBorders);
        when(poland.get("borders")).thenReturn(polandBorders);
        when(slovakia.get("borders")).thenReturn(slovakiaBorders);

        countries.put("CZE", czechRepublic);
        countries.put("DEU", germany);
        countries.put("AUT", austria);
        countries.put("ITA", italy);
        countries.put("POL", poland);
        countries.put("SVK", slovakia);

        when(countryService.getCountries()).thenReturn(countries);
    }

    @Test
    void testFindShortestRoute_SuccessfulRoute() {
        List<String> result = routingService.findShortestRoute("CZE", "ITA");

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("CZE", result.get(0));
        assertEquals("AUT", result.get(1));
        assertEquals("ITA", result.get(2));
    }

    @Test
    void testFindShortestRoute_NoRoute() {
        List<String> result = routingService.findShortestRoute("CZE", "USA");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindShortestRoute_DirectNeighbor() {
        List<String> result = routingService.findShortestRoute("CZE", "SVK");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("CZE", result.get(0));
        assertEquals("SVK", result.get(1));
    }

    @Test
    void testFindShortestRoute_SameCountry() {
        List<String> result = routingService.findShortestRoute("CZE", "CZE");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CZE", result.get(0));
    }

    @Test
    void testFindShortestRoute_InvalidCountry() {
        List<String> result = routingService.findShortestRoute("XXX", "DEU");

        assertTrue(result.isEmpty());
    }

}
