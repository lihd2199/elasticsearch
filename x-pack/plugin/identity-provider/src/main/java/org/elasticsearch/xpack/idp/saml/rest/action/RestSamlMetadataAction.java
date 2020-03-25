/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.idp.saml.rest.action;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.license.XPackLicenseState;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.action.RestBuilderListener;
import org.elasticsearch.xpack.idp.action.SamlMetadataAction;
import org.elasticsearch.xpack.idp.action.SamlMetadataRequest;
import org.elasticsearch.xpack.idp.action.SamlMetadataResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class RestSamlMetadataAction extends IdpBaseRestHandler {

    public RestSamlMetadataAction(XPackLicenseState licenseState) {
        super(licenseState);
    }

    @Override
    public String getName() {
        return "saml_metadata_action";
    }

    @Override
    public List<Route> routes() {
        return Collections.singletonList(new Route(GET, "/_idp/saml/metadata/{sp_entity_id}"));
    }

    @Override
    protected RestChannelConsumer innerPrepareRequest(RestRequest request, NodeClient client) throws IOException {
        final String spEntityId = request.param("sp_entity_id");
        final SamlMetadataRequest metadataRequest = new SamlMetadataRequest(spEntityId);
        return channel -> client.execute(SamlMetadataAction.INSTANCE, metadataRequest,
            new RestBuilderListener<SamlMetadataResponse>(channel) {
                @Override
                public RestResponse buildResponse(SamlMetadataResponse response, XContentBuilder builder) throws Exception {
                    builder.startObject();
                    builder.field("metadata", response.getXmlString());
                    builder.endObject();
                    return new BytesRestResponse(RestStatus.OK, builder);
                }
            });

    }
}