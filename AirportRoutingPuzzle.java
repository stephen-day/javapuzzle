package sdtest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*
 * Puzzle contains a list of airports and list of one-way routes.
 *
 * Goal: add the minimum number of new routes from startingCity-XXX such that all cities can be reached from startingCity.
 *
 * This is from the following video by former Google recruiter which recommends this as a complex algorithm/puzzle:
 *    https://www.youtube.com/watch?v=qz9tKlF431k
 *
 * I used the details to solve the problem myself before watching the remaining part of the video and seeing how this could be done
 * with graphing algorithms.
 */
public class AirportRoutingPuzzle
{
    public static void main(String[] args)
    {
        String[] airports = { "BGI", "CDG", "DEL", "DOH", "DSM", "EWR", "EYW", "HND", "ICN",
                              "JFK", "LGA", "LHR", "ORD", "SAN", "SFO", "SIN", "TLV", "BUD" };

        String[][] routePairs = {
                { "DSM", "ORD" },
                { "ORD", "BGI" },
                { "BGI", "LGA" },
                { "SIN", "CDG" },
                { "CDG", "SIN" },
                { "CDG", "BUD" },
                { "DEL", "DOH" },
                { "DEL", "CDG" },
                { "TLV", "DEL" },
                { "EWR", "HND" },
                { "HND", "ICN" },
                { "HND", "JFK" },
                { "ICN", "JFK" },
                { "JFK", "LGA" },
                { "EYW", "LHR" },
                { "LHR", "SFO" },
                { "SFO", "SAN" },
                { "SFO", "DSM" },
                { "SAN", "EYW" }
        };

        String startingCity = "LGA";

        // Perform calculation and print result.
        Set<String> reqdRoutes = new AirportRoutingPuzzle().calcReqdRoutes(startingCity, airports, routePairs);

        System.out.println("The following " + reqdRoutes.size() +
                           " new route" + (reqdRoutes.size() == 1 ? "" : "s") +
                           " are required");

        for (String dest : reqdRoutes)
            System.out.println("  " + startingCity + "-" + dest);
    }

    Map<Integer, Set<Integer>> mOrigToAllDestsMap;
    Set<Integer> mUnreachedCityIds;
    Set<Integer> mReqdCityIds;

    public AirportRoutingPuzzle()
    {
        mUnreachedCityIds = new HashSet<>();
        mReqdCityIds = new HashSet<>();
    }

    public Set<String> calcReqdRoutes(String inStartingCity,
                                      String[] inAirports,
                                      String[][] inRoutePairs)
    {
        // Translate city strings to ids.
        Map<String, Integer> cityToIdMap = createCityToIdMap(inAirports);
        Integer startingCityId = cityToIdMap.get(inStartingCity);

        Set<Integer> allCityIdsExclStart = new HashSet<>(cityToIdMap.values());
        allCityIdsExclStart.remove(startingCityId);

        // Translate mRoutePairs into two maps (orig->dest's and dest->orig's).
        // - Ignore routes with the startingCity as a destination (we don't want to go from startingCity to startingCity).
        Map<Integer, Set<Integer>> origToDestsMap = createOrigToDestsMap(startingCityId, cityToIdMap, inRoutePairs);
        Map<Integer, Set<Integer>> destToOriginsMap = createDestToOriginsMap(origToDestsMap);

        // Calculate all the destination cities that each city can currently get to (via any number of connections).
        mOrigToAllDestsMap = createOrigToAllDestsMap(origToDestsMap);

        mUnreachedCityIds.addAll(allCityIdsExclStart);

        // Any cities that are not a destination in our route list are cities we cannot get to via another city. A route to these
        // cities must be part of our final result. This is an efficiency improvement.
        addNonDestCities(allCityIdsExclStart, destToOriginsMap);

        // For each un-routed city, find the best city to add.
        // - For each city, find the city BBB that can get to AAA, but where AAA cannot get to BBB.
        addRemainingCities(destToOriginsMap);

        // Translate ids back to strings and return the cities.
        Set<String> reqdCities = new HashSet<>();
        for (Integer id : mReqdCityIds)
            reqdCities.add(inAirports[id]);

        return(reqdCities);
    }

    private static Map<String, Integer> createCityToIdMap(String[] inAirports)
    {
        Map<String, Integer> cityToIdMap = new HashMap<>();

        for (int idx = 0; idx < inAirports.length; idx++)
            cityToIdMap.put(inAirports[idx], idx);

        return(cityToIdMap);
    }

    // Create a map of each possible origin city to the immediate (zero connection) destination cities for that origin.
    // Note: leaf nodes (cities with no departing flights) will not be represented as keys in this map.
    private static Map<Integer, Set<Integer>> createOrigToDestsMap(Integer inIgnoreDestCityId,
                                                                   Map<String, Integer> inCityToIdMap,
                                                                   String[][] inRoutePairs)
    {
        Map<Integer, Set<Integer>> origToDestsMap = new HashMap<>();

        for (int idx = 0; idx < inRoutePairs.length; idx++)
        {
            Integer fromCityId = inCityToIdMap.get(inRoutePairs[idx][0]);
            Integer toCityId = inCityToIdMap.get(inRoutePairs[idx][1]);

            if (toCityId.equals(inIgnoreDestCityId))
                continue;

            Set<Integer> destsSet = origToDestsMap.get(fromCityId);
            if (destsSet == null)
            {
                destsSet = new HashSet<>();
                origToDestsMap.put(fromCityId, destsSet);
            }

            destsSet.add(toCityId);
        }

        return(origToDestsMap);
    }

    // Create a map of each possible destination city to the immediate (zero connection) origin cities for that destination.
    private static Map<Integer, Set<Integer>> createDestToOriginsMap(Map<Integer, Set<Integer>> inOrigToDestsMap)
    {
        Map<Integer, Set<Integer>> destToOriginsMap = new HashMap<>();

        for (Entry<Integer, Set<Integer>> entry : inOrigToDestsMap.entrySet())
        {
            for (Integer destId : entry.getValue())
            {
                Set<Integer> destsSet = destToOriginsMap.get(destId);
                if (destsSet == null)
                {
                    destsSet = new HashSet<>();
                    destToOriginsMap.put(destId, destsSet);
                }

                destsSet.add(entry.getKey());
            }
        }

        return(destToOriginsMap);
    }

    // Create a map of each possible origin city to all destination cities for that origin (any number of connections).
    // Note: leaf nodes (cities with no departing flights) will not be represented as keys in this map.
    private static Map<Integer, Set<Integer>> createOrigToAllDestsMap(Map<Integer, Set<Integer>> inOrigToDestsMap)
    {
        Map<Integer, Set<Integer>> origToAllDestsMap = new HashMap<>();

        for (Integer originId : inOrigToDestsMap.keySet())
        {
            Set<Integer> destsForCity = new HashSet<>();

            getAllCitiesForOrigin(originId, inOrigToDestsMap, origToAllDestsMap, destsForCity);

            origToAllDestsMap.put(originId, destsForCity);
        }

        return(origToAllDestsMap);
    }

    // Obtain all destinations for the passed city by recursing over the each of the city's destinations.
    // All destinations are added to inMdDestsForCity (and this is used to prevent processing the same city multiple times).
    private static void getAllCitiesForOrigin(Integer inOriginCity,
                                              Map<Integer, Set<Integer>> inOrigToDestsMap,
                                              Map<Integer, Set<Integer>> inOrigToAllDestsMap,
                                              Set<Integer> inMdDestsForCity)
    {
        Set<Integer> directDestIds = inOrigToDestsMap.get(inOriginCity);

        // If the city is a leaf node (no outbound flights), nothing to do (already added to inMdDestsForCity by caller).
        if (directDestIds == null)
            return;

        for (Integer destId : directDestIds)
        {
            if (inMdDestsForCity.contains(destId))      // City already processed.
                continue;

            inMdDestsForCity.add(destId);

            // For efficiency, if we've already calculated the destinations for this city code (i.e. must be a subset of the
            // current city), add them all to this city.
            //
            // Otherwise, recursively add all of its destinations.
            if (inOrigToAllDestsMap.containsKey(destId))
                inMdDestsForCity.addAll(inOrigToAllDestsMap.get(destId));
            else
                getAllCitiesForOrigin(destId, inOrigToDestsMap, inOrigToAllDestsMap, inMdDestsForCity);
        }
    }

    // Any cities that are not a destination in our route list are cities we cannot get to via another city.
    private void addNonDestCities(Set<Integer> inAllCityIdsExclStart,
                                  Map<Integer, Set<Integer>> inDestToOriginsMap)
    {
        for (Integer id : inAllCityIdsExclStart)
        {
            if (!inDestToOriginsMap.containsKey(id))
                addRequiredCity(id);
        }
    }

    // Add the remaining unreached cities - finding the best city to add.
    // The best city to add is a city that can get to you, but you cannot get to it.
    // For example, if BBB can get to AAA, but AAA cannot get to BBB, then BBB is the better city (more inclusive).
    private void addRemainingCities(Map<Integer, Set<Integer>> inDestToOriginsMap)
    {
        while (!mUnreachedCityIds.isEmpty())
        {
            Integer cityId = mUnreachedCityIds.iterator().next();

            Integer betterOriginId = findBetterOriginCity(cityId, inDestToOriginsMap);

            addRequiredCity((betterOriginId == null) ? cityId : betterOriginId);
        }
    }

    // Find a better origin city which travels to the passed destination city.
    // The best city is a city that can get to the destination, but the destination cannot get to it.
    // Returns null if there are no such better origin cities.
    private Integer findBetterOriginCity(Integer inDestId,
                                         Map<Integer, Set<Integer>> inDestToOriginsMap)
    {
        Set<Integer> origins = inDestToOriginsMap.get(inDestId);
        if (origins == null)
            return(null);

        Set<Integer> destIdsForReqdDest = mOrigToAllDestsMap.get(inDestId);

        for (Integer originId : origins)
        {
            // The origin is not better if the destination can already get to it.
            if (destIdsForReqdDest != null && destIdsForReqdDest.contains(originId))
                continue;

            // This is a better origin city than the passed destination city.
            // Recursively check if this origin has a better origin city.
            Integer betterOriginId = findBetterOriginCity(originId, inDestToOriginsMap);
            if (betterOriginId != null)
                return(betterOriginId);

            return(originId);
        }

        return(null);
    }

    private void addRequiredCity(Integer inReqdCityId)
    {
        mReqdCityIds.add(inReqdCityId);

        markCityAndDestsReached(inReqdCityId);
    }

    private void markCityAndDestsReached(Integer inCityId)
    {
        markSingleCityReached(inCityId);

        Set<Integer> allDests = mOrigToAllDestsMap.get(inCityId);
        if (allDests == null) return;

        for (Integer destId : allDests)
            markSingleCityReached(destId);
    }

    private void markSingleCityReached(Integer inCityId)
    {
        mUnreachedCityIds.remove(inCityId);
    }
}
