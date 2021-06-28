
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
  
// A directed Bfs using
// adjacency list representation
public class Bfs {
  
    // No. of vertices in Bfs
    private int v;
  
    // adjacency list
    private ArrayList<Integer>[] adjList;
  
    // Constructor
    public Bfs(int vertices)
    {
  
        // initialise vertex count
        this.v = vertices;
  
        // initialise adjacency list
        initAdjList();
    }
  
    // utility method to initialise
    // adjacency list
    @SuppressWarnings("unchecked")
    private void initAdjList()
    {
        adjList = new ArrayList[v];
  
        for (int i = 0; i < v; i++) {
            adjList[i] = new ArrayList<Integer>();
        }
    }
  
    // add edge from u to v
    public void add(int u, int v)
    {
        // Add v to u's list.
        adjList[u].add(v);
    }
  
    // Prints all paths from
    // 's' to 'd'
    public void printAllPaths(int s, int d)
    {
        boolean[] isVisited = new boolean[v];
        ArrayList<Integer> pathList = new ArrayList<Integer>();
  
        // add source to path[]
        pathList.add(s);
  
        // Call recursive utility
        printAllPathsUtil(s, d, isVisited, pathList);
    }
  
    // A recursive function to print
    // all paths from 'u' to 'd'.
    // isVisited[] keeps track of
    // vertices in current path.
    // localPathList<> stores actual
    // vertices in the current path
    private void printAllPathsUtil(Integer u, Integer d,
                                   boolean[] isVisited,
                                   List<Integer> localPathList)
    {
  
        if (u.equals(d)) {
            System.out.println(localPathList);
            // if match found then no need to traverse more till depth
            return;
        }
  
        // Mark the current node
        isVisited[u] = true;
  
        // Recur for all the vertices
        // adjacent to current vertex
        for (Integer i : adjList[u]) {
            if (!isVisited[i]) {
                // store current node
                // in path[]
                localPathList.add(i);
                printAllPathsUtil(i, d, isVisited, localPathList);
  
                // remove current node
                // in path[]
                localPathList.remove(i);
            }
        }
        
  
        // Mark the current node
        isVisited[u] = false;
    }
    private void AllPathsUtil(Integer u, Integer d,
            boolean[] isVisited,
            List<Integer> localPathList,JTextArea jt)
{

if (u.equals(d)) {
for(int i=0;i<localPathList.size();i++) {
	jt.append(String.valueOf(localPathList.get(i))+"  ");
}
jt.append("\n");
// if match found then no need to traverse more till depth
return;
}

// Mark the current node
isVisited[u] = true;

// Recur for all the vertices
// adjacent to current vertex
for (Integer i : adjList[u]) {
if (!isVisited[i]) {
// store current node
// in path[]
localPathList.add(i);
AllPathsUtil(i, d, isVisited, localPathList,jt);

// remove current node
// in path[]
localPathList.remove(i);
}}
isVisited[u] = false;
}
    public void AllPaths(int s, int d,JTextArea jt)
    {
        boolean[] isVisited = new boolean[v];
        ArrayList<Integer> pathList = new ArrayList<Integer>();
  
        // add source to path[]
        pathList.add(s);
  
        // Call recursive utility
        AllPathsUtil(s, d, isVisited, pathList,jt);
    }
    public void test(JTextArea jt) {
    	jt.append("1 ");
    }

  
    // Driver program
    public static void main(String[] args)
    {
        // Create a sample Bfs
        Bfs g = new Bfs(4);
        int matrix[][]= {{0,1,1,1},{1,0,0,1},{0,0,0,1},{1,1,1,0}};
        for(int i=0;i<4;i++)
        	for(int j=0;j<4;j++) 
        		if(matrix[i][j]>0) g.add(i, j);
    //    g.add(0, 1);
    //    g.add(0, 2);
    //    g.add(0, 3);
    //    g.add(2, 0);
    //    g.add(2, 1);
    //    g.add(1, 3);
  
        // arbitrary source
        int s = 1;
  
        // arbitrary destination
        int d = 3;
  
        System.out.println(
            "Following are all different paths from "
            + s + " to " + d);
        g.printAllPaths(s, d);
    }
}