Smart City / Smart Campus Scheduling Report
1. Introduction

In this project, I worked on analyzing city and campus tasks using graphs. Each task is a node, and dependencies between tasks are edges with weights. My main goal was to:

Find strongly connected components (SCCs) to see which tasks are tightly connected.

Build a condensation DAG from the SCCs.

Get a topological order of the tasks.

Find shortest and longest paths in the DAG.

Identify the critical path to know the most important task sequence.


2. Dataset

I used JSON files to represent tasks and dependencies. For example, small-1.json has 6 tasks and 6 edges. The nodes represent tasks like A, B, C… and edges show which task depends on another.

3. How I Solved the Problem
   3.1 SCCs

I used Tarjan's algorithm to find strongly connected components. This helps to see which tasks are in a cycle or closely linked, so they should be considered together.

3.2 Condensation DAG

After finding SCCs, I turned each component into a single node. The new DAG preserves dependencies between the components.

3.3 Topological Sort

I applied Kahn’s algorithm to get a topological order of components. It ensures tasks are done in the correct order.

3.4 Shortest and Longest Paths

I calculated:

Shortest paths from the first component to all others (minimum total weight).

Longest paths (critical path) to find the sequence that takes the most time or is most important.

3.5 Metrics

I also recorded metrics like:

DFS visits and edges

Number of relaxations

Kahn algorithm pushes and pops

Execution time in nanoseconds

![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_3.png](img_3.png)

5. Graphs

I drew simple diagrams to visualize:

Original task graph

Condensation DAG

Critical path

This helped me understand the task dependencies better.

6. My Thoughts

SCCs are useful because they show which tasks can be considered as a group.
The critical path shows the tasks that are the most important and take the most time.
Shortest paths show how quickly tasks can be completed.
Overall, I learned how graph algorithms can help plan tasks efficiently.

7. Conclusion

I successfully:

Found SCCs and condensation DAG

Got topological order

Calculated shortest and longest paths

Identified the critical path

For the future, I could try this with bigger datasets or real-time updates.

Summary

In this project, I analyzed tasks in a smart city/campus using graphs. I first identified strongly connected components (SCCs) to find tightly linked tasks. Then I built a condensation DAG, sorted the tasks topologically, and computed both shortest and longest paths.

The critical path showed the most important sequence of tasks, while shortest paths showed the fastest way to complete tasks. I also recorded metrics like DFS visits, relaxations, and Kahn algorithm operations.

Overall, the project helped me understand task dependencies, identify bottlenecks, and learn how graph algorithms can be applied to real-world scheduling problems.
