package rest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;


public class PerformanceTest {
	public static void main(String[] args) {
		final double thinkTime = 3;
		ArrayList<Double> xValues = new ArrayList<Double>();
		ArrayList<Double> yValues = new ArrayList<Double>();
		
		for (int i = 1; i <= 20; i++) {
			long startTime = System.nanoTime();
			double arrivalRate = i/thinkTime;//the number of req/sec
			CountDownLatch latch = new CountDownLatch(i);
			
			// the function each user will execute
			Runnable runnable = () -> {
				// start the timer
				long threadStartTime = System.nanoTime();
				URL url;
				try {
					url = new URL("https://qasimahmed.me/BookLand/rest/partner/read/getProductInfo?key=bc629434-da50-416f-aead-c1c492b88271&productId=111-0-3343-2656-3");
					HttpURLConnection con;
					con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					con.connect();

				} catch (MalformedURLException e2) {
					e2.printStackTrace();
				}catch (IOException e1) {
					e1.printStackTrace();
				}
				// end the timer
				long threadEndTime = System.nanoTime();
				
				// this will be the total execution time of the current thread
				System.out.println("The total thread execution time of the current thread is " +  (threadEndTime-threadStartTime)/1000000.0 + " milliseconds.");
				latch.countDown();
			};
			// the average response time of each get request 
			long averageTime = 0;
			
			// create i requests
			for (int j = 1; j <= i; j++) {
				// run the get request
				Thread myThread = new Thread(runnable);
				myThread.start();
			}
			// wait for the threads to return
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			long endTime = System.nanoTime();
			averageTime = endTime-startTime;
			
			yValues.add((double) averageTime/1000000.0);
			xValues.add(arrivalRate);
			System.out.println("the program with " + i + " user(s) will have an arrival rate of " + String.format("%.2f", arrivalRate) + " and took " + String.format("%.2f", (endTime - startTime) / 1000000.0) + " milliseconds to execute");
		}
		
		System.out.println();
		System.out.println("----------------End of scalability test for 1-20 concurrent users----------------");
		System.out.println("The domain of arrival rates(req/s) will be:");
		System.out.println(xValues);
		System.out.println("The range of response times(ms) will be:");
		System.out.println(yValues);

	}
}
