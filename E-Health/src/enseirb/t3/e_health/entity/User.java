package enseirb.t3.e_health.entity;

public class User {
	private int id;
	private String username;
	private String password;
	private String lastname;
	private String firstname;
	private String type;
	
	public User(){
		this.username=null;
		this.password=null;
		this.firstname=null;
		this.lastname=null;
		this.type = null;
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
