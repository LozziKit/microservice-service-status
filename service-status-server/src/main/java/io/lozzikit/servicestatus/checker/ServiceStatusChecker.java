package io.lozzikit.servicestatus.checker;

import io.lozzikit.servicestatus.checker.jobs.CheckTask;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.service.ServiceManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Class in charge of checking services' status
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ServiceStatusChecker  {

    //Constants fields used for object id
    public static final String DEFAULT_EXPAND = "HISTORY";

    //Service manager used to interface with Service DAO
    @Autowired
    ServiceManager serviceManager;

    @Autowired
    JobListener listener;

    private final Scheduler scheduler;     //Quartz scheduler used for recurrent event triggering

    /**
     * Default constructor
     * @throws SchedulerException If quartz fails to retrieve the default scheduler
     */
    public ServiceStatusChecker() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    @javax.annotation.PostConstruct
    public void init() throws SchedulerException {
        scheduler.getListenerManager().addJobListener(listener);
        scheduler.start();
    }

    @javax.annotation.PreDestroy
    public void destroy(){
        System.err.println("Service status checker destroyed.");
    }

    /**
     * Schedules a singles service to be monitored
     * @param s The service to check
     */
    public void schedule(ServiceEntity s){

        JobDetail job = newJob(CheckTask.class)
                .withIdentity("service-"+s.getId())
                .build();

        job.getJobDataMap().put(CheckTask.UUID,s.getId());
        job.getJobDataMap().put(CheckTask.SERVICE, s);

        Trigger trigger = newTrigger()
                .withIdentity("trigger-"+s.getId())
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(s.getInterval())
                        .repeatForever())
                .build();

        try {
            //Tries to schedule the setup job
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Schedules all events stored in the service service
     */
    public void scheduleAll(){
        List<ServiceEntity> services = serviceManager.getAllServices();
        scheduleAll(services);
    }

    /**
     * Schedules all given services for later check
     * @param services The services whose state needs checking
     */
    public void scheduleAll(List<ServiceEntity> services){
        services.forEach(this::schedule);
    }

    /**
     * Get the list of all scheduled jobs
     * @return A JobKey list of all scheduled jobs
     * @throws SchedulerException if an error was encounter with the scheduler
     */
    public List<JobKey> getScheduledTasks() throws SchedulerException {

        List<JobKey> scheduledTasks = new ArrayList<>();

        for (String groupName : scheduler.getJobGroupNames())
            scheduledTasks.addAll(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)));

        return scheduledTasks;
    }

    /**
     * Get the scheduler
     * @return The main quartz scheduler
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Unschedules a service given by its name
     * @param s The service whose monitoring needs removing
     * @throws SchedulerException if no associated service was found or if the unscheduling failed
     */
    public void removeScheduledTask(ServiceEntity s) throws SchedulerException {
        scheduler.deleteJob(JobKey.jobKey("service-"+s.getId()));
    }

    /**
     * Clear all scheduling data
     * @throws SchedulerException
     */
    public void clear() throws SchedulerException {
        scheduler.clear();
    }

    /**
     * Updates the schedule associated with the given service's name
     * @param s The service to update
     * @param interval The new interval to schedule
     * @throws SchedulerException if the rescheduling fails
     */
    public void updateSchedule(ServiceEntity s, int interval) throws SchedulerException {

        scheduler.rescheduleJob(TriggerKey.triggerKey("trigger-"+s.getId()),
                newTrigger()
                        .withIdentity("trigger-"+s.getId())
                        .startNow()
                        .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(interval)
                                .repeatForever())
                        .build());
    }
}
