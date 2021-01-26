import java.util.*;
import java.io.*;

public class Road_Trip {
    private Graph g;
    //make road trip !
    public Road_Trip() {
        g = new Graph();
    }
    //setting up Dijkstras and setting up graph with the input of the user
    public void setDijkstra(Graph graph) {
        g = graph;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Road_Trip trip = new Road_Trip();
        File city = new File("RoadTrip_Roads.csv");
        File attraction = new File("RoadTrip_Attractions.csv");

        trip.loadCities(city);
        trip.loadAttractions(attraction);
        trip.loadRoads(city);

        ArrayList<String> attractions = new ArrayList<String>();

        System.out.println("Input the city and state abbreviation you want to start with like 'Chicago IL': ");
        String begin = scanner.nextLine();
        System.out.println("Input the city and state abbreviation you want to end with like 'Chicago IL': ");
        String finish = scanner.nextLine();
        System.out.println("Input the number of attractions you want to see as a positive number: ");
        int num = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < num; i++) {
            System.out.println("Input the name of the attraction " + (i+1) + "'s name: ");
            attractions.add(scanner.nextLine());
        }
        scanner.close();

        List<String> finalPath = trip.route(begin, finish, attractions);

        for (int i = 1; i < finalPath.size(); i++) {
            if(finalPath.get(i-1).equals(finalPath.get(i)) ) {
                finalPath.remove(i--);
            }
        }
        System.out.println("-----Route-----");
        for (int i = 0; i < finalPath.size(); i++) {
            System.out.println(finalPath.get(i));
        }

    }

    //looks for most efficient path
    public List<String> route(String city_begin, String city_finish, List<String> attractions) {
        List<String> route = new ArrayList<String>();

        String user_location = city_begin;
        g.Dijkstra(g.edgeMatrix, g.cities.get(user_location));

        while(attractions.size() > 0){
            g.path = new Stack<Integer>();
            int index = g.find_nearest(attractions, city_finish);
            user_location = g.cityMap.get(index);
            for (int i = 0; i < attractions.size(); i++) {
                if (g.attractions.get(attractions.get(i)).equals(user_location)){
                    attractions.remove(i);
                }
            }
            while (!g.path.empty()) {
                route.add(g.cityMap.get(g.path.pop()));
            }
        }

        g.path = new Stack<Integer>();
        g.find_nearest(attractions, city_finish);
        while (!g.path.empty()) {
            route.add(g.cityMap.get(g.path.pop()));
        }
        return route;
    }

    //load cities from hashtable
    public void loadCities(File file) {
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\n");

            while (scanner.hasNextLine()) {
                String city1 = scanner.next();
                String city2 = scanner.next();
                scanner.next();
                scanner.next();
                g.add_City(city1);
                g.add_City(city2);
            }
            scanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    //load attractions
    public void loadAttractions(File file){
        try {
            Hashtable<String, String> attractions = new Hashtable<String, String>();
            Scanner scanner = new Scanner(file);

            scanner.useDelimiter(",|\\n");

            while (scanner.hasNextLine()) {
                String attraction = scanner.next();
                String city2 = scanner.next();

                attractions.put(attraction, city2);
            }

            g.setAttractions(attractions);
            scanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    //load edges
    public void loadRoads(File file) {
        int[][] edges = new int[g.cityMap.size()][g.cityMap.size()];
        Hashtable<String, Integer> cities = g.getCities();

        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter(",|\\n");

            while (scanner.hasNextLine()) {
                String city1 = scanner.next();
                String city2 = scanner.next();
                int miles = scanner.nextInt();

                scanner.next();

                int index1 = (int) cities.get(city1);
                int index2 = (int) cities.get(city2);

                edges[index1][index2] = miles;
                edges[index2][index1] = miles;
            }
            scanner.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        g.setEdgeMatrix(edges);
    }
}