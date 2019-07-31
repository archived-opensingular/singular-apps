
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import java.util.Map;

@WebListener
public class WicketRequestCacheRequestListener implements ServletRequestListener, Loggable {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        if (ApplicationContextProvider.isConfigured()) {
            try {
                ApplicationContextProvider.get()
                        .getBeansOfType(WicketRequestCacheManager.class)
                        .values()
                        .forEach(WicketRequestCacheManager::clearCache);
            } catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {
                getLogger().warn(e.getMessage(), e);
            }
        }
    }
}