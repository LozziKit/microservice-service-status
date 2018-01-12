package io.lozzikit.servicestatus.api.endpoints;

import io.lozzikit.servicestatus.api.ServicesApi;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.dto.Service;
import io.lozzikit.servicestatus.api.dto.Status;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.entities.StatusEntity;
import io.lozzikit.servicestatus.service.ServiceManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ServicesApiController implements ServicesApi {

    @Autowired
    ServiceManager serviceManager;

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
            @ApiResponse(code = 201, message = "OK", response = Void.class),
            @ApiResponse(code = 404, message = "Not found", response = Void.class),
            @ApiResponse(code = 422, message = "Invalid payload", response = Void.class)})
    @RequestMapping(value = "/services/{id}",
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    @Override
    public ResponseEntity<Void> updateService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(required = true) @Valid @RequestBody NewService service) {
        serviceManager.updateService(id, toServiceEntity(service));
        return ResponseEntity.noContent().build();
    }

    private ServiceEntity toServiceEntity(NewService service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setName(service.getName());
        serviceEntity.setDescription(service.getDescription());
        serviceEntity.setUrl(service.getUrl());
        serviceEntity.setPort(service.getPort());
        serviceEntity.setInterval(service.getInterval());

        return serviceEntity;
    }

    private Service toDto(ServiceEntity serviceEntity) {
        Service service = new Service();

        service.setName(serviceEntity.getName());
        service.setDescription(serviceEntity.getDescription());
        service.setUrl(serviceEntity.getUrl());
        service.setPort(serviceEntity.getPort());
        service.setInterval(serviceEntity.getInterval());
        service.setLocation(serviceManager.getLocationUrl(serviceEntity.getId()));
        // TODO: add status support
        service.setStatuses(serviceEntity.getStatuses().stream().map(this::toDto).collect(Collectors.toList()));
        service.setLastStatus(toDto(serviceEntity.getStatuses().get(0)));

        return service;
    }

    private StatusEntity toStatusEntity(Status status, ServiceEntity service){
        return new StatusEntity(status.getUpdateAt().toDate(),
                status.getHttpStatus(),status.getStatus(),service);
    }

    private Status toDto(StatusEntity statusEntity){
        Status status = new Status();
        status.setHttpStatus(statusEntity.getHttpStatus());
        status.setStatus(statusEntity.getStatus());
        status.setUpdateAt(new DateTime(statusEntity.getCheckAt()));
        return status;
    }

}
