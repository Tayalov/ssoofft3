package graph.app;

import graph.common.*;
import graph.scc.*;
import graph.scc.CondensationGraph.DAG;
import graph.topo.TopologicalSortKahn;
import graph.dagsp.*;

import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String path; // объявляем переменную один раз

        if (args.length == 0) {
            path = "data/small-1.json"; // присваиваем значение без повторного объявления
            System.out.println("No path provided. Using default: " + path);
        } else {
            path = args[0]; // присваиваем значение аргумента
        }

        Graph g = GraphLoader.loadFromJsonFile(path);
        Metrics metrics = new Metrics();

        TarjanSCC tarjan = new TarjanSCC(g, metrics);
        List<List<String>> sccs = tarjan.run();

        System.out.println("SCC count: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++)
            System.out.println("Comp " + i + ": " + sccs.get(i) + " size=" + sccs.get(i).size());

        DAG dag = CondensationGraph.build(sccs, g);

        Map<String, Integer> compOf = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++)
            for (String v : sccs.get(i))
                compOf.put(v, i);

        Map<Integer, List<Edge>> compEdges = new LinkedHashMap<>();
        for (int i = 0; i < sccs.size(); i++)
            compEdges.put(i, new ArrayList<>());

        for (String u : g.nodes()) {
            for (Edge e : g.neighbors(u)) {
                int cu = compOf.get(u);
                int cv = compOf.get(e.getTo());
                if (cu != cv) compEdges.get(cu).add(new Edge(String.valueOf(cv), e.getWeight()));
            }
        }

        List<Integer> topo = TopologicalSortKahn.kahn(dag.adj, metrics);
        System.out.println("Topo order (components): " + topo);

        int source = topo.get(0);

        PathResult sp = DAGShortestPath.shortest(dag, topo, source, compEdges, metrics);
        PathResult lp = DAGLongestPath.longest(dag, topo, source, compEdges);

        System.out.println("\nMetrics:");
        System.out.println("Elapsed time (ns) = " + metrics.getElapsedNanos());
        System.out.println("DFS visits = " + metrics.dfsVisits);
        System.out.println("DFS edges = " + metrics.dfsEdges);
        System.out.println("Relaxations = " + metrics.relaxations);
        System.out.println("Kahn pushes = " + metrics.kahnPushes);
        System.out.println("Kahn pops = " + metrics.kahnPops);

        long max = Long.MIN_VALUE;
        int target = -1;
        for (Map.Entry<Integer, Long> e : lp.dist.entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); target = e.getKey(); }
        }

        System.out.println("\nCritical path length = " + max + " (target component = " + target + ")");
        if (target != -1) {
            List<Integer> pathComp = lp.reconstructPath(target, source);
            System.out.println("Critical path components: " + pathComp);
            System.out.println("Critical path expanded (members):");
            for (int c : pathComp)
                System.out.println(" comp " + c + " -> " + dag.members.get(c));
        }

        System.out.println("\nPer-component summary:");
        for (int i = 0; i < sccs.size(); i++) {
            long dmin = sp.dist.getOrDefault(i, Long.MAX_VALUE / 4);
            long dmax = lp.dist.getOrDefault(i, Long.MIN_VALUE / 4);
            System.out.println("Comp " + i +
                    " members=" + dag.members.get(i) +
                    " shortest=" + (dmin >= Long.MAX_VALUE/8 ? "INF" : dmin) +
                    " longest=" + (dmax <= Long.MIN_VALUE/8 ? "N/A" : dmax));
        }
    }
}

