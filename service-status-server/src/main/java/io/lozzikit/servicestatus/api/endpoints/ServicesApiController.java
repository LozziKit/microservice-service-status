package io.lozzikit.servicestatus.api.endpoints;

import io.lozzikit.servicestatus.api.ServicesApi;
import io.lozzikit.servicestatus.api.dto.*;
import io.lozzikit.servicestatus.entities.IncidentEntity;
import io.lozzikit.servicestatus.entities.IncidentUpdateEntity;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.entities.StatusEntity;
import io.lozzikit.servicestatus.service.IncidentManager;
import io.lozzikit.servicestatus.service.ServiceManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ServicesApiController implements ServicesApi {

    @Autowired
    ServiceManager serviceManager;

    @Autowired
    IncidentManager incidentManager;

    @ApiOperation(value = "Add a service to monitor", notes = "", response = Void.class, tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", response = Void.class),
            @ApiResponse(code = 422, message = "Invalid payload", response = Void.class)})
    @RequestMapping(value = "/services",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<Void> addService(@ApiParam(value = "Service object that needs to be added to the status page", required = true) @Valid @RequestBody NewService newService) {
        ServiceEntity service = serviceManager.createService(toServiceEntity(newService));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(service.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Delete an existing service", notes = "", response = Void.class, tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "OK", response = Void.class),
            @ApiResponse(code = 404, message = "Not found", response = Void.class)})
    @RequestMapping(value = "/services/{id}",
            method = RequestMethod.DELETE)
    @Override
    public ResponseEntity<Void> deleteService(@ApiParam(required = true) @PathVariable("id") UUID id) {
        serviceManager.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Get details of a service", notes = "", response = Service.class, tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Service.class)})
    @RequestMapping(value = "/services/{id}",
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<Service> getService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(allowableValues = "STATUS") @RequestParam(value = "expand", required = false, defaultValue = "HISTORY") String expand) {
        ServiceEntity serviceEntity = serviceManager.getService(id, expand);
        return ResponseEntity.ok(toDto(serviceEntity));
    }

    @ApiOperation(value = "Get a list of all services", notes = "", response = Service.class, responseContainer = "List", tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of services", response = Service.class)})
    @RequestMapping(value = "/services",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<List<Service>> getServices(@ApiParam(allowableValues = "STATUS") @RequestParam(value = "expand", required = false, defaultValue = "HISTORY") String expand) {
        List<ServiceEntity> serviceEntities = serviceManager.getAllServices(expand);
        List<Service> services = new ArrayList<>();

        serviceEntities.forEach(serviceEntity -> services.add(toDto(serviceEntity)));
        return ResponseEntity.ok(services);
    }

    @ApiOperation(value = "Update an existing service", notes = "", response = Void.class, tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content", response = Void.class),
            @ApiResponse(code = 404, message = "Not found", response = Void.class),
            @ApiResponse(code = 422, message = "Invalid payload", response = Void.class)})
    @RequestMapping(value = "/services/{id}",
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    @Override
    public ResponseEntity<Void> updateService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(required = true) @Valid @RequestBody NewService service) {
        serviceManager.updateService(id, toServiceEntity(service));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @ApiOperation(value = "Create a new incident for this service", notes = "", response = Void.class, tags = {"Incident",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Creation successful", response = Void.class),
            @ApiResponse(code = 404, message = "Service not found", response = Void.class),
            @ApiResponse(code = 422, message = "Invalid payload", response = Void.class)})
    @RequestMapping(value = "/services/{id}/incidents",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    @Override
    public ResponseEntity<Void> addIncident(@ApiParam(required = true) @PathVariable("id") UUID idService,
                                            @ApiParam(required = true) @Valid @RequestBody NewIncident incident
    ) {
        IncidentEntity incidentEntity = incidentManager.createIncident(idService, toIncidentEntity(incident));
        incidentEntity.getIncidentUpdates().add(toIncidentUpdateEntity(incident.getIncidentUpdate()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{incidentId}")
                .buildAndExpand(incidentEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Add an update to an incident", notes = "", response = Void.class, tags = {"Incident",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Creation successful", response = Void.class),
            @ApiResponse(code = 404, message = "Service not found", response = Void.class),
            @ApiResponse(code = 422, message = "Invalid payload", response = Void.class)})
    @RequestMapping(value = "/services/{id}/incidents/{incidentId}",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    @Override
    public ResponseEntity<Void> addIncidentUpdate(@ApiParam(required = true) @Valid @RequestBody IncidentUpdate incidentUpdate,
                                                  @ApiParam(required = true) @PathVariable("id") UUID idService,
                                                  @ApiParam(required = true) @PathVariable("incidentId") UUID idIncident) {
        incidentManager.addIncidentUpdate(idService, idIncident, toIncidentUpdateEntity(incidentUpdate));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Get details of an incident", notes = "", response = Void.class, tags = {"Incident",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the incident", response = Void.class),
            @ApiResponse(code = 404, message = "Not found", response = Void.class)})
    @RequestMapping(value = "/services/{id}/incidents/{incidentId}",
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<Incident> getIncidentDetails(@ApiParam(required = true) @PathVariable("id") UUID idService,
                                                       @ApiParam(required = true) @PathVariable("incidentId") UUID idIncident) {
        Optional<IncidentEntity> incident = incidentManager.getIncident(idService, idIncident);
        if (incident.isPresent()) {
            return ResponseEntity.ok(toDto(incident.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @ApiOperation(value = "Get a list of all incidents of a service", notes = "", response = Incident.class, responseContainer = "List", tags = {"Incident",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of incidents", response = Incident.class,responseContainer = "List")})
    @RequestMapping(value = "/services/{id}/incidents",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<List<Incident>> getIncidents(UUID serviceId) {
        Set<IncidentEntity> incidentEntities = incidentManager.getAllIncidents(serviceId);
        List<Incident> incidents = new ArrayList<>();

        incidentEntities.forEach(incidentEntity -> incidents.add(toDto(incidentEntity)));
        return ResponseEntity.ok(incidents);
    }

    private ServiceEntity toServiceEntity(NewService service) {
        return new ServiceEntity(service.getName(), service.getDescription(), service.getUrl(), service.getPort(), service.getInterval());
    }

    private IncidentEntity toIncidentEntity(NewIncident incident) {
        return new IncidentEntity(incident.getTitle(), toIncidentUpdateEntity(incident.getIncidentUpdate()));
    }

    private IncidentUpdateEntity toIncidentUpdateEntity(IncidentUpdate incidentUpdate) {
        return new IncidentUpdateEntity(incidentUpdate.getIncidentType(), incidentUpdate.getMessage());
    }

    private Service toDto(ServiceEntity serviceEntity) {
        Service service = new Service();

        service.setName(serviceEntity.getName());
        service.setDescription(serviceEntity.getDescription());
        service.setUrl(serviceEntity.getUrl());
        service.setPort(serviceEntity.getPort());
        service.setInterval(serviceEntity.getInterval());
        service.setLocation(serviceManager.getLocationUrl(serviceEntity.getId()));

        service.setLastStatus(toDto(serviceEntity.getStatuses().get(0)));

        return service;
    }

    private StatusEntity toStatusEntity(Status status, ServiceEntity service) {
        return new StatusEntity(status.getUpdateAt().toDate(),
                status.getHttpStatus(), status.getStatus(), service);
    }

    private Incident toDto(IncidentEntity incidentEntity) {
        Incident incident = new Incident();
        incident.setTitle(incidentEntity.getTitle());
        List<IncidentUpdate> incidentUpdates = new LinkedList<>();
        incidentEntity.getIncidentUpdates().forEach(incidentUpdateEntity -> incidentUpdates.add(toDto(incidentUpdateEntity)));
        return incident;
    }

    private IncidentUpdate toDto(IncidentUpdateEntity incidentUpdateEntity) {
        IncidentUpdate incidentUpdate = new IncidentUpdate();
        incidentUpdate.setIncidentType(incidentUpdateEntity.getIncidentType());
        incidentUpdate.setMessage(incidentUpdateEntity.getMessage());
        return incidentUpdate;
    }


    private Status toDto(StatusEntity statusEntity) {
        Status status = new Status();
        status.setHttpStatus(statusEntity.getHttpStatus());
        status.setStatus(statusEntity.getStatus());
        status.setUpdateAt(new DateTime(statusEntity.getCheckAt()));
        return status;
    }

}
