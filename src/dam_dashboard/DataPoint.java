class DataPoint {
	private float distance;
	private String time;
	private String state;
	private int damAngle;
	private String sender;
	
	public DataPoint(float distance, String time, String state, int damAngle, String sender) {
		this.distance = distance;
		this.time = time;
		this.state = state;
		this.damAngle = damAngle;
		this.sender = sender;
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
}
