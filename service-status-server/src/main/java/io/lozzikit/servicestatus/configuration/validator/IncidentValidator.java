package io.lozzikit.servicestatus.configuration.validator;

import io.lozzikit.servicestatus.api.dto.Incident;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IncidentValidator implements
        ConstraintValidator<IncidentConstraint, Incident> {
    @Override
    public void initialize(IncidentConstraint incidentConstraint) {

    }

    @Override
    public boolean isValid(Incident incident, ConstraintValidatorContext constraintValidatorContext) {
        return incident != null && true ;
    }


   /* @Override
    public boolean isValid(String contactField,
                           ConstraintValidatorContext cxt) {
        return contactField != null && contactField.matches("[0-9]+")
                && (contactField.length() > 8) && (contactField.length() < 14);
    }*/
}
