/**
 * Copyright (c) 2014, NetIDE Consortium (Create-Net (CN), Telefonica Investigacion Y Desarrollo SA (TID), Fujitsu 
 * Technology Solutions GmbH (FTS), Thales Communications & Security SAS (THALES), Fundacion Imdea Networks (IMDEA),
 * Universitaet Paderborn (UPB), Intel Research & Innovation Ireland Ltd (IRIIL) )
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Authors:
 *     Telefonica I+D
 */
/**
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.openflowplugin.pyretic;

import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev100924.MacAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.node.NodeConnectorKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

import java.util.Arrays;

/**
 * 
 */
public abstract class PacketUtils {

    

    /**
     * @param payload
     * @return destination MAC address
     */
    public static byte[] extractDstMac(byte[] payload) {
        return Arrays.copyOfRange(payload, 0, 6);
    }

    /**
     * @param payload
     * @return source MAC address
     */
    public static byte[] extractSrcMac(byte[] payload) {
        return Arrays.copyOfRange(payload, 6, 12);
    }

    /**
     * @param payload
     * @return source MAC address
     */
    public static byte[] extractEtherType(byte[] payload) {
        return Arrays.copyOfRange(payload, 12, 14);
    }

    /**
     * @param rawMac
     * @return {@link org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev100924.MacAddress} wrapping string value, baked upon binary MAC
     *         address
     */
    public static MacAddress rawMacToMac(byte[] rawMac) {
        MacAddress mac = null;
        if (rawMac != null && rawMac.length == 6) {
            StringBuffer sb = new StringBuffer();
            for (byte octet : rawMac) {
                sb.append(String.format(":%02X", octet));
            }
            mac = new MacAddress(sb.substring(1));
        }
        return mac;
    }

    /**
     * @param nodeInstId
     * @param nodeKey
     * @param port
     * @return port wrapped into {@link org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef}
     */
    public static NodeConnectorRef createNodeConnRef(InstanceIdentifier<Node> nodeInstId, NodeKey nodeKey, String port) {
        StringBuilder sBuild = new StringBuilder(nodeKey.getId().getValue()).append(":").append(port);
        NodeConnectorKey nConKey = new NodeConnectorKey(new NodeConnectorId(sBuild.toString()));
        InstanceIdentifier<NodeConnector> portPath = InstanceIdentifier.builder(nodeInstId)
                .child(NodeConnector.class, nConKey).build();
        return new NodeConnectorRef(portPath);
    }
}
