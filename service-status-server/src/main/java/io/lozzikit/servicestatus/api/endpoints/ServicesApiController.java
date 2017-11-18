package io.lozzikit.servicestatus.api.endpoints;

import io.lozzikit.servicestatus.api.ServicesApi;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.dto.Service;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.service.ServiceService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ServicesApiController implements ServicesApi {

    @Autowired
    ServiceService serviceService;

    @Override
    public ResponseEntity<Void> addService(@ApiParam(required = true) @Valid @RequestBody NewService newService) {
        ServiceEntity service = serviceService.createService(toServiceEntity(newService));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(service.getId()).toUri();
        
        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Void> deleteService(@ApiParam(required = true) @PathVariable("id") UUID id) {
        serviceService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Service> getService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(allowableValues = "STATUS") @RequestParam(value = "expand", required = false, defaultValue = "HISTORY") String expand) {
        ServiceEntity serviceEntity = serviceService.getService(id, expand);
        return ResponseEntity.ok(toDto(serviceEntity));
    }

    @Override
    public ResponseEntity<List<Service>> getServices(@ApiParam(allowableValues = "STATUS") @RequestParam(value = "expand", required = false, defaultValue = "HISTORY") String expand) {
        List<ServiceEntity> serviceEntities = serviceService.getAllServices(expand);
        List<Service> services = new ArrayList<>();

        serviceEntities.forEach(serviceEntity -> services.add(toDto(serviceEntity)));
        return ResponseEntity.ok(services);
    }

    @Override
    public ResponseEntity<Void> updateService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(required = true) @RequestBody NewService service) {
        serviceService.updateService(id, toServiceEntity(service));
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
        service.setLocation(serviceService.getLocationUrl(serviceEntity.getId()));
        // TODO: add status support
        service.setStatuses(null);
        service.setLastStatus(null);

        return service;
    }

}
