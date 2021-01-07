/* Dato in ingresso - uscita richiesti GET - POST api/data */
class DataPoint {
	private float distance;
	private String time;
	private String state;
	private int damAngle;
	private String sender;
	private boolean modOp;
	
	public DataPoint(float distance, String time, String state, int damAngle, String sender, boolean modOp) {
		this.distance = distance;
		this.time = time;
		this.state = state;
		this.damAngle = damAngle;
		this.sender = sender;
		this.modOp = modOp;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getState() {
		return state;
	}
	
	public int getDamAngle() {
		return damAngle;
	}
	
	public String getSender() {
		return sender;
	}
	
	public boolean getModOp() {
		return modOp;
	}
}
