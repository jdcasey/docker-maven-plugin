/*
    Copyright 2014 Wouter Danes

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

*/

package net.wouterdanes.docker.remoteapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import net.wouterdanes.docker.remoteapi.model.ImageDescriptor;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Optional;

/**
 * This class is responsible for talking to the Docker Remote API "images" endpoint.<br> See <a
 * href="http://docs.docker.io/reference/api/docker_remote_api_v1.12/#22-images">
 * http://docs.docker.io/reference/api/docker_remote_api_v1.12/#22-images</a>
 */
public class ImagesService extends BaseService {

    public ImagesService(String dockerApiRoot) {
        super(dockerApiRoot, "/images");
    }

    public void deleteImage(final String imageId) {
        try {
            getServiceEndPoint()
                    .path(imageId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .delete(String.class);
        } catch (WebApplicationException e) {
            throw makeImageTargetingException("Cannot remove image", e);
        }
    }

    public String pullImage(final String image) {
        ImageDescriptor descriptor = new ImageDescriptor(image);

        WebTarget target = getServiceEndPoint()
                .path("create");

        target = target.queryParam("fromImage", descriptor.getRegistryRepositoryAndImage());

        if (descriptor.getTag().isPresent()) {
            target = target.queryParam("tag", descriptor.getTag().get());
        }

        System.out.printf("setting auth header '%s' with value: '%s'\n", REGISTRY_AUTH_HEADER, getRegistryAuthHeaderValue() );
        System.out.printf( "Sending request to: '%s'\n", target.getUri() );

        Response response = target.request()
                .header(REGISTRY_AUTH_HEADER, getRegistryAuthHeaderValue())
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(null);

        System.out.printf( "Response status: '%s'\n", response.getStatus() );
        response.getHeaders().forEach( (key,values)->{
            System.out.printf( "Response header: '%s' = %s\n", key, values );
        } );

        InputStream inputStream = (InputStream) response.getEntity();

        parseStreamToDisplayImageDownloadStatus(inputStream);

        return response.readEntity(String.class);
    }

    private static void parseStreamToDisplayImageDownloadStatus(final InputStream inputStream) {
        String json;
        try
        {
            json = IOUtils.toString( inputStream );
            System.out.printf( "Got JSON response:\n\n%s\n\n", json );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot read image download status response: " + e.getMessage(), e );
        }

//        InputStreamReader isr = new InputStreamReader(inputStream);
//        BufferedReader reader = new BufferedReader(isr);

        JsonStreamParser parser = new JsonStreamParser( new StringReader( json ) );

        while (parser.hasNext()) {
            JsonElement element = parser.next();
            JsonObject object = element.getAsJsonObject();
            if (object.has("status")) {
                System.out.print(object.get("status").getAsString() + ".");
            }
            if (object.has("error")) {
                System.err.println("ERROR: " + object.get("error").getAsString());
            }
        }
        System.out.println("");
    }

    public String pushImage(String nameAndTag) {
        try {
            WebTarget target = createPushRequestFromTag(nameAndTag);

            return target.request()
                    .header(REGISTRY_AUTH_HEADER, getRegistryAuthHeaderValue())
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .post(null, String.class);

        } catch (WebApplicationException e) {
            throw makeImageTargetingException(nameAndTag, e);

        }
    }

    public void tagImage(final String imageId, final String nameAndTag) {
        ImageDescriptor descriptor = new ImageDescriptor(nameAndTag);

        WebTarget target = getServiceEndPoint()
                .path(imageId)
                .path("tag")
                .queryParam("repo", descriptor.getRegistryRepositoryAndImage())
                .queryParam("force", 1);

        Optional<String> targetTag = descriptor.getTag();
        if (targetTag.isPresent()) {
            target = target.queryParam("tag", targetTag.get());
        }

        Response response = target.request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(null);

        Response.StatusType statusInfo = response.getStatusInfo();

        response.close();

        checkImageTargetingResponse(imageId, statusInfo);
    }

    private WebTarget createPushRequestFromTag(final String nameAndTag) {
        ImageDescriptor descriptor = new ImageDescriptor(nameAndTag);
        WebTarget target = getServiceEndPoint()
                .path(descriptor.getRegistryRepositoryAndImage())
                .path("push");

        if (descriptor.getTag().isPresent()) {
            target = target.queryParam("tag", descriptor.getTag().get());
        }

        return target;
    }
}
