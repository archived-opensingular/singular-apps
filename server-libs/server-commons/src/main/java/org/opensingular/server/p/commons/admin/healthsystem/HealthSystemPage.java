package org.opensingular.server.p.commons.admin.healthsystem;

import org.opensingular.server.commons.wicket.view.template.Content;
import org.opensingular.server.commons.wicket.view.template.Template;


public class HealthSystemPage extends Template {

	public static final String HEALTH_SYSTEM_MOUNT_PATH = "healthsystem";

    public HealthSystemPage(){
    }

	@Override
	protected Content getContent(String id) {
		return new HealthSystemContent(id);
	}
	
	@Override
	protected boolean withMenu() {
		return false;
	}


}