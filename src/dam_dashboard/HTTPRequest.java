
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HTTPRequest {

	private static final String GET_URL = "http://c5c6ce2e7eff.ngrok.io/api/data";

	public static DataPoint sendGET() throws IOException {
		Float distance = 0.0f;
		String state = "", time = "", sender = "";
		int angle = 0;
		URL obj = new URL(GET_URL);
		DataPoint dp = null; 
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		//System.out.println("GET Response Code :: " + responseCode);
		
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
					distance = (float) array.getJSONObject(0).getDouble("distance");
					state = array.getJSONObject(0).getString("state");
					time = array.getJSONObject(0).getString("time");
					angle = array.getJSONObject(0).getInt("open-angle");
					sender = array.getJSONObject(0).getString("sender");
					
					dp = new DataPoint(distance, time, state, angle, sender);
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

}