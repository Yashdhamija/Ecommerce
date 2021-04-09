package rest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;


public class ScalabilityTest {
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
				URL url;
				try {

					Partner partner = new Partner();
					String orders = partner.getOrdersByPartNumber("bc629434-da50-416f-aead-c1c492b88271","232-4-3454-3462-5" );
					System.out.println(orders);

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				latch.countDown();
			};
		
			long averageTime = 0;
			
			// create i requests
			for (int j = 1; j <= i; j++) {

				// run the get request
				Thread myThread = new Thread(runnable);
				myThread.start();
				long endTime = System.nanoTime();
				averageTime = endTime-startTime;
				System.out.println("the program with " + i + " user(s) will have an arrival rate of " + String.format("%.2f", arrivalRate) + " and took " + String.format("%.2f", (endTime - startTime) / 1000000.0) + " milliseconds to execute");
				yValues.add((double) averageTime);
				xValues.add(arrivalRate);
			}
		
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		

			
		}


	}
}
