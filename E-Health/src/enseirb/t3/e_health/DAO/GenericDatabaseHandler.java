package enseirb.t3.e_health.DAO;


public interface GenericDatabaseHandler<T> {

	public void create(T object);
	
	public T retrive(int id);
	
	public int update(T object);
	
	public void delete(int id);
}
