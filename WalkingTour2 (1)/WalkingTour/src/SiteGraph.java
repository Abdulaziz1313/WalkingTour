import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class SiteGraph {
	
	private HashMap<Site, ArrayList<Site>> graph;
	
	public SiteGraph() {
		graph = new HashMap<Site, ArrayList<Site>>();
	}
	
	public HashMap<Site, ArrayList<Site>> getGraph(){
		return graph;
	}
	

	public boolean loadGraph(String filename) {
	    try {
	        File f = new File(filename);
	        Scanner reader = new Scanner(f);
	        ArrayList<String> neighbours = new ArrayList<String>();
	        ArrayList<Site> sites = new ArrayList<Site>();
	        while (reader.hasNextLine()) {
	          String data = reader.nextLine();
	          String[] parts = data.split("#");
	          int ID = Integer.valueOf(parts[0].strip());
	          String name = parts[1].strip();
	          double latitude = Double.valueOf(parts[2].strip());
	          double longitude = Double.valueOf(parts[3].strip());
	          neighbours.add(parts[4].strip());
	          Site s = new Site(ID, name, latitude, longitude);
	          sites.add(s);
	        }
	        reader.close();
	        int i = 0;
	        for (Site s : sites) {
	        	String[] adj = neighbours.get(i).split(",");
	        	ArrayList<Site> currAdj = new ArrayList<Site>();
	        	for(int j = 0; j < adj.length; j++) {
	        		currAdj.add(sites.get(Integer.valueOf(adj[j])));
	        	}
	        	graph.put(s, currAdj);
	        	i++;
	        }
	       
	        if(graph.size() > 0)
	        	return true;
	        else
	        	return false;
	      } catch (FileNotFoundException e) {
	        System.out.println("An error occurred. " + e.getMessage());
	        return false;
	      }
	}
	
	public boolean insertSite(Site s, ArrayList<Site> neighbours) {
		if(searchByName(s.getName()) != null)
			return false;
		else {
			graph.put(s, neighbours);
			return true;
		}
	}
	
	public Site searchByName(String name) {
		Site found = null;
		for (Entry<Site, ArrayList<Site>> entry : graph.entrySet()) {
			Site current = entry.getKey();
			if(current.getName().toLowerCase().equals(name.toLowerCase()))
				found = current;
		}
		return found;
	}
	
	public Site getByID(int id) {
		Site found = null;
		for (Entry<Site, ArrayList<Site>> entry : graph.entrySet()) {
			Site current = entry.getKey();
			if(current.getID() == id)
				found = current;
		}
		return found;
	}
	

	public double calcDistance(Site s1, Site s2) {
		double R = 6371e3;
		double f1 = s1.getLatitude() * Math.PI/180;;
		double f2 = s2.getLatitude() * Math.PI/180;
		double deltaf = (s2.getLatitude() - s1.getLatitude()) * Math.PI/180;
		double deltal = (s2.getLongitude() - s1.getLongitude()) * Math.PI/180;
		double a = Math.sin(deltaf/2) * Math.sin(deltaf/2) + Math.cos(f1) * Math.cos(f2) * Math.sin(deltal/2) * Math.sin(deltal/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return R * c;
	}
	
	public ArrayList<Site> findNeighbours(Site s){
		ArrayList<Site> neighbours = new ArrayList<Site>();
		for (Entry<Site, ArrayList<Site>> entry : graph.entrySet()) {
			Site current = entry.getKey();
			if(current.getName().toLowerCase().equals(s.getName().toLowerCase())) {
					neighbours = entry.getValue();
			}
		}
		return neighbours;
	}
	
	public Site findClosestSite(Site s) {
		ArrayList<Site> neighbours = findNeighbours(s);

		if(neighbours.size() == 0)
			return null;
		else {
			double minDist = calcDistance(s, neighbours.get(0));
			Site minSite = neighbours.get(0);
			
			for(int i = 1; i < neighbours.size(); i++) {
				if(calcDistance(s, neighbours.get(i)) > minDist) {
					minDist = calcDistance(s, neighbours.get(i));
					minSite = neighbours.get(i);
				}
					
			}
			System.out.println("The closest distance is equal to " + String.format("%.2f", minDist) + " metres,"
					+ " which is approx " + String.format("%.2f", minDist/1000.0) + " kilometers.");
			return minSite;
		}
	}
	
	
	public boolean saveGraph(String filename) {
		try {
			FileWriter writer = new FileWriter(filename);
			String line = "";
			for (Entry<Site, ArrayList<Site>> entry : graph.entrySet()) {
				Site key = entry.getKey();		
				ArrayList<Site> values = entry.getValue();
				line = key.getID() + "#" + key.getName() + "#" + key.getLatitude() + "#" + key.getLongitude() + "#";
				for(Site v : values) {
					line += v.getID() + ",";
				}
				line = line.substring(0, line.length() - 1) + "\n";
				writer.write(line);
			}
			writer.close();
			return true;
    	} catch (IOException e) {
        System.out.println("An error occurred. " + e.getMessage());
        return false;
    	}
	}
	
	public String toString() {
		String out = "";
		for (Entry<Site, ArrayList<Site>> entry : graph.entrySet()) {
			Site current = entry.getKey();
			out += current.toString() + "\n--- Adjacent sites ---\n";
			ArrayList<Site> adjacent = entry.getValue();
			if(adjacent.size() == 0)
				out += "No adjacent sites yet!\n";
			else {
				for(Site s : adjacent) {
					out += s.getName() + "\n";
				}
			}
		}
		return out;
	}
}
