package uk.ac.tees;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import uk.ac.tees.agent.SomeUserAgent;
import uk.ac.tees.agent.UserAgent;
import uk.ac.tees.net.NetworkConstants;
import uk.ac.tees.net.message.impl.StringMessage;
import uk.ac.tees.portal.Portal;

public final class EntryPoint {

	public static void main(String[] args) {
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		
		Router router = new Router("router1", NetworkConstants.SERVER_PORT, threadFactory);
		router.start();
		
		Portal portal1 = new Portal("portal1", threadFactory);
		portal1.start();
		UserAgent userAgent1 = new SomeUserAgent("1", threadFactory);
		UserAgent userAgent2 = new SomeUserAgent("2", threadFactory);
		portal1.addAgent(userAgent1);
		portal1.addAgent(userAgent2);
		portal1.connectToRouter("localhost", NetworkConstants.SERVER_PORT);
		
		Portal portal2 = new Portal("portal2", threadFactory);
		UserAgent userAgent3 = new SomeUserAgent("3", threadFactory);
		UserAgent userAgent4 = new SomeUserAgent("4", threadFactory);
		portal2.addAgent(userAgent3);
		portal2.addAgent(userAgent4);
		portal2.connectToRouter("localhost", NetworkConstants.SERVER_PORT);
	}
	
	void method1() {
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		
		Portal portal = new Portal("portal1", threadFactory);
		portal.start();
		
		UserAgent userAgent1 = new SomeUserAgent("1", threadFactory);
		UserAgent userAgent2 = new SomeUserAgent("2", threadFactory);
		
		portal.addAgent(userAgent1);
		portal.addAgent(userAgent2);
		
		try (Scanner scanner = new Scanner(System.in)) {
			while (scanner.hasNext()) {
				String[] msg = scanner.nextLine().split(":");
				
				UserAgent agent = portal.getAgent(msg[0]);
				
				if (agent == null) {
					System.out.println("Source agent " + msg[0] + " doesn't exist");
					continue;
				}
				
				if (!portal.containsAgent(msg[1])) {
					System.out.println("Destination agent" + msg[1] + " doesn't exist");
					continue;
				}
				
				agent.send(new StringMessage(agent.getUid(), msg[1], msg[2]));
			}
		}
	}

}