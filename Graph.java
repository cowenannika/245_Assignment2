import java.util.*;

public class Graph {

    protected Hashtable<String, Integer> cities;
    protected Hashtable<String, String> attractions;
    protected List<String> cityMap;
    protected Stack<Integer> path;
    protected int count = 0;
    protected int edgeMatrix[][];
    private int dist[];
    private int parents[];
    private int numVertices;


    public Graph() {
        path = new Stack<Integer>();
        cities = new Hashtable<String, Integer>();
        attractions = new Hashtable<String, String>();
        cityMap = new ArrayList<String>();
        numVertices = 0;
    }

    public void add_City(String city) {
        try {
            if (!cities.containsKey(city)) {
                cityMap.add(city);
                cities.put(city, count++);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAttractions(Hashtable<String, String> attractions) {
        this.attractions = attractions;
    }
    public void setCities(Hashtable<String, Integer> cities) {
        this.cities = cities;
    }
    public void setEdgeMatrix(int[][] edges) {
        edgeMatrix = edges;
        numVertices = edges.length;
    }

    public Hashtable<String, Integer> getCities() {
        return cities;
    }

    //run Dijkstras
    void Dijkstra(int graph[][], int src)
    {
        dist = new int[numVertices];
        Boolean visited[] = new Boolean[numVertices];
        parents = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            dist[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        dist[src] = 0;
        parents[src] = -1;

        for (int count = 0; count < numVertices - 1; count++) {
            int u = minimum_Distance(dist, visited);
            if (u == -1) { break;}
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    parents[v] = u;
                }
            }
        }
    }

    //find city that has not been visited that is the most close
    private int minimum_Distance(int dist[], Boolean visited[])
    {
        int min = Integer.MAX_VALUE, min_index = -1;
        for (int v = 0; v < numVertices; v++)
            if (visited[v] == false && dist[v] < min) {
                min = dist[v];
                min_index = v;
            }
        return min_index;
    }

    private void addPath(int currentVertex)
    {
        if (currentVertex != -1) {
            path.push(currentVertex);
        }
        if (parents[currentVertex] != -1) {
            addPath(parents[currentVertex]);
        }
    }

    //gives back closest city to user location
    protected int find_nearest(List<String> attraction, String destination) {
        if (attraction.size() != 0) {
            int shortest_city = cities.get(attractions.get(attraction.get(0)));
            if (attraction.size() >= 2) {
                for (int i = 1; i < attraction.size(); i++) {
                    if (dist[shortest_city] > dist[cities.get(attractions.get(attraction.get(i)))]) {
                        shortest_city = cities.get(attractions.get(attraction.get(i)));
                    }
                }
            }
            addPath(shortest_city);
            Dijkstra(edgeMatrix, shortest_city);
            return shortest_city;
        }
        else {
            addPath(cities.get(destination));
            return cities.get(destination);
        }
    }
}