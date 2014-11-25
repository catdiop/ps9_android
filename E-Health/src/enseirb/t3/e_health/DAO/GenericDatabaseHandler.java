package enseirb.t3.e_health.DAO;



public interface GenericDatabaseHandler<T> {

	public void createUser(T object);
	
	public T retrieveUser(int id);
	
	public int updateUser(T object);
	
	public void deleteUser(int id);

}
