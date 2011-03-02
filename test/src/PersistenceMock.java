import java.util.List;

import com.logansrings.booklibrary.persistence.Persistable;
import com.logansrings.booklibrary.persistence.PersistenceDelegate;



public class PersistenceMock implements PersistenceDelegate {

	boolean findOneCalled;
	Persistable findOneReturnValue;
	
	@Override
	public Persistable findOne(Persistable persistable) {
		findOneCalled = true;
		return findOneReturnValue;
	}

	@Override
	public boolean persist(Persistable persistable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean exists(Persistable persistable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Persistable> findAny(Persistable persistable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Persistable persistable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Persistable> findAll(Persistable persistable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Persistable findById(Persistable persistable) {
		// TODO Auto-generated method stub
		return null;
	}

}
