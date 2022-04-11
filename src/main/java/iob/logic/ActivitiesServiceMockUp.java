package iob.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		ActivityEntity entity = converter.toEntity(activity);
		activityDataBaseMockup.put(activity.getActivityId().toString(), entity);
		return entity.toString();
	}

	@Override
	public List<ActivityBoundary> getAllActivities() {
		List<ActivityBoundary> activtiesBoundary = activityDataBaseMockup.values().stream()
                .map(v -> converter.toBoundary(v))
                .collect(Collectors.toList());
        
		return activtiesBoundary;
	}

	@Override
	public void deleteAllActivities() {
		this.activityDataBaseMockup.clear();	
		
	}

	
	
	
	
}
