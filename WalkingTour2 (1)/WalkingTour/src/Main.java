import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map.Entry;

public class Main {

	public static Scanner sc = new Scanner(System.in);

	public static char mainMenu() {
		System.out.println("\n*** MAIN MENU ***");
		System.out.println("D. Display all Sites");
		System.out.println("A. Add a new Site");
		System.out.println("N. Search for a Site by name");
		System.out.println("I. Search for a Site by ID");
		System.out.println("F. Find adjacent Sites");
		System.out.println("C. Find the closest Site");
		System.out.println("E. Exit");
		System.out.println("Please choose an option: ");
		char option = sc.nextLine().charAt(0);
		return option;
	}

	public static void main(String[] args) {
		System.out.println("Welcome to The Walking Tour Program!");
		SiteGraph sg = new SiteGraph();
		Site nSIte;
		String name;
		double latitude, longitude;
		int id;
		boolean saved = false;

		if (sg.loadGraph("graph.txt"))
			System.out.println("\nInitial data read successfully!");
		else
			System.out.println("\nError with reading the initial data!");

		char entry = ' ';
		while (Character.toLowerCase(entry) != 'e' && !saved) {
			entry = Character.toLowerCase(mainMenu());
			switch (entry) {
			case 'd':
				if (sg.getGraph().size() == 0)
					System.out.println("There aren't any sites in the graph yet!");
				else {
					System.out.println("The following sites exist in the graph: ");
					System.out.println(sg);
				}
				break;
			case 'a':
				System.out.println("Enter a site name: ");
				name = sc.nextLine();
				System.out.println("Enter a site latitude: ");
				latitude = sc.nextDouble();
				System.out.println("Enter a site longitude: ");
				longitude = sc.nextDouble();
				id = sg.getGraph().size();
				if (latitude >= 0 && latitude <= 90 && longitude >= 0 && longitude <= 180) {
					nSIte = new Site(id, name, latitude, longitude);
					if (sg.getGraph().size() > 0) {
						System.out.println("* Available neighbours * ");
						ArrayList<Site> neighbours = new ArrayList<Site>();
						int neighbour = 0;
						while (neighbour != -1) {
							for (Entry<Site, ArrayList<Site>> gr : sg.getGraph().entrySet()) {
								Site current = gr.getKey();
								System.out.println(current.getID() + ". " + current.getName());
							}
							System.out.println("Please pick a neighbour or press -1 to go back: ");
							neighbour = Integer.valueOf(sc.next());
							if(neighbour != -1) {
								Site n = sg.getByID(neighbour);
								if (!neighbours.contains(n)) {
									neighbours.add(n);
									System.out.println("A new adjacent site is added successfully!");
								} else
									System.out.println("The site already exists in the adjacency list");
							}
						}
						if (sg.insertSite(nSIte, neighbours))
							System.out.println("A new site wiith neighbours is added successfully!");
						else
							System.out.println("Could not add a new site with neighbours!");
						} else {
							if (sg.insertSite(nSIte, new ArrayList<Site>()))
								System.out.println("A new site is added successfully!");
							else
								System.out.println("Could not add a new site!");
						}
				} else {
					System.out.println("Invalid values for latitude and/or longitude.");
				}
				sc.nextLine();
				break;
			case 'n':
				System.out.println("Please enter a name of the site: ");
				name = sc.nextLine();
				if (sg.searchByName(name) != null) {
					System.out.println("Site found! " + sg.searchByName(name));
				} else {
					System.out.println("Could not find the site with name " + name + "!");
				}
				break;
			case 'i':
				System.out.println("Please enter an ID of the site: ");
				id = Integer.valueOf(sc.nextLine());
				if (sg.getByID(id) != null) {
					System.out.println("Site found! ");
					System.out.println(sg.getByID(id));
				} else {
					System.out.println("Could not find the site with ID " + id + "!");
				}
				break;
			case 'f':
				System.out.println("Please enter a name of the site: ");
				name = sc.nextLine();
				if (sg.searchByName(name) != null) {
					Site found = sg.searchByName(name);
					ArrayList<Site> adj = sg.findNeighbours(found);
					if (adj.size() == 0)
						System.out.println("No adjacent sites found!");
					else {
						System.out.println("--- The adjacent sites --- ");
						for (Site a : adj) {
							System.out.println(a.getName());
						}
					}
				} else {
					System.out.println("Could not find the site with name " + name + "!");
				}
				break;
			case 'c':
				System.out.println("Please enter a name of the site: ");
				name = sc.nextLine();
				if (sg.searchByName(name) != null) {
					Site found = sg.searchByName(name);
					Site closest = sg.findClosestSite(found);
					System.out.println(
							"The closest distance from " + name + " is to the " + closest.getName() + ".");
				} else {
					System.out.println("Could not find the site with name " + name + "!");
				}
				break;
			case 'e':
				if (sg.saveGraph("graph.txt")) {
					System.out.println("The graph data was saved successfully!");
					System.out.println("\nThank you for using our program, have a nice day!");
					saved = true;
				} else
					System.out.println("Could not save the graph data!");
				break;
			default:
				System.out.println("Invalid option chosen, please retry!");
				break;
			}
		}
	}

}
