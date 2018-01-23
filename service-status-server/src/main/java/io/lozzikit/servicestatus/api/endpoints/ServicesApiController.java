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
    @Override
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
    public ResponseEntity<Void> deleteService(@ApiParam(value = "ID of service to delete", required = true) @PathVariable("id") UUID id) {
        serviceManager.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "get the status history of a service", notes = "", response = Status.class, responseContainer = "List", tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "list of status of a service", response = Status.class),
            @ApiResponse(code = 404, message = "Not found", response = Status.class)})
    @RequestMapping(value = "/services/{id}/history",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<List<Status>> getHistory(@ApiParam(value = "ID of the service", required = true) @PathVariable("id") UUID id) {
        List<StatusEntity> statusEntities = serviceManager.getService(id).getStatuses();
        List<Status> status = statusEntities
                .stream()
                .sorted(Comparator.comparing(StatusEntity::getCheckAt))
                .map(statusEntity -> toDto(statusEntity))
                .collect(Collectors.toList());

        return ResponseEntity.ok(status);
    }

    @ApiOperation(value = "Get details of a service", notes = "", response = Service.class, tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Service.class)})
    @RequestMapping(value = "/services/{id}",
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<Service> getService(@ApiParam(value = "ID of service to update", required = true) @PathVariable("id") UUID id) {
        ServiceEntity serviceEntity = serviceManager.getService(id);
        return ResponseEntity.ok(toDto(serviceEntity));
    }


    @ApiOperation(value = "Get a list of all services", notes = "", response = Service.class, responseContainer = "List", tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of services", response = Service.class)})
    @RequestMapping(value = "/services",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<List<Service>> getServices() {
        List<ServiceEntity> serviceEntities = serviceManager.getAllServices();
        List<Service> services = new ArrayList<>();

        serviceEntities.forEach(serviceEntity -> services.add(toDto(serviceEntity)));
        return ResponseEntity.ok(services);
    }

    @ApiOperation(value = "Update an existing service", notes = "", response = Void.class, tags = {"Service",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", response = Void.class),
            @ApiResponse(code = 404, message = "Not found", response = Void.class),
            @ApiResponse(code = 422, message = "Invalid payload", response = Void.class)})
    @RequestMapping(value = "/services/{id}",
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    @Override
    public ResponseEntity<Void> updateService(@ApiParam(value = "ID of service to update", required = true) @PathVariable("id") UUID id,
                                              @ApiParam(value = "Service object that needs to be modified", required = true) @Valid @RequestBody NewService service) {
        serviceManager.updateService(id, toServiceEntity(service));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //Incidents

    @ApiOperation(value = "Get a list of all Incident of a service", notes = "", response = Incident.class, responseContainer = "List", tags = {"Incident",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the list of services", response = Incident.class),
            @ApiResponse(code = 404, message = "Not found", response = Incident.class)})
    @RequestMapping(value = "/services/{idService}/incidents",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<List<Incident>> getIncidents(@ApiParam(value = "ID of the service", required = true) @PathVariable("idService") UUID idService) {
        Set<IncidentEntity> incidentEntities = incidentManager.getAllIncidents(idService);
        List<Incident> incidents = new ArrayList<>();

        incidentEntities.forEach(incidentEntity -> incidents.add(toDto(incidentEntity)));
        return ResponseEntity.ok(incidents);
    }

    @ApiOperation(value = "Create a new incident for this service", notes = "", response = Void.class, tags = {"Incident",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Creation successful", response = Void.class),
            @ApiResponse(code = 404, message = "Service not found", response = Void.class),
            @ApiResponse(code = 422, message = "Invalid payload", response = Void.class)})
    @RequestMapping(value = "/services/{idService}/incidents",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    @Override
    public ResponseEntity<Void> addIncident(@ApiParam(value = "ID of the service", required = true) @PathVariable("idService") UUID idService,
                                            @ApiParam(value = "Incident object to be added to the status page", required = true) @Valid @RequestBody NewIncident newIncident) {
        IncidentEntity incidentEntity = incidentManager.createIncident(idService, toIncidentEntity(newIncident), new IncidentUpdateEntity(newIncident.getType(),newIncident.getMessage()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{incidentId}")
                .buildAndExpand(incidentEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Add an update to an incident", notes = "", response = Void.class, tags = {"Incident",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Creation successful", response = Void.class),
            @ApiResponse(code = 404, message = "Not found", response = Void.class),
            @ApiResponse(code = 422, message = "Invalid payload", response = Void.class)})
    @RequestMapping(value = "/services/{idService}/incidents/{idIncident}",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    @Override
    public ResponseEntity<Void> addIncidentUpdate(@ApiParam(value = "Incident update to be added to the incident", required = true) @Valid @RequestBody IncidentUpdate incidentUpdate,
                                                  @ApiParam(value = "ID of the service",required=true ) @PathVariable("idService") UUID idService,
                                                  @ApiParam(value = "ID of the incident to update", required = true) @PathVariable("idIncident") UUID idIncident) {
        incidentManager.addIncidentUpdate(idIncident, idService,toIncidentUpdateEntity(incidentUpdate));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Get details of an incident", notes = "", response = Incident.class, tags = {"Incident",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return the incident", response = Incident.class),
            @ApiResponse(code = 404, message = "Not found", response = Incident.class)})
    @RequestMapping(value = "/services/{idService}/incidents/{idIncident}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    @Override
    public ResponseEntity<Incident> getIncidentDetails(@ApiParam(value = "ID of the service", required = true) @PathVariable("idService") UUID idService,
                                                       @ApiParam(value = "ID of the incidents to get", required = true) @PathVariable("idIncident") UUID idIncident) {
        IncidentEntity incident = incidentManager.getIncident(idService, idIncident);
        return ResponseEntity.ok(toDto(incident));
    }

    //Transformations

    private ServiceEntity toServiceEntity(NewService service) {
        return new ServiceEntity(service.getName(), service.getDescription(), service.getUrl(), service.getPort(), service.getInterval());
    }

    private IncidentEntity toIncidentEntity(NewIncident incident) {
        return new IncidentEntity(incident.getTitle(), incident.getType(),incident.getMessage());
    }

    private IncidentUpdateEntity toIncidentUpdateEntity(IncidentUpdate incidentUpdate) {
        return new IncidentUpdateEntity(incidentUpdate.getType(), incidentUpdate.getMessage());
    }

    private StatusEntity toStatusEntity(Status status, ServiceEntity service) {
        return new StatusEntity(status.getUpdateAt().toDate(),
                status.getHttpStatus(), status.getState(), service);
    }

    private Service toDto(ServiceEntity serviceEntity) {
        Service service = new Service();

        service.setName(serviceEntity.getName());
        service.setDescription(serviceEntity.getDescription());
        service.setUrl(serviceEntity.getUrl());
        service.setPort(serviceEntity.getPort());
        service.setInterval(serviceEntity.getInterval());
        service.setLocation(serviceManager.getLocationUrl(serviceEntity.getId()));

        if (!serviceEntity.getStatuses().isEmpty()) {
            int lastIndex = serviceEntity.getStatuses().size() - 1;
            service.setLastStatus(toDto(serviceEntity.getStatuses().get(lastIndex)));
        }

        return service;
    }

    private Incident toDto(IncidentEntity incidentEntity) {
        Incident incident = new Incident();
        incident.setTitle(incidentEntity.getTitle());
        incident.setUpdates(incidentEntity.getIncidentUpdates()
                .stream()
                .sorted(Comparator.comparing(IncidentUpdateEntity::getDate))
                .map(incidentUpdateEntity -> (toDto(incidentUpdateEntity)))
                .collect(Collectors.toList()));
        incident.setLocation(incidentManager.getLocationUrl(incidentEntity));
        return incident;
    }

    private IncidentUpdate toDto(IncidentUpdateEntity incidentUpdateEntity) {
        IncidentUpdate incidentUpdate = new IncidentUpdate();
        incidentUpdate.setType(incidentUpdateEntity.getIncidentType());
        incidentUpdate.setMessage(incidentUpdateEntity.getMessage());
        incidentUpdate.setDate(new DateTime(incidentUpdateEntity.getDate()));
        return incidentUpdate;
    }

    private Status toDto(StatusEntity statusEntity) {
        Status status = new Status();
        status.setHttpStatus(statusEntity.getHttpStatus());
        status.setState(statusEntity.getStatus());
        status.setUpdateAt(new DateTime(statusEntity.getCheckAt()));
        return status;
    }

}
