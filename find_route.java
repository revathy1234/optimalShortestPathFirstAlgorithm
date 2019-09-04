
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashSet;

public class find_route {

	
	public static ArrayList<Node> createGraph(String filename) {
		ArrayList<Node> map = new ArrayList<Node>();
		File f = new File(filename);
		try {
			InputStream in = new FileInputStream(f);
			BufferedReader buff_read = new BufferedReader(new InputStreamReader(in));
			String ln;
			
			while ((ln = buff_read.readLine()) != null) {
				if (ln.equals("END OF INPUT"))
					break;
				String[] val = ln.split(" ");
				String origin_city = val[0].toString();
				String destination_city = val[1].toString();
				int c = Integer.parseInt(val[2]);
				Node org = new Node(origin_city);
				Node dest = new Node(destination_city);
				int idx = -1;
				if (map.contains(org) == true) {
					idx = getIndex(org, map);
					org = map.get(idx);
				}
				if (map.contains(dest) == true) {
					idx = getIndex(dest, map);
					dest = map.get(idx);
					dest.insertEdge(org, c);
					idx = getIndex(dest, map);
					map.set(idx, dest);
				} else {
					dest.insertEdge(org, c);
					map.add(dest);
				}
				org.insertEdge(dest, c);
				if (map.contains(org) == true) {
					idx = getIndex(org, map);
					map.set(idx, org);
				} else {
					map.add(org);
				}
			}
			buff_read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Reached End of Line");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static HashMap<String, Integer> addHvalues(String filename) {
		HashMap<String, Integer> hvalues = new HashMap<String, Integer>();
		File f = new File(filename);
		try {
			InputStream in = new FileInputStream(f);
			BufferedReader buff_read = new BufferedReader(new InputStreamReader(in));
			String ln;
			while ((ln = buff_read.readLine()) != null) {
				if (ln.equals("END OF INPUT"))
					break;
				String[] val = ln.split(" ");
				String org_city = val[0].toString();
				int hvalue = Integer.parseInt(val[1]);
				
				Node origin = new Node(org_city);
				origin.hval = hvalue;
				hvalues.put(origin.city, origin.hval);
			}
			in.close();
			buff_read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Reached End of Line");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hvalues;
	}
	
	public static void UniformCostSearch(Node origin, Node destination, String type, HashMap<String, Integer> hvalues) {
		origin.pathCost = 0;
		PriorityQueue<Node> frontier_queue = new PriorityQueue<Node>(25, origin);
		frontier_queue.add(origin);
		Node cur = null;
		HashSet<Node> explored = new HashSet<Node>();
		do {
			cur = frontier_queue.poll();
			explored.add(cur);
			for (Edge e : cur.toCity) {
				Node child = e.destination;
				int c = e.cost;
				int hval = 0;
				if(hvalues != null)
					hval = hvalues.get(child.city);
				if (frontier_queue.contains(child) == false) {
					child.pathCost = cur.pathCost + c;
					if(type.equals("inf"))
						child.pathCost += hval;
					
					if (explored.contains(child) == false) {
						child.parent = cur;
						frontier_queue.add(child);
					}
				} else if ((frontier_queue.contains(child) == true)) {
					if((type == "inf" && child.pathCost > cur.pathCost + c + hval) || (type == "uninf" && child.pathCost > cur.pathCost + c))
					{
						child.parent = cur;
						frontier_queue.remove(child);
						frontier_queue.add(child);
					}
				}
			}
		} while (frontier_queue.isEmpty() == false);
	}

	private static int getIndex(Node node, ArrayList<Node> graph) {
		for (int k = 0; k < graph.size(); k++)
			if (graph.get(k).toString().equals(node.toString()))
				return k;
		return -1;
	}

	public static void main(String[] args) {
		
		if (args.length < 4) { System.out.println("Wrong arguments"); System.exit(0);
		}
		
		ArrayList<Node> graph = createGraph(args[1]);
		//ArrayList<Node> graph = createGraph("C:\\Users\\Navya\\eclipse-workspace\\find_route\\src\\input.txt");
		Node origin = null, destination = null;
		for (Node node : graph) {
			if (node.toString().equals(args[2].toString()))//"Bremen"))
				origin = node;
			else if (node.toString().equals(args[3].toString()))//"Frankfurt"))
				destination = node;
		}
		int dst = 0;
		//System.out.println("args[0]"+args[0]);
		HashMap <String, Integer> hvalues = new HashMap<String, Integer>();
		hvalues = null;
		if(args[0].equals("inf"))
		{
			addHvalues(args[4]);
			//hvalues = addHvalues("C:\\Users\\Navya\\eclipse-workspace\\find_route\\src\\h_kassel.txt");
		}
		//hvalues = addHvalues("C:\\Users\\Navya\\eclipse-workspace\\find_route\\src\\h_kassel.txt");
		UniformCostSearch(origin, destination, args[0], hvalues);
		List<Node> route = new ArrayList<Node>();
		for (Node node = destination; node != null; node = node.parent)
			route.add(node);
		Collections.reverse(route);
		
		for (Node node : route) {
			if (node.parent != null)
				dst += node.calculateCost();
		}
		
		if (dst > 0) {
			System.out.println("distance: " + dst + " km");
			System.out.println("route: ");
			for (Node node : route) {
				if (node.parent != null)
					System.out.println(node.parent + " to " + node + ", " + node.calculateCost() + " km");
			}
		} else {
			System.out.println("distance: infinity");
			System.out.println("route: ");
			System.out.print("none");
		}
	}
}
