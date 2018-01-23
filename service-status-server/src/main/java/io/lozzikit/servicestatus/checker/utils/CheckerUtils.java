package io.lozzikit.servicestatus.checker.utils;

import io.lozzikit.servicestatus.checker.ServiceStatusChecker;
import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * Utilitarian class for action on the service status checker
 */
public class CheckerUtils {

    /**
     * Computes the appropriate schedule builder based on an interval and a time granularity
     * @param interval The interval to set between two check
     * @param granularity The units of the interval checks ([ms], [s], [m], [h])
     * @return The appropriate ScheduleBuilder
     */
    public static ScheduleBuilder<SimpleTrigger> appropriateSchedule(int interval, ServiceStatusChecker.TIME_GRANULARITY granularity){
        SimpleScheduleBuilder scheduleBuilder = simpleSchedule();
        switch (granularity){
            case MILLISECONDS:
                scheduleBuilder.withIntervalInMilliseconds(interval);
                break;
            case SECONDS:
                scheduleBuilder.withIntervalInSeconds(interval);
                break;
            case MINUTES:
                scheduleBuilder.withIntervalInMinutes(interval);
                break;
            case HOURS:
                scheduleBuilder.withIntervalInHours(interval);
                break;
        }
        scheduleBuilder.repeatForever();
        return scheduleBuilder;
    }
}
