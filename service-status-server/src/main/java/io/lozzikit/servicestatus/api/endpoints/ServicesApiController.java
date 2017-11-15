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
import java.util.UUID;

@RestController
public class ServicesApiController implements ServicesApi {

    @Autowired
    ServiceService serviceService;

    @Override
    public ResponseEntity<Void> addService(@ApiParam(required = true) @Valid @RequestBody NewService newService) {
        ServiceEntity service = serviceService.createService(newService);
        return null;
    }


    public ResponseEntity<Void> deleteService(@ApiParam(required = true) @PathVariable("id") UUID id) {
        return null;
    }

    public ResponseEntity<Service> getService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(allowableValues = "STATUS") @Valid @RequestParam(value = "expand", required = false) String expand) {
        return null;
    }


    public ResponseEntity<Void> getServices(@ApiParam(allowableValues = "STATUS") @RequestParam(value = "expand", required = false) String expand) {
        return null;
    }


    public ResponseEntity<Void> updateService(@ApiParam(required = true) @PathVariable("id") UUID id,
                                              @ApiParam(required = true) @RequestBody NewService service) {
        return null;
    }

}
