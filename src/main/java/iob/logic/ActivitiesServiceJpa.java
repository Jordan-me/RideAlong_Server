package iob.boundries;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.logic.ActivitiesService;
@Service
public class ActivitiesServiceJpa  implements ActivitiesService {

	@Override
	public Object invokeActivity(ActivityBoundary activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActivityBoundary> getAllActivities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllActivities() {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}
