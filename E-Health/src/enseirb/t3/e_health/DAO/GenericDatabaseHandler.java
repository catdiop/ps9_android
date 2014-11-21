package enseirb.t3.e_health.DAO;

import enseirb.t3.e_health.entity.Data;


public interface GenericDatabaseHandler<T> {

	public void create(T object);
	
	public T retrive(int id);
	
	public int update(T object);
	
	public void delete(int id);

}
