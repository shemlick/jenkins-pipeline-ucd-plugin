/**
 * © Copyright IBM Corporation 2017.
 * This is licensed under the following license.
 * The Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
package com.urbancode.jenkins.plugins.ucdeploy;

import hudson.AbortException;
import hudson.model.TaskListener;

import java.io.IOException;
import java.net.URI;

import org.apache.http.impl.client.DefaultHttpClient;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.urbancode.ud.client.SystemClient;

/**
 * This class allows use of the UrbanCode Deploy REST client
 * to make system related REST calls
 */
public class SystemHelper {
    private SystemClient sysClient;

    public SystemHelper(URI ucdUrl, DefaultHttpClient httpClient, TaskListener listener) {
        sysClient = new SystemClient(ucdUrl, httpClient);
    }

    public boolean isMaintenanceEnabled() throws AbortException {
        boolean maintenanceEnabled;

        try {
            JSONObject systemConfig = sysClient.getSystemConfiguration();
            maintenanceEnabled = systemConfig.getBoolean("enableMaintenanceMode");
        }
        catch (IOException ex) {
            throw new AbortException("Invalid http response code returned when acquiring UCD system configuration:"
                    + ex.getMessage());
        }
        catch (JSONException ex) {
            throw new AbortException("Failed to acquire UCD system configuration: " + ex.getMessage());
        }

        return maintenanceEnabled;
    }
}
