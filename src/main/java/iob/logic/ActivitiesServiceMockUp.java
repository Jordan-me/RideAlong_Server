package iob.logic;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.data.ActivityEntity;


//@Service
public class ActivitiesServiceMockUp  implements ActivitiesService {
	private Map<String,ActivityEntity> activityDataBaseMockup;
	private ActivitiesConverter converter;
	private String domainName;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
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
		activity.setActivityId(new ActivityId(domainName,UUID.randomUUID().toString()));
		activity.setCreatedTimestamp(new Date());
		ActivityEntity entity = converter.toEntity(activity);
		activityDataBaseMockup.put(activity.getActivityId().toString().toLowerCase(), entity);
		ActivityBoundary boundary = converter.toBoundary(entity);
		return boundary;
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
