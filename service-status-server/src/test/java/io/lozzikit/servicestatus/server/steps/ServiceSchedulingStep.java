package io.lozzikit.servicestatus.server.steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.endpoints.ServicesApiController;
import io.lozzikit.servicestatus.checker.ServiceStatusChecker;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.service.ServiceManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.quartz.JobBuilder.newJob;

public class ServiceSchedulingStep {

    private final int PORT = 20000;

    private ServiceStatusChecker serviceStatusChecker;
    private List<ServiceEntity> services;

    private ServiceManager serviceManager;

    private Map<ServiceEntity, String> endpoints = new HashMap<>();
    private Map<ServiceEntity, Integer> responses = new HashMap<>();
    private Map<String, Integer> delays = new HashMap<>();

    public ServiceSchedulingStep() throws SchedulerException {
        StdSchedulerFactory.getDefaultScheduler().clear();
    }

    @Given("^there is a Service server$")
    public void thereIsAServiceServer()  {
    }

    @And("^the server is up and running$")
    public void theServerIsUpAndRunning() {
    }


    @And("^I have a service checker$")
    public void iHaveAServiceChecker() throws Throwable {
        serviceStatusChecker = new ServiceStatusChecker();
    }

    @And("^I have multiples services:$")
    public void iHaveMultiplesServices(DataTable table) {
        services = new ArrayList<>();
        table.asMaps(String.class, String.class).forEach(map -> {
            ServiceEntity service = new ServiceEntity();
            service.setId(UUID.randomUUID());
            service.setName("Service n. "+map.get("id"));
            service.setDescription(service.getName()+" used for testing");
            service.setInterval(Integer.parseInt(map.get("delay")));
            service.setStatuses(new ArrayList<>());

            String endpoint = map.get("url");

            try {
                URL url = new URL("http","127.0.0.1",PORT, endpoint);
                service.setUrl(url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            services.add(service);
            endpoints.put(service,endpoint);
            responses.put(service, Integer.valueOf(map.get("response")));
            delays.put("service-"+service.getId(),service.getInterval());
        });
    }

    @And("^the server is setup to answer accordingly$")
    public void theServerIsSetupToAnswerAccordingly()  {
        services.forEach(service -> {
           // mockServer
            //        .stubFor(get(urlEqualTo(endpoints.get(service)))
            //        .willReturn(aResponse().withStatus(responses.get(service))));
        });
    }

    @And("^the service checker scheduled all tasks$")
    public void theServiceCheckerScheduledAllTasks()  {
        serviceStatusChecker.scheduleAll(services);
    }

    @When("^each task shall be scheduled correctly$")
    public void eachTaskShallBeScheduledCorrectly() throws SchedulerException {
        assertEquals(services.size(),serviceStatusChecker.getScheduledTasks().size());
    }

    @Then("^each task shall be executed after their delay$")
    public void eachTaskShallBeExecutedAfterTheirDelay() throws SchedulerException {
        List<JobKey> tasks = serviceStatusChecker.getScheduledTasks();

        for(JobKey task : tasks){
            List<Trigger> triggers = (List<Trigger>) serviceStatusChecker.getScheduler().getTriggersOfJob(task);
            Date nextFireTime = triggers.get(0).getFireTimeAfter(new Date());
            long diff = nextFireTime.getTime() - System.currentTimeMillis();
            long diffMinutes = (long) (diff / (60.0 * 1000) % 60)+1;
            assertEquals( delays.get(task.getName()).intValue(), diffMinutes);
        }

    }

    @Then("^the server can be shutdown$")
    public void theServerCanBeShutdown()  {
    }

    @When("^a service's delay is changed$")
    public void aServiceSDelayIsChanged() throws SchedulerException {
        for(ServiceEntity service : services)
            serviceStatusChecker.updateSchedule(service, service.getInterval()+1);
    }

    @Then("^its next trigger's fire time shall be updated$")
    public void itsNextTriggerSFireTimeShallBeUpdated() throws SchedulerException {
        List<JobKey> tasks = serviceStatusChecker.getScheduledTasks();

        for(JobKey task : tasks){
            List<Trigger> triggers = (List<Trigger>) serviceStatusChecker.getScheduler().getTriggersOfJob(task);
            Date nextFireTime = triggers.get(0).getFireTimeAfter(new Date());
            long diff = nextFireTime.getTime() - System.currentTimeMillis();
            long diffMinutes = (long) (diff / (60.0 * 1000) % 60)+1;
            System.err.println(task.getName()+" : "+nextFireTime.getMinutes()+" vs "+new Date().getMinutes());
            assertNotEquals( delays.get(task.getName()).intValue(), diffMinutes);
        }
    }

    @When("^a service is deleted$")
    public void aServiceIsDeleted()  {
        services.forEach(service -> {
            try {
                serviceStatusChecker.removeScheduledTask(service);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
    }

    @Then("^the scheduler shouln't have any trace of future check$")
    public void theSchedulerShoulnTHaveAnyTraceOfFutureCheck() throws SchedulerException {
        List<JobKey> tasks = serviceStatusChecker.getScheduledTasks();
        assertEquals(0, tasks.size());
    }

    @And("^the service checker is reset$")
    public void theServiceCheckerIsReset() throws SchedulerException {
        serviceStatusChecker.clear();
    }

    @When("^a service is contacted$")
    public void aServiceIsContacted() throws SchedulerException {

    }

    @Then("^a coherent response shall be added to its status list$")
    public void aCoherentResponseShallBeAddedToItsStatusList()  {


    }
}
