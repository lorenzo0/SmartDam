
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;

public class HTTPRequest {

	private static final String GET_URL = "http://c5c6ce2e7eff.ngrok.io/api/data";
	private static final String POST_URL = "http://c5c6ce2e7eff.ngrok.io/api/log";
	private static final String POST_PARAMS = "{\"sender\": DASHBOARD, \"message\": Dasboard has requested GET data from service }";
	private static final String POST_PARAMS_NO = "{\"sender\": DASHBOARD, \"message\": Dasboard has requested GET data from service, are there any data? }";

	boolean size;
	
	public DataPoint sendGET() throws IOException {
		Float distance = 0.0f;
		String state = "", time = "", sender = "";
		boolean modOp = false;
		int angle = 0;
		size = false;
		URL obj = new URL(GET_URL);
		DataPoint dp = null; 
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setReadTimeout(10000);
		int responseCode = con.getResponseCode();
		
		if (responseCode == HttpURLConnection.HTTP_OK) {
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			try {
					JSONArray array = new JSONArray(response.toString());
					if(array.length()>2)
						size = true;
					distance = (float) array.getJSONObject(0).getDouble("distance");
					state = array.getJSONObject(0).getString("state");
					time = array.getJSONObject(0).getString("time");
					angle = array.getJSONObject(0).getInt("open-angle");
					sender = array.getJSONObject(0).getString("sender");
					modOp = array.getJSONObject(0).getBoolean("mod-op");
					
					dp = new DataPoint(distance, time, state, angle, sender, modOp);
					return dp;
					
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
            
		} else {
			System.out.println("GET request not worked");
		}
		
		return dp;

	}
	
	public XYSeriesCollection sendGETArray() throws IOException {
		Float distance = 0.0f;
		String state = "", time = "", sender = "";
		int angle = 0;
		URL obj = new URL(GET_URL);
		DataPoint dp = null; 
		var series = new XYSeries("Last 20 records");
		
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setReadTimeout(10000);
		int responseCode = con.getResponseCode();
		
		if (responseCode == HttpURLConnection.HTTP_OK) {
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			try {
					JSONArray array = new JSONArray(response.toString());
					if(array.length()>20) {
						for(int i=0; i<20; i++) {
							series.add(i, array.getJSONObject(i).getDouble("distance"));
						}
					}else {
						for(int i=0; i<array.length(); i++) {
							series.add(i, array.getJSONObject(i).getDouble("distance"));
						}
					}
					
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
            
		} else {
			System.out.println("GET request not worked");
		}
		
		var finalDataset = new XYSeriesCollection();
        finalDataset.addSeries(series);
		
		return finalDataset;

	}
	
	public void sendPOST(Boolean bool) throws IOException {
		
		URL obj = new URL(POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");

		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		if(bool)
			os.write(POST_PARAMS.getBytes());
		else
			os.write(POST_PARAMS_NO.getBytes());
		os.flush();
		os.close();

		int responseCode = con.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
	}
	
	public boolean sizeArrayList() {
		return size;
	}

}