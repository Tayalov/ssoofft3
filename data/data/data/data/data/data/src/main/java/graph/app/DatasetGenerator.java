package graph.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

/**
 * Generates 9 datasets into ./data
 */
public class DatasetGenerator {
    static class GraphSpec { public List<String> nodes; public List<Map<String,Object>> edges; }

    public static void main(String[] args) throws Exception {
        ObjectMapper om = new ObjectMapper();
        new File("data").mkdirs();

        // small-1: simple DAG
        GraphSpec g1 = new GraphSpec();
        g1.nodes = List.of("A","B","C","D","E","F");
        g1.edges = new ArrayList<>();
        g1.edges.add(Map.of("from","A","to","B","w",3));
        g1.edges.add(Map.of("from","A","to","C","w",2));
        g1.edges.add(Map.of("from","B","to","D","w",4));
        g1.edges.add(Map.of("from","C","to","D","w",1));
        g1.edges.add(Map.of("from","D","to","E","w",5));
        g1.edges.add(Map.of("from","E","to","F","w",2));
        om.writerWithDefaultPrettyPrinter().writeValue(new File("data/small-1.json"), g1);

        // small-2: single cycle + tail
        GraphSpec g2 = new GraphSpec();
        g2.nodes = List.of("A","B","C","D","E","F");
        g2.edges = new ArrayList<>();
        g2.edges.add(Map.of("from","A","to","B","w",1));
        g2.edges.add(Map.of("from","B","to","C","w",1));
        g2.edges.add(Map.of("from","C","to","A","w",1)); // cycle A-B-C
        g2.edges.add(Map.of("from","C","to","D","w",2));
        g2.edges.add(Map.of("from","D","to","E","w",3));
        om.writerWithDefaultPrettyPrinter().writeValue(new File("data/small-2.json"), g2);

        // small-3: dense 6 nodes
        GraphSpec g3 = new GraphSpec();
        g3.nodes = List.of("1","2","3","4","5","6");
        g3.edges = new ArrayList<>();
        for (String u : g3.nodes) for (String v : g3.nodes) if (!u.equals(v)) g3.edges.add(Map.of("from",u,"to",v,"w",1));
        om.writerWithDefaultPrettyPrinter().writeValue(new File("data/small-3.json"), g3);

        // medium & large random
        genRandom("data/medium-1.json", 12, 30, 123);
        genRandom("data/medium-2.json", 16, 48, 456);
        genRandom("data/medium-3.json", 20, 70, 789);

        genRandom("data/large-1.json", 24, 150, 101112);
        genRandom("data/large-2.json", 35, 250, 131415);
        genRandom("data/large-3.json", 48, 450, 161718);

        System.out.println("Wrote 9 dataset files to ./data");
    }

    static void genRandom(String path, int n, int maxEdges, long seed) throws Exception {
        Random rng = new Random(seed);
        GraphSpec g = new GraphSpec();
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) nodes.add("N" + i);
        g.nodes = nodes;
        g.edges = new ArrayList<>();
        int possible = n * (n - 1);
        int m = Math.min(maxEdges, Math.max(n, (int)(possible * (0.08 + 0.12 * rng.nextDouble()))));
        Set<String> used = new HashSet<>();
        while (g.edges.size() < m) {
            int u = rng.nextInt(n), v = rng.nextInt(n);
            if (u == v) continue;
            String key = u + "->" + v;
            if (used.contains(key)) continue;
            used.add(key);
            g.edges.add(Map.of("from", nodes.get(u), "to", nodes.get(v), "w", 1 + rng.nextInt(10)));
        }
        ObjectMapper om = new ObjectMapper();
        om.writerWithDefaultPrettyPrinter().writeValue(new File(path), g);
    }
}
