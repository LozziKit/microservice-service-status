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
        ServiceEntity service = serviceService.createService(newService);
        return ResponseEntity.created(URI.create(serviceService.getLocationUrl(service.getId()))).build();
    }

    public ResponseEntity<Void> deleteService(@ApiParam(required = true) @PathVariable("id") UUID id) {
        serviceService.deleteServiceById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Service> getService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(allowableValues = "STATUS") @RequestParam(value = "expand", required = false, defaultValue = "HISTORY") String expand) {
        ServiceEntity serviceEntity = serviceService.getService(id, expand);
        return ResponseEntity.ok(serviceService.toDto(serviceEntity));
    }

    public ResponseEntity<List<Service>> getServices(@ApiParam(allowableValues = "STATUS") @RequestParam(value = "expand", required = false, defaultValue = "HISTORY") String expand) {
        List<ServiceEntity> serviceEntities = serviceService.getAllServices(expand);
        List<Service> services = new ArrayList<>();

        serviceEntities.forEach(serviceEntity -> services.add(serviceService.toDto(serviceEntity)));
        return ResponseEntity.ok(services);
    }

    public ResponseEntity<Void> updateService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(required = true) @RequestBody NewService service) {
        serviceService.updateService(id, service);
        return ResponseEntity.noContent().build();
    }

}
