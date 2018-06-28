package org.opensingular.requirement.commons.dispacher;

import org.opensingular.flow.persistence.entity.AbstractTaskInstanceEntity;
import org.opensingular.lib.commons.util.Loggable;
import org.opensingular.requirement.commons.config.IServerContext;
import org.opensingular.requirement.commons.form.FormAction;
import org.opensingular.requirement.commons.service.RequirementInstance;
import org.opensingular.requirement.commons.service.RequirementService;
import org.opensingular.requirement.commons.spring.security.AuthorizationService;
import org.opensingular.requirement.commons.spring.security.SingularRequirementUserDetails;
import org.opensingular.requirement.commons.wicket.SingularSession;
import org.opensingular.requirement.commons.wicket.view.util.ActionContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * check if the logged in user is the owner of the requested requirement
 */
public class IsOwnerPageAccessChecker implements PageAccessChecker, Loggable {
    private final List<IServerContext> contextsToCheck;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private RequirementService<?, ?> requirementService;

    public IsOwnerPageAccessChecker(List<IServerContext> contextsToCheck) {
        this.contextsToCheck = contextsToCheck;
    }

    @Override
    public boolean hasAccess(ActionContext context) {

        SingularRequirementUserDetails userDetails = SingularSession.get().getUserDetails();

        Long requirementId = context.getRequirementId().orElse(null);
        boolean hasPermission = authorizationService.hasPermission(
                requirementId,
                context.getFormName().orElse(null),
                String.valueOf(userDetails.getUserPermissionKey()),
                context.getFormAction().map(FormAction::name).orElse(null)
        );

        if (requirementId != null && contextsToCheck.stream().anyMatch(userDetails::isContext)) {
            hasPermission &= isOwner(userDetails, requirementId);
        }

        // Qualquer modo de edição o usuário deve ter permissão e estar alocado na tarefa,
        // para os modos de visualização basta a permissão.
        if (isViewModeEdit(context) || isAnnotationModeEdit(context)) {
            return hasPermission && !isTaskAssignedToAnotherUser(context);
        } else {
            return hasPermission;
        }
    }

    protected boolean isOwner(SingularRequirementUserDetails userDetails, Long requirementId) {
        String applicantId = userDetails.getApplicantId();
        RequirementInstance requirement = requirementService.getRequirement(requirementId);
        boolean truth = Objects.equals(requirement.getApplicant().getIdPessoa(), applicantId);
        if (!truth) {
            getLogger()
                    .info("User {} (SingularRequirementUserDetails::getApplicantId={}) is not owner of Requirement with id={}. Expected owner id={} ",
                            userDetails.getUsername(), applicantId, requirementId, requirement.getApplicant().getIdPessoa());
        }
        return truth;
    }

    private boolean isViewModeEdit(ActionContext context) {
        return context.getFormAction().map(FormAction::isViewModeEdit).orElse(Boolean.FALSE);
    }

    @SuppressWarnings("OptionalIsPresent")
    private boolean isTaskAssignedToAnotherUser(ActionContext config) {
        String username = SingularSession.get().getUsername();
        Optional<Long> requirementIdOpt = config.getRequirementId();
        if (requirementIdOpt.isPresent()) {
            return requirementService.findCurrentTaskEntityByRequirementId(requirementIdOpt.get())
                    .map(AbstractTaskInstanceEntity::getTaskHistory)
                    .filter(histories -> !histories.isEmpty())
                    .map(histories -> histories.get(histories.size() - 1))
                    .map(history -> history.getAllocatedUser() != null
                            && history.getAllocationEndDate() == null
                            && !username.equalsIgnoreCase(history.getAllocatedUser().getCodUsuario()))
                    .orElse(Boolean.FALSE);
        }
        return false;
    }

    private boolean isAnnotationModeEdit(ActionContext context) {
        return context.getFormAction().map(FormAction::isAnnotationModeEdit).orElse(Boolean.FALSE);
    }
}