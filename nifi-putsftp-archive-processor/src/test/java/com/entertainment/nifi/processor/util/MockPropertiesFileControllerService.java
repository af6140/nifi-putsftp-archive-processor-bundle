package com.entertainment.nifi.processor.util;

import com.entertainment.nifi.controller.PropertiesFileService;
import org.apache.nifi.annotation.lifecycle.OnEnabled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.components.ValidationContext;
import org.apache.nifi.components.ValidationResult;
import org.apache.nifi.controller.AbstractControllerService;
import org.apache.nifi.controller.ConfigurationContext;
import org.apache.nifi.controller.ControllerServiceInitializationContext;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.MockConfigurationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dwang on 6/6/17.
 */
public class MockPropertiesFileControllerService extends AbstractControllerService implements PropertiesFileService {

    private Map<String,Map<String,String>> properties =new HashMap<String, Map<String,String>>();
    public void addPropertyMap(String scope, Map<String,String> scopeProperties) {
        this.properties.put(scope, scopeProperties);
    }
    @Override
    public String getProperty(String scope, String propertyName) {
        getLogger().debug("Looking up in scope " + scope + " property "+ propertyName);
        return this.properties.get(scope).get(propertyName);
    }

    @Override
    public void onPropertyModified(PropertyDescriptor propertyDescriptor, String s, String s1) {

    }

    @Override
    public String getIdentifier() {
        return "mockpropertiesfileservice";
    }

    @OnEnabled
    public void onConfigured(final MockConfigurationContext context) throws InitializationException {
        System.out.println("Enabled mock service");

    }

}
