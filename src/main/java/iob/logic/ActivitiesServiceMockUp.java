package iob.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundries.ActivityBoundary;
import iob.data.ActivityEntity;


@Service
public class ActivitiesServiceMockUp  implements ActivitiesService {
	private Map<String,ActivityEntity> activityDataBaseMockup;
	private ActivitiesConverter converter;
	
	@Autowired
	public ActivitiesServiceMockUp(ActivitiesConverter converter) {
		super();
		this.converter = converter;
	}
	
	@Autowired
	public void setConverter(ActivitiesConverter converter) {
		this.converter = converter;
	}
	
	@PostConstruct
	public void init() {
		// thread safe Map
		this.activityDataBaseMockup = Collections.synchronizedMap(new HashMap<>());
	}
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
